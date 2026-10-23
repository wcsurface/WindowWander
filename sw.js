const CACHE_NAME = 'road-trip-bingo-v212';
const ASSETS = [
  '/',
  'index.html',
  'style.css?v=212',
  'app.js?v=212',
  'manifest.json',
  'road_trip_bingo_logo.png',
  'nav_home_icon.png',
  'nav_shop_icon.png',
  'nav_settings_icon.png',
  'bg_main_home.jpg',
  'bg_settings.jpg',
  'bg_leaderboard.jpg',
  'bg_classic_roadtrip.jpg',
  'bg_weird_roadtrip.jpg',
  'bg_rocky_mountains.jpg',
  'bg_desert_southwest.jpg',
  'bg_northeast.jpg',
  'bg_south.jpg',
  'bg_west_coast.jpg',
  'bg_heartland.jpg',
  'bg_holiday.jpg'
];

self.addEventListener('install', (event) => {
  self.skipWaiting();
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(ASSETS))
  );
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) => {
      return Promise.all(
        keys.map((key) => caches.delete(key))
      );
    }).then(() => self.clients.claim())
  );
});

// NETWORK-FIRST FETCH STRATEGY (ALWAYS FECHES FRESH ASSETS FROM VERCEL)
self.addEventListener('fetch', (event) => {
  event.respondWith(
    fetch(event.request).then((networkResponse) => {
      if (networkResponse && networkResponse.status === 200) {
        const responseClone = networkResponse.clone();
        caches.open(CACHE_NAME).then((cache) => {
          cache.put(event.request, responseClone);
        });
      }
      return networkResponse;
    }).catch(() => {
      return caches.match(event.request);
    })
  );
});
