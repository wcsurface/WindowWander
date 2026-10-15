// ROAD TRIP BINGO - WEB APPLICATION ENGINE

const DECKS = [
  { id: 'deck_a', title: 'Classic Road Trip', audience: 'Families & Kids', count: 42, icon: '🚗', bg: 'linear-gradient(135deg, #2563EB, #1E293B)' },
  { id: 'deck_b', title: 'Weird Road Trip', audience: 'Teens & Adults', count: 42, icon: '🤪', bg: 'linear-gradient(135deg, #F97316, #1E293B)' },
  { id: 'deck_c', title: 'Mountain West & Rockies', audience: 'Everyone', count: 42, icon: '🏔️', bg: 'linear-gradient(135deg, #0D9488, #1E293B)' },
  { id: 'deck_d', title: 'Desert & Southwest', audience: 'Everyone', count: 42, icon: '🌵', bg: 'linear-gradient(135deg, #F59E0B, #1E293B)' },
  { id: 'deck_e', title: 'Northeast & Mid-Atlantic', audience: 'Everyone', count: 42, icon: '🌉', bg: 'linear-gradient(135deg, #3B82F6, #1E293B)' },
  { id: 'deck_f', title: 'The South', audience: 'Everyone', count: 42, icon: '🌳', bg: 'linear-gradient(135deg, #10B981, #1E293B)' },
  { id: 'deck_g', title: 'West Coast', audience: 'Everyone', count: 42, icon: '🏄', bg: 'linear-gradient(135deg, #14B8A6, #1E293B)' },
  { id: 'deck_h', title: 'Heart of the Country', audience: 'Everyone', count: 42, icon: '🌽', bg: 'linear-gradient(135deg, #EAB308, #1E293B)' },
  { id: 'deck_holiday', title: 'Holiday Road Trip', audience: 'Everyone', count: 30, icon: '🎄', bg: 'linear-gradient(135deg, #DC2626, #1E293B)', price: '$0.99' }
];

const GAME_MODES = [
  { id: 'classic', title: 'Classic Line Bingo', desc: 'Complete a horizontal, vertical, or diagonal line.' },
  { id: 'two_lines', title: 'Two Lines Bingo', desc: 'Complete two winning lines.' },
  { id: 'blackout', title: 'Blackout Bingo', desc: 'Find all 24 discoverable items and fill your card.' }
];

const WEIRD_PROMPTS = [
  "Funny Bumper Sticker",
  "Over Accessorized Vehicle",
  "Funny Billboard Slogan",
  "Cryptic Vanity Plate",
  "Car Older Than Driver",
  "Giant Roadside Statue",
  "Hilarious Business Name",
  "Mismatched Body Panels",
  "Custom Decorated Truck",
  "Inflatable Advert character",
  "Bizarre Slogan Sign",
  "Strange Attraction Sign",
  "Dashboard Ducks",
  "Hyper Specific Shop",
  "Unexpected Item Towed",
  "Sign with Spelling Error",
  "Farm Animals",
  "Comically Large Accessory",
  "Unusual Color Building",
  "Double Meaning Sign",
  "Very Old Lady Driving",
  "Anime Stickers",
  "Lawnmowing",
  "Dog in a Car",
  "Warning Sign",
  "Rest Area Sign",
  "American Flag on Semi",
  "Attorney Billboard",
  "24 Hour Diner",
  "Giant Statue",
  "Police Car",
  "Band Sticker",
  "Duct Tape Repair Job",
  "Green Car",
  "Road Kill",
  "Smoking Road Worker",
  "Train",
  "Casino Sign",
  "Mexican Food Restaurant",
  "Car Crash",
  "Expired Car Plate",
  "Cowboy Hat"
];

const MOUNTAIN_WEST_PROMPTS = [
  "Ski Rack on Car",
  "Snow Capped Peak",
  "Wildlife Crossing Sign",
  "Elk or Deer in Distance",
  "Mountain Pass Sign",
  "Bicyclest",
  "River Rafting Billboard",
  "National Forest Sign",
  "Rock Formation Cliffs",
  "Evergreen Forest",
  "Dust Covered 4x4",
  "Camper Van",
  "Fly Fisherman",
  "Wildfire Danger Sign",
  "Winding Canyon Road",
  "Mountain Lake / Reservoir",
  "Ranch Gate Structure",
  "Cattle",
  "Log Cabin Lodge",
  "Snowplow",
  "Gondola or Chairlift",
  "Elevation Sign",
  "Jerky Sign",
  "Timber Hauling Truck",
  "Falling Rock Sign",
  "Runaway Truck Ramp",
  "Hot Springs Advert",
  "Chain Up Station",
  "Casino Sign",
  "Horse",
  "Wildflowers",
  "Park Ranger Truck",
  "Tumbleweed",
  "Sports Team Sticker",
  "Waterfall",
  "Tour Offering Sign",
  "Outdoors Store",
  "Aspen Tree",
  "Tunnel Through Rock",
  "Construction Zone",
  "Traffic Jam",
  "Police Car"
];

const DESERT_SOUTHWEST_PROMPTS = [
  "Cactus",
  "Red Rock Mesa",
  "Solar Energy Farm",
  "Desert Wildlife Sign",
  "Roadside Trading Post",
  "Western Saloon Sign",
  "Mexican Restaurant",
  "Fresh Chile Stand",
  "Adobe Architecture",
  "Dry Riverbed",
  "Tumbleweed",
  "Giant Roadside Statue",
  "Route 66 Sign",
  "Turquoise Building",
  "Alien Sign",
  "Police Car",
  "Point of Interest Sign",
  "Construction Zone",
  "Cattle Skull Decoration",
  "Campground",
  "Truck with 2 Trailers",
  "Animal Crossing Sign",
  "Dump Truck",
  "Convertable Car",
  "Hotel Over 5 Floors",
  "Horse Trailer",
  "Roadside Jerky Stand",
  "Golf Course Sign",
  "Giant American Flag",
  "Attorney Sign",
  "Railroad Train",
  "Water Tower",
  "Thermometer Over 100",
  "Overpass",
  "ATV on Trailer",
  "Rain",
  "Dust Devil Whirlwind",
  "Band Sticker",
  "Yellow Car",
  "Motorcycle",
  "Rest Area",
  "Desert Joshua Tree"
];

const NORTHEAST_PROMPTS = [
  "Historic Stone Wall",
  "Covered Bridge",
  "Food Drive Thru",
  "State Toll Booth",
  "Lighthouse",
  "Historic Colonial Home",
  "Seafood Shack",
  "Apple Stand",
  "Lush Rolling Hills",
  "Pine Forest",
  "Gas Station Sign",
  "Giant Roadside Statue",
  "Interstate Route Shield",
  "Historic Town Sign",
  "Antique Shop Sign",
  "Police Car",
  "Point of Interest Sign",
  "Construction Zone",
  "Lighthouse License Plate",
  "Campground",
  "Commuter bus",
  "Deer Crossing Sign",
  "Dump Truck",
  "Convertible Car",
  "Brick House",
  "Boat on Trailer",
  "Maple Syrup Sign",
  "Golf Course Sign",
  "Giant American Flag",
  "Attorney Sign",
  "Commuter Train",
  "Water Tower",
  "Historic Milestone",
  "Multi Level Overpass",
  "Kayak on Roof",
  "Rain",
  "Lighthouse Sign",
  "Band Sticker",
  "Yellow Car",
  "Motorcycle",
  "Rest Area",
  "Weeping Willow Tree"
];

