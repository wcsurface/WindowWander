// ROAD TRIP BINGO - WEB APPLICATION ENGINE

const DECKS = [
  { id: 'deck_a', title: 'Classic Road Trip', audience: 'Families & Kids', count: 42, icon: '🚗', bg: 'linear-gradient(135deg, #2563EB, #1E293B)' },
  { id: 'deck_b', title: 'Weird Road Trip', audience: 'Teens & Adults', count: 42, icon: '🤪', bg: 'linear-gradient(135deg, #F97316, #1E293B)' },
  { id: 'deck_c', title: 'Mountain West & Rockies', audience: 'Everyone', count: 42, icon: '🏔️', bg: 'linear-gradient(135deg, #0D9488, #1E293B)' },
  { id: 'deck_d', title: 'Desert & Southwest', audience: 'Everyone', count: 42, icon: '🌵', bg: 'linear-gradient(135deg, #F59E0B, #1E293B)' },
  { id: 'deck_e', title: 'Northeast & Mid-Atlantic', audience: 'Everyone', count: 42, icon: '🌉', bg: 'linear-gradient(135deg, #3B82F6, #1E293B)' },
  { id: 'deck_f', title: 'The South', audience: 'Everyone', count: 42, icon: '🌳', bg: 'linear-gradient(135deg, #10B981, #1E293B)' },
  { id: 'deck_g', title: 'West Coast', audience: 'Everyone', count: 42, icon: '🏄', bg: 'linear-gradient(135deg, #14B8A6, #1E293B)' },
  { id: 'deck_h', title: 'Heart of the Country', audience: 'Everyone', count: 42, icon: '🌽', bg: 'linear-gradient(135deg, #EAB308, #1E293B)' }
];

const GAME_MODES = [
  { id: 'classic', title: 'Classic Line Bingo', desc: 'Complete a horizontal, vertical, or diagonal line.' },
  { id: 'two_lines', title: 'Two Lines Bingo', desc: 'Complete two winning lines.' },
  { id: 'blackout', title: 'Blackout Bingo', desc: 'Find all 24 discoverable items and fill your card.' }
];

const MOUNTAIN_WEST_PROMPTS = [
  "Ski Rack on Car",
  "Snow-Capped Peak",
  "Wildlife Crossing Sign",
  "Elk or Deer in Distance",
  "Mountain Pass Sign",
  "Trailhead Parking Sign",
  "River Rafting Billboard",
  "National Forest Boundary",
  "Rock Formation Cliffs",
  "Evergreen Forest",
  "Dust-Covered 4x4",
  "Overland Camper Van",
  "Fly Fishing Access",
  "Wildfire Danger Sign",
  "Winding Canyon Road",
  "Mountain Lake",
  "Ranch Gate Structure",
  "Cattle Guard in Road",
  "Log Cabin Lodge",
  "Snowplow",
  "Ski Resort Chairlift",
  "Continental Divide Sign",
  "Moose Crossing Warning",
  "Timber Hauling Truck",
  "Bouldering Crash Pad",
  "Runaway Truck Ramp",
  "Hot Springs Advert",
  "Avalanche Chute Sign",
  "Ghost Town Landmark",
  "Mountain Bike Trailhead",
  "High Altitude Prairie",
  "Park Ranger Truck",
  "Geology Info Board",
  "Weather Station",
  "Frozen Waterfall",
  "Gold Panning Sign",
  "Fly Shop Trout Statue",
  "Hay Bale Stacks",
  "Rodeo Arena Grounds",
  "Wild Mustang Crossing",
  "Summit Pass Flag",
  "Mountain Solar Farm"
];

class AudioEngine {
  static playPop() {
    try {
      const ctx = new (window.AudioContext || window.webkitAudioContext)();
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();
      osc.type = 'triangle';
      osc.frequency.setValueAtTime(523.25, ctx.currentTime);
      osc.frequency.exponentialRampToValueAtTime(783.99, ctx.currentTime + 0.12);
      gain.gain.setValueAtTime(0.2, ctx.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.12);
      osc.connect(gain);
      gain.connect(ctx.destination);
      osc.start();
      osc.stop(ctx.currentTime + 0.12);
    } catch(e) {}
  }
}

class BingoWebEngine {
  static generateBoard(deckId = 'deck_c') {
    const prompts = MOUNTAIN_WEST_PROMPTS;
    const shuffled = [...prompts].sort(() => 0.5 - Math.random());
    const tiles = [];
    let promptIdx = 0;

    for (let i = 0; i < 25; i++) {
      if (i === 12) {
        tiles.push({ index: 12, name: 'FREE SPACE', isCenter: true, completed: true });
      } else {
        tiles.push({ index: i, name: shuffled[promptIdx++] || `Sight #${i}`, isCenter: false, completed: false });
      }
    }
    return tiles;
  }

