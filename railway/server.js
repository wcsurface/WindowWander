// ROAD TRIP BINGO - RAILWAY PERSISTENT MULTIPLAYER & USER ACCOUNTS SERVER
const express = require('express');
const http = require('http');
const crypto = require('crypto');
const { WebSocketServer } = require('ws');
const { Pool } = require('pg');

const app = express();
const server = http.createServer(app);
const wss = new WebSocketServer({ server });

const PORT = process.env.PORT || 3000;

// RAILWAY POSTGRESQL DATABASE POOL
const db = process.env.DATABASE_URL ? new Pool({
  connectionString: process.env.DATABASE_URL,
  ssl: process.env.DATABASE_URL.includes('localhost') ? false : { rejectUnauthorized: false }
}) : null;

// IN-MEMORY FALLBACK STORAGE FOR ROOMS & USERS
const rooms = new Map();
const usersDb = new Map();

// VERIFY & INIT DB TABLES
async function initDbTables() {
  if (db) {
    try {
      await db.query(`
        CREATE TABLE IF NOT EXISTS room_lobbies (
          room_code VARCHAR(10) PRIMARY KEY,
          host_nickname VARCHAR(100) NOT NULL,
          deck_id VARCHAR(50) DEFAULT 'deck_a',
          players_json TEXT NOT NULL,
          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
      `);
      console.log('Railway PostgreSQL room_lobbies table verified.');
    } catch(e) {
      console.error('Failed to init room_lobbies table:', e.message);
    }
  }
}
initDbTables();

function hashPassword(password) {
  return crypto.createHash('sha256').update(password + '_roadtrip_salt_2026').digest('hex');
}

function generateRoomCode() {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
  let code = '';
  for (let i = 0; i < 4; i++) {
    code += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return code;
}

// ENABLE CORS & JSON PARSING
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  if (req.method === 'OPTIONS') return res.sendStatus(200);
  next();
});

app.use(express.json());

// HEALTH CHECK
app.get('/health', async (req, res) => {
  let dbStatus = 'disconnected (in-memory mode)';
  if (db) {
    try {
      await db.query('SELECT 1');
      dbStatus = 'connected (Railway PostgreSQL)';
    } catch (e) {
      dbStatus = 'error: ' + e.message;
    }
  }
  res.json({
    status: 'ok',
    database: dbStatus,
    activeRooms: rooms.size,
    timestamp: new Date().toISOString()
  });
});

// -------------------------------------------------------------
// USER ACCOUNTS AUTHENTICATION & LEADERBOARD ENDPOINTS
// -------------------------------------------------------------

// 1. REGISTER USER ACCOUNT ON RAILWAY POSTGRESQL
app.post('/api/auth/register', async (req, res) => {
  const { username, password, nickname } = req.body;
  if (!username || !password) {
    return res.status(400).json({ success: false, error: 'Username and password are required' });
  }

  const cleanUser = username.toLowerCase().trim();
  const passHash = hashPassword(password);
  const displayNick = nickname || username;

  if (db) {
    try {
      const existing = await db.query('SELECT id FROM users WHERE username = $1', [cleanUser]);
      if (existing.rows.length > 0) {
        return res.status(400).json({ success: false, error: 'Username is already registered' });
      }

      const result = await db.query(
        `INSERT INTO users (username, password_hash, nickname, level, xp, earned_badges, streak_days, bingos_won, games_played, squares_found)
         VALUES ($1, $2, $3, 1, 0, 'first_ride', 1, 0, 0, 0)
         RETURNING id, username, nickname, level, xp, earned_badges, streak_days, bingos_won, games_played, squares_found`,
        [cleanUser, passHash, displayNick]
      );

      const u = result.rows[0];
      return res.json({
        success: true,
        user: {
          id: u.id.toString(),
          username: u.username,
          nickname: u.nickname,
          level: u.level,
          xp: u.xp,
          earnedBadges: u.earned_badges ? u.earned_badges.split(',') : ['first_ride'],
          streakDays: u.streak_days || 1,
          bingosWon: u.bingos_won || 0,
          gamesPlayed: u.games_played || 0,
          squaresFound: u.squares_found || 0
        }
      });
    } catch (err) {
      console.error('DB Register Error:', err);
      return res.status(500).json({ success: false, error: 'Database registration failed' });
    }
  } else {
    // IN-MEMORY FALLBACK REGISTER
    if (usersDb.has(cleanUser)) {
      return res.status(400).json({ success: false, error: 'Username is already registered' });
    }

    const newUser = {
      id: 'usr_' + Date.now(),
      username: cleanUser,
      passwordHash: passHash,
      nickname: displayNick,
      level: 1,
      xp: 0,
      earnedBadges: ['first_ride'],
      streakDays: 1,
      bingosWon: 0,
      gamesPlayed: 0,
      squaresFound: 0
    };

    usersDb.set(cleanUser, newUser);
    return res.json({
      success: true,
      user: {
        id: newUser.id,
        username: newUser.username,
        nickname: newUser.nickname,
        level: newUser.level,
        xp: newUser.xp,
        earnedBadges: newUser.earnedBadges,
        streakDays: newUser.streakDays,
        bingosWon: newUser.bingosWon,
        gamesPlayed: newUser.gamesPlayed,
        squaresFound: newUser.squaresFound
      }
    });
  }
});

