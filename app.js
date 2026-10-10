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

const SAMPLE_PROMPTS = [
  "Red Car", "Motorcycle", "Semi-Truck", "School Bus", "Emergency Vehicle",
  "Out-of-State Plate", "Billboard", "Gas Station", "Exit Sign", "Bridge",
  "Railroad Crossing", "Wind Turbine", "Construction Equipment", "Tractor", "Farm Animals",
  "Water Tower", "RV / Camper", "Pickup Truck", "Roof Rack", "Yellow Vehicle",
  "Vehicle Towing Trailer", "Rest Area Sign", "Toll Booth", "Roadside Diner"
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
  static generateBoard() {
    const shuffled = [...SAMPLE_PROMPTS].sort(() => 0.5 - Math.random());
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

  static checkWin(tiles) {
    const grid = Array(5).fill(0).map(() => Array(5).fill(false));
    tiles.forEach(t => { if (t.completed) grid[Math.floor(t.index/5)][t.index%5] = true; });

    let lines = 0;
    for (let r=0; r<5; r++) if (grid[r].every(Boolean)) lines++;
    for (let c=0; c<5; c++) if ([0,1,2,3,4].every(r => grid[r][c])) lines++;
    if ([0,1,2,3,4].every(i => grid[i][i])) lines++;
    if ([0,1,2,3,4].every(i => grid[i][4-i])) lines++;

    return lines > 0;
  }
}

class Router {
  constructor() {
    this.currentScreen = 'home';
    this.selectedDeck = DECKS[0];
    this.board = BingoWebEngine.generateBoard();
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
    this.board = BingoWebEngine.generateBoard();
    this.stats.games += 1;
    this.saveStats();
    this.navigate('game');
  }

  toggleTile(index) {
    if (index === 12) return;
    this.board[index].completed = !this.board[index].completed;
    AudioEngine.playClick();
    if (BingoWebEngine.checkWin(this.board)) {
      this.stats.bingos += 1;
      this.stats.xp += 100;
      this.saveStats();
    }
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
            <div class="avatar-circle">⭐</div>
            <div>
              <div style="font-weight:900;font-size:14px;color:white;">Road Explorer</div>
              <div style="font-size:11px;color:#FBBF24;font-weight:800;">Level ${level} • ${this.stats.xp} XP</div>
            </div>
          </div>
          <button class="settings-gear" onclick="router.navigate('settings')">⚙️</button>
        </div>

        <div class="hero-card">
          <svg class="hero-svg" viewBox="0 0 400 180">
            <defs>
              <linearGradient id="skyGrad" x1="0" y1="0" x2="0" y2="1">
                <stop offset="0%" stop-color="#0284C7"/>
                <stop offset="50%" stop-color="#38BDF8"/>
                <stop offset="100%" stop-color="#070C18"/>
              </linearGradient>
            </defs>
            <rect width="400" height="180" rx="20" fill="url(#skyGrad)" />
            <circle cx="310" cy="50" r="45" fill="#F59E0B" opacity="0.3" />
            <circle cx="310" cy="50" r="28" fill="#FBBF24" />
            <path d="M0 120 L100 70 L180 110 L280 50 L360 100 L400 80 L400 180 L0 180 Z" fill="#1E3A8A" opacity="0.7"/>
            <path d="M40 180 C 120 135, 80 100, 200 80 L220 80 C 140 105, 180 145, 140 180 Z" fill="#334155"/>
            <path d="M90 180 C 130 137, 100 104, 210 82" stroke="#F59E0B" stroke-width="4" fill="none"/>
            <!-- CAR -->
            <rect x="80" y="125" width="50" height="20" rx="6" fill="#F97316"/>
            <rect x="90" y="113" width="30" height="14" rx="5" fill="#C2410C"/>
            <circle cx="92" cy="145" r="6" fill="#020617"/>
            <circle cx="120" cy="145" r="6" fill="#020617"/>
          </svg>
          <div class="hero-tag">ROAD TRIP EDITION</div>
          <h1 class="hero-title">ROAD TRIP BINGO</h1>
        </div>

        <button class="btn btn-primary-hero btn-beveled" onclick="router.navigate('game')" style="margin-bottom:14px;">
          ⚡ PLAY NOW ▶
        </button>

        <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px;margin-bottom:14px;">
          <div class="card card-gradient-blue" onclick="router.navigate('game')" style="cursor:pointer;">
            <span style="font-size:24px;">🌐</span>
            <div style="font-weight:900;font-size:15px;margin-top:8px;">Multiplayer</div>
            <div style="font-size:11px;color:#94A3B8;">Play in car room</div>
          </div>
          <div class="card card-gradient-teal" onclick="router.navigate('decks')" style="cursor:pointer;">
            <span style="font-size:24px;">🎴</span>
            <div style="font-weight:900;font-size:15px;margin-top:8px;">Deck Library</div>
            <div style="font-size:11px;color:#94A3B8;">8 Themed Decks</div>
          </div>
        </div>

        <div class="card" onclick="router.navigate('settings')" style="cursor:pointer;display:flex;justify-content:space-between;align-items:center;">
          <div style="display:flex;align-items:center;gap:10px;">
            <span style="font-size:22px;">📍</span>
            <div>
              <div style="font-size:10px;font-weight:900;color:#F59E0B;">LOCATION REGION</div>
              <div style="font-weight:800;font-size:14px;">Mountain West & Rockies</div>
            </div>
          </div>
          <span style="color:#0D9488;font-weight:800;font-size:12px;">Change ></span>
        </div>

        <div class="card card-gradient-teal">
          <div style="display:flex;justify-content:space-between;align-items:center;">
            <span style="font-weight:900;color:#10B981;font-size:12px;">🎯 DAILY ROAD TRIP PASS</span>
            <span class="pill-badge">+150 XP</span>
          </div>
          <p style="font-size:12px;color:#E2E8F0;margin-top:6px;">Spot a yellow vehicle, semi-truck, and an out-of-state plate today!</p>
        </div>
      `;
    } else if (this.currentScreen === 'game') {
      const isWin = BingoWebEngine.checkWin(this.board);
      const completedCount = this.board.filter(t => t.completed).length;

      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
          <button class="back-pill" onclick="router.navigate('home')">◀ Exit</button>
          <div style="text-align:center;">
            <div style="font-weight:900;font-size:16px;">${this.selectedDeck.title}</div>
            <div style="font-size:11px;color:#FBBF24;font-weight:700;">Classic Line Bingo</div>
          </div>
          <div style="width:50px;"></div>
        </div>

        <div style="display:flex;justify-content:space-between;align-items:center;font-size:11px;color:#94A3B8;margin-bottom:4px;">
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
          <button class="btn btn-outline" style="width:auto;min-height:42px;padding:0 16px;font-size:13px;" onclick="router.board=BingoWebEngine.generateBoard();router.render();">
            🔄 New Board
          </button>
          ${isWin ? '<div style="background:#F59E0B;color:#0F172A;font-weight:900;padding:8px 14px;border-radius:12px;font-size:13px;">🎉 BINGO!</div>' : ''}
        </div>
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
            <p style="font-size:12px;color:#CBD5E1;margin-top:6px;margin-bottom:12px;">42 Distinct Roadside Prompts</p>
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <span style="font-size:11px;color:#94A3B8;">🎯 42 Prompts • Medium</span>
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
window.addEventListener('DOMContentLoaded', () => router.render());
