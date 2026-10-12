const CACHE_NAME = 'road-trip-bingo-v52';
const ASSETS = [
  '/',
  'index.html',
  'style.css?v=52',
  'app.js?v=52',
  'manifest.json',
  'road_trip_bingo_animated_logo.gif',
  'nav_home_icon.png',
  'nav_login_icon.png',
  'nav_shop_icon.png',
  'nav_settings_icon.png',
  'bg_main_home.jpg',
  'bg_settings.jpg',
  'bg_classic_roadtrip.jpg',
  'bg_weird_roadtrip.jpg',
  'bg_rocky_mountains.jpg',
  'bg_desert_southwest.jpg',
  'bg_northeast.jpg',
  'bg_south.jpg',
  'bg_west_coast.jpg',
  'bg_heartland.jpg'
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
        keys.map((key) => {
          if (key !== CACHE_NAME) {
            return caches.delete(key);
          }
        })
      );
    }).then(() => self.clients.claim())
  );
});

self.addEventListener('fetch', (event) => {
  event.respondWith(
    caches.match(event.request).then((response) => {
      return response || fetch(event.request);
    })
  );
});
