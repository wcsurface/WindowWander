// VERCEL PERSISTENT LIVE ROOMS API ENDPOINT WITH INSTANT IN-MEMORY SYNC
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

// Memory fallback cache
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

  // If memory cache has room updated within last 5 mins, prefer instant memory cache
  const cachedRoom = memoryRooms.get(cleanCode);
  if (cachedRoom && cachedRoom.updatedAt && (Date.now() - cachedRoom.updatedAt < 300000)) {
    return cachedRoom;
  }

  // Otherwise fetch from REST store
  try {
    const masterIndex = await getMasterIndex();
    const roomId = masterIndex[cleanCode];
    if (roomId) {
      const roomRes = await httpRequest(`${API_STORE_URL}/${roomId}`, 'GET');
      if (roomRes && roomRes.data && Array.isArray(roomRes.data.players)) {
        roomRes.data.updatedAt = Date.now();
        memoryRooms.set(cleanCode, roomRes.data);
        return roomRes.data;
      }
    }
  } catch(e) {}

  if (cachedRoom) {
    return cachedRoom;
  }

  return null;
}

async function saveRoomByCode(code, room) {
  const cleanCode = code.toUpperCase().trim();
  room.updatedAt = Date.now();
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
        isStarted: false,
        hasWinner: false,
        winnerNickname: null,
        players: [{ nickname: hostNickname, isHost: true, completedTiles: [12], hasBingo: false }]
      };
    } else {
      if (!room.players.some(p => p.nickname === hostNickname)) {
        room.players.push({ nickname: hostNickname, isHost: true, completedTiles: [12], hasBingo: false });
      }
    }
    await saveRoomByCode(code, room);
    return res.status(200).json({ success: true, room });
  }

  // 2. JOIN ROOM (PASSENGER)
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
        room.players.push({ nickname, isHost: false, completedTiles: [12], hasBingo: false });
        await saveRoomByCode(code, room);
      }
      return res.status(200).json({ success: true, room });
    } else {
      return res.status(404).json({ success: false, error: `Room code '${code}' not found. Please verify the code and try again.` });
    }
  }

  // 3. LAUNCH GAME (HOST LAUNCHES ROOM FOR ALL PASSENGERS)
  if (action === 'launch') {
    let room = await getRoomByCode(code);
    if (room) {
      room.isStarted = true;
      room.hasWinner = false;
      room.winnerNickname = null;
      if (deckId) room.deckId = deckId;
      if (gameMode) room.gameMode = gameMode;
      await saveRoomByCode(code, room);
      return res.status(200).json({ success: true, room });
    }
    return res.status(404).json({ success: false, error: 'Room not found' });
  }

  // 4. UPDATE COMPLETED TILES & WINNER STATUS FOR LIVE MULTIPLAYER
  if (action === 'update_tiles') {
    let room = await getRoomByCode(code);
    if (room) {
      let player = room.players.find(p => p.nickname === nickname);
      if (!player) {
        player = { nickname, isHost: false, completedTiles: [12], hasBingo: false };
        room.players.push(player);
      }

      player.completedTiles = Array.isArray(body.completedTiles) ? body.completedTiles : [12];
      if (body.hasBingo) {
        player.hasBingo = true;
        room.hasWinner = true;
        room.winnerNickname = nickname;
      }
      await saveRoomByCode(code, room);
      return res.status(200).json({ success: true, room });
    }
    return res.status(404).json({ success: false, error: 'Room not found' });
  }

  // 5. UPDATE ROOM DECK AND RULES SETTINGS (HOST)
  if (action === 'update_settings') {
    let room = await getRoomByCode(code);
    if (room) {
      if (deckId) room.deckId = deckId;
      if (gameMode) room.gameMode = gameMode;
      await saveRoomByCode(code, room);
      return res.status(200).json({ success: true, room });
    }
    return res.status(404).json({ success: false, error: 'Room not found' });
  }

  // 6. RESET ROOM FOR NEXT ROUND (RETURNS ALL PASSENGERS TO PRE-GAME LOBBY)
  if (action === 'reset_room') {
    let room = await getRoomByCode(code);
    if (room) {
      room.isStarted = false;
      room.hasWinner = false;
      room.winnerNickname = null;
      if (Array.isArray(room.players)) {
        room.players.forEach(p => {
          p.completedTiles = [12];
          p.hasBingo = false;
        });
      }
      await saveRoomByCode(code, room);
      return res.status(200).json({ success: true, room });
    }
    return res.status(404).json({ success: false, error: 'Room not found' });
  }

  // 7. GET ROOM
  if (action === 'get') {
    let room = await getRoomByCode(code);
    if (room) {
      return res.status(200).json({ success: true, room });
    }
    return res.status(404).json({ success: false, error: `Room code '${code}' not found.` });
  }

  return res.status(200).json({ success: true, activeRooms: memoryRooms.size });
};