  static checkWin(tiles, mode = 'classic') {
    const grid = Array(5).fill(0).map(() => Array(5).fill(false));
    tiles.forEach(t => { if (t.completed) grid[Math.floor(t.index/5)][t.index%5] = true; });

    let lines = 0;
    for (let r=0; r<5; r++) if (grid[r].every(Boolean)) lines++;
    for (let c=0; c<5; c++) if ([0,1,2,3,4].every(r => grid[r][c])) lines++;
    if ([0,1,2,3,4].every(i => grid[i][i])) lines++;
    if ([0,1,2,3,4].every(i => grid[i][4-i])) lines++;

    if (mode === 'two_lines') return lines >= 2;
    if (mode === 'blackout') return tiles.every(t => t.completed);
    return lines >= 1;
  }
}

class Router {
  constructor() {
    this.currentScreen = 'home';
    this.selectedDeck = DECKS[2]; // Defaulted to local region deck (Mountain West)
    this.selectedMode = GAME_MODES[0];
    this.board = BingoWebEngine.generateBoard(this.selectedDeck.id);
    this.history = [];
    this.roomCode = 'ROAD';
    this.showInCarModal = false;
    this.showJoinCodeInput = false;
    this.showOnlineModal = false;
    this.isSearchingOpponent = false;
    this.opponentName = null;
    this.leaderboardTab = 0;
    this.showAccountModal = false;
    this.isLoggedIn = false;
    this.userNickname = 'Road Explorer';
    this.stats = JSON.parse(localStorage.getItem('bingo_stats') || '{"xp":250,"games":3,"bingos":1,"items":18}');

    // REAL-TIME OPPONENT LIVE BOARD TILES
    this.op1Tiles = [0, 4, 12, 18, 22];
    this.op2Tiles = [2, 7, 12, 14, 20, 24];

    setInterval(() => {
      if (this.currentScreen === 'game') {
        if (this.op1Tiles.length < 20) {
          const rem1 = Array.from({length:25},(_,i)=>i).filter(i => !this.op1Tiles.includes(i));
          if (rem1.length) this.op1Tiles.push(rem1[Math.floor(Math.random()*rem1.length)]);
        }
        if (this.op2Tiles.length < 20) {
          const rem2 = Array.from({length:25},(_,i)=>i).filter(i => !this.op2Tiles.includes(i));
          if (rem2.length) this.op2Tiles.push(rem2[Math.floor(Math.random()*rem2.length)]);
        }
        this.render();
      }
    }, 4000);
  }

  navigate(screen) {
    this.currentScreen = screen;
    this.updateActiveNav();
    this.render();
  }

  updateActiveNav() {
    ['home', 'login', 'shop', 'settings'].forEach(s => {
      const btn = document.getElementById(`nav-${s}`);
      if (btn) {
        const wrapper = btn.querySelector('.nav-icon-wrapper');
        if (s === this.currentScreen) {
          btn.classList.add('active');
          if (wrapper) wrapper.classList.add('active');
        } else {
          btn.classList.remove('active');
          if (wrapper) wrapper.classList.remove('active');
        }
      }
    });
  }

  selectDeckAndPlay(deck) {
    this.selectedDeck = deck;
    this.startCarRoom();
  }

  startCarRoomFromHome() {
    this.selectedDeck = DECKS[2];
    this.startCarRoom();
  }

  startOnlineMatchmaking() {
    this.showOnlineModal = false;
    this.isSearchingOpponent = true;
    this.opponentName = null;
    this.render();

    setTimeout(() => {
      const opponents = ["Alex (Passenger)", "Jordan (RV Traveler)", "Taylor (Backseat)", "Morgan (Roadie)"];
      this.opponentName = opponents[Math.floor(Math.random() * opponents.length)];
      this.render();

      setTimeout(() => {
        this.isSearchingOpponent = false;
        this.board = BingoWebEngine.generateBoard(this.selectedDeck.id);
        this.navigate('game');
      }, 1200);
    }, 2000);
  }

  cancelMatchmaking() {
    this.isSearchingOpponent = false;
    this.opponentName = null;
    this.render();
  }

  startCarRoom() {
    const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
    let code = '';
    for (let i = 0; i < 4; i++) code += chars.charAt(Math.floor(Math.random() * chars.length));
    this.roomCode = code;
    this.showInCarModal = false;
    this.showJoinCodeInput = false;
    this.navigate('multiplayer');
  }

  joinCarRoom(code) {
    if (!code || code.length < 4) return;
    this.roomCode = code.toUpperCase().trim();
    this.showInCarModal = false;
    this.showJoinCodeInput = false;
    this.navigate('multiplayer');
  }

