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
  if (!username) return null;
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
  if (!username) return;
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
  const rawUsername = (body.username || body.nickname || '').trim();
  const username = rawUsername.toLowerCase();
  const password = body.password || 'default_pass';
  const nickname = rawUsername || 'Road Explorer';

  if (!username && action !== 'health' && action !== 'leaderboard') {
    return res.status(400).json({ success: false, error: 'Username required' });
  }

  // 1. REGISTER USER
  if (action === 'register' || req.url.includes('register')) {
    const cleanUser = username.toLowerCase().trim();
    let existing = await getUserByUsername(cleanUser);
    if (existing) {
      return res.status(200).json({ success: false, error: 'Username is already taken. Only 1 account per username is allowed. Please log in instead.' });
    }

    const newUser = {
      username: cleanUser,
      passwordHash: hashPassword(password),
      nickname: rawUsername || username,
      level: 1,
      xp: 0,
      earnedBadges: ['first_ride'],
      streakDays: 1,
      bingosWon: 0,
      gamesPlayed: 0,
      squaresFound: 0
    };

    await saveUserRecord(cleanUser, newUser);
    return res.status(200).json({ success: true, user: newUser });
  }

  // 2. LOGIN USER
  if (action === 'login' || req.url.includes('login')) {
    const cleanUser = username.toLowerCase().trim();
    let user = await getUserByUsername(cleanUser);

    // RESTORE & ENFORCE WILL'S LEVEL 8 ACCOUNT DATA & PASSWORD
    if (cleanUser === 'will') {
      if (!user || (user.xp !== undefined && user.xp < 750)) {
        user = {
          username: 'will',
          passwordHash: hashPassword('!929Ford'),
          nickname: 'Will',
          level: 8,
          xp: 750,
          earnedBadges: ['first_ride', 'bingo_master', 'speedy_explorer', 'highway_hero'],
          streakDays: 5,
          bingosWon: 8,
          gamesPlayed: 15,
          squaresFound: 42
        };
        await saveUserRecord('will', user);
      } else {
        user.passwordHash = hashPassword('!929Ford');
        await saveUserRecord('will', user);
      }
    }

    if (!user) {
      return res.status(200).json({ success: false, error: 'Account not found. You can only log in if you have already created an account.' });
    }

    const passHash = hashPassword(password);

    // STRICT PASSWORD VERIFICATION
    if (user.passwordHash && user.passwordHash !== passHash) {
      return res.status(200).json({ success: false, error: 'Incorrect password. Login only works with the password used at account creation.' });
    }

    if (!user.passwordHash) {
      user.passwordHash = passHash;
      await saveUserRecord(cleanUser, user);
    }

    return res.status(200).json({ success: true, user });
  }

  // 3. SAVE PROFILE DATA
  if (action === 'save_profile' || action === 'save-profile' || req.url.includes('save-profile')) {
    const cleanUser = username.toLowerCase().trim();
    let user = await getUserByUsername(cleanUser);

    if (!user) {
      return res.status(200).json({ success: false, error: 'Cannot save progress: Account does not exist.' });
    }

    user.xp = body.xp !== undefined ? body.xp : (user.xp || 0);
    user.level = body.level || Math.floor((user.xp) / 100) + 1;
    user.earnedBadges = Array.isArray(body.earnedBadges) ? body.earnedBadges : (user.earnedBadges || ['first_ride']);
    user.streakDays = body.streakDays || user.streakDays || 1;
    user.bingosWon = body.bingosWon !== undefined ? body.bingosWon : (user.bingosWon || 0);
    user.gamesPlayed = body.gamesPlayed !== undefined ? body.gamesPlayed : (user.gamesPlayed || 0);
    user.squaresFound = body.squaresFound !== undefined ? body.squaresFound : (user.squaresFound || 0);

    await saveUserRecord(cleanUser, user);
    return res.status(200).json({ success: true, user });
  }

  // 4. GLOBAL PUBLIC LEADERBOARD
  if (action === 'leaderboard' || req.url.includes('leaderboard')) {
    // Seed top featured accounts into memory
    await getUserByUsername('will');
    await getUserByUsername('testaccount');

    const allUsers = Array.from(memoryUsers.values());

    const topXp = allUsers
      .map(u => ({
        username: u.username,
        nickname: u.nickname || u.username,
        level: u.level || Math.floor((u.xp || 0) / 100) + 1,
        xp: u.xp || 0,
        bingosWon: u.bingosWon || 0,
        gamesPlayed: u.gamesPlayed || 0,
        squaresFound: u.squaresFound || 0
      }))
      .sort((a, b) => b.xp - a.xp || b.bingosWon - a.bingosWon)
      .slice(0, 50);

    const topItems = [...allUsers]
      .map(u => ({
        username: u.username,
        nickname: u.nickname || u.username,
        level: u.level || Math.floor((u.xp || 0) / 100) + 1,
        xp: u.xp || 0,
        bingosWon: u.bingosWon || 0,
        gamesPlayed: u.gamesPlayed || 0,
        squaresFound: u.squaresFound || 0
      }))
      .sort((a, b) => b.squaresFound - a.squaresFound || b.xp - a.xp)
      .slice(0, 50);

    return res.status(200).json({ success: true, leaderboard: topXp, topItems });
  }

  return res.status(200).json({ success: true });
};
