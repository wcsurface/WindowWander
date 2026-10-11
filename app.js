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
  static playClick() {
    try {
      const ctx = new (window.AudioContext || window.webkitAudioContext)();
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();
      osc.type = 'sine';
      osc.frequency.setValueAtTime(587.33, ctx.currentTime);
      gain.gain.setValueAtTime(0.12, ctx.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.01, ctx.currentTime + 0.1);
      osc.connect(gain);
      gain.connect(ctx.destination);
      osc.start();
      osc.stop(ctx.currentTime + 0.1);
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
    this.selectedDeck = DECKS[2]; // Defaulted to location deck (Mountain West)
    this.selectedMode = GAME_MODES[0]; // Strictly Classic Bingo for Online
    this.board = BingoWebEngine.generateBoard(this.selectedDeck.id);
    this.roomCode = 'ROAD';
    this.showOnlineModal = false;
    this.isSearchingOpponent = false;
    this.opponentName = null;
    this.showAccountModal = false;
    this.isLoggedIn = false;
    this.stats = JSON.parse(localStorage.getItem('bingo_stats') || '{"xp":0,"games":0,"bingos":0}');
  }

  navigate(screen) {
    this.currentScreen = screen;
    this.updateActiveNav();
    this.render();
  }

  updateActiveNav() {
    ['home', 'decks', 'stats', 'settings'].forEach(s => {
      const btn = document.getElementById(`nav-${s}`);
      if (btn) {
        if (s === this.currentScreen) btn.classList.add('active');
        else btn.classList.remove('active');
      }
    });
  }

  selectDeckAndPlay(deck) {
    this.selectedDeck = deck;
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
    this.navigate('multiplayer');
  }

  joinCarRoom(code) {
    if (!code || code.length < 4) return;
    this.roomCode = code.toUpperCase().trim();
    this.navigate('multiplayer');
  }

  toggleTile(index) {
    if (index === 12) return;
    this.board[index].completed = !this.board[index].completed;
    AudioEngine.playClick();
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

  handleGoogleSignIn() {
    this.isLoggedIn = true;
    this.showAccountModal = false;
    alert("✅ Signed in with Google! Your XP, Level, and Bingo achievements are safely backed up.");
    this.render();
  }

  saveStats() {
    localStorage.setItem('bingo_stats', JSON.stringify(this.stats));
  }

  render() {
    const main = document.getElementById('main-content');
    if (!main) return;

    if (this.currentScreen === 'home') {
      const level = Math.floor(this.stats.xp / 100) + 1;
      main.innerHTML = `
        <div class="user-header">
          <div class="profile-badge">
            <div class="avatar-circle">🐻</div>
            <div>
              <div style="font-weight:900;font-size:13px;color:white;">${this.isLoggedIn ? 'RoadExplorer (Google)' : 'Road Explorer'}</div>
              <div style="font-size:10px;color:#FBBF24;font-weight:800;">Lvl ${level} Explorer • ${this.stats.xp} XP</div>
            </div>
          </div>
          <div style="display:flex;gap:8px;">
            <div class="currency-pill">🪙 1,250</div>
            <button class="settings-gear" onclick="router.navigate('settings')">⚙️</button>
          </div>
        </div>

        <div class="hero-card">
          <svg class="hero-svg" viewBox="0 0 400 200">
            <defs>
              <linearGradient id="skyGrad" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#0284C7"/>
                <stop offset="50%" stop-color="#38BDF8"/>
                <stop offset="100%" stop-color="#7DD3FC"/>
              </linearGradient>
            </defs>
            <rect width="400" height="200" rx="20" fill="url(#skyGrad)" />
            <circle cx="330" cy="50" r="45" fill="#F59E0B" opacity="0.4" />
            <circle cx="330" cy="50" r="28" fill="#FBBF24" />
            <!-- CLOUDS -->
            <circle cx="60" cy="35" r="18" fill="white" opacity="0.85"/>
            <circle cx="80" cy="32" r="24" fill="white" opacity="0.85"/>
            <circle cx="100" cy="35" r="18" fill="white" opacity="0.85"/>
            <!-- PARALLAX SCENERY MOUNTAINS -->
            <path d="M0 130 L90 70 L180 120 L280 55 L360 100 L400 80 L400 200 L0 200 Z" fill="#1E3A8A" opacity="0.65"/>
            <!-- GREEN ROLLING HILLS -->
            <path d="M0 140 C 120 120, 240 150, 400 130 L400 200 L0 200 Z" fill="#15803D"/>

            <!-- HORIZONTAL HIGHWAY ROAD ACROSS BOTTOM -->
            <rect x="0" y="145" width="400" height="55" fill="#334155" />
            <line x1="0" y1="145" x2="400" y2="145" stroke="#FFFFFF" stroke-width="2.5" />
            <line x1="0" y1="198" x2="400" y2="198" stroke="#FFFFFF" stroke-width="2.5" />

            <!-- FORWARD-DRIVING SCROLLING DASHED LINE -->
            <line x1="0" y1="172" x2="400" y2="172" stroke="#F59E0B" stroke-width="4" stroke-dasharray="24 16">
              <animate attributeName="stroke-dashoffset" from="0" to="40" dur="0.5s" repeatCount="indefinite"/>
            </line>

            <!-- FORWARD-DRIVING CAR WITH BOUNCE -->
            <g>
              <animateTransform attributeName="transform" type="translate" values="0,0; 0,-3; 0,0" dur="0.25s" repeatCount="indefinite"/>
              <rect x="150" y="152" width="75" height="25" rx="8" fill="#F97316"/>
              <rect x="168" y="138" width="42" height="18" rx="7" fill="#C2410C"/>
              <rect x="172" y="142" width="15" height="12" rx="3" fill="#BAE6FD"/>
              <rect x="191" y="142" width="15" height="12" rx="3" fill="#BAE6FD"/>
              <circle cx="223" cy="164" r="4" fill="#FDE047"/>
              <circle cx="166" cy="178" r="9" fill="#020617"/>
              <circle cx="210" cy="178" r="9" fill="#020617"/>
              <circle cx="166" cy="178" r="4" fill="#94A3B8"/>
              <circle cx="210" cy="178" r="4" fill="#94A3B8"/>
              <rect x="174" y="128" width="28" height="10" rx="4" fill="#F59E0B"/>
            </g>
          </svg>
          <h1 class="hero-title">ROAD TRIP BINGO</h1>
        </div>

        <button class="btn btn-primary-hero btn-beveled" onclick="router.showOnlineModal=true;router.render();" style="margin-bottom:14px;">
          ⚡ PLAY NOW ▶
        </button>

        <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:14px;">
          <div class="road-sign-card sign-blue" onclick="router.showOnlineModal=true;router.render();" style="cursor:pointer;">
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <span style="font-size:26px;">🌐</span>
              <span class="sign-badge">LIVE</span>
            </div>
            <div style="font-weight:900;font-size:16px;margin-top:8px;">Multiplayer</div>
            <div style="font-size:11px;color:#E2E8F0;">Play online room</div>
          </div>
          <div class="road-sign-card sign-teal" onclick="router.navigate('decks')" style="cursor:pointer;">
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <span style="font-size:26px;">🎴</span>
              <span class="sign-badge">8 DECKS</span>
            </div>
            <div style="font-weight:900;font-size:16px;margin-top:8px;">Deck Library</div>
            <div style="font-size:11px;color:#E2E8F0;">8 Themed Decks</div>
          </div>
        </div>

        <div class="card card-gradient-teal">
          <div style="display:flex;justify-content:space-between;align-items:center;">
            <span style="font-weight:900;color:#10B981;font-size:12px;">🎯 DAILY ROAD TRIP PASS</span>
            <span class="pill-badge">+150 XP</span>
          </div>
          <p style="font-size:12px;color:#E2E8F0;margin-top:6px;">Spot a yellow vehicle, semi-truck, and an out-of-state plate today!</p>
        </div>

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

        <!-- SEARCHING FOR OPPONENT MODAL OVERLAY -->
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
    } else if (this.currentScreen === 'multiplayer-options') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
          <h2 style="font-weight:900;">🚗 In-Car Play</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>

        <div class="card" style="margin-bottom:16px;">
          <h3 style="color:#FBBF24;margin-bottom:6px;">🚗 START A GAME (HOST)</h3>
          <p style="font-size:12px;color:#CBD5E1;margin-bottom:14px;">Generates a QR code & 4-letter code so passengers in your car can scan or type to join!</p>
          <button class="btn btn-yellow btn-beveled" onclick="router.startCarRoom()">START NEW GAME ▶</button>
        </div>

        <div class="card">
          <h3 style="color:#38BDF8;margin-bottom:6px;">📱 JOIN A GAME (CODE)</h3>
          <p style="font-size:12px;color:#CBD5E1;margin-bottom:10px;">Enter the 4-letter room code shown on the host's screen:</p>
          <input id="room-code-input" type="text" maxlength="4" placeholder="e.g. ROAD" style="width:100%;height:48px;border-radius:12px;border:2px solid #38BDF8;background:#0F172A;color:white;font-size:20px;font-weight:900;text-align:center;text-transform:uppercase;margin-bottom:12px;" />
          <button class="btn btn-teal btn-beveled" onclick="router.joinCarRoom(document.getElementById('room-code-input').value)">JOIN CAR ROOM ▶</button>
        </div>
      `;
    } else if (this.currentScreen === 'multiplayer') {
      const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=https://windowwander.vercel.app?room=${this.roomCode}`;
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
          <h2 style="font-weight:900;">🌐 Car Room Lobby</h2>
          <button class="back-pill" onclick="router.navigate('home')">Exit</button>
        </div>

        <div class="card" style="text-align:center;background:linear-gradient(180deg, rgba(245,158,11,0.25), #1E293B);">
          <div style="font-size:12px;font-weight:900;color:#F59E0B;">CAR ROOM CODE</div>
          <div style="font-size:42px;font-weight:900;letter-spacing:4px;color:white;margin:4px 0;">${this.roomCode}</div>

          <!-- QR CODE FOR SCANNING -->
          <div style="display:inline-block;background:white;padding:10px;border-radius:16px;margin:12px 0;">
            <img src="${qrUrl}" alt="Scan QR Code to Join" style="width:140px;height:140px;display:block;" />
          </div>
          <div style="font-size:12px;color:#CBD5E1;">Scan QR code with phone camera or enter code <strong>${this.roomCode}</strong> to join!</div>
        </div>

        <h3 style="margin:16px 0 8px 0;">Connected Passengers</h3>
        <div class="card" style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
          <span>👑 <strong>Road Explorer (Host)</strong></span>
          <span style="color:#10B981;font-weight:800;font-size:12px;">Ready</span>
        </div>

        <!-- 1. DECK / LOCATION SELECTOR BUTTON -->
        <div style="margin-bottom:12px;">
          <label style="font-size:11px;font-weight:900;color:#F59E0B;display:block;margin-bottom:4px;">🎴 PLAY LOCATION / DECK</label>
          <select id="lobby-deck-select" onchange="router.selectedDeck=DECKS.find(d=>d.id===this.value)" style="width:100%;height:48px;border-radius:14px;background:#0F172A;color:white;border:2px solid #F59E0B;padding:0 12px;font-size:14px;font-weight:800;">
            ${DECKS.map(d => `<option value="${d.id}" ${d.id===this.selectedDeck.id ? 'selected' : ''}>🎴 ${d.title} (${d.audience})</option>`).join('')}
          </select>
        </div>

        <!-- 2. GAME MODE / WINNING CONDITION BUTTON -->
        <div style="margin-bottom:16px;">
          <label style="font-size:11px;font-weight:900;color:#F59E0B;display:block;margin-bottom:4px;">🎯 WINNING CONDITION</label>
          <select id="lobby-mode-select" onchange="router.selectedMode=GAME_MODES.find(m=>m.id===this.value)" style="width:100%;height:48px;border-radius:14px;background:#0F172A;color:white;border:2px solid #F59E0B;padding:0 12px;font-size:14px;font-weight:800;">
            ${GAME_MODES.map(m => `<option value="${m.id}" ${m.id===this.selectedMode.id ? 'selected' : ''}>🎯 ${m.title} — ${m.desc}</option>`).join('')}
          </select>
        </div>

        <button class="btn btn-orange btn-beveled" onclick="router.navigate('game')">
          START BINGO BOARD ▶
        </button>
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
          <div style="width:50px;"></div>
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
            <div class="tile ${t.completed ? 'completed' : ''} ${t.isCenter ? 'center' : ''}" onclick="router.toggleTile(${t.index})">
              ${t.isCenter ? '⭐<br>FREE' : `${t.name}${t.completed ? '<br>✓' : ''}`}
            </div>
          `).join('')}
        </div>

        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:12px;">
          <button class="btn btn-outline" style="width:auto;min-height:42px;padding:0 16px;font-size:13px;" onclick="router.board=BingoWebEngine.generateBoard(router.selectedDeck.id);router.render();">
            🔄 New Board
          </button>
          ${isWin ? '<div style="background:#F59E0B;color:#0F172A;font-weight:900;padding:8px 14px;border-radius:12px;font-size:13px;">🎉 BINGO!</div>' : ''}
        </div>

        <!-- ACCOUNT CREATION / GOOGLE SIGN-IN POPUP OVERLAY -->
        ${this.showAccountModal ? `
          <div style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.88);display:flex;align-items:center;justify-content:center;padding:20px;z-index:300;">
            <div class="card" style="text-align:center;max-width:380px;width:100%;padding:24px;">
              <div style="font-size:36px;margin-bottom:8px;">💾</div>
              <h2 style="font-weight:900;color:#FBBF24;margin-bottom:8px;">SAVE YOUR ACHIEVEMENTS!</h2>
              <p style="font-size:12px;color:#CBD5E1;margin-bottom:18px;">Sign in with Google or create a free account to back up your Level, XP, earned badges, and lifetime Bingo wins!</p>

              <button class="btn btn-teal btn-beveled" style="margin-bottom:10px;font-size:15px;" onclick="router.handleGoogleSignIn()">
                🌐 SIGN IN WITH GOOGLE
              </button>

              <button class="btn btn-yellow btn-beveled" style="margin-bottom:14px;font-size:15px;" onclick="router.handleGoogleSignIn()">
                ✉️ CREATE FREE ACCOUNT
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