  toggleTile(index, event) {
    if (index === 12) return;

    // SAVE HISTORY FOR UNDO FEATURE
    this.history.push(JSON.parse(JSON.stringify(this.board)));

    this.board[index].completed = !this.board[index].completed;

    if (this.board[index].completed) {
      AudioEngine.playPop();
      if (navigator.vibrate) navigator.vibrate(40);
      this.stats.items = (this.stats.items || 0) + 1;

      // TRIGGER SCREEN-WIDE UNBOUNDED STAR EXPLOSION BURST
      if (event) this.triggerStarExplosion(event.clientX, event.clientY);
    }

    if (BingoWebEngine.checkWin(this.board, this.selectedMode.id)) {
      this.stats.bingos += 1;
      this.stats.xp += 100;
      this.saveStats();
      if (!this.isLoggedIn) {
        this.showAccountModal = true;
      }
    }
    this.render();
  }

  triggerStarExplosion(x, y) {
    const icons = ['⭐', '✨', '🌟', '💎', '💥', '🎉', '💫', '🚀', '🔥', '⚡'];
    for (let i = 0; i < 42; i++) {
      const p = document.createElement('div');
      p.className = 'star-particle';
      p.innerText = icons[i % icons.length];
      p.style.left = `${x}px`;
      p.style.top = `${y}px`;
      const angle = (i * (360 / 42)) + (Math.random() * 12);
      const speed = 120 + Math.random() * 280;
      p.style.setProperty('--dx', `${Math.cos(angle * Math.PI / 180) * speed}px`);
      p.style.setProperty('--dy', `${Math.sin(angle * Math.PI / 180) * speed}px`);
      document.body.appendChild(p);
      setTimeout(() => p.remove(), 950);
    }
  }

  undoLastTap() {
    if (this.history.length > 0) {
      this.board = this.history.pop();
      this.render();
    }
  }

  loginUser(user, pass) {
    if (!user) return;
    this.isLoggedIn = true;
    this.userNickname = user;
    this.navigate('home');
  }

  logoutUser() {
    this.isLoggedIn = false;
    this.userNickname = 'Road Explorer';
    this.navigate('home');
  }

  saveStats() {
    localStorage.setItem('bingo_stats', JSON.stringify(this.stats));
  }