const THE_SOUTH_PROMPTS = [
  "Tow Truck",
  "Fire Truck",
  "24 Hour Diner",
  "Massive Truckstop",
  "Swamp or Marsh",
  "Historic Courthouse Square",
  "BBQ Smokehouse",
  "Farm Equipment",
  "Dirt Road",
  "Political Bumper Sticker",
  "Funny Bumper Sticker",
  "Giant Roadside Statue",
  "Interstate Route Shield",
  "Flowers",
  "Fruit Stand Sign",
  "Police Car",
  "Point of Interest Sign",
  "Construction Zone",
  "College Football Flag",
  "Campground",
  "Modified Pickup Truck",
  "Wildlife Crossing Sign",
  "Dump Truck",
  "Convertible Car",
  "Church",
  "Boat on Trailer",
  "Sweet Tea Billboard",
  "Golf Course Sign",
  "Giant American Flag",
  "Attorney Sign",
  "Freight Train",
  "Water Tower",
  "Fireworks Superstore",
  "Elevated Highway",
  "ATV on Trailer",
  "Rain",
  "Cowboy Hat",
  "Band Sticker",
  "Yellow Car",
  "Motorcycle",
  "Rest Area",
  "Pond or Lake"
];

const WEST_COAST_PROMPTS = [
  "Ocean Overlook",
  "Lighthouse",
  "Burger Restaurant",
  "Flowers",
  "Boat",
  "Mission Style Architecture",
  "Fish Restaurant Sign",
  "Fruit Farm Stand",
  "Rolling Golden Hills",
  "Redwood Tree",
  "Electric Vehicle",
  "Giant Roadside Statue",
  "Interstate Route Shield",
  "Car Crash",
  "Surf Shop Sign",
  "Police Car",
  "Point of Interest Sign",
  "Construction Zone",
  "Vanity Plate",
  "Campground",
  "RV Motorhome",
  "Wildlife Crossing Sign",
  "Dump Truck",
  "Convertible Car",
  "Spanish Tile Roofed Building",
  "Surfboard",
  "Coffee Shop",
  "Golf Course Sign",
  "Giant American Flag",
  "Attorney Sign",
  "Train",
  "Water Tower",
  "Solar Panel Array",
  "Dirt Road",
  "Bike Rack with Bicycles",
  "Rain",
  "Fog",
  "Band Sticker",
  "Yellow Car",
  "Motorcycle",
  "Rest Area",
  "Tall Palm Tree"
];

const HEARTLAND_MIDWEST_PROMPTS = [
  "Cornfield",
  "Barn or Silo",
  "Massive Truck Stop",
  "Coffee Shop",
  "Wind Turbine",
  "Church",
  "24 Hour Diner",
  "Dirt Road",
  "Farm Equipment",
  "Traffic Jam",
  "Fire Truck",
  "Giant Roadside Statue",
  "Interstate Route Shield",
  "Political Bumper Sticker",
  "Antique Car",
  "Police Car",
  "Point of Interest Sign",
  "Construction Zone",
  "Ice Cream Sign",
  "Campground",
  "Trucker Hat",
  "Wildlife Crossing Sign",
  "Dump Truck",
  "Convertible Car",
  "School Bus",
  "Lawn Mowing",
  "Billboard with Animal",
  "Golf Course Sign",
  "Giant American Flag",
  "Attorney Sign",
  "Train",
  "Water Tower",
  "Bridge",
  "Multi Level Overpass",
  "Pickup Truck with Camper Shell",
  "Rain",
  "Rainbow",
  "Band Sticker",
  "Yellow Car",
  "Motorcycle",
  "Rest Area",
  "Sports Team Sticker"
];

class BackgroundMusicEngine {
  static audio = null;

  static init() {
    if (!this.audio) {
      this.audio = new Audio('road_trip_bingo_theme.mp3');
      this.audio.loop = true;
      this.audio.volume = 0.18; // 18% quiet background music volume
    }
  }

  static syncState(currentScreen) {
    this.init();
    if (currentScreen === 'game') {
      this.pause();
    } else {
      this.play();
    }
  }

  static play() {
    this.init();
    if (this.audio && this.audio.paused) {
      this.audio.play().catch(() => {});
    }
  }

  static pause() {
    if (this.audio && !this.audio.paused) {
      this.audio.pause();
    }
  }
}

class AudioEngine {
  static playPop() {
    try {
      const ctx = new (window.AudioContext || window.webkitAudioContext)();
      const now = ctx.currentTime;

      // CHEERFUL BUBBLY RISING TWO-NOTE CHIME (C5 -> C6)
      const osc1 = ctx.createOscillator();
      const gain1 = ctx.createGain();
      osc1.type = 'sine';
      osc1.frequency.setValueAtTime(523.25, now);
      osc1.frequency.exponentialRampToValueAtTime(1046.50, now + 0.10);
      gain1.gain.setValueAtTime(0.25, now);
      gain1.gain.exponentialRampToValueAtTime(0.001, now + 0.12);
      osc1.connect(gain1);
      gain1.connect(ctx.destination);
      osc1.start(now);
      osc1.stop(now + 0.12);

      const osc2 = ctx.createOscillator();
      const gain2 = ctx.createGain();
      osc2.type = 'triangle';
      osc2.frequency.setValueAtTime(659.25, now + 0.03);
      osc2.frequency.exponentialRampToValueAtTime(1318.51, now + 0.14);
      gain2.gain.setValueAtTime(0.20, now + 0.03);
      gain2.gain.exponentialRampToValueAtTime(0.001, now + 0.15);
      osc2.connect(gain2);
      gain2.connect(ctx.destination);
      osc2.start(now + 0.03);
      osc2.stop(now + 0.15);
    } catch(e) {}
  }

  static playVictoryFanfare() {
    try {
      const ctx = new (window.AudioContext || window.webkitAudioContext)();
      const now = ctx.currentTime;
      const notes = [
        { f: 523.25, start: 0, duration: 0.09 },
        { f: 659.25, start: 0.09, duration: 0.09 },
        { f: 783.99, start: 0.18, duration: 0.10 },
        { f: 1046.50, start: 0.28, duration: 0.35 }
      ];

      notes.forEach(n => {
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        osc.type = 'triangle';
        osc.frequency.setValueAtTime(n.f, now + n.start);
        gain.gain.setValueAtTime(0.3, now + n.start);
        gain.gain.exponentialRampToValueAtTime(0.001, now + n.start + n.duration);
        osc.connect(gain);
        gain.connect(ctx.destination);
        osc.start(now + n.start);
        osc.stop(now + n.start + n.duration);
      });
    } catch(e) {}
  }

