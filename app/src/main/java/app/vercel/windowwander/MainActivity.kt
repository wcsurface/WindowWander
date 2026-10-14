package app.vercel.windowwander

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import app.vercel.windowwander.data.AdMobManager
import app.vercel.windowwander.data.BackgroundMusicManager
import app.vercel.windowwander.ui.screens.BingoGameScreen
import app.vercel.windowwander.ui.screens.DeckSelectScreen
import app.vercel.windowwander.ui.screens.HomeScreen
import app.vercel.windowwander.ui.screens.LeaderboardScreen
import app.vercel.windowwander.ui.screens.LoginScreen
import app.vercel.windowwander.ui.screens.MultiplayerScreen
import app.vercel.windowwander.ui.screens.PrivacyTermsScreen
import app.vercel.windowwander.ui.screens.SettingsScreen
import app.vercel.windowwander.ui.screens.ShopScreen
import app.vercel.windowwander.ui.screens.StatsAchievementsScreen
import app.vercel.windowwander.ui.theme.WindowWanderTheme
import app.vercel.windowwander.viewmodel.BingoViewModel
import app.vercel.windowwander.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: BingoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableImmersiveStickyMode()
        AdMobManager.initialize(this)

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            // BACKGROUND MUSIC SYNC ACROSS SCREENS
            LaunchedEffect(uiState.currentScreen, uiState.soundEnabled) {
                BackgroundMusicManager.syncMusicState(
                    context = this@MainActivity,
                    currentScreen = uiState.currentScreen,
                    soundEnabled = uiState.soundEnabled
                )
            }

            WindowWanderTheme(darkTheme = uiState.darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (uiState.currentScreen) {
                        Screen.HOME -> HomeScreen(
                            profile = uiState.playerProfile,
                            selectedRegion = uiState.selectedRegion,
                            selectedDeck = uiState.selectedDeck,
                            isSearchingOpponent = uiState.isSearchingOpponent,
                            opponentFoundName = uiState.opponentFoundName,
                            isLoggedIn = uiState.isLoggedIn,
                            onQuickPlay = { viewModel.startNewGame() },
                            onPlayInCarHost = { viewModel.createLocalQuickPlayRoomFromHome() },
                            onPlayInCarJoin = { code -> viewModel.joinMultiplayerRoom(code) },
                            onStartOnlineMatchmaking = { deck -> viewModel.startOnlineMatchmaking(deck) },
                            onCancelMatchmaking = { viewModel.cancelMatchmaking() },
                            onChooseDeck = { viewModel.navigateTo(Screen.DECK_SELECT) },
                            onOpenLeaderboard = { viewModel.navigateTo(Screen.LEADERBOARD) },
                            onOpenLogin = { viewModel.navigateTo(Screen.LOGIN) },
                            onOpenShop = { viewModel.navigateTo(Screen.SHOP) },
                            onOpenStats = { viewModel.navigateTo(Screen.STATS) },
                            onOpenSettings = { viewModel.navigateTo(Screen.SETTINGS) },
                            onOpenPrivacy = { viewModel.navigateTo(Screen.PRIVACY_TERMS) },
                            onWatchRewardedAd = {
                                AdMobManager.showRewardedAd(
                                    activity = this@MainActivity,
                                    onRewardEarned = { viewModel.unlockBonusGamesFromAd() },
                                    onAdFailed = {}
                                )
                            }
                        )

                        Screen.DECK_SELECT -> DeckSelectScreen(
                            onSelectDeckAndPlay = { deck ->
                                viewModel.selectDeck(deck)
                                viewModel.createLocalQuickPlayRoom()
                            },
                            onBackToHome = { viewModel.navigateTo(Screen.HOME) }
                        )

                        Screen.BINGO_GAME -> BingoGameScreen(
                            deck = uiState.selectedDeck,
                            tiles = uiState.currentBoard,
                            winResult = uiState.winResult,
                            gameMode = uiState.gameMode,
                            canUndo = uiState.canUndo,
                            showCelebration = uiState.showBingoCelebration,
                            showAccountPrompt = uiState.showAccountPrompt,
                            soundEnabled = uiState.soundEnabled,
                            hapticsEnabled = uiState.hapticsEnabled,
                            hasReachedDailyLimit = uiState.playerProfile.hasReachedDailyLimit,
                            multiplayerRoom = uiState.multiplayerRoom,
                            currentUserNickname = uiState.playerProfile.nickname,
                            onTapTile = { index -> viewModel.tapTile(index) },
                            onUndo = { viewModel.undoLastTap() },
                            onNewGame = { viewModel.startNewGame() },
                            onNextRound = { viewModel.startNextRound() },
                            onDismissCelebration = { viewModel.dismissCelebration() },
                            onGoogleSignIn = { viewModel.handleGoogleSignIn() },
                            onDismissAccountPrompt = { viewModel.dismissAccountPrompt() },
                            onBackToHome = { viewModel.navigateTo(Screen.HOME) },
                            onWatchRewardedAd = {
                                AdMobManager.showRewardedAd(
                                    activity = this@MainActivity,
                                    onRewardEarned = { viewModel.unlockBonusGamesFromAd() },
                                    onAdFailed = {}
                                )
                            }
                        )

                        Screen.MULTIPLAYER_LOBBY -> MultiplayerScreen(
                            room = uiState.multiplayerRoom,
                            selectedDeck = uiState.selectedDeck,
                            selectedGameMode = uiState.gameMode,
                            currentUserNickname = uiState.playerProfile.nickname,
                            onSelectDeck = { deck -> viewModel.selectDeck(deck) },
                            onSelectGameMode = { gameMode -> viewModel.selectGameMode(gameMode) },
                            onCreateRoom = { viewModel.createLocalQuickPlayRoom() },
                            onJoinRoom = { code -> viewModel.joinMultiplayerRoom(code) },
                            onStartGame = { viewModel.launchGameByHost() },
                            onBackToHome = { viewModel.navigateTo(Screen.HOME) }
                        )

                        Screen.LEADERBOARD -> LeaderboardScreen(
                            profile = uiState.playerProfile,
                            isLoggedIn = uiState.isLoggedIn,
                            onNavigateHome = { viewModel.navigateTo(Screen.HOME) },
                            onNavigateLogin = { viewModel.navigateTo(Screen.LOGIN) },
                            onNavigateShop = { viewModel.navigateTo(Screen.SHOP) },
                            onNavigateSettings = { viewModel.navigateTo(Screen.SETTINGS) },
                            onBackToHome = { viewModel.navigateTo(Screen.HOME) }
                        )

                        Screen.SHOP -> ShopScreen(
                            xp = uiState.playerProfile.xp,
                            isLoggedIn = uiState.isLoggedIn,
                            onNavigateHome = { viewModel.navigateTo(Screen.HOME) },
                            onNavigateLogin = { viewModel.navigateTo(Screen.LOGIN) },
                            onNavigateShop = { viewModel.navigateTo(Screen.SHOP) },
                            onNavigateSettings = { viewModel.navigateTo(Screen.SETTINGS) },
                            onBackToHome = { viewModel.navigateTo(Screen.HOME) }
                        )

                        Screen.LOGIN -> LoginScreen(
                            profile = uiState.playerProfile,
                            isLoggedIn = uiState.isLoggedIn,
                            onLoginSubmit = { user, pass -> viewModel.loginUser(user, pass) },
                            onLogOut = { viewModel.logoutUser() },
                            onNavigateHome = { viewModel.navigateTo(Screen.HOME) },
                            onNavigateLogin = { viewModel.navigateTo(Screen.LOGIN) },
                            onNavigateShop = { viewModel.navigateTo(Screen.SHOP) },
                            onNavigateSettings = { viewModel.navigateTo(Screen.SETTINGS) }
                        )

                        Screen.STATS -> StatsAchievementsScreen(
                            profile = uiState.playerProfile,
                            isLoggedIn = uiState.isLoggedIn,
                            onNavigateHome = { viewModel.navigateTo(Screen.HOME) },
                            onNavigateLogin = { viewModel.navigateTo(Screen.LOGIN) },
                            onNavigateShop = { viewModel.navigateTo(Screen.SHOP) },
                            onNavigateSettings = { viewModel.navigateTo(Screen.SETTINGS) },
                            onBackToHome = { viewModel.navigateTo(Screen.HOME) }
                        )

                        Screen.SETTINGS -> SettingsScreen(
                            currentNickname = uiState.playerProfile.nickname,
                            selectedRegion = uiState.selectedRegion,
                            soundEnabled = uiState.soundEnabled,
                            hapticsEnabled = uiState.hapticsEnabled,
                            confirmTapEnabled = uiState.confirmTapEnabled,
                            kidsModeEasy = uiState.kidsModeEasy,
                            darkMode = uiState.darkMode,
                            isLoggedIn = uiState.isLoggedIn,
                            onUpdateNickname = { name -> viewModel.updateProfile(nickname = name, avatarId = uiState.playerProfile.avatarId) },
                            onSelectRegion = { region -> viewModel.setRegion(region) },
                            onToggleSound = { enabled -> viewModel.toggleSound(enabled) },
                            onToggleHaptics = { enabled -> viewModel.toggleHaptics(enabled) },
                            onToggleConfirmTap = { enabled -> viewModel.toggleConfirmTap(enabled) },
                            onToggleKidsMode = { enabled -> viewModel.toggleKidsMode(enabled) },
                            onNavigateHome = { viewModel.navigateTo(Screen.HOME) },
                            onNavigateLogin = { viewModel.navigateTo(Screen.LOGIN) },
                            onNavigateShop = { viewModel.navigateTo(Screen.SHOP) },
                            onNavigateSettings = { viewModel.navigateTo(Screen.SETTINGS) },
                            onBackToHome = { viewModel.navigateTo(Screen.HOME) }
                        )

                        Screen.PRIVACY_TERMS -> PrivacyTermsScreen(
                            onBackToHome = { viewModel.navigateTo(Screen.HOME) }
                        )
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        BackgroundMusicManager.pauseMusic()
    }

    override fun onResume() {
        super.onResume()
        BackgroundMusicManager.syncMusicState(this, viewModel.uiState.value.currentScreen, viewModel.uiState.value.soundEnabled)
    }

    override fun onDestroy() {
        super.onDestroy()
        BackgroundMusicManager.stopAndRelease()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            enableImmersiveStickyMode()
        }
    }

    private fun enableImmersiveStickyMode() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())
    }
}
