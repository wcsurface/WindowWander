// VERCEL SERVERLESS LIVE ROOMS API ENDPOINT
if (!global.__BINGO_ROOMS__) {
  global.__BINGO_ROOMS__ = new Map();
}
const rooms = global.__BINGO_ROOMS__;

module.exports = (req, res) => {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  const body = req.body || {};
  const action = body.action || req.query.action;
  const code = (body.code || req.query.code || '').toUpperCase().trim();
  const hostNickname = body.hostNickname || 'Host Passenger';
  const nickname = body.nickname || 'Passenger';
  const deckId = body.deckId || 'deck_a';

  if (!code && action !== 'health') {
    return res.status(400).json({ success: false, error: 'Room code required' });
  }

  if (action === 'create') {
    let room = rooms.get(code);
    if (!room) {
      room = {
        code,
        hostNickname,
        deckId,
        players: [{ nickname: hostNickname, isHost: true }]
      };
      rooms.set(code, room);
    } else {
      if (!room.players.some(p => p.nickname === hostNickname)) {
        room.players.push({ nickname: hostNickname, isHost: true });
      }
    }
    return res.status(200).json({ success: true, room });
  }

  if (action === 'join') {
    let room = rooms.get(code);
    if (!room) {
      room = {
        code,
        hostNickname: nickname,
        deckId,
        players: [{ nickname, isHost: true }]
      };
      rooms.set(code, room);
    } else {
      if (!room.players.some(p => p.nickname === nickname)) {
        room.players.push({ nickname, isHost: false });
      }
    }
    return res.status(200).json({ success: true, room });
  }

  if (action === 'get') {
    const room = rooms.get(code);
    if (room) {
      return res.status(200).json({ success: true, room });
    }
    return res.status(200).json({
      success: true,
      room: {
        code,
        hostNickname,
        deckId,
        players: [{ nickname, isHost: true }]
      }
    });
  }

  return res.status(200).json({ success: true, activeRooms: rooms.size });
};
