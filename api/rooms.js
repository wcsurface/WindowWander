// VERCEL PERSISTENT LIVE ROOMS API ENDPOINT WITH MASTER INDEX
const https = require('https');

const API_STORE_URL = 'https://api.restful-api.dev/objects';
const MASTER_INDEX_ID = 'ff808181a09d98f701a1329f61b04e3f';

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

// In-memory cache map for fast warm-instance lookups
const memoryRooms = new Map();

async function getMasterIndex() {
  try {
    const res = await httpRequest(`${API_STORE_URL}/${MASTER_INDEX_ID}`, 'GET');
    if (res && res.data) {
      return res.data;
    }
  } catch(e) {}
  return {};
}

async function updateMasterIndex(indexData) {
  try {
    const payload = {
      name: "BINGO_MASTER_INDEX",
      data: indexData
    };
    await httpRequest(`${API_STORE_URL}/${MASTER_INDEX_ID}`, 'PUT', payload);
  } catch(e) {}
}

async function getRoomByCode(code) {
  const cleanCode = code.toUpperCase().trim();
  if (memoryRooms.has(cleanCode)) {
    return memoryRooms.get(cleanCode);
  }

  try {
    const masterIndex = await getMasterIndex();
    const roomId = masterIndex[cleanCode];
    if (roomId) {
      const roomRes = await httpRequest(`${API_STORE_URL}/${roomId}`, 'GET');
      if (roomRes && roomRes.data) {
        memoryRooms.set(cleanCode, roomRes.data);
        return roomRes.data;
      }
    }
  } catch(e) {}

  return null;
}

async function saveRoomByCode(code, room) {
  const cleanCode = code.toUpperCase().trim();
  memoryRooms.set(cleanCode, room);

  try {
    const masterIndex = await getMasterIndex();
    let roomId = masterIndex[cleanCode];

    if (!roomId) {
      const payload = {
        name: `BINGO_ROOM_${cleanCode}`,
        data: room
      };
      const createRes = await httpRequest(API_STORE_URL, 'POST', payload);
      if (createRes && createRes.id) {
        roomId = createRes.id;
        masterIndex[cleanCode] = roomId;
        await updateMasterIndex(masterIndex);
      }
    } else {
      const payload = {
        name: `BINGO_ROOM_${cleanCode}`,
        data: room
      };
      await httpRequest(`${API_STORE_URL}/${roomId}`, 'PUT', payload);
    }
  } catch(e) {
    console.error('Save room error:', e);
  }
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
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
  const gameMode = body.gameMode || 'CLASSIC_LINE';

  if (!code && action !== 'health') {
    return res.status(400).json({ success: false, error: 'Room code required' });
  }

  // 1. CREATE ROOM (HOST)
  if (action === 'create') {
    let room = await getRoomByCode(code);
    if (!room) {
      room = {
        code,
        hostNickname,
        deckId,
        gameMode,
        players: [{ nickname: hostNickname, isHost: true, completedTiles: [12] }]
      };
    } else {
      if (!room.players.some(p => p.nickname === hostNickname)) {
        room.players.push({ nickname: hostNickname, isHost: true, completedTiles: [12] });
      }
    }
    await saveRoomByCode(code, room);
    return res.status(200).json({ success: true, room });
  }

  // 2. JOIN ROOM (PASSENGER) - RETRIES UP TO 5 TIMES, NEVER CREATES HOST ROOM
  if (action === 'join') {
    let room = null;
    for (let attempt = 0; attempt < 5; attempt++) {
      room = await getRoomByCode(code);
      if (room) break;
      await sleep(500);
    }

    if (room) {
      const existingPlayer = room.players.find(p => p.nickname === nickname);
      if (!existingPlayer) {
        room.players.push({ nickname, isHost: false, completedTiles: [12] });
        await saveRoomByCode(code, room);
      }
      return res.status(200).json({ success: true, room });
    } else {
      return res.status(404).json({ success: false, error: `Room code '${code}' not found. Please verify the code and try again.` });
    }
  }

  // 3. GET ROOM
  if (action === 'get') {
    let room = await getRoomByCode(code);
    if (room) {
      return res.status(200).json({ success: true, room });
    }
    return res.status(404).json({ success: false, error: `Room code '${code}' not found.` });
  }

  return res.status(200).json({ success: true, activeRooms: memoryRooms.size });
};
