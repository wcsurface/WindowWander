package app.vercel.windowwander.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.vercel.windowwander.data.AccountService
import app.vercel.windowwander.data.DeckRepository
import app.vercel.windowwander.data.MultiplayerService
import app.vercel.windowwander.data.UserPreferences
import app.vercel.windowwander.engine.BingoEngine
import app.vercel.windowwander.engine.WinResult
import app.vercel.windowwander.model.BingoTile
import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.model.GameMode
import app.vercel.windowwander.model.MultiplayerRoom
import app.vercel.windowwander.model.PlayerProfile
import app.vercel.windowwander.model.Region
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    DECK_SELECT,
    BINGO_GAME,
    MULTIPLAYER_LOBBY,
    LEADERBOARD,
    STATS,
    SETTINGS,
    PRIVACY_TERMS,
    SHOP,
    LOGIN
}

data class BingoUiState(
    val currentScreen: Screen = Screen.HOME,
    val selectedRegion: Region = Region.UNIVERSAL,
    val selectedDeck: Deck = DeckRepository.DECKS[2], // Mountain West
    val gameMode: GameMode = GameMode.CLASSIC_LINE,
    val currentBoard: List<BingoTile> = emptyList(),
    val winResult: WinResult = WinResult(false, 0, emptyList(), emptyList()),
    val playerProfile: PlayerProfile = PlayerProfile(),
    val multiplayerRoom: MultiplayerRoom? = null,
    val isSearchingOpponent: Boolean = false,
    val opponentFoundName: String? = null,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val confirmTapEnabled: Boolean = false,
    val kidsModeEasy: Boolean = false,
    val darkMode: Boolean = false,
    val showBingoCelebration: Boolean = false,
    val showAccountPrompt: Boolean = false,
    val isLoggedIn: Boolean = false,
    val canUndo: Boolean = false
)

class BingoViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = UserPreferences(application)
    private val _uiState = MutableStateFlow(BingoUiState())
    val uiState: StateFlow<BingoUiState> = _uiState.asStateFlow()

    private val boardHistory = mutableListOf<List<BingoTile>>()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        val loadedProfile = preferences.getProfile()
        _uiState.update {
            it.copy(
                playerProfile = loadedProfile,
                selectedRegion = preferences.getSelectedRegion(),
                soundEnabled = preferences.isSoundEnabled(),
                hapticsEnabled = preferences.isHapticsEnabled(),
                confirmTapEnabled = preferences.isConfirmTapEnabled(),
                kidsModeEasy = preferences.isKidsModeEasy(),
                darkMode = preferences.isDarkMode()
            )
        }
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectDeck(deck: Deck) {
        _uiState.update { it.copy(selectedDeck = deck) }
    }

    fun loginUser(username: String, pass: String) {
        viewModelScope.launch {
            val result = AccountService.login(username, pass)
            val loadedProfile = result.profile ?: _uiState.value.playerProfile.copy(nickname = username)
            preferences.saveProfile(loadedProfile)
            _uiState.update {
                it.copy(
                    playerProfile = loadedProfile,
                    isLoggedIn = true,
                    currentScreen = Screen.HOME
                )
            }
        }
    }

    fun logoutUser() {
        _uiState.update {
            it.copy(
                isLoggedIn = false,
                currentScreen = Screen.HOME
            )
        }
    }

    fun startNewGame(
        deck: Deck = _uiState.value.selectedDeck,
        gameMode: GameMode = GameMode.CLASSIC_LINE
    ) {
        val newBoard = BingoEngine.generateBoard(
            deck = deck,
            selectedRegion = _uiState.value.selectedRegion,
            kidsModeEasy = _uiState.value.kidsModeEasy
        )

        boardHistory.clear()
        boardHistory.add(newBoard)

        val updatedProfile = _uiState.value.playerProfile.copy(
            gamesPlayed = _uiState.value.playerProfile.gamesPlayed + 1,
            dailyGamesPlayedToday = _uiState.value.playerProfile.dailyGamesPlayedToday + 1
        )
        preferences.saveProfile(updatedProfile)

        _uiState.update {
            it.copy(
                currentScreen = Screen.BINGO_GAME,
                selectedDeck = deck,
                gameMode = gameMode,
                currentBoard = newBoard,
                winResult = WinResult(false, 0, emptyList(), emptyList()),
                playerProfile = updatedProfile,
                showBingoCelebration = false,
                showAccountPrompt = false,
                canUndo = false
            )
        }
    }

    fun tapTile(index: Int) {
        val currentBoard = _uiState.value.currentBoard
        if (index !in currentBoard.indices) return

        val updatedBoard = currentBoard.mapIndexed { i, tile ->
            if (i == index) tile.copy(isCompleted = !tile.isCompleted) else tile
        }

        boardHistory.add(updatedBoard)

        val winEval = BingoEngine.evaluateWin(updatedBoard, _uiState.value.gameMode)
        val newlyWon = winEval.hasWon && !_uiState.value.winResult.hasWon

        var profile = _uiState.value.playerProfile
        if (newlyWon) {
            val bonusXp = 100
            val newXp = profile.xp + bonusXp
            profile = profile.copy(
                xp = newXp,
                bingosWon = profile.bingosWon + 1
            )
        }

        preferences.saveProfile(profile)
        if (_uiState.value.isLoggedIn) {
            viewModelScope.launch {
                AccountService.saveProfileToRailway(profile)
            }
        }

        _uiState.update {
            it.copy(
                currentBoard = updatedBoard,
                winResult = winEval,
                playerProfile = profile,
                showBingoCelebration = newlyWon || (winEval.hasWon && it.showBingoCelebration),
                showAccountPrompt = newlyWon,
                canUndo = boardHistory.size > 1
            )
        }
    }

    fun undoLastTap() {
        if (boardHistory.size > 1) {
            val previousBoard = boardHistory.removeAt(boardHistory.size - 1)
            val winEval = BingoEngine.evaluateWin(previousBoard, _uiState.value.gameMode)

            _uiState.update {
                it.copy(
                    currentBoard = previousBoard,
                    winResult = winEval,
                    canUndo = boardHistory.size > 1,
                    showBingoCelebration = winEval.hasWon
                )
            }
        }
    }

    fun dismissCelebration() {
        _uiState.update { it.copy(showBingoCelebration = false) }
    }

    fun handleGoogleSignIn() {
        val updated = _uiState.value.playerProfile.copy(
            nickname = "RoadExplorer_User",
            earnedBadges = _uiState.value.playerProfile.earnedBadges + "cloud_backup"
        )
        preferences.saveProfile(updated)
        _uiState.update {
            it.copy(
                playerProfile = updated,
                isLoggedIn = true,
                showAccountPrompt = false
            )
        }
    }

    fun dismissAccountPrompt() {
        _uiState.update { it.copy(showAccountPrompt = false) }
    }

    fun createLocalQuickPlayRoom() {
        val room = MultiplayerService.createLocalQuickPlayRoom(
            hostNickname = _uiState.value.playerProfile.nickname,
            hostAvatarId = _uiState.value.playerProfile.avatarId,
            deckId = _uiState.value.selectedDeck.id,
            region = _uiState.value.selectedRegion,
            gameMode = _uiState.value.gameMode
        )
        _uiState.update {
            it.copy(
                multiplayerRoom = room,
                currentScreen = Screen.MULTIPLAYER_LOBBY
            )
        }
    }

    fun createLocalQuickPlayRoomFromHome() {
        selectDeck(DeckRepository.DECKS[2]) // Default Mountain West
        createLocalQuickPlayRoom()
    }

    fun joinMultiplayerRoom(code: String) {
        val joinedRoom = MultiplayerService.joinRoomByCode(
            roomCode = code,
            playerNickname = _uiState.value.playerProfile.nickname,
            playerAvatarId = _uiState.value.playerProfile.avatarId
        )
        if (joinedRoom != null) {
            _uiState.update {
                it.copy(
                    multiplayerRoom = joinedRoom,
                    currentScreen = Screen.MULTIPLAYER_LOBBY
                )
            }
        }
    }

    fun startOnlineMatchmaking(deck: Deck = _uiState.value.selectedDeck) {
        _uiState.update {
            it.copy(
                selectedDeck = deck,
                isSearchingOpponent = true,
                opponentFoundName = null
            )
        }

        viewModelScope.launch {
            delay(2000)
            val opponents = listOf("Alex (Passenger)", "Jordan (RV Traveler)", "Taylor (Backseat)", "Morgan (Roadie)")
            val foundName = opponents.random()
            _uiState.update { it.copy(opponentFoundName = foundName) }

            delay(1200)
            _uiState.update { it.copy(isSearchingOpponent = false) }
            startNewGame(deck = deck)
        }
    }

    fun cancelMatchmaking() {
        _uiState.update {
            it.copy(
                isSearchingOpponent = false,
                opponentFoundName = null
            )
        }
    }

    fun setRegion(region: Region) {
        preferences.setSelectedRegion(region)
        _uiState.update { it.copy(selectedRegion = region) }
    }

    fun toggleSound(enabled: Boolean) {
        preferences.setSoundEnabled(enabled)
        _uiState.update { it.copy(soundEnabled = enabled) }
    }

    fun toggleHaptics(enabled: Boolean) {
        preferences.setHapticsEnabled(enabled)
        _uiState.update { it.copy(hapticsEnabled = enabled) }
    }

    fun toggleConfirmTap(enabled: Boolean) {
        preferences.setConfirmTapEnabled(enabled)
        _uiState.update { it.copy(confirmTapEnabled = enabled) }
    }

    fun toggleKidsMode(enabled: Boolean) {
        preferences.setKidsModeEasy(enabled)
        _uiState.update { it.copy(kidsModeEasy = enabled) }
    }

    fun updateProfile(nickname: String, avatarId: String) {
        val updated = _uiState.value.playerProfile.copy(nickname = nickname, avatarId = avatarId)
        preferences.saveProfile(updated)
        if (_uiState.value.isLoggedIn) {
            viewModelScope.launch {
                AccountService.saveProfileToRailway(updated)
            }
        }
        _uiState.update { it.copy(playerProfile = updated) }
    }
}
