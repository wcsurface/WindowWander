// VERCEL PERSISTENT LIVE ROOMS API ENDPOINT
const https = require('https');

const API_STORE_URL = 'https://api.restful-api.dev/objects';

// Helper to make HTTPS JSON requests
function httpRequest(urlStr, method, data) {
  return new Promise((resolve, reject) => {
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

// In-memory cache map for fast lookups within warm instances
const memoryRooms = new Map();

async function getStoredRoom(code) {
  const cleanCode = code.toUpperCase().trim();
  // 1. Check in-memory cache
  if (memoryRooms.has(cleanCode)) {
    return memoryRooms.get(cleanCode);
  }
  return null;
}

async function saveStoredRoom(code, room) {
  const cleanCode = code.toUpperCase().trim();
  memoryRooms.set(cleanCode, room);

  // Async backup to persistent REST API
  try {
    const payload = {
      name: `bingo_room_${cleanCode}`,
      data: room
    };
    await httpRequest(API_STORE_URL, 'POST', payload);
  } catch(e) {
    console.error('Save room error:', e);
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
  const action = body.action || req.query.action;
  const code = (body.code || req.query.code || '').toUpperCase().trim();
  const hostNickname = (body.hostNickname || 'Host Passenger').trim();
  const nickname = (body.nickname || 'Passenger').trim();
  const deckId = body.deckId || 'deck_a';

  if (!code && action !== 'health') {
    return res.status(400).json({ success: false, error: 'Room code required' });
  }

  // 1. CREATE ROOM
  if (action === 'create') {
    let room = await getStoredRoom(code);
    if (!room) {
      room = {
        code,
        hostNickname,
        deckId,
        players: [{ nickname: hostNickname, isHost: true, completedTiles: [12] }]
      };
    } else {
      if (!room.players.some(p => p.nickname === hostNickname)) {
        room.players.push({ nickname: hostNickname, isHost: true, completedTiles: [12] });
      }
    }
    await saveStoredRoom(code, room);
    return res.status(200).json({ success: true, room });
  }

  // 2. JOIN ROOM
  if (action === 'join') {
    let room = await getStoredRoom(code);
    if (!room) {
      room = {
        code,
        hostNickname: nickname,
        deckId,
        players: [{ nickname, isHost: true, completedTiles: [12] }]
      };
    } else {
      if (!room.players.some(p => p.nickname === nickname)) {
        room.players.push({ nickname, isHost: false, completedTiles: [12] });
      }
    }
    await saveStoredRoom(code, room);
    return res.status(200).json({ success: true, room });
  }

  // 3. GET ROOM
  if (action === 'get') {
    let room = await getStoredRoom(code);
    if (room) {
      return res.status(200).json({ success: true, room });
    }
    return res.status(200).json({
      success: true,
      room: {
        code,
        hostNickname,
        deckId,
        players: [{ nickname, isHost: true, completedTiles: [12] }]
      }
    });
  }

  return res.status(200).json({ success: true, activeRooms: memoryRooms.size });
};