  static playGameOverSadTrombone() {
    try {
      const ctx = new (window.AudioContext || window.webkitAudioContext)();
      const now = ctx.currentTime;
      const notes = [
        { f: 622.25, start: 0, duration: 0.14 },
        { f: 587.33, start: 0.14, duration: 0.14 },
        { f: 554.37, start: 0.28, duration: 0.14 }
      ];

      notes.forEach(n => {
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        osc.type = 'sawtooth';
        osc.frequency.setValueAtTime(n.f, now + n.start);
        gain.gain.setValueAtTime(0.2, now + n.start);
        gain.gain.exponentialRampToValueAtTime(0.001, now + n.start + n.duration);
        osc.connect(gain);
        gain.connect(ctx.destination);
        osc.start(now + n.start);
        osc.stop(now + n.start + n.duration);
      });

      const oscFinal = ctx.createOscillator();
      const gainFinal = ctx.createGain();
      oscFinal.type = 'sawtooth';
      oscFinal.frequency.setValueAtTime(523.25, now + 0.42);
      oscFinal.frequency.exponentialRampToValueAtTime(370.00, now + 0.85);
      gainFinal.gain.setValueAtTime(0.25, now + 0.42);
      gainFinal.gain.exponentialRampToValueAtTime(0.001, now + 0.85);
      oscFinal.connect(gainFinal);
      gainFinal.connect(ctx.destination);
      oscFinal.start(now + 0.42);
      oscFinal.stop(now + 0.85);
    } catch(e) {}
  }
}

class RailwayAuthService {
  static baseUrl = "https://windowwander-production.up.railway.app";

  static async register(username, password, nickname) {
    try {
      const res = await fetch(`${this.baseUrl}/api/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password, nickname: nickname || username })
      });
      return await res.json();
    } catch(err) {
      return { success: true, user: { username, nickname: nickname || username, level: 1, xp: 0, earnedBadges: ['first_ride'], streakDays: 1, bingosWon: 0, gamesPlayed: 0, squaresFound: 0 } };
    }
  }

  static async login(username, password) {
    try {
      const res = await fetch(`${this.baseUrl}/api/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      });
      return await res.json();
    } catch(err) {
      return { success: true, user: { username, nickname: username, level: 1, xp: 0, earnedBadges: ['first_ride'], streakDays: 1, bingosWon: 0, gamesPlayed: 0, squaresFound: 0 } };
    }
  }

  static async saveProfile(username, level, xp, earnedBadges, streakDays, bingosWon, gamesPlayed, squaresFound) {
    try {
      await fetch(`${this.baseUrl}/api/auth/save-profile`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username,
          level,
          xp,
          earnedBadges,
          streakDays,
          bingosWon,
          gamesPlayed,
          squaresFound
        })
      });
    } catch(err) {}
  }

  static async getLeaderboard() {
    try {
      const res = await fetch(`${this.baseUrl}/api/auth/leaderboard`);
      return await res.json();
    } catch(err) {
      return { success: false };
    }
  }

  static async createRoom(hostNickname, deckId) {
    try {
      const res = await fetch('/api/rooms', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ action: 'create', hostNickname, deckId })
      });
      return await res.json();
    } catch(err) {
      return { success: false };
    }
  }

  static async joinRoom(code, nickname) {
    try {
      const res = await fetch('/api/rooms', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ action: 'join', code, nickname })
      });
      return await res.json();
    } catch(err) {
      return { success: false };
    }
  }

  static async launchGame(code, deckId, gameMode) {
    try {
      const res = await fetch('/api/rooms', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ action: 'launch', code, deckId, gameMode })
      });
      return await res.json();
    } catch(err) {
      return { success: false };
    }
  }

  static async updateTiles(code, nickname, completedTiles, hasBingo) {
    try {
      const res = await fetch('/api/rooms', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ action: 'update_tiles', code, nickname, completedTiles, hasBingo })
      });
      return await res.json();
    } catch(err) {
      return { success: false };
    }
  }

  static async getRoom(code) {
    try {
      const res = await fetch('/api/rooms', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ action: 'get', code })
      });
      return await res.json();
    } catch(err) {
      return { success: false };
    }
  }
}

class BingoWebEngine {
  static generateBoard(deckId = 'deck_c') {
    let prompts = MOUNTAIN_WEST_PROMPTS;
    if (deckId === 'deck_b') prompts = WEIRD_PROMPTS;
    if (deckId === 'deck_d') prompts = DESERT_SOUTHWEST_PROMPTS;
    if (deckId === 'deck_e') prompts = NORTHEAST_PROMPTS;
    if (deckId === 'deck_f') prompts = THE_SOUTH_PROMPTS;
    if (deckId === 'deck_g') prompts = WEST_COAST_PROMPTS;
    if (deckId === 'deck_h') prompts = HEARTLAND_MIDWEST_PROMPTS;

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
    this.userNickname = localStorage.getItem('bingo_nickname') || ('Road Explorer ' + Math.floor(100 + Math.random() * 900));
    if (!localStorage.getItem('bingo_nickname')) {
      localStorage.setItem('bingo_nickname', this.userNickname);
    }
    this.showDailyLimitModal = false;
    this.dailyGamesPlayed = parseInt(localStorage.getItem('daily_games_played') || '0', 10);
    this.bonusGamesUnlocked = parseInt(localStorage.getItem('bonus_games_unlocked') || '0', 10);
    this.unlockedDecks = JSON.parse(localStorage.getItem('unlocked_decks') || '["deck_a","deck_b","deck_c","deck_d","deck_e","deck_f","deck_g","deck_h"]');
    this.lastPlayDate = localStorage.getItem('last_play_date') || '';
    const todayStr = new Date().toISOString().split('T')[0];
    if (this.lastPlayDate !== todayStr) {
      this.dailyGamesPlayed = 0;
      this.bonusGamesUnlocked = 0;
      this.lastPlayDate = todayStr;
      localStorage.setItem('daily_games_played', '0');
      localStorage.setItem('bonus_games_unlocked', '0');
      localStorage.setItem('last_play_date', todayStr);
    }
  }

  unlockDeck(deckId) {
    if (!this.unlockedDecks.includes(deckId)) {
      this.unlockedDecks.push(deckId);
      localStorage.setItem('unlocked_decks', JSON.stringify(this.unlockedDecks));
    }
    this.render();
  }

  maxDailyGames() { return 2; }
  hasReachedDailyLimit() { return this.dailyGamesPlayed >= 2; }

  watchRewardedAd() {
    this.dailyGamesPlayed = 0;
    localStorage.setItem('daily_games_played', '0');
    this.showDailyLimitModal = false;
    this.render();
  }

  checkNicknameInput(val) {
    const btn = document.getElementById('save-nickname-btn');
    if (btn) {
      const hasChanged = val.trim() !== '' && val.trim() !== this.userNickname;
      btn.disabled = !hasChanged;
      btn.style.opacity = hasChanged ? '1' : '0.35';
      btn.style.cursor = hasChanged ? 'pointer' : 'not-allowed';
    }
  }

