package app.vercel.windowwander.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
    STATS,
    SETTINGS,
    PRIVACY_TERMS
}

data class UiState(
    val currentScreen: Screen = Screen.HOME,
    val selectedDeck: Deck = DeckRepository.DECKS[0],
    val currentBoard: List<BingoTile> = emptyList(),
    val winResult: WinResult = WinResult(false, 0, emptyList(), emptyList()),
    val gameMode: GameMode = GameMode.CLASSIC_LINE,
    val selectedRegion: Region = Region.UNIVERSAL,
    val playerProfile: PlayerProfile = PlayerProfile(),
    val multiplayerRoom: MultiplayerRoom? = null,
    val showBingoCelebration: Boolean = false,
    val showAccountPrompt: Boolean = false,
    val isSearchingOpponent: Boolean = false,
    val opponentFoundName: String? = null,
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val confirmTapEnabled: Boolean = false,
    val kidsModeEasy: Boolean = false,
    val darkMode: Boolean = false,
    val canUndo: Boolean = false
)

class BingoViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = UserPreferences(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val boardHistory = mutableListOf<List<BingoTile>>()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        val profile = preferences.getProfile()
        val region = preferences.getSelectedRegion()
        val sound = preferences.isSoundEnabled()
        val haptics = preferences.isHapticsEnabled()
        val confirmTap = preferences.isConfirmTapEnabled()
        val kidsMode = preferences.isKidsModeEasy()
        val darkMode = preferences.isDarkMode()

        _uiState.update {
            it.copy(
                playerProfile = profile,
                selectedRegion = region,
                soundEnabled = sound,
                hapticsEnabled = haptics,
                confirmTapEnabled = confirmTap,
                kidsModeEasy = kidsMode,
                darkMode = darkMode
            )
        }
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectDeck(deck: Deck) {
        _uiState.update { it.copy(selectedDeck = deck) }
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

        val initialWin = BingoEngine.evaluateWin(newBoard, gameMode)

        val currentProfile = _uiState.value.playerProfile
        val updatedProfile = currentProfile.copy(
            gamesPlayed = currentProfile.gamesPlayed + 1,
            dailyGamesPlayedToday = currentProfile.dailyGamesPlayedToday + 1
        )
        preferences.saveProfile(updatedProfile)

        _uiState.update {
            it.copy(
                selectedDeck = deck,
                currentBoard = newBoard,
                winResult = initialWin,
                gameMode = gameMode,
                playerProfile = updatedProfile,
                showBingoCelebration = false,
                showAccountPrompt = false,
                isSearchingOpponent = false,
                opponentFoundName = null,
                currentScreen = Screen.BINGO_GAME,
                canUndo = false
            )
        }
    }

    fun startOnlineMatchmaking(deck: Deck = _uiState.value.selectedDeck) {
        _uiState.update {
            it.copy(
                selectedDeck = deck,
                gameMode = GameMode.CLASSIC_LINE, // Online is strictly Classic Bingo
                isSearchingOpponent = true,
                opponentFoundName = null
            )
        }

        viewModelScope.launch {
            delay(2000) // Matchmaking delay
            val opponentNames = listOf("Alex (Passenger)", "Jordan (RV Traveler)", "Taylor (Backseat)", "Morgan (Roadie)")
            val opponent = opponentNames.random()

            _uiState.update {
                it.copy(
                    opponentFoundName = opponent
                )
            }

            delay(1200) // Transition to board
            startNewGame(deck = deck, gameMode = GameMode.CLASSIC_LINE)
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

    fun tapTile(tileIndex: Int) {
        val currentBoard = _uiState.value.currentBoard
        if (tileIndex < 0 || tileIndex >= currentBoard.size) return

        boardHistory.add(currentBoard)

        val updatedBoard = BingoEngine.toggleTile(currentBoard, tileIndex)
        val winEval = BingoEngine.evaluateWin(updatedBoard, _uiState.value.gameMode)

        val wasAlreadyWon = _uiState.value.winResult.hasWon
        val newlyWon = winEval.hasWon && !wasAlreadyWon

        var profile = _uiState.value.playerProfile

        val isNowCompleted = updatedBoard[tileIndex].isCompleted
        if (isNowCompleted && !updatedBoard[tileIndex].isCenter) {
            profile = profile.copy(
                xp = profile.xp + 10,
                squaresFound = profile.squaresFound + 1
            )
        }

        if (newlyWon) {
            profile = profile.copy(
                xp = profile.xp + 100,
                bingosWon = profile.bingosWon + 1
            )
        }

        preferences.saveProfile(profile)

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

    fun dismissAccountPrompt() {
        _uiState.update { it.copy(showAccountPrompt = false) }
    }

    fun handleGoogleSignIn() {
        val updated = _uiState.value.playerProfile.copy(
            nickname = "RoadExplorer_Google",
            earnedBadges = _uiState.value.playerProfile.earnedBadges + "cloud_backup"
        )
        preferences.saveProfile(updated)
        _uiState.update {
            it.copy(
                playerProfile = updated,
                showAccountPrompt = false
            )
        }
    }

    fun setRegion(region: Region) {
        preferences.setSelectedRegion(region)
        _uiState.update { it.copy(selectedRegion = region) }
    }

    fun updateProfile(nickname: String, avatarId: String) {
        val updated = _uiState.value.playerProfile.copy(
            nickname = nickname,
            avatarId = avatarId
        )
        preferences.saveProfile(updated)
        _uiState.update { it.copy(playerProfile = updated) }
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

    fun createLocalQuickPlayRoom() {
        val profile = _uiState.value.playerProfile
        val room = MultiplayerService.createLocalQuickPlayRoom(
            hostNickname = profile.nickname,
            hostAvatarId = profile.avatarId,
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

    fun joinMultiplayerRoom(roomCode: String) {
        val profile = _uiState.value.playerProfile
        val room = MultiplayerService.joinRoomByCode(
            roomCode = roomCode,
            playerNickname = profile.nickname,
            playerAvatarId = profile.avatarId
        )

        _uiState.update {
            it.copy(
                multiplayerRoom = room,
                currentScreen = Screen.MULTIPLAYER_LOBBY
            )
        }
    }
}