  render() {
    const main = document.getElementById('main-content');
    if (!main) return;

    this.updateActiveNav();

    // UPDATE FOOTER NAV BUTTON LABEL FOR LOGIN / LOGOUT
    const navLoginBtn = document.getElementById('nav-login-label');
    if (navLoginBtn) {
      navLoginBtn.innerText = this.isLoggedIn ? 'Log Out' : 'Log In';
    }

    // MAIN LOBBY BACKGROUND THEME USED ON HOME, LOGIN, AND SHOP
    if (['home', 'login', 'shop'].includes(this.currentScreen)) {
      document.body.classList.add('main-home-theme');
      document.body.classList.remove('deck-game-theme');
    } else if (this.currentScreen === 'game') {
      document.body.classList.remove('main-home-theme');
      document.body.classList.add('deck-game-theme');
      const deckMap = {
        'deck_a': 'bg_classic_roadtrip.jpg',
        'deck_b': 'bg_weird_roadtrip.jpg',
        'deck_c': 'bg_rocky_mountains.jpg',
        'deck_d': 'bg_desert_southwest.jpg',
        'deck_e': 'bg_northeast.jpg',
        'deck_f': 'bg_south.jpg',
        'deck_g': 'bg_west_coast.jpg',
        'deck_h': 'bg_heartland.jpg'
      };
      const bgImg = deckMap[this.selectedDeck.id] || 'bg_classic_roadtrip.jpg';
      document.body.style.backgroundImage = `url('${bgImg}')`;
    } else {
      document.body.classList.remove('main-home-theme');
      document.body.classList.remove('deck-game-theme');
      document.body.style.backgroundImage = '';
    }

    if (this.currentScreen === 'home') {
      const level = Math.floor(this.stats.xp / 100) + 1;
      main.innerHTML = `
        <div class="user-header">
          <div class="profile-badge">
            <div class="avatar-circle">🐻</div>
            <div>
              <div style="font-weight:900;font-size:13px;color:white;">${this.userNickname}</div>
              <div style="font-size:10px;color:#FBBF24;font-weight:800;">Lvl ${level} Explorer • ${this.stats.xp} XP</div>
            </div>
          </div>
          <!-- COINS PILL ONLY (SETTINGS GEAR REMOVED FROM TOP RIGHT) -->
          <div class="currency-pill">🪙 1,250</div>
        </div>

        <!-- CENTERED OFFICIAL LOGO FLOATING DIRECTLY ON MAIN LOBBY BACKGROUND -->
        <div style="width:100%;height:220px;display:flex;align-items:center;justify-content:center;margin-bottom:16px;background:transparent;">
          <img src="road_trip_bingo_animated_logo.gif" alt="Road Trip Bingo Animated Logo" style="width:100%;height:100%;object-fit:contain;filter:drop-shadow(0 10px 20px rgba(0,0,0,0.3));" />
        </div>

        <!-- ⚡ PLAY NOW ▶ LARGE CREAM CAPSULE BUTTON -->
        <button class="btn btn-mockup-play" onclick="router.showInCarModal=true;router.render();" style="margin-bottom:18px;">
          ⚡ PLAY NOW ▶
        </button>

        <!-- TWO CARTOON ROAD SIGN CARDS -->
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:14px;margin-bottom:14px;">
          <!-- MULTIPLAYER ARENA (KEEPS LIVE TEXT) -->
          <div class="road-sign-card sign-blue" onclick="router.showOnlineModal=true;router.render();" style="cursor:pointer;">
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <span style="font-size:26px;">🌐</span>
              <span class="sign-badge">LIVE</span>
            </div>
            <div style="font-weight:900;font-size:16px;margin-top:10px;">Multiplayer Arena</div>
            <div style="font-size:11px;color:#E2E8F0;">Online Now!</div>
          </div>

          <!-- DECK LIBRARY (NO ORANGE HIGHLIGHTED BADGE AT ALL) -->
          <div class="road-sign-card sign-teal" onclick="router.navigate('decks')" style="cursor:pointer;">
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <span style="font-size:26px;">🎴</span>
            </div>
            <div style="font-weight:900;font-size:16px;margin-top:10px;">Deck Library</div>
            <div style="font-size:11px;color:#E2E8F0;">8 Decks!</div>
          </div>
        </div>

        <!-- LEADERBOARD BUTTON BETWEEN MULTIPLAYER/DECK AND SAFETY BANNER (TOP CROWN REMOVED, ENLARGED TROPHY & TEXT) -->
        <div class="road-sign-card" onclick="router.navigate('leaderboard')" style="cursor:pointer;background:linear-gradient(180deg, #1D4ED8, #1E3A8A);margin-bottom:16px;padding:22px 20px;">
          <div style="display:flex;justify-content:flex-end;align-items:center;margin-bottom:4px;">
            <span class="sign-badge" style="background:#FBBF24;color:#0F172A;">RANKS</span>
          </div>
          <div style="font-weight:900;font-size:22px;color:white;">🏆 LEADERBOARD</div>
          <div style="font-size:13px;color:#E2E8F0;font-weight:700;">Points & Items Found Rankings</div>
        </div>

        <!-- ORANGE PILL SAFETY BANNER -->
        <div class="safety-banner-pill" style="margin-bottom:16px;">
          Passengers Play. Drivers Drive.
        </div>

        <!-- IN-CAR PLAY MODAL -->
        ${this.showInCarModal ? `
          <div style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.88);display:flex;align-items:center;justify-content:center;padding:20px;z-index:300;" onclick="router.showInCarModal=false;router.showJoinCodeInput=false;router.render();">
            <div class="card" style="text-align:center;max-width:380px;width:100%;padding:24px;" onclick="event.stopPropagation();">
              <h2 style="font-weight:900;color:#FBBF24;margin-bottom:6px;">🚗 IN-CAR PLAY</h2>
              <p style="font-size:12px;color:#CBD5E1;margin-bottom:18px;">Play bingo with passengers in your car!</p>

              ${!this.showJoinCodeInput ? `
                <button class="btn btn-mockup-play" style="margin-bottom:12px;font-size:16px;" onclick="router.startCarRoomFromHome()">
                  🚗 START A GAME (HOST)
                </button>
                <button class="btn btn-teal btn-beveled" style="margin-bottom:14px;font-size:16px;" onclick="router.showJoinCodeInput=true;router.render();">
                  📱 JOIN A GAME (CODE)
                </button>
                <button style="background:none;border:none;color:#94A3B8;font-size:12px;font-weight:700;cursor:pointer;" onclick="router.showInCarModal=false;router.navigate('game');">
                  Play Solo Single-Player ▶
                </button>
              ` : `
                <p style="font-size:13px;font-weight:800;color:white;margin-bottom:8px;">Enter 4-Letter Code from Host:</p>
                <input id="in-car-code-input" type="text" maxlength="4" placeholder="e.g. ROAD" style="width:100%;height:48px;border-radius:12px;border:2px solid #FBBF24;background:#0F172A;color:white;font-size:20px;font-weight:900;text-align:center;text-transform:uppercase;margin-bottom:14px;" />
                <button class="btn btn-teal btn-beveled" style="margin-bottom:10px;" onclick="router.joinCarRoom(document.getElementById('in-car-code-input').value)">
                  JOIN CAR ROOM ▶
                </button>
                <button style="background:none;border:none;color:#94A3B8;font-size:12px;font-weight:700;cursor:pointer;" onclick="router.showJoinCodeInput=false;router.render();">
                  ◀ Back
                </button>
              `}
            </div>
          </div>
        ` : ''}

        <!-- ONLINE MULTIPLAYER MODAL OVERLAY -->
        ${this.showOnlineModal && !this.isSearchingOpponent ? `
          <div style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.88);display:flex;align-items:center;justify-content:center;padding:20px;z-index:300;" onclick="router.showOnlineModal=false;router.render();">
            <div class="card" style="text-align:center;max-width:380px;width:100%;padding:24px;" onclick="event.stopPropagation();">
              <h2 style="font-weight:900;color:#FBBF24;margin-bottom:6px;">🌐 ONLINE MULTIPLAYER</h2>
              <p style="font-size:12px;color:#CBD5E1;margin-bottom:16px;">Play Classic Bingo against online passengers across America!</p>

