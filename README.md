# 🚗 ROAD TRIP BINGO (WindowWander)
> **A complete, launch-ready, mobile-first casual arcade road trip game for passengers.**

Road Trip Bingo transforms ordinary car rides into friendly, real-time competitions with 8 themed bingo decks, regional intelligence, local and online multiplayer, XP leveling, and full offline support.

---

## 🎴 Starter Decks Included (320+ Prompts)
1. **Deck A: Classic Road Trip** – Families & kids (Red car, semi-truck, school bus, rest area, out-of-state plate, etc.)
2. **Deck B: Weird Road Trip** – Teens & adults (Bizarre vanity plates, absurd billboards, giant statues, funny stickers)
3. **Deck C: Mountain West & Rockies** – Ski racks, snow-capped peaks, wildlife crossings, canyon passes
4. **Deck D: Desert & Southwest** – Saguaro cacti, red rock mesas, Hatch chiles, Route 66 shields
5. **Deck E: Northeast & Mid-Atlantic** – Covered bridges, toll plazas, stone homes, diners, autumn foliage
6. **Deck F: The South** – Boiled peanut stands, sweet tea, waffle houses, cypress swamps, live oaks
7. **Deck G: West Coast** – Redwood trees, Pacific highway cliffs, espresso shacks, surfboards, food trucks
8. **Deck H: Heart of the Country** – Endless cornfields, grain silos, farm windmills, tractors on road

---

## 🚀 STEP-BY-STEP DEPLOYMENT & LAUNCH GUIDE

### 1. Connecting GitKraken Desktop & GitHub
1. Open **GitKraken Desktop**.
2. Select **File > Open Repository** and open this project directory: `C:/Users/wcsur/AndroidStudioProjects/WindowWander`.
3. In GitKraken, click **Remote > Add Remote** and connect your current GitHub account.
4. Create a new repository named `road-trip-bingo` on GitHub (**separated from your euchre game**).
5. Stage all files, create your initial commit: `"Launch Road Trip Bingo v1.0.0"`.
6. Push your branch to `origin/main`.
7. In GitKraken or GitHub, select **Create Release** and tag as `v1.0.0`.

---

### 2. Deploying Web Version to Vercel
1. Go to [Vercel.com](https://vercel.com) and log in.
2. Click **Add New > Project**.
3. Import your new `road-trip-bingo` GitHub repository.
4. Vercel automatically detects `index.html` and `vercel.json`.
5. Click **Deploy**. Your app will be live at `https://road-trip-bingo.vercel.app` (or your custom domain)!

---

### 3. Deploying Multiplayer Backend to Railway
1. Go to [Railway.app](https://railway.app) and log in.
2. Click **New Project > Deploy from GitHub repo**.
3. Select `road-trip-bingo`.
4. Railway will automatically detect `railway/railway.json` and start `node railway/server.js`.
5. Add a PostgreSQL database plugin in Railway if you want persistent database storage.

---

### 4. Running the Native Android App in Android Studio
1. Open this project in **Android Studio**.
2. Sync Gradle files (`Gradle Sync`).
3. Select your connected Android device or emulator.
4. Click **Run (`Shift + F10`)** or build an APK via **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

---

## 🛡️ Safety & Privacy
- **Passengers play. Drivers drive.** Clear safety reminders ensure drivers stay focused on the road.
- **No continuous GPS tracking.** Uses simple 1-time location or manual region selection.
- **Offline Capable.** Full single-player game works offline in cellular dead zones.

---

## 🧪 Testing
Run automated unit tests in terminal or Android Studio:
```bash
./gradlew testDebugUnitTest
```
All 9 unit tests verify prompt catalog integrity, board generation, and line win detection logic.