// 2. LOG IN USER ACCOUNT & FETCH DATA FROM RAILWAY POSTGRESQL
app.post('/api/auth/login', async (req, res) => {
  const { username, password } = req.body;
  if (!username || !password) {
    return res.status(400).json({ success: false, error: 'Username and password are required' });
  }

  const cleanUser = username.toLowerCase().trim();
  const passHash = hashPassword(password);

  if (db) {
    try {
      const result = await db.query(
        'SELECT id, username, password_hash, nickname, level, xp, earned_badges, streak_days, bingos_won, games_played, squares_found FROM users WHERE username = $1',
        [cleanUser]
      );

      if (result.rows.length === 0) {
        return res.status(401).json({ success: false, error: 'Account not found. Please register first.' });
      }

      const u = result.rows[0];
      if (u.password_hash !== passHash) {
        return res.status(401).json({ success: false, error: 'Invalid password. Please try again.' });
      }

      return res.json({
        success: true,
        user: {
          id: u.id.toString(),
          username: u.username,
          nickname: u.nickname,
          level: u.level,
          xp: u.xp,
          earnedBadges: u.earned_badges ? u.earned_badges.split(',') : ['first_ride'],
          streakDays: u.streak_days || 1,
          bingosWon: u.bingos_won || 0,
          gamesPlayed: u.games_played || 0,
          squaresFound: u.squares_found || 0
        }
      });
    } catch (err) {
      console.error('DB Login Error:', err);
      return res.status(500).json({ success: false, error: 'Database login query failed' });
    }
  } else {
    // IN-MEMORY FALLBACK LOGIN
    let u = usersDb.get(cleanUser);
    if (!u) {
      u = {
        id: 'usr_' + Date.now(),
        username: cleanUser,
        passwordHash: passHash,
        nickname: username,
        level: 1,
        xp: 0,
        earnedBadges: ['first_ride'],
        streakDays: 1,
        bingosWon: 0,
        gamesPlayed: 0,
        squaresFound: 0
      };
      usersDb.set(cleanUser, u);
    }

    if (u.passwordHash !== passHash) {
      return res.status(401).json({ success: false, error: 'Invalid password. Please try again.' });
    }

    return res.json({
      success: true,
      user: {
        id: u.id,
        username: u.username,
        nickname: u.nickname,
        level: u.level,
        xp: u.xp,
        earnedBadges: u.earnedBadges,
        streakDays: u.streakDays,
        bingosWon: u.bingosWon,
        gamesPlayed: u.gamesPlayed,
        squaresFound: u.squaresFound
      }
    });
  }
});