              <div style="margin-bottom:14px;text-align:left;">
                <label style="font-size:11px;font-weight:900;color:#F59E0B;display:block;margin-bottom:4px;">🎴 SELECT DECK / REGION</label>
                <select id="online-deck-select" onchange="router.selectedDeck=DECKS.find(d=>d.id===this.value)" style="width:100%;height:48px;border-radius:14px;background:#0F172A;color:white;border:2px solid #F59E0B;padding:0 12px;font-size:14px;font-weight:800;">
                  ${DECKS.map(d => `<option value="${d.id}" ${d.id===this.selectedDeck.id ? 'selected' : ''}>🎴 ${d.title} (${d.audience})</option>`).join('')}
                </select>
              </div>

              <div style="background:#0F172A;border:1.5px solid #2563EB;padding:12px;border-radius:12px;font-size:13px;font-weight:800;color:white;margin-bottom:18px;">
                🎯 Rule: Classic Bingo (Line Win)
              </div>

              <button class="btn btn-orange btn-beveled" style="font-size:18px;" onclick="router.startOnlineMatchmaking()">
                START GAME ▶
              </button>
            </div>
          </div>
        ` : ''}

        <!-- SEARCHING FOR OPPONENT STATUS OVERLAY -->
        ${this.isSearchingOpponent ? `
          <div style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.92);display:flex;align-items:center;justify-content:center;padding:20px;z-index:300;">
            <div class="card" style="text-align:center;max-width:380px;width:100%;padding:28px;">
              <div style="font-size:42px;margin-bottom:12px;">${this.opponentName ? '🎉' : '🔍'}</div>
              ${!this.opponentName ? `
                <h2 style="font-weight:900;color:#FBBF24;margin-bottom:6px;">SEARCHING FOR OPPONENT...</h2>
                <p style="font-size:12px;color:#CBD5E1;margin-bottom:18px;">Matching with another passenger traveling in America...</p>
                <button style="background:none;border:none;color:#94A3B8;font-size:12px;font-weight:700;cursor:pointer;" onclick="router.cancelMatchmaking()">Cancel Search</button>
              ` : `
                <h2 style="font-weight:900;color:#10B981;margin-bottom:6px;">OPPONENT FOUND!</h2>
                <p style="font-size:16px;font-weight:800;color:white;margin-bottom:6px;">Match: ${this.opponentName}</p>
                <p style="font-size:12px;color:#CBD5E1;">Loading Classic Bingo board...</p>
              `}
            </div>
          </div>
        ` : ''}
      `;
    } else if (this.currentScreen === 'login') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
          <h2 style="font-weight:900;color:white;">${this.isLoggedIn ? '👤 Account Profile' : '🔑 Log In / Join'}</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>

        <div style="width:100%;height:180px;display:flex;align-items:center;justify-content:center;margin-bottom:16px;background:transparent;">
          <img src="road_trip_bingo_animated_logo.gif" alt="Road Trip Bingo Animated Logo" style="width:100%;height:100%;object-fit:contain;filter:drop-shadow(0 10px 20px rgba(0,0,0,0.3));" />
        </div>

        ${!this.isLoggedIn ? `
          <div class="card" style="text-align:center;padding:24px;">
            <h3 style="font-weight:900;color:#FBBF24;margin-bottom:8px;">LOG IN TO YOUR ACCOUNT</h3>
            <p style="font-size:12px;color:#CBD5E1;margin-bottom:18px;">Enter your username and password to back up your level, XP, and bingo wins!</p>

            <input id="web-user-input" type="text" placeholder="Username / Email" style="width:100%;height:48px;border-radius:12px;border:2px solid #FBBF24;background:#0F172A;color:white;font-size:16px;font-weight:800;padding:0 14px;margin-bottom:12px;" />
            <input id="web-pass-input" type="password" placeholder="Password" style="width:100%;height:48px;border-radius:12px;border:2px solid #FBBF24;background:#0F172A;color:white;font-size:16px;font-weight:800;padding:0 14px;margin-bottom:18px;" />

            <button class="btn btn-mockup-play" style="min-height:54px;font-size:18px;margin-bottom:12px;" onclick="router.loginUser(document.getElementById('web-user-input').value, document.getElementById('web-pass-input').value)">
              🔑 LOG IN
            </button>
          </div>
        ` : `
          <div class="card" style="text-align:center;padding:24px;">
            <div style="font-size:38px;margin-bottom:10px;">🐻</div>
            <h3 style="font-weight:900;color:white;margin-bottom:4px;">${this.userNickname}</h3>
            <p style="font-size:12px;color:#FBBF24;font-weight:800;margin-bottom:16px;">Level ${Math.floor(this.stats.xp/100)+1} Explorer • ${this.stats.xp} XP</p>
            <div style="background:rgba(16,185,129,0.2);border:1.5px solid #10B981;border-radius:12px;padding:12px;color:#10B981;font-weight:900;font-size:13px;margin-bottom:20px;">
              ✅ Account Synced & Backed Up!
            </div>
            <button class="btn btn-orange" style="min-height:48px;font-size:15px;" onclick="router.logoutUser()">
              🚪 LOG OUT OF ACCOUNT
            </button>
          </div>
        `}
      `;
    } else if (this.currentScreen === 'shop') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
          <h2 style="font-weight:900;color:white;">🏪 Road Trip Shop</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>

        <div style="width:100%;height:180px;display:flex;align-items:center;justify-content:center;margin-bottom:16px;background:transparent;">
          <img src="road_trip_bingo_animated_logo.gif" alt="Road Trip Bingo Animated Logo" style="width:100%;height:100%;object-fit:contain;filter:drop-shadow(0 10px 20px rgba(0,0,0,0.3));" />
        </div>

        <!-- LARGE PROMINENT ROAD SIGN STYLE UNDER CONSTRUCTION CARD -->
        <div class="road-sign-card" style="background:linear-gradient(180deg, #D97706, #B45309, #78350F);padding:28px;margin-bottom:16px;text-align:center;">
          <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
            <span style="font-size:38px;">🏪</span>
            <span class="sign-badge" style="background:#F97316;color:white;font-size:12px;padding:4px 10px;">COMING SOON</span>
          </div>
          <h2 style="font-weight:900;font-size:22px;color:white;margin-bottom:10px;">🚧 UNDER CONSTRUCTION 🚧</h2>
          <p style="font-size:14px;font-weight:800;color:#FEF3C7;line-height:1.5;">
            The Road Trip Shop is opening soon!<br>
            Keep earning coins on your road trips to unlock custom vehicle skins, tile stamps, and avatars!
          </p>
        </div>
      `;
    } else if (this.currentScreen === 'leaderboard') {
      const level = Math.floor(this.stats.xp / 100) + 1;
      const pointsList = [
        { rank: 1, name: "RoadCaptain_99", emoji: "🦊", val: 4850 },
        { rank: 2, name: "NomadNate", emoji: "🦅", val: 3920 },
        { rank: 3, name: "PacificWanderer", emoji: "🏄", val: 3100 },
        { rank: 4, name: `${this.userNickname} (You)`, emoji: "🐻", val: this.stats.xp, isUser: true },
        { rank: 5, name: "SierraScout", emoji: "🌲", val: 1840 }
      ].sort((a,b) => b.val - a.val);

      const itemsList = [
        { rank: 1, name: "RoadCaptain_99", emoji: "🦊", val: 380 },
        { rank: 2, name: "NomadNate", emoji: "🦅", val: 295 },
        { rank: 3, name: `${this.userNickname} (You)`, emoji: "🐻", val: this.stats.items || 18, isUser: true },
        { rank: 4, name: "PacificWanderer", emoji: "🏄", val: 210 },
        { rank: 5, name: "SierraScout", emoji: "🌲", val: 145 }
      ].sort((a,b) => b.val - a.val);

      const activeList = this.leaderboardTab === 0 ? pointsList : itemsList;

      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
          <h2 style="font-weight:900;">🏆 Leaderboard</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>

        <div style="display:grid;grid-template-columns:1fr 1fr;gap:6px;background:#0F172A;padding:4px;border-radius:16px;margin-bottom:16px;">
          <button onclick="router.leaderboardTab=0;router.render();" style="height:42px;border-radius:12px;border:none;font-weight:900;font-size:13px;cursor:pointer;background:${this.leaderboardTab===0 ? '#FBBF24' : 'transparent'};color:${this.leaderboardTab===0 ? '#0F172A' : 'white'};">
            ⭐ Total Points (XP)
          </button>
          <button onclick="router.leaderboardTab=1;router.render();" style="height:42px;border-radius:12px;border:none;font-weight:900;font-size:13px;cursor:pointer;background:${this.leaderboardTab===1 ? '#FBBF24' : 'transparent'};color:${this.leaderboardTab===1 ? '#0F172A' : 'white'};">
            👀 Items Found
          </button>
        </div>

        ${activeList.map((item, idx) => `
          <div class="card" style="display:flex;justify-content:space-between;align-items:center;margin-bottom:10px;border:${item.isUser ? '2px solid #FBBF24' : '1px solid rgba(255,255,255,0.1)'};">
            <div style="display:flex;align-items:center;gap:10px;">
              <div style="width:32px;height:32px;border-radius:50%;background:${idx===0?'#EAB308':idx===1?'#94A3B8':idx===2?'#B45309':'#0F172A'};display:flex;align-items:center;justify-content:center;font-weight:900;font-size:12px;">#${idx+1}</div>
              <span style="font-size:22px;">${item.emoji}</span>
              <div>
                <strong style="font-size:14px;color:white;display:block;">${item.name}</strong>
                <span style="font-size:11px;color:#FBBF24;">Lvl ${level} Explorer</span>
              </div>
            </div>
            <div style="background:#0F172A;padding:6px 12px;border-radius:10px;font-weight:900;color:#10B981;font-size:13px;">
              ${item.val} ${this.leaderboardTab===0 ? 'XP' : 'Sights'}
            </div>
          </div>
        `).join('')}
      `;
    } else if (this.currentScreen === 'game') {
      const isWin = BingoWebEngine.checkWin(this.board, this.selectedMode.id);
      const completedCount = this.board.filter(t => t.completed).length;

      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
          <button class="back-pill" onclick="router.navigate('home')">◀ Exit</button>
          <div style="text-align:center;display:flex;align-items:center;gap:6px;">
            <span style="font-size:20px;">🐻</span>
            <div>
              <div style="font-weight:900;font-size:16px;">${this.selectedDeck.title}</div>
              <div style="font-size:11px;color:#FBBF24;font-weight:700;">${this.selectedMode.title}</div>
            </div>
          </div>
          <button class="back-pill" style="background:#F97316;border-color:#C2410C;" onclick="router.undoLastTap()">↩ Undo</button>
        </div>

        <div style="display:flex;justify-content:space-between;align-items:center;font-size:11px;color:#E2E8F0;margin-bottom:4px;font-weight:700;">
          <span>Sights Discovered</span>
          <span style="color:#F59E0B;font-weight:900;">${completedCount} / 25</span>
        </div>
        <div class="progress-bar">
          <div class="progress-fill" style="width:${(completedCount/25)*100}%;"></div>
        </div>

        <!-- B I N G O HEADERS -->
        <div class="bingo-header-row">
          <div class="bingo-h h-b">B</div>
          <div class="bingo-h h-i">I</div>
          <div class="bingo-h h-n">N</div>
          <div class="bingo-h h-g">G</div>
          <div class="bingo-h h-o">O</div>
        </div>

        <div class="bingo-grid">
          ${this.board.map(t => `
            <div class="tile ${t.completed ? 'completed' : ''} ${t.isCenter ? 'center' : ''}" onclick="router.toggleTile(${t.index}, event)">
              ${t.isCenter ? '⭐<br>FREE' : `${t.name}`}
            </div>
          `).join('')}
        </div>

        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:12px;">
          <button class="btn btn-outline" style="width:auto;min-height:42px;padding:0 16px;font-size:13px;" onclick="router.board=BingoWebEngine.generateBoard(router.selectedDeck.id);router.render();">
            🔄 New Board
          </button>
          ${isWin ? '<div style="background:#F59E0B;color:#0F172A;font-weight:900;padding:8px 14px;border-radius:12px;font-size:13px;">🎉 BINGO!</div>' : ''}
        </div>

        <!-- LIVE OPPONENT BOARDS SECTION AT THE BOTTOM -->
        <div style="margin-top:18px;width:100%;">
          <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
            <span style="font-size:12px;font-weight:900;color:#FBBF24;">LIVE OPPONENT BOARDS (2)</span>
            <span style="font-size:11px;font-weight:800;color:#10B981;">⚡ Live Sync</span>
          </div>

          <div style="display:grid;grid-template-columns:1fr 1fr;gap:10px;">
            <!-- OPPONENT 1 MINI CARD -->
            <div class="card" style="padding:10px;margin-bottom:0;">
              <div style="display:flex;justify-content:space-between;align-items:center;font-size:11px;font-weight:800;margin-bottom:6px;">
                <span>🦊 Alex (Passenger)</span>
                <span style="color:#FBBF24;">${this.op1Tiles.length}/25</span>
              </div>
              <div style="display:grid;grid-template-columns:repeat(5,1fr);gap:2px;aspect-ratio:1;">
                ${Array.from({length:25},(_,i)=>`
                  <div style="aspect-ratio:1;border-radius:3px;background:${i===12?'#FBBF24':this.op1Tiles.includes(i)?'#10B981':'#2A364F'};display:flex;align-items:center;justify-content:center;font-size:6px;color:white;font-weight:900;">
                    ${i===12?'⭐':this.op1Tiles.includes(i)?'✓':''}
                  </div>
                `).join('')}
              </div>
            </div>

            <!-- OPPONENT 2 MINI CARD -->
            <div class="card" style="padding:10px;margin-bottom:0;">
              <div style="display:flex;justify-content:space-between;align-items:center;font-size:11px;font-weight:800;margin-bottom:6px;">
                <span>🦅 Jordan (RV)</span>
                <span style="color:#FBBF24;">${this.op2Tiles.length}/25</span>
              </div>
              <div style="display:grid;grid-template-columns:repeat(5,1fr);gap:2px;aspect-ratio:1;">
                ${Array.from({length:25},(_,i)=>`
                  <div style="aspect-ratio:1;border-radius:3px;background:${i===12?'#FBBF24':this.op2Tiles.includes(i)?'#10B981':'#2A364F'};display:flex;align-items:center;justify-content:center;font-size:6px;color:white;font-weight:900;">
                    ${i===12?'⭐':this.op2Tiles.includes(i)?'✓':''}
                  </div>
                `).join('')}
              </div>
            </div>
          </div>
        </div>

        <!-- ACCOUNT CREATION / GOOGLE SIGN-IN POPUP OVERLAY -->
        ${this.showAccountModal ? `
          <div style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.88);display:flex;align-items:center;justify-content:center;padding:20px;z-index:300;">
            <div class="card" style="text-align:center;max-width:380px;width:100%;padding:24px;">
              <div style="font-size:36px;margin-bottom:8px;">💾</div>
              <h2 style="font-weight:900;color:#FBBF24;margin-bottom:8px;">SAVE YOUR ACHIEVEMENTS!</h2>
              <p style="font-size:12px;color:#CBD5E1;margin-bottom:18px;">Sign in with your account to back up your Level, XP, earned badges, and lifetime Bingo wins!</p>

              <button class="btn btn-teal btn-beveled" style="margin-bottom:10px;font-size:15px;" onclick="router.navigate('login')">
                🔑 LOG IN / REGISTER NOW
              </button>

              <button style="background:none;border:none;color:#94A3B8;font-size:12px;font-weight:700;cursor:pointer;" onclick="router.showAccountModal=false;router.render();">
                Continue as Guest (Skip)
              </button>
            </div>
          </div>
        ` : ''}
      `;
    } else if (this.currentScreen === 'decks') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
          <h2 style="font-weight:900;">🎴 Deck Library</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>
        ${DECKS.map(d => `
          <div class="card" style="background:${d.bg};margin-bottom:12px;">
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <strong style="font-size:16px;color:white;">${d.icon} ${d.title}</strong>
              <span class="pill-badge-yellow">${d.audience}</span>
            </div>
            <p style="font-size:12px;color:#CBD5E1;margin-top:6px;margin-bottom:12px;">${d.count} Distinct Roadside Prompts</p>
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <span style="font-size:11px;color:#94A3B8;">🎯 ${d.count} Prompts • Medium</span>
              <button class="btn btn-orange" style="width:auto;min-height:36px;padding:0 14px;font-size:12px;" onclick="router.selectDeckAndPlay(DECKS.find(x=>x.id==='${d.id}'))">PLAY DECK ▶</button>
            </div>
          </div>
        `).join('')}
      `;
    } else if (this.currentScreen === 'stats') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
          <h2 style="font-weight:900;">🏆 Stats & Badges</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>
        <div class="card card-gradient-teal">
          <h3>Level ${Math.floor(this.stats.xp/100)+1} Explorer</h3>
          <p style="font-size:13px;color:#F59E0B;font-weight:800;margin-top:4px;">${this.stats.xp} Total XP Earned</p>
          <div class="progress-bar" style="margin-top:10px;">
            <div class="progress-fill" style="width:${this.stats.xp%100}%;"></div>
          </div>
        </div>

        <h3 style="margin-top:16px;margin-bottom:8px;">Earned Badges</h3>
        <div class="card">
          <p>🐻 <strong>Dash the Bear</strong> ✓ (Unlocked)</p>
        </div>
        <div class="card">
          <p>🚗 <strong>First Ride</strong> ✓ (Unlocked)</p>
        </div>
        <div class="card">
          <p>🏆 <strong>Bingo Master</strong> ${this.stats.bingos >= 5 ? '✓ (Unlocked)' : '(Locked)'}</p>
        </div>
      `;
    } else if (this.currentScreen === 'settings') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
          <h2 style="font-weight:900;">⚙️ Settings</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>
        <div class="card">
          <h4>Passenger Safety Reminder</h4>
          <p style="font-size:12px;color:#94A3B8;margin-top:6px;">Always play from passenger seats. Drivers keep full attention on the road.</p>
        </div>
      `;
    }
  }
}

const router = new Router();
window.addEventListener('DOMContentLoaded', () => {
  const params = new URLSearchParams(window.location.search);
  const roomParam = params.get('room');
  if (roomParam) {
    router.joinCarRoom(roomParam);
  } else {
    router.render();
  }
});
