// VERCEL PERSISTENT USER ACCOUNTS & PROFILES API ENDPOINT
const https = require('https');
const crypto = require('crypto');

const API_STORE_URL = 'https://api.restful-api.dev/objects';
const USER_INDEX_ID = 'ff808181a09d98f701a133a54006505e';

function httpRequest(urlStr, method, data) {
  return new Promise((resolve) => {
    try {
      const url = new URL(urlStr);
      const payload = data ? JSON.stringify(data) : null;

      const options = {
        hostname: url.hostname,
        port: url.port || 443,
        path: url.pathname + url.search,
        method: method || 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      };

      if (payload) {
        options.headers['Content-Length'] = Buffer.byteLength(payload);
      }

      const req = https.request(options, (res) => {
        let body = '';
        res.on('data', chunk => body += chunk);
        res.on('end', () => {
          try {
            const parsed = JSON.parse(body);
            resolve(parsed);
          } catch(e) {
            resolve({ error: body });
          }
        });
      });

      req.on('error', (err) => resolve({ error: err.message }));

      if (payload) {
        req.write(payload);
      }
      req.end();
    } catch(err) {
      resolve({ error: err.message });
    }
  });
}

function hashPassword(password) {
  return crypto.createHash('sha256').update(password + '_roadtrip_salt_2026').digest('hex');
}

const memoryUsers = new Map();

async function getUserIndex() {
  try {
    const res = await httpRequest(`${API_STORE_URL}/${USER_INDEX_ID}`, 'GET');
    if (res && res.data) {
      return res.data;
    }
  } catch(e) {}
  return {};
}

async function updateUserIndex(indexData) {
  try {
    const payload = {
      name: "BINGO_USER_INDEX",
      data: indexData
    };
    await httpRequest(`${API_STORE_URL}/${USER_INDEX_ID}`, 'PUT', payload);
  } catch(e) {}
}

async function getUserByUsername(username) {
  const cleanUser = username.toLowerCase().trim();
  if (memoryUsers.has(cleanUser)) {
    return memoryUsers.get(cleanUser);
  }

  try {
    const userIndex = await getUserIndex();
    const userId = userIndex[cleanUser];
    if (userId) {
      const userRes = await httpRequest(`${API_STORE_URL}/${userId}`, 'GET');
      if (userRes && userRes.data) {
        memoryUsers.set(cleanUser, userRes.data);
        return userRes.data;
      }
    }
  } catch(e) {}

  return null;
}

async function saveUserRecord(username, userData) {
  const cleanUser = username.toLowerCase().trim();
  memoryUsers.set(cleanUser, userData);

  try {
    const userIndex = await getUserIndex();
    let userId = userIndex[cleanUser];

    if (!userId) {
      const payload = {
        name: `BINGO_USER_${cleanUser}`,
        data: userData
      };
      const createRes = await httpRequest(API_STORE_URL, 'POST', payload);
      if (createRes && createRes.id) {
        userId = createRes.id;
        userIndex[cleanUser] = userId;
        await updateUserIndex(userIndex);
      }
    } else {
      const payload = {
        name: `BINGO_USER_${cleanUser}`,
        data: userData
      };
      await httpRequest(`${API_STORE_URL}/${userId}`, 'PUT', payload);
    }
  } catch(e) {
    console.error('Save user error:', e);
  }
}

module.exports = async (req, res) => {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  const body = req.body || {};
  const action = body.action || req.query.action || '';
  const username = (body.username || '').toLowerCase().trim();
  const password = body.password || '';
  const nickname = (body.nickname || username).trim();

  // 1. REGISTER USER
  if (action === 'register' || req.url.includes('register')) {
    if (!username || !password) {
      return res.status(400).json({ success: false, error: 'Username and password required' });
    }

    let existing = await getUserByUsername(username);
    if (existing) {
      return res.status(400).json({ success: false, error: 'Username already registered' });
    }

    const newUser = {
      username,
      passwordHash: hashPassword(password),
      nickname: nickname || username,
      level: 1,
      xp: 0,
      earnedBadges: ['first_ride'],
      streakDays: 1,
      bingosWon: 0,
      gamesPlayed: 0,
      squaresFound: 0
    };

    await saveUserRecord(username, newUser);
    return res.status(200).json({ success: true, user: newUser });
  }

  // 2. LOGIN USER
  if (action === 'login' || req.url.includes('login')) {
    if (!username || !password) {
      return res.status(400).json({ success: false, error: 'Username and password required' });
    }

    const passHash = hashPassword(password);
    let user = await getUserByUsername(username);

    if (!user) {
      // Auto-register user on first login so users can seamlessly log in
      user = {
        username,
        passwordHash: passHash,
        nickname: nickname || username,
        level: 1,
        xp: 250,
        earnedBadges: ['first_ride'],
        streakDays: 1,
        bingosWon: 1,
        gamesPlayed: 3,
        squaresFound: 18
      };
      await saveUserRecord(username, user);
      return res.status(200).json({ success: true, user });
    }

    if (user.passwordHash && user.passwordHash !== passHash) {
      return res.status(401).json({ success: false, error: 'Invalid password' });
    }

    return res.status(200).json({ success: true, user });
  }

  // 3. SAVE PROFILE DATA
  if (action === 'save_profile' || action === 'save-profile' || req.url.includes('save-profile')) {
    if (!username) {
      return res.status(400).json({ success: false, error: 'Username required' });
    }

    let user = await getUserByUsername(username) || {
      username,
      passwordHash: hashPassword('default'),
      nickname: username
    };

    user.level = body.level || Math.floor((body.xp || 0) / 100) + 1;
    user.xp = body.xp !== undefined ? body.xp : (user.xp || 0);
    user.earnedBadges = Array.isArray(body.earnedBadges) ? body.earnedBadges : ['first_ride'];
    user.streakDays = body.streakDays || user.streakDays || 1;
    user.bingosWon = body.bingosWon !== undefined ? body.bingosWon : (user.bingosWon || 0);
    user.gamesPlayed = body.gamesPlayed !== undefined ? body.gamesPlayed : (user.gamesPlayed || 0);
    user.squaresFound = body.squaresFound !== undefined ? body.squaresFound : (user.squaresFound || 0);

    await saveUserRecord(username, user);
    return res.status(200).json({ success: true, user });
  }

  return res.status(200).json({ success: true });
};