// 3. FULL USER ACCOUNT DATA SAVE / SYNC TO RAILWAY POSTGRESQL DB
app.post('/api/auth/save-profile', async (req, res) => {
  const { username, level, xp, earnedBadges, streakDays, bingosWon, gamesPlayed, squaresFound } = req.body;
  if (!username) {
    return res.status(400).json({ success: false, error: 'Username required' });
  }

  const cleanUser = username.toLowerCase().trim();
  const calculatedLevel = level || Math.floor((xp || 0) / 100) + 1;
  const badgeStr = Array.isArray(earnedBadges) ? earnedBadges.join(',') : (earnedBadges || 'first_ride');

  if (db) {
    try {
      const result = await db.query(
        `UPDATE users
         SET level = $1,
             xp = $2,
             earned_badges = $3,
             streak_days = $4,
             bingos_won = $5,
             games_played = $6,
             squares_found = $7,
             updated_at = CURRENT_TIMESTAMP
         WHERE username = $8
         RETURNING id, username, nickname, level, xp, earned_badges, streak_days, bingos_won, games_played, squares_found`,
        [calculatedLevel, xp || 0, badgeStr, streakDays || 1, bingosWon || 0, gamesPlayed || 0, squaresFound || 0, cleanUser]
      );

      if (result.rows.length === 0) {
        return res.status(404).json({ success: false, error: 'User not found' });
      }

      const u = result.rows[0];
      return res.json({
        success: true,
        user: {
          id: u.id.toString(),
          username: u.username,
          nickname: u.nickname,
          level: u.level,
          xp: u.xp,
          earnedBadges: u.earned_badges ? u.earned_badges.split(',') : ['first_ride'],
          streakDays: u.streak_days || 1,
          bingosWon: u.bingos_won || 0,
          gamesPlayed: u.games_played || 0,
          squaresFound: u.squares_found || 0
        }
      });
    } catch (err) {
      console.error('DB Save Profile Error:', err);
      return res.status(500).json({ success: false, error: 'Save profile failed' });
    }
  } else {
    const u = usersDb.get(cleanUser);
    if (u) {
      u.level = calculatedLevel;
      u.xp = xp || 0;
      u.earnedBadges = Array.isArray(earnedBadges) ? earnedBadges : ['first_ride'];
      u.streakDays = streakDays || 1;
      u.bingosWon = bingosWon || 0;
      u.gamesPlayed = gamesPlayed || 0;
      u.squaresFound = squaresFound || 0;
      return res.json({ success: true, user: u });
    }
    return res.status(404).json({ success: false, error: 'User not found' });
  }
});

// 4. REAL USERS LEADERBOARD FROM RAILWAY POSTGRESQL DB (ZERO FAKE BOTS!)
app.get('/api/auth/leaderboard', async (req, res) => {
  if (db) {
    try {
      const pointsResult = await db.query(
        'SELECT nickname, level, xp, squares_found FROM users ORDER BY xp DESC LIMIT 20'
      );
      const itemsResult = await db.query(
        'SELECT nickname, level, xp, squares_found FROM users ORDER BY squares_found DESC LIMIT 20'
      );

      return res.json({
        success: true,
        pointsList: pointsResult.rows.map((u, i) => ({
          rank: i + 1,
          name: u.nickname,
          emoji: '🐻',
          level: u.level || 1,
          val: u.xp || 0
        })),
        itemsList: itemsResult.rows.map((u, i) => ({
          rank: i + 1,
          name: u.nickname,
          emoji: '🐻',
          level: u.level || 1,
          val: u.squares_found || 0
        }))
      });
    } catch (err) {
      console.error('DB Leaderboard Query Error:', err);
    }
  }

  // IN-MEMORY REAL LEADERBOARD
  const allUsers = Array.from(usersDb.values());
  const pointsList = allUsers.sort((a,b) => b.xp - a.xp).map((u, i) => ({
    rank: i + 1,
    name: u.nickname,
    emoji: '🐻',
    level: u.level || 1,
    val: u.xp || 0
  }));
  const itemsList = allUsers.sort((a,b) => b.squaresFound - a.squaresFound).map((u, i) => ({
    rank: i + 1,
    name: u.nickname,
    emoji: '🐻',
    level: u.level || 1,
    val: u.squaresFound || 0
  }));

  res.json({ success: true, pointsList, itemsList });
});

