// ROAD TRIP BINGO - RAILWAY PERSISTENT MULTIPLAYER SERVER
const express = require('express');
const http = require('http');
const { WebSocketServer } = require('ws');

const app = express();
const server = http.createServer(app);
const wss = new WebSocketServer({ server });

const PORT = process.env.PORT || 3000;

// IN-MEMORY ROOM STORAGE (OR BACKED BY POSTGRESQL DB)
const rooms = new Map();

function generateRoomCode() {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
  let code = '';
  for (let i = 0; i < 4; i++) {
    code += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return code;
}

app.use(express.json());

app.get('/health', (req, res) => {
  res.json({ status: 'ok', activeRooms: rooms.size, timestamp: new Date().toISOString() });
});

app.post('/api/rooms/create', (req, res) => {
  const { hostNickname, deckId } = req.body;
  const code = generateRoomCode();
  const room = {
    code,
    hostNickname,
    deckId: deckId || 'deck_a',
    players: [{ nickname: hostNickname, isHost: true, score: 0 }],
    discoveries: []
  };
  rooms.set(code, room);
  res.json({ success: true, room });
});

wss.on('connection', (ws) => {
  ws.on('message', (message) => {
    try {
      const data = JSON.parse(message);
      if (data.type === 'JOIN_ROOM') {
        const room = rooms.get(data.code);
        if (room) {
          room.players.push({ nickname: data.nickname, isHost: false, score: 0 });
          ws.roomCode = data.code;
          ws.send(JSON.stringify({ type: 'ROOM_JOINED', room }));
        }
      } else if (data.type === 'CLAIM_TILE') {
        const room = rooms.get(ws.roomCode);
        if (room) {
          room.discoveries.push({
            promptId: data.promptId,
            claimedBy: data.nickname,
            timestamp: Date.now()
          });
          // Broadcast to all room clients
          wss.clients.forEach(client => {
            if (client.roomCode === ws.roomCode && client.readyState === 1) {
              client.send(JSON.stringify({ type: 'TILE_CLAIMED', room }));
            }
          });
        }
      }
    } catch(err) {
      console.error(err);
    }
  });
});

server.listen(PORT, () => {
  console.log(`Road Trip Bingo Railway Server running on port ${PORT}`);
});