  saveNicknameFromInput() {
    const input = document.getElementById('nickname-settings-input');
    if (input && input.value.trim() !== '' && input.value.trim() !== this.userNickname) {
      this.userNickname = input.value.trim();
      localStorage.setItem('bingo_nickname', this.userNickname);
      this.render();
    }
  }

  navigate(screen) {
    this.currentScreen = screen;
    BackgroundMusicEngine.syncState(screen);
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

  async startCarRoomFromHome() {
    this.selectedDeck = DECKS[2];
    await this.startCarRoom();
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

  async startCarRoom() {
    const res = await RailwayAuthService.createRoom(this.userNickname, this.selectedDeck.id);
    if (res.success && res.room) {
      this.roomCode = res.room.code;
      this.activeRoom = res.room;
    } else {
      const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
      let code = '';
      for (let i = 0; i < 4; i++) code += chars.charAt(Math.floor(Math.random() * chars.length));
      this.roomCode = code;
      this.activeRoom = {
        code: this.roomCode,
        players: [{ nickname: this.userNickname, isHost: true }]
      };
    }
    this.showInCarModal = false;
    this.showJoinCodeInput = false;
    this.startRoomSync(this.roomCode);
    this.navigate('multiplayer');
  }

  async joinCarRoom(code) {
    if (!code || code.length < 4) return;
    const upperCode = code.toUpperCase().trim();
    this.roomCode = upperCode;

    const res = await RailwayAuthService.joinRoom(upperCode, this.userNickname);
    if (res.success && res.room) {
      this.activeRoom = res.room;
    } else {
      this.activeRoom = {
        code: upperCode,
        players: [{ nickname: this.userNickname, isHost: false }]
      };
    }
    this.showInCarModal = false;
    this.showJoinCodeInput = false;
    this.startRoomSync(upperCode);
    this.navigate('multiplayer');
  }

  async hostLaunchGame() {
    await RailwayAuthService.launchGame(this.roomCode, this.selectedDeck.id, this.selectedMode.id);
    this.board = BingoWebEngine.generateBoard(this.selectedDeck.id);
    this.navigate('game');
  }

  startRoomSync(code) {
    if (this.syncTimer) clearInterval(this.syncTimer);
    this.syncTimer = setInterval(async () => {
      if (['multiplayer', 'game'].includes(this.currentScreen)) {
        const res = await RailwayAuthService.getRoom(code);
        if (res.success && res.room) {
          this.activeRoom = res.room;

          // AUTO-LAUNCH JOINED PASSENGERS WHEN HOST LAUNCHES!
          if (this.currentScreen === 'multiplayer' && res.room.isStarted) {
            this.board = BingoWebEngine.generateBoard(this.selectedDeck.id);
            this.navigate('game');
          } else {
            this.render();
          }
        }
      }
    }, 1200);
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

      // SYNC COMPLETED TILES TO LIVE SERVER
      const completedIndices = this.board.filter(t => t.completed).map(t => t.index);
      const isWin = BingoWebEngine.checkWin(this.board, this.selectedMode.id);
      RailwayAuthService.updateTiles(this.roomCode, this.userNickname, completedIndices, isWin);

      if (isWin) {
        AudioEngine.playVictoryFanfare();
      }

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

  async loginUser(user, pass) {
    if (!user) return;
    const res = await RailwayAuthService.login(user, pass);
    if (res.success && res.user) {
      this.isLoggedIn = true;
      this.userNickname = res.user.nickname || user;
      if (res.user.xp > 0) this.stats.xp = res.user.xp;
      if (res.user.bingosWon > 0) this.stats.bingos = res.user.bingosWon;
      if (res.user.gamesPlayed > 0) this.stats.games = res.user.gamesPlayed;
      if (res.user.squaresFound > 0) this.stats.items = res.user.squaresFound;
      this.saveStats();
    }
    this.navigate('home');
  }

  logoutUser() {
    this.isLoggedIn = false;
    this.userNickname = 'Road Explorer ' + Math.floor(100 + Math.random() * 900);
    localStorage.removeItem('bingo_nickname');
    this.navigate('home');
  }

  saveStats() {
    localStorage.setItem('bingo_stats', JSON.stringify(this.stats));
    if (this.isLoggedIn && this.userNickname) {
      const calculatedLevel = Math.floor((this.stats.xp || 0) / 100) + 1;
      RailwayAuthService.saveProfile(
        this.userNickname,
        calculatedLevel,
        this.stats.xp || 0,
        ['first_ride'],
        1,
        this.stats.bingos || 0,
        this.stats.games || 0,
        this.stats.items || 0
      );
    }
  }

  render() {
    const main = document.getElementById('main-content');
    if (!main) return;

    BackgroundMusicEngine.syncState(this.currentScreen);
    this.updateActiveNav();

    // UPDATE FOOTER NAV BUTTON LABEL FOR LOGIN / LOGOUT
    const navLoginBtn = document.getElementById('nav-login-label');
    if (navLoginBtn) {
      navLoginBtn.innerText = this.isLoggedIn ? 'Log Out' : 'Log In';
    }

    // SETTINGS PAGE HAS DEDICATED DOODLE BACKGROUND THEME
    if (this.currentScreen === 'settings') {
      document.body.classList.add('settings-theme');
      document.body.classList.remove('leaderboard-theme');
      document.body.classList.remove('stats-theme');
      document.body.classList.remove('main-home-theme');
      document.body.classList.remove('deck-game-theme');
      document.body.style.backgroundImage = '';
    } else if (this.currentScreen === 'leaderboard') {
      document.body.classList.add('leaderboard-theme');
      document.body.classList.remove('settings-theme');
      document.body.classList.remove('stats-theme');
      document.body.classList.remove('main-home-theme');
      document.body.classList.remove('deck-game-theme');
      document.body.style.backgroundImage = '';
    } else if (this.currentScreen === 'stats') {
      document.body.classList.add('stats-theme');
      document.body.classList.remove('settings-theme');
      document.body.classList.remove('leaderboard-theme');
      document.body.classList.remove('main-home-theme');
      document.body.classList.remove('deck-game-theme');
      document.body.style.backgroundImage = '';
    } else if (['home', 'login', 'shop'].includes(this.currentScreen)) {
      document.body.classList.remove('settings-theme');
      document.body.classList.remove('leaderboard-theme');
      document.body.classList.add('main-home-theme');
      document.body.classList.remove('deck-game-theme');
      document.body.style.backgroundImage = '';
    } else if (this.currentScreen === 'game') {
      document.body.classList.remove('settings-theme');
      document.body.classList.remove('leaderboard-theme');
      document.body.classList.remove('main-home-theme');
      document.body.classList.add('deck-game-theme');
      const deckMap = {
        'deck_holiday': 'bg_holiday.jpg',
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
      document.body.classList.remove('settings-theme');
      document.body.classList.remove('leaderboard-theme');
      document.body.classList.remove('main-home-theme');
      document.body.classList.remove('deck-game-theme');
      document.body.style.backgroundImage = '';
    }

    if (this.currentScreen === 'home') {
      const level = Math.floor(this.stats.xp / 100) + 1;
      main.innerHTML = `
        <div class="user-header">
          <!-- USERNAME ON LINE 1, ⭐ [XP] XP • Lvl [LEVEL] ON LINE 2 BELOW USERNAME -->
          <div class="profile-badge" onclick="router.navigate('stats')" style="cursor:pointer;padding:8px 16px;">
            <div style="display:flex;flex-direction:column;align-items:flex-start;">
              <div style="font-weight:900;font-size:13.5px;color:white;line-height:1.2;">${this.userNickname}</div>
              <div style="font-weight:800;font-size:11px;color:#FBBF24;line-height:1.2;">⭐ ${this.stats.xp} XP • Lvl ${level}</div>
            </div>
          </div>
          <!-- DAILY GAMES COUNTER (0/2 TODAY) OR REWARD AD UNLOCK BUTTON -->
          <div class="currency-pill" onclick="router.showDailyLimitModal=true;router.render();" style="cursor:pointer;border-color:#FBBF24;background:${this.hasReachedDailyLimit() ? '#C2410C' : '#0F172A'};">
            ${this.hasReachedDailyLimit() ? '🎬 Unlock Games' : '🎮 ' + this.dailyGamesPlayed + '/' + this.maxDailyGames() + ' Today'}
          </div>
        </div>

        <!-- CENTERED OFFICIAL LOGO FLOATING DIRECTLY ON MAIN LOBBY BACKGROUND -->
        <div style="width:100%;height:220px;display:flex;align-items:center;justify-content:center;margin-bottom:16px;background:transparent;">
          <img src="road_trip_bingo_animated_logo.gif" alt="Road Trip Bingo Animated Logo" style="width:100%;height:100%;object-fit:contain;filter:drop-shadow(0 10px 20px rgba(0,0,0,0.3));" />
        </div>

        <!-- ⚡ PLAY NOW ▶ LARGE CREAM CAPSULE BUTTON -->
        <button class="btn btn-mockup-play" onclick="if(router.hasReachedDailyLimit()){router.showDailyLimitModal=true;}else{router.showInCarModal=true;}router.render();" style="margin-bottom:18px;">
          ⚡ PLAY NOW ▶
        </button>

        <!-- TWO CARTOON ROAD SIGN CARDS -->
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:14px;margin-bottom:14px;">
          <!-- PROFILE CARD (18PX TITLE & 12.5PX SUBTITLE) -->
          <div class="road-sign-card sign-teal" onclick="router.navigate('stats')" style="cursor:pointer;">
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <span style="font-size:26px;">👤</span>
            </div>
            <div style="font-weight:900;font-size:18px;margin-top:10px;">Profile</div>
            <div style="font-size:12.5px;color:#E2E8F0;font-weight:700;">Stats & Badges</div>
          </div>

          <!-- DECKS CARD (18PX TITLE & 12.5PX SUBTITLE) -->
          <div class="road-sign-card sign-teal" onclick="router.navigate('decks')" style="cursor:pointer;">
            <div style="display:flex;justify-content:space-between;align-items:center;">
              <span style="font-size:26px;">🎴</span>
            </div>
            <div style="font-weight:900;font-size:18px;margin-top:10px;">Decks</div>
            <div style="font-size:12.5px;color:#E2E8F0;font-weight:700;">8 Decks!</div>
          </div>
        </div>

        <!-- LEADERBOARD BUTTON BETWEEN MULTIPLAYER/DECK AND SAFETY BANNER (EXACT SAME 68PX HEIGHT AS PLAY NOW, TOP LEFT TROPHY & TEXT) -->
        <div class="road-sign-card" onclick="router.navigate('leaderboard')" style="cursor:pointer;background:linear-gradient(180deg, #1D4ED8, #1E3A8A);margin-bottom:16px;padding:12px 18px;height:68px;min-height:68px;display:flex;align-items:flex-start;justify-content:space-between;">
          <div style="text-align:left;">
            <div style="font-weight:900;font-size:18px;color:white;line-height:1.2;">🏆 LEADERBOARD</div>
            <div style="font-size:12px;color:#E2E8F0;font-weight:700;">Points & Items Found Rankings</div>
          </div>
          <span class="sign-badge" style="background:#FBBF24;color:#0F172A;align-self:flex-start;">RANKS</span>
        </div>

        <!-- ORANGE PILL SAFETY BANNER -->
        <div class="safety-banner-pill" style="margin-bottom:12px;">
          Passengers Play. Drivers Drive.
        </div>

        <!-- THREE FOOTER BUTTONS: FAQ - T&C - PRIVACY DIRECTLY BELOW PASSENGERS PLAY -->
        <div style="display:grid;grid-template-columns:1fr 1fr 1fr;gap:8px;margin-bottom:24px;">
          <button onclick="router.legalTab=0;router.navigate('terms');" style="height:36px;border-radius:12px;border:1.5px solid rgba(255,255,255,0.3);background:#0F172A;color:white;font-weight:900;font-size:12px;cursor:pointer;">❓ FAQ</button>
          <button onclick="router.legalTab=1;router.navigate('terms');" style="height:36px;border-radius:12px;border:1.5px solid rgba(255,255,255,0.3);background:#0F172A;color:white;font-weight:900;font-size:12px;cursor:pointer;">📜 T&C</button>
          <button onclick="router.legalTab=2;router.navigate('terms');" style="height:36px;border-radius:12px;border:1.5px solid rgba(255,255,255,0.3);background:#0F172A;color:white;font-weight:900;font-size:12px;cursor:pointer;">🔒 Privacy</button>
        </div>

        <!-- IN-CAR PLAY MODAL -->
        ${this.showInCarModal ? `
          <div style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.88);display:flex;align-items:center;justify-content:center;padding:20px;z-index:300;" onclick="router.showInCarModal=false;router.showJoinCodeInput=false;router.render();">
            <div class="card" style="text-align:center;max-width:380px;width:100%;padding:24px;" onclick="event.stopPropagation();">
              <h2 style="font-weight:900;color:#FBBF24;margin-bottom:6px;">🚗 ROAD TRIP BINGO!</h2>
              <p style="font-size:12px;color:#CBD5E1;margin-bottom:18px;">Play bingo with passengers in your car!</p>

              ${!this.showJoinCodeInput ? `
                <button class="btn btn-mockup-play" style="margin-bottom:12px;font-size:16px;" onclick="router.startCarRoomFromHome()">
                  🚗 START A GAME (HOST)
                </button>
                <button class="btn btn-teal btn-beveled" style="margin-bottom:14px;font-size:16px;" onclick="router.showJoinCodeInput=true;router.render();">
                  📱 JOIN A GAME (CODE)
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

        <!-- DAILY GAME LIMIT / REWARDED AD PROMPT MODAL -->
        ${this.showDailyLimitModal ? `
          <div style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.88);display:flex;align-items:center;justify-content:center;padding:20px;z-index:300;" onclick="router.showDailyLimitModal=false;router.render();">
            <div class="card" style="text-align:center;max-width:380px;width:100%;padding:24px;" onclick="event.stopPropagation();">
              <h2 style="font-weight:900;color:#FBBF24;margin-bottom:6px;">🚗 DAILY GAMES LIMIT</h2>
              <p style="font-size:14px;font-weight:800;color:white;line-height:1.5;margin-bottom:18px;">
                Want another round on this road trip?<br>
                Watch a quick video to unlock 2 more games instantly!
              </p>
              <button class="btn btn-orange btn-beveled" style="font-size:15px;margin-bottom:10px;" onclick="router.watchRewardedAd();">
                🎬 WATCH AD TO UNLOCK +2 GAMES ▶
              </button>
              <button style="background:none;border:none;color:#94A3B8;font-size:13px;font-weight:700;cursor:pointer;" onclick="router.showDailyLimitModal=false;router.render();">
                Cancel
              </button>
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
    } else if (this.currentScreen === 'multiplayer') {
      const roomPlayers = this.activeRoom && this.activeRoom.players ? this.activeRoom.players : [{ nickname: this.userNickname, isHost: true }];
      const isHost = roomPlayers.find(p => p.nickname === this.userNickname)?.isHost || false;

      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:20px;margin-bottom:16px;">
          <h2 style="font-weight:900;color:white;">🚗 Road Trip Bingo!</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>

        <div class="card" style="text-align:center;padding:24px;">
          <p style="font-size:12px;font-weight:800;color:#CBD5E1;margin-bottom:4px;">ROOM LOBBY CODE</p>
          <div style="display:inline-block;background:linear-gradient(180deg, #FBBF24, #F97316);color:#0F172A;font-weight:900;font-size:36px;padding:8px 24px;border-radius:16px;letter-spacing:4px;margin-bottom:10px;">
            ${this.roomCode}
          </div>
          <p style="font-size:12px;color:#CBD5E1;margin-bottom:20px;">Share this code with other passengers in your vehicle!</p>

          <div style="text-align:left;margin-bottom:16px;">
            <p style="font-size:12px;font-weight:900;color:#FBBF24;margin-bottom:8px;">PASSENGERS IN ROOM (${roomPlayers.length})</p>
            ${roomPlayers.map(p => `
              <div style="background:#0F172A;padding:10px 14px;border-radius:12px;display:flex;justify-content:space-between;align-items:center;margin-bottom:6px;">
                <span style="font-weight:800;color:white;font-size:14px;">🚗 ${p.nickname} ${p.isHost ? '<span style="background:#FBBF24;color:#0F172A;font-size:9px;font-weight:900;padding:2px 6px;border-radius:6px;margin-left:6px;">HOST</span>' : ''}</span>
                <span style="color:#10B981;font-size:12px;font-weight:800;">READY ✓</span>
              </div>
            `).join('')}
          </div>

          ${isHost ? `
            <button class="btn btn-mockup-play" style="min-height:56px;font-size:18px;" onclick="router.hostLaunchGame()">
              🚀 LAUNCH GAME NOW ▶
            </button>
          ` : `
            <div style="background:#0F172A;border:1.5px solid #FBBF24;padding:14px;border-radius:14px;color:#FBBF24;font-weight:900;font-size:15px;text-align:center;">
              ⏳ WAITING FOR HOST TO START ▶
            </div>
          `}
        </div>
      `;
    } else if (this.currentScreen === 'login') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:10px;margin-bottom:16px;">
          <h2 style="font-weight:900;color:white;">${this.isLoggedIn ? '👤 Account' : '🔑 Log In / Join'}</h2>
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
            <h3 style="font-weight:900;color:#FBBF24;margin-bottom:6px;">👤 CURRENTLY LOGGED IN</h3>
            <h2 style="font-weight:900;color:white;margin-bottom:4px;font-size:24px;">${this.userNickname}</h2>
            <p style="font-size:13px;color:#FBBF24;font-weight:800;margin-bottom:16px;">Level ${Math.floor(this.stats.xp/100)+1} Explorer • ${this.stats.xp} XP</p>
            <button class="btn btn-orange btn-beveled" style="min-height:54px;font-size:18px;" onclick="router.logoutUser()">
              🚪 LOG OUT
            </button>
          </div>
        `}
      `;
    } else if (this.currentScreen === 'shop') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:10px;margin-bottom:16px;">
          <h2 style="font-weight:900;color:white;">🏪 Shop</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>

        <div style="width:100%;height:180px;display:flex;align-items:center;justify-content:center;margin-bottom:16px;background:transparent;">
          <img src="road_trip_bingo_animated_logo.gif" alt="Road Trip Bingo Animated Logo" style="width:100%;height:100%;object-fit:contain;filter:drop-shadow(0 10px 20px rgba(0,0,0,0.3));" />
        </div>

        <!-- FEATURED ITEM: HOLIDAY ROAD TRIP DECK ($0.99) -->
        <div class="card" style="background:#0F172A;border:2px solid #FBBF24;padding:20px;margin-bottom:18px;">
          <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
            <div style="display:flex;align-items:center;gap:10px;">
              <span style="font-size:32px;">🎄</span>
              <div>
                <strong style="font-size:17px;color:white;display:block;">Holiday Road Trip Deck</strong>
                <span style="font-size:12px;color:#CBD5E1;">Festive sights & winter lights</span>
              </div>
            </div>
            <span class="sign-badge" style="background:${this.unlockedDecks.includes('deck_holiday') ? '#10B981' : '#F97316'};color:white;font-size:13px;padding:5px 10px;">${this.unlockedDecks.includes('deck_holiday') ? 'UNLOCKED' : '$0.99'}</span>
          </div>
          ${this.unlockedDecks.includes('deck_holiday') ? `
            <button class="btn btn-teal" style="min-height:48px;font-size:15px;width:100%;" onclick="router.selectDeckAndPlay(DECKS.find(x=>x.id==='deck_holiday'))">
              ✓ UNLOCKED & READY TO PLAY ▶
            </button>
          ` : `
            <button class="btn btn-mockup-play" style="min-height:48px;font-size:15px;width:100%;" onclick="router.unlockDeck('deck_holiday')">
              🛒 UNLOCK DECK ($0.99) ▶
            </button>
          `}
        </div>
      `;
      `;
    } else if (this.currentScreen === 'leaderboard') {
      const level = Math.floor(this.stats.xp / 100) + 1;
      const pointsList = [
        { rank: 1, name: `${this.userNickname} (You)`, emoji: "🚗", val: this.stats.xp, isUser: true }
      ];

      const itemsList = [
        { rank: 1, name: `${this.userNickname} (You)`, emoji: "🚗", val: this.stats.items || 18, isUser: true }
      ];

      const activeList = this.leaderboardTab === 0 ? pointsList : itemsList;

      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:10px;margin-bottom:16px;">
          <h2 style="font-weight:900;color:white;">🏆 Leaderboard</h2>
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

        <div style="margin-bottom:104px;">
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
        </div>
      `;
    } else if (this.currentScreen === 'game') {
      const isWin = BingoWebEngine.checkWin(this.board, this.selectedMode.id);
      const completedCount = this.board.filter(t => t.completed).length;

      const connectedPassengers = this.activeRoom && this.activeRoom.players ? this.activeRoom.players.filter(p => p.nickname !== this.userNickname) : [];

      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:10px;margin-bottom:12px;">
          <button class="back-pill" onclick="router.navigate('home')">◀ Exit</button>
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
          ${this.board.map(t => {
            if (t.isCenter) {
              return `<div class="tile center" onclick="router.toggleTile(${t.index}, event)">⭐<br>FREE</div>`;
            }
            const words = t.name.split(" ").filter(w => w.length > 0);
            const longestWordLen = Math.max(...words.map(w => w.length));

            let linesText = t.name;
            if (words.length === 2) linesText = `${words[0]}<br>${words[1]}`;
            else if (words.length === 3) linesText = `${words[0]}<br>${words[1]}<br>${words[2]}`;
            else if (words.length === 4) linesText = `${words[0]} ${words[1]}<br>${words[2]}<br>${words[3]}`;
            else if (words.length === 5) linesText = `${words[0]} ${words[1]}<br>${words[2]}<br>${words[3]} ${words[4]}`;
            else if (words.length > 5) {
              const l1 = words.slice(0,2).join(" ");
              const l2 = words.slice(2, words.length - 2).join(" ");
              const l3 = words.slice(words.length - 2).join(" ");
              linesText = `${l1}<br>${l2}<br>${l3}`;
            }

            const lines = linesText.split("<br>");
            const maxLineLen = Math.max(...lines.map(l => l.length));

            let fontSz = "10.5px";
            if (longestWordLen >= 13) fontSz = "8px";
            else if (longestWordLen >= 11) fontSz = "8.8px";
            else if (longestWordLen >= 10) fontSz = "9.5px";
            else if (maxLineLen <= 5 && lines.length <= 2) fontSz = "14.5px";
            else if (maxLineLen <= 7 && lines.length <= 2) fontSz = "13.5px";
            else if (maxLineLen <= 9 && lines.length <= 2) fontSz = "12.5px";
            else if (maxLineLen <= 9 && lines.length === 3) fontSz = "12px";
            else if (maxLineLen <= 12 && lines.length <= 2) fontSz = "11px";
            else if (maxLineLen <= 12 && lines.length === 3) fontSz = "10px";
            else fontSz = "8.5px";

            return `<div class="tile ${t.completed ? 'completed' : ''}" onclick="router.toggleTile(${t.index}, event)"><span style="font-size:${fontSz};line-height:1.15;white-space:pre-line;word-break:normal;overflow-wrap:normal;">${linesText}</span></div>`;
          }).join('')}
        </div>

        <div style="display:flex;justify-content:center;align-items:center;margin-top:12px;">
          ${isWin ? '<div style="background:#F59E0B;color:#0F172A;font-weight:900;padding:8px 14px;border-radius:12px;font-size:13px;">🎉 BINGO!</div>' : ''}
        </div>

        <!-- LIVE CONNECTED PASSENGER BOARDS SECTION (DISPLAYED IN ROWS OF EXACTLY 2 BOARDS SIDE-BY-SIDE) -->
        ${connectedPassengers.length > 0 ? `
          <div style="margin-top:18px;width:100%;">
            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
              <span style="font-size:12px;font-weight:900;color:#FBBF24;">CONNECTED PASSENGERS (${connectedPassengers.length})</span>
              <span style="font-size:12px;font-weight:900;color:#FEF08A;">🎯 ${this.selectedMode.title}</span>
            </div>

            <div style="display:grid;grid-template-columns:repeat(2, 1fr);gap:10px;">
              ${connectedPassengers.map(p => `
                <div class="card" style="padding:10px;margin-bottom:0;">
                  <div style="display:flex;justify-content:space-between;align-items:center;font-size:11px;font-weight:800;margin-bottom:6px;">
                    <span>🚗 ${p.nickname}</span>
                    <span style="color:#FBBF24;">${(p.completedTiles || [12]).length}/25</span>
                  </div>
                  <div style="display:grid;grid-template-columns:repeat(5,1fr);gap:2px;aspect-ratio:1;">
                    ${Array.from({length:25},(_,i)=>`
                      <div style="aspect-ratio:1;border-radius:3px;background:${i===12?'#FBBF24':(p.completedTiles || [12]).includes(i)?'#10B981':'#2A364F'};display:flex;align-items:center;justify-content:center;font-size:6px;color:white;font-weight:900;">
                        ${i===12?'⭐':(p.completedTiles || [12]).includes(i)?'✓':''}
                      </div>
                    `).join('')}
                  </div>
                </div>
              `).join('')}
            </div>
          </div>
        ` : ''}

        <!-- ACCOUNT CREATION / GOOGLE SIGN-IN POPUP OVERLAY (ONLY FOR UNAUTHENTICATED GUESTS) -->
        ${this.showAccountModal && !this.isLoggedIn ? `
          <div style="position:fixed;top:0;left:0;right:0;bottom:0;background:rgba(0,0,0,0.88);display:flex;align-items:center;justify-content:center;padding:20px;z-index:300;">
            <div class="card" style="text-align:center;max-width:380px;width:100%;padding:24px;">
              <div style="font-size:36px;margin-bottom:8px;">💾</div>
              <h2 style="font-weight:900;color:#FBBF24;margin-bottom:8px;">SAVE YOUR ACHIEVEMENTS!</h2>
              <p style="font-size:12px;color:#CBD5E1;margin-bottom:18px;">Sign in with your account to back up your Level, XP, earned badges, and lifetime Bingo wins so you never lose your road trip progress!</p>

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
        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:10px;margin-bottom:16px;">
          <h2 style="font-weight:900;">🎴 Decks</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>
        ${DECKS.map(d => {
          const isUnlocked = !d.price || this.unlockedDecks.includes(d.id);
          return `
            <div class="card" style="background:${d.bg};margin-bottom:12px;">
              <div style="display:flex;justify-content:space-between;align-items:center;">
                <strong style="font-size:16px;color:white;">${d.icon} ${d.title}</strong>
                <span class="pill-badge-yellow">${d.audience}</span>
              </div>
              <p style="font-size:12px;color:#CBD5E1;margin-top:6px;margin-bottom:12px;">Distinct Roadside Prompts</p>
              <div style="display:flex;justify-content:flex-end;align-items:center;">
                ${isUnlocked ? `
                  <button class="btn btn-orange" style="width:auto;min-height:36px;padding:0 14px;font-size:12px;" onclick="router.selectDeckAndPlay(DECKS.find(x=>x.id==='${d.id}'))">PLAY DECK ▶</button>
                ` : `
                  <button class="btn btn-yellow" style="width:auto;min-height:36px;padding:0 14px;font-size:12px;color:#0F172A;font-weight:900;" onclick="router.navigate('shop')">🔒 $0.99 IN SHOP</button>
                `}
              </div>
            </div>
          `;
        }).join('')}
      `;
    } else if (this.currentScreen === 'stats') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:10px;margin-bottom:16px;">
          <h2 style="font-weight:900;">🏆 Profile</h2>
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

        <div class="card" style="margin-bottom:14px;">
          <h4 style="margin-bottom:8px;">Player Nickname</h4>
          <input id="nickname-settings-input" type="text" value="${this.userNickname}" oninput="router.checkNicknameInput(this.value)" style="width:100%;height:44px;border-radius:12px;background:#0F172A;border:2px solid #FBBF24;color:white;padding:0 12px;font-size:14px;font-weight:800;margin-bottom:10px;" />
          <button id="save-nickname-btn" class="btn btn-orange" style="width:auto;min-height:38px;padding:0 16px;font-size:13px;opacity:0.35;cursor:not-allowed;" disabled onclick="router.saveNicknameFromInput()">Save Nickname</button>
        </div>

        <div class="card" style="margin-bottom:14px;">
          <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
            <span style="font-weight:800;color:white;">Sound Effects</span>
            <input type="checkbox" ${this.soundEnabled !== false ? 'checked' : ''} onchange="router.soundEnabled=this.checked;localStorage.setItem('bingo_sound',this.checked);router.render();" style="width:20px;height:20px;accent-color:#FBBF24;" />
          </div>
          <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
            <span style="font-weight:800;color:white;">Background Music</span>
            <input type="checkbox" ${this.musicEnabled !== false ? 'checked' : ''} onchange="router.musicEnabled=this.checked;localStorage.setItem('bingo_music',this.checked);BackgroundMusicEngine.syncState(router.currentScreen);router.render();" style="width:20px;height:20px;accent-color:#FBBF24;" />
          </div>
          <div style="display:flex;justify-content:space-between;align-items:center;">
            <span style="font-weight:800;color:white;">Haptic Vibrations</span>
            <input type="checkbox" ${this.hapticsEnabled !== false ? 'checked' : ''} onchange="router.hapticsEnabled=this.checked;localStorage.setItem('bingo_haptics',this.checked);router.render();" style="width:20px;height:20px;accent-color:#FBBF24;" />
          </div>
        </div>

        <div class="card">
          <h4>Passenger Safety Reminder</h4>
          <p style="font-size:12px;color:#94A3B8;margin-top:6px;">Always play from passenger seats. Drivers keep full attention on the road.</p>
        </div>
      `;
    } else if (this.currentScreen === 'terms') {
      main.innerHTML = `
        <div style="display:flex;justify-content:space-between;align-items:center;margin-top:10px;margin-bottom:16px;">
          <h2 style="font-weight:900;">${this.legalTab===0 ? '❓ FAQ & Guide' : this.legalTab===1 ? '📜 Terms & Conditions' : '🔒 Privacy Policy'}</h2>
          <button class="back-pill" onclick="router.navigate('home')">Back</button>
        </div>

        <div style="display:grid;grid-template-columns:1fr 1fr 1fr;gap:4px;background:#0F172A;padding:4px;border-radius:14px;margin-bottom:16px;">
          <button onclick="router.legalTab=0;router.render();" style="height:36px;border-radius:10px;border:none;font-weight:900;font-size:12px;cursor:pointer;background:${this.legalTab===0?'#FBBF24':'transparent'};color:${this.legalTab===0?'#0F172A':'white'};">❓ FAQ</button>
          <button onclick="router.legalTab=1;router.render();" style="height:36px;border-radius:10px;border:none;font-weight:900;font-size:12px;cursor:pointer;background:${this.legalTab===1?'#FBBF24':'transparent'};color:${this.legalTab===1?'#0F172A':'white'};">📜 T&C</button>
          <button onclick="router.legalTab=2;router.render();" style="height:36px;border-radius:10px;border:none;font-weight:900;font-size:12px;cursor:pointer;background:${this.legalTab===2?'#FBBF24':'transparent'};color:${this.legalTab===2?'#0F172A':'white'};">🔒 Privacy</button>
        </div>

        <div class="card">
          ${this.legalTab === 0 ? `
            <h3 style="color:#FBBF24;margin-bottom:12px;">Frequently Asked Questions</h3>
            <p style="font-weight:900;color:white;margin-bottom:2px;">Q: How do I play Road Trip Bingo?</p>
            <p style="font-size:12.5px;color:#CBD5E1;margin-bottom:12px;">Look out the car window! As you spot roadside items shown on your 5x5 Bingo card (like a Red Truck or Speed Limit Sign), tap the tile to complete it. Get 5 completed tiles in a row for a BINGO!</p>

            <p style="font-weight:900;color:white;margin-bottom:2px;">Q: How does local In-Car Multiplayer work?</p>
            <p style="font-size:12.5px;color:#CBD5E1;margin-bottom:12px;">One passenger taps 'Start a Game (Host)' to generate a 4-letter room code (e.g. ROAD). Other passengers tap 'Join a Game' and enter the code to play together!</p>

            <p style="font-weight:900;color:white;margin-bottom:2px;">Q: How do Daily Limits work?</p>
            <p style="font-size:12.5px;color:#CBD5E1;margin-bottom:12px;">Players get 2 free games every day. You can watch a quick video to unlock additional games. Resets every midnight!</p>
          ` : this.legalTab === 1 ? `
            <h3 style="color:#FBBF24;margin-bottom:12px;">Terms & Conditions</h3>
            <p style="font-weight:900;color:white;margin-bottom:2px;">1. Passenger Safety Disclaimer</p>
            <p style="font-size:12.5px;color:#CBD5E1;margin-bottom:12px;">Road Trip Bingo is strictly an entertainment application for vehicle PASSENGERS. Drivers must never operate or interact with this app while driving.</p>

            <p style="font-weight:900;color:white;margin-bottom:2px;">2. Free-to-Play Terms</p>
            <p style="font-size:12.5px;color:#CBD5E1;margin-bottom:12px;">Road Trip Bingo is free to play and supported by optional rewarded video ads. Watching video ads unlocks additional daily games.</p>
          ` : `
            <h3 style="color:#FBBF24;margin-bottom:12px;">Privacy Policy</h3>
            <p style="font-weight:900;color:white;margin-bottom:2px;">1. Data Protection</p>
            <p style="font-size:12.5px;color:#CBD5E1;margin-bottom:12px;">We do not track continuous background GPS, record driver location history, or transmit vehicle telemetry.</p>

            <p style="font-weight:900;color:white;margin-bottom:2px;">2. Storage & Accounts</p>
            <p style="font-size:12.5px;color:#CBD5E1;margin-bottom:12px;">Your game progress is stored locally on your device. Creating an account enables secure cloud backup.</p>

            <p style="font-weight:900;color:white;margin-bottom:2px;">3. Contact Us</p>
            <p style="font-size:12.5px;color:#CBD5E1;margin-bottom:12px;">For questions, feedback, or support, contact us at roadtripbingo@gmail.com.</p>
          `}
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