// -------------------------------------------------------------
// MULTIPLAYER ROOMS REST API & WEBSOCKET ENDPOINTS FOR LIVE IN-CAR GAMEPLAY
// -------------------------------------------------------------
app.post('/api/rooms/create', async (req, res) => {
  const { code, hostNickname, deckId } = req.body;
  const roomCode = (code || generateRoomCode()).toUpperCase().trim();
  const hostNick = hostNickname || 'Host Passenger';
  const deck = deckId || 'deck_a';
  const initialPlayers = [{ nickname: hostNick, isHost: true, completedTiles: [12] }];

  if (db) {
    try {
      const existing = await db.query('SELECT players_json FROM room_lobbies WHERE room_code = $1', [roomCode]);
      if (existing.rows.length > 0) {
        let players = JSON.parse(existing.rows[0].players_json);
        if (!players.some(p => p.nickname === hostNick)) {
          players.push({ nickname: hostNick, isHost: true, completedTiles: [12] });
        }
        await db.query(
          'UPDATE room_lobbies SET host_nickname = $1, deck_id = $2, players_json = $3, updated_at = CURRENT_TIMESTAMP WHERE room_code = $4',
          [hostNick, deck, JSON.stringify(players), roomCode]
        );
        return res.json({ success: true, room: { code: roomCode, hostNickname: hostNick, deckId: deck, players } });
      } else {
        await db.query(
          'INSERT INTO room_lobbies (room_code, host_nickname, deck_id, players_json) VALUES ($1, $2, $3, $4)',
          [roomCode, hostNick, deck, JSON.stringify(initialPlayers)]
        );
        return res.json({ success: true, room: { code: roomCode, hostNickname: hostNick, deckId: deck, players: initialPlayers } });
      }
    } catch(err) {
      console.error('DB Create Room Error:', err);
    }
  }

  // IN-MEMORY FALLBACK
  let room = rooms.get(roomCode);
  if (!room) {
    room = {
      code: roomCode,
      hostNickname: hostNick,
      deckId: deck,
      players: initialPlayers
    };
    rooms.set(roomCode, room);
  } else {
    if (!room.players.some(p => p.nickname === hostNick)) {
      room.players.push({ nickname: hostNick, isHost: true, completedTiles: [12] });
    }
  }

  res.json({ success: true, room });
});

app.post('/api/rooms/join', async (req, res) => {
  const { code, nickname } = req.body;
  if (!code || !nickname) {
    return res.status(400).json({ success: false, error: 'Code and nickname required' });
  }

  const upperCode = code.toUpperCase().trim();
  const playerNick = nickname.trim();

  if (db) {
    try {
      const existing = await db.query('SELECT host_nickname, deck_id, players_json FROM room_lobbies WHERE room_code = $1', [upperCode]);
      if (existing.rows.length > 0) {
        let players = JSON.parse(existing.rows[0].players_json);
        if (!players.some(p => p.nickname === playerNick)) {
          players.push({ nickname: playerNick, isHost: false, completedTiles: [12] });
          await db.query(
            'UPDATE room_lobbies SET players_json = $1, updated_at = CURRENT_TIMESTAMP WHERE room_code = $2',
            [JSON.stringify(players), upperCode]
          );
        }
        return res.json({ success: true, room: { code: upperCode, hostNickname: existing.rows[0].host_nickname, deckId: existing.rows[0].deck_id, players } });
      } else {
        // Room created dynamically when joined
        const initialPlayers = [{ nickname: playerNick, isHost: true, completedTiles: [12] }];
        await db.query(
          'INSERT INTO room_lobbies (room_code, host_nickname, deck_id, players_json) VALUES ($1, $2, $3, $4)',
          [upperCode, playerNick, 'deck_a', JSON.stringify(initialPlayers)]
        );
        return res.json({ success: true, room: { code: upperCode, hostNickname: playerNick, deckId: 'deck_a', players: initialPlayers } });
      }
    } catch(err) {
      console.error('DB Join Room Error:', err);
    }
  }

  // IN-MEMORY FALLBACK
  let room = rooms.get(upperCode);
  if (!room) {
    room = {
      code: upperCode,
      hostNickname: playerNick,
      deckId: 'deck_a',
      players: [{ nickname: playerNick, isHost: true, completedTiles: [12] }]
    };
    rooms.set(upperCode, room);
  } else {
    if (!room.players.some(p => p.nickname === playerNick)) {
      room.players.push({ nickname: playerNick, isHost: false, completedTiles: [12] });
    }
  }

  res.json({ success: true, room });
});

