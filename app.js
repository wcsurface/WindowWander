// ROAD TRIP BINGO - WEB APPLICATION ENGINE

const DECKS = [
  { id: 'deck_a', title: 'Classic Road Trip', audience: 'Families & Kids', count: 42, icon: '🚗' },
  { id: 'deck_b', title: 'Weird Road Trip', audience: 'Teens & Adults', count: 42, icon: '🤪' },
  { id: 'deck_c', title: 'Mountain West & Rockies', audience: 'Everyone', count: 42, icon: '🏔️' },
  { id: 'deck_d', title: 'Desert & Southwest', audience: 'Everyone', count: 42, icon: '🌵' },
  { id: 'deck_e', title: 'Northeast & Mid-Atlantic', audience: 'Everyone', count: 42, icon: '🌉' },
  { id: 'deck_f', title: 'The South', audience: 'Everyone', count: 42, icon: '🌳' },
  { id: 'deck_g', title: 'West Coast', audience: 'Everyone', count: 42, icon: '🏄' },
  { id: 'deck_h', title: 'Heart of the Country', audience: 'Everyone', count: 42, icon: '🌽' }
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
      osc.frequency.setValueAtTime(587.33, ctx.currentTime); // D5
      gain.gain.setValueAtTime(0.1, ctx.currentTime);
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
    this.board = BingoWebEngine.generateBoard();
    this.stats = JSON.parse(localStorage.getItem('bingo_stats') || '{"xp":0,"games":0,"bingos":0}');
  }

  navigate(screen) {
    this.currentScreen = screen;
    this.render();
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
      main.innerHTML = `
        <div style="margin-bottom:16px;">
          <h1 style="font-size:24px;font-weight:900;color:#0F172A;">ROAD TRIP BINGO</h1>
          <p style="font-size:13px;color:#64748B;">Level ${Math.floor(this.stats.xp/100)+1} Explorer • ${this.stats.xp} XP</p>
        </div>

        <button class="btn btn-yellow" onclick="router.navigate('game')" style="margin-bottom:12px;">
          ⚡ QUICK PLAY (IN CAR)
        </button>

        <button class="btn btn-teal" onclick="router.navigate('decks')" style="margin-bottom:12px;">
          🎴 CHOOSE A DECK (8 DECKS)
        </button>

        <div class="card" style="margin-top:16px;">
          <h3>🏆 Your Progress</h3>
          <p style="font-size:13px;color:#475569;margin-top:4px;">
            Bingos Won: <strong>${this.stats.bingos}</strong> | Games: <strong>${this.stats.games}</strong>
          </p>
        </div>
      `;
    } else if (this.currentScreen === 'game') {
      const isWin = BingoWebEngine.checkWin(this.board);
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
          <h2>Classic Road Trip</h2>
          <button style="background:none;border:none;color:#0D9488;font-weight:bold;cursor:pointer;" onclick="router.navigate('home')">Exit</button>
        </div>

        ${isWin ? '<div class="card" style="background:#FEF3C7;color:#92400E;text-align:center;font-weight:bold;">🎉 BINGO ACHIEVED! +100 XP</div>' : ''}

        <div class="bingo-grid">
          ${this.board.map(t => `
            <div class="tile ${t.completed ? 'completed' : ''} ${t.isCenter ? 'center' : ''}" onclick="router.toggleTile(${t.index})">
              ${t.isCenter ? '⭐<br>FREE' : t.name}
            </div>
          `).join('')}
        </div>

        <button class="btn btn-outline" onclick="router.board=BingoWebEngine.generateBoard();router.stats.games++;router.saveStats();router.render();" style="margin-top:12px;">
          🔄 New Board
        </button>
      `;
    } else if (this.currentScreen === 'decks') {
      main.innerHTML = `
        <h2 style="margin-bottom:16px;">🎴 Deck Catalog</h2>
        ${DECKS.map(d => `
          <div class="card" style="display:flex;justify-content:space-between;align-items:center;">
            <div>
              <strong>${d.icon} ${d.title}</strong>
              <p style="font-size:12px;color:#64748B;">${d.audience} • ${d.count} Prompts</p>
            </div>
            <button class="btn btn-teal" style="width:auto;min-height:36px;padding:0 12px;font-size:12px;" onclick="router.navigate('game')">PLAY</button>
          </div>
        `).join('')}
      `;
    } else if (this.currentScreen === 'stats') {
      main.innerHTML = `
        <h2>🏆 Player Stats</h2>
        <div class="card" style="margin-top:16px;">
          <p>Total XP: <strong>${this.stats.xp}</strong></p>
          <p>Bingos Won: <strong>${this.stats.bingos}</strong></p>
          <p>Games Played: <strong>${this.stats.games}</strong></p>
        </div>
      `;
    } else if (this.currentScreen === 'settings') {
      main.innerHTML = `
        <h2>⚙️ Settings</h2>
        <div class="card" style="margin-top:16px;">
          <p><strong>Passenger Safety Reminder:</strong> Always play from passenger seats.</p>
          <p style="font-size:12px;color:#64748B;margin-top:8px;">Road Trip Bingo Version 1.0.0 (Vercel Ready)</p>
        </div>
      `;
    }
  }
}

const router = new Router();
window.addEventListener('DOMContentLoaded', () => router.render());