app.post('/api/rooms/get', async (req, res) => {
  const { code } = req.body;
  const upperCode = (code || '').toUpperCase().trim();

  if (db) {
    try {
      const result = await db.query('SELECT host_nickname, deck_id, players_json FROM room_lobbies WHERE room_code = $1', [upperCode]);
      if (result.rows.length > 0) {
        const u = result.rows[0];
        return res.json({
          success: true,
          room: {
            code: upperCode,
            hostNickname: u.host_nickname,
            deckId: u.deck_id,
            players: JSON.parse(u.players_json)
          }
        });
      }
    } catch(err) {
      console.error('DB Get Room Error:', err);
    }
  }

  const room = rooms.get(upperCode);
  if (room) {
    res.json({ success: true, room });
  } else {
    res.status(404).json({ success: false, error: 'Room not found' });
  }
});

app.post('/api/rooms/update-tiles', async (req, res) => {
  const { code, nickname, completedTiles } = req.body;
  const upperCode = (code || '').toUpperCase().trim();

  if (db) {
    try {
      const result = await db.query('SELECT host_nickname, deck_id, players_json FROM room_lobbies WHERE room_code = $1', [upperCode]);
      if (result.rows.length > 0) {
        let players = JSON.parse(result.rows[0].players_json);
        const player = players.find(p => p.nickname === nickname);
        if (player) {
          player.completedTiles = completedTiles || [12];
          await db.query(
            'UPDATE room_lobbies SET players_json = $1, updated_at = CURRENT_TIMESTAMP WHERE room_code = $2',
            [JSON.stringify(players), upperCode]
          );
        }
        return res.json({
          success: true,
          room: {
            code: upperCode,
            hostNickname: result.rows[0].host_nickname,
            deckId: result.rows[0].deck_id,
            players
          }
        });
      }
    } catch(err) {
      console.error('DB Update Tiles Error:', err);
    }
  }

  const room = rooms.get(upperCode);
  if (room) {
    const player = room.players.find(p => p.nickname === nickname);
    if (player) {
      player.completedTiles = completedTiles || [12];
    }
    res.json({ success: true, room });
  } else {
    res.status(404).json({ success: false, error: 'Room not found' });
  }
});

wss.on('connection', (ws) => {
  ws.on('message', (message) => {
    try {
      const data = JSON.parse(message);
      if (data.type === 'JOIN_ROOM') {
        const upperCode = (data.code || '').toUpperCase().trim();
        const room = rooms.get(upperCode);
        if (room) {
          if (!room.players.some(p => p.nickname === data.nickname)) {
            room.players.push({ nickname: data.nickname, isHost: false, completedTiles: [12] });
          }
          ws.roomCode = upperCode;
          ws.send(JSON.stringify({ type: 'ROOM_JOINED', room }));
        }
      } else if (data.type === 'UPDATE_TILES') {
        const room = rooms.get(ws.roomCode);
        if (room) {
          const player = room.players.find(p => p.nickname === data.nickname);
          if (player) {
            player.completedTiles = data.completedTiles || [12];
          }
          // Broadcast updated room state to all connected passengers
          wss.clients.forEach(client => {
            if (client.roomCode === ws.roomCode && client.readyState === 1) {
              client.send(JSON.stringify({ type: 'ROOM_UPDATED', room }));
            }
          });
        }
      }
    } catch(err) {
      console.error('WS Error:', err);
    }
  });
});

server.listen(PORT, () => {
  console.log(`Road Trip Bingo Railway Server running on port ${PORT}`);
});
