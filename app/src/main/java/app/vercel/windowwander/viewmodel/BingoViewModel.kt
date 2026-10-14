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
import kotlin.random.Random

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
        val loggedIn = preferences.isLoggedIn() || (loadedProfile.nickname.isNotBlank() && !loadedProfile.nickname.startsWith("Road Explorer"))

        _uiState.update {
            it.copy(
                playerProfile = loadedProfile,
                isLoggedIn = loggedIn,
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
        _uiState.update {
            it.copy(
                currentScreen = screen,
                showBingoCelebration = if (screen == Screen.HOME) false else it.showBingoCelebration,
                showAccountPrompt = if (screen == Screen.HOME) false else it.showAccountPrompt,
                multiplayerRoom = if (screen == Screen.HOME) null else it.multiplayerRoom
            )
        }
    }

    fun selectDeck(deck: Deck) {
        _uiState.update { it.copy(selectedDeck = deck) }
        updateRoomSettings(deck = deck)
    }

    fun selectGameMode(gameMode: GameMode) {
        _uiState.update { it.copy(gameMode = gameMode) }
        updateRoomSettings(gameMode = gameMode)
    }

    fun updateRoomSettings(deck: Deck = _uiState.value.selectedDeck, gameMode: GameMode = _uiState.value.gameMode) {
        _uiState.update { it.copy(selectedDeck = deck, gameMode = gameMode) }

        val roomCode = _uiState.value.multiplayerRoom?.roomCode
        if (!roomCode.isNullOrBlank()) {
            viewModelScope.launch {
                AccountService.updateRoomSettingsOnRailway(
                    code = roomCode,
                    deckId = deck.id,
                    gameMode = gameMode.name
                )
            }
        }
    }

    fun loginUser(username: String, pass: String) {
        viewModelScope.launch {
            val result = AccountService.login(username, pass)
            val loadedProfile = result.profile ?: _uiState.value.playerProfile.copy(nickname = username)
            preferences.saveProfile(loadedProfile)
            preferences.setLoggedIn(true)
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
        preferences.setLoggedIn(false)
        val guestNick = "Road Explorer " + Random.nextInt(100, 999)
        val guestProfile = PlayerProfile(nickname = guestNick)
        preferences.saveProfile(guestProfile)

        _uiState.update {
            it.copy(
                playerProfile = guestProfile,
                isLoggedIn = false,
                currentScreen = Screen.HOME
            )
        }
    }

    fun startNewGame(
        deck: Deck = _uiState.value.selectedDeck,
        gameMode: GameMode = _uiState.value.gameMode
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

    fun launchGameByHost() {
        val roomCode = _uiState.value.multiplayerRoom?.roomCode ?: return
        val deck = _uiState.value.selectedDeck
        val gameMode = _uiState.value.gameMode

        _uiState.update {
            it.copy(
                multiplayerRoom = it.multiplayerRoom?.copy(isStarted = true)
            )
        }

        viewModelScope.launch {
            AccountService.launchGameOnRailway(roomCode, deck.id, gameMode.name)
        }

        startNewGame(deck = deck, gameMode = gameMode)
    }

    fun startNextRound() {
        _uiState.update { it.copy(showBingoCelebration = false) }

        val roomCode = _uiState.value.multiplayerRoom?.roomCode
        if (!roomCode.isNullOrBlank()) {
            viewModelScope.launch {
                AccountService.resetRoomOnRailway(roomCode)
            }
            _uiState.update {
                it.copy(
                    multiplayerRoom = it.multiplayerRoom?.copy(isStarted = false, isFinished = false, winnerNickname = null),
                    currentScreen = Screen.MULTIPLAYER_LOBBY
                )
            }
        } else {
            startNewGame()
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

        val roomCode = _uiState.value.multiplayerRoom?.roomCode
        if (!roomCode.isNullOrBlank()) {
            val completedIndices = updatedBoard.filter { it.isCompleted }.map { it.index }
            viewModelScope.launch {
                AccountService.updateTilesOnRailway(
                    code = roomCode,
                    nickname = _uiState.value.playerProfile.nickname,
                    completedTiles = completedIndices,
                    hasBingo = newlyWon
                )
            }
        }

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
                showAccountPrompt = newlyWon && !it.isLoggedIn,
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
        _uiState.update {
            it.copy(
                showBingoCelebration = false,
                multiplayerRoom = if (it.currentScreen == Screen.HOME || it.winResult.hasWon) null else it.multiplayerRoom
            )
        }
    }

    fun unlockBonusGamesFromAd() {
        val currentProfile = _uiState.value.playerProfile
        val updatedProfile = currentProfile.copy(
            dailyGamesPlayedToday = 0
        )
        preferences.saveProfile(updatedProfile)
        _uiState.update { it.copy(playerProfile = updatedProfile) }
    }

    fun handleGoogleSignIn() {
        val updated = _uiState.value.playerProfile.copy(
            nickname = "RoadExplorer_User",
            earnedBadges = _uiState.value.playerProfile.earnedBadges + "cloud_backup"
        )
        preferences.saveProfile(updated)
        preferences.setLoggedIn(true)
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
        val hostNickname = _uiState.value.playerProfile.nickname
        val hostAvatarId = _uiState.value.playerProfile.avatarId
        val deckId = _uiState.value.selectedDeck.id

        val localRoom = MultiplayerService.createLocalQuickPlayRoom(
            hostNickname = hostNickname,
            hostAvatarId = hostAvatarId,
            deckId = deckId,
            region = _uiState.value.selectedRegion,
            gameMode = _uiState.value.gameMode
        )

        _uiState.update {
            it.copy(
                multiplayerRoom = localRoom,
                currentScreen = Screen.MULTIPLAYER_LOBBY
            )
        }

        // Start polling IMMEDIATELY
        startLiveRoomSync(localRoom.roomCode)

        viewModelScope.launch {
            val res = AccountService.createRoomOnRailway(localRoom.roomCode, hostNickname, deckId)
            if (res.success) {
                val railwayRoom = localRoom.copy(
                    roomCode = res.roomCode.ifBlank { localRoom.roomCode },
                    players = res.players.ifEmpty { localRoom.players }
                )
                _uiState.update { it.copy(multiplayerRoom = railwayRoom) }
            }
        }
    }

    fun createLocalQuickPlayRoomFromHome() {
        selectDeck(DeckRepository.DECKS[2]) // Default Mountain West
        createLocalQuickPlayRoom()
    }

    fun joinMultiplayerRoom(code: String) {
        val cleanCode = code.uppercase().trim()
        val playerNickname = _uiState.value.playerProfile.nickname
        val playerAvatarId = _uiState.value.playerProfile.avatarId

        val localRoom = MultiplayerService.joinRoomByCode(
            roomCode = cleanCode,
            playerNickname = playerNickname,
            playerAvatarId = playerAvatarId
        )

        _uiState.update {
            it.copy(
                multiplayerRoom = localRoom,
                currentScreen = Screen.MULTIPLAYER_LOBBY
            )
        }

        // Start polling IMMEDIATELY so passenger never misses host launch!
        startLiveRoomSync(cleanCode)

        viewModelScope.launch {
            val res = AccountService.joinRoomOnRailway(cleanCode, playerNickname)
            if (res.success) {
                val railwayRoom = localRoom.copy(
                    roomCode = res.roomCode.ifBlank { cleanCode },
                    players = res.players.ifEmpty { localRoom.players }
                )
                _uiState.update { it.copy(multiplayerRoom = railwayRoom) }
            }
        }
    }

    private fun startLiveRoomSync(roomCode: String) {
        val targetCode = roomCode.uppercase().trim()

        viewModelScope.launch {
            while (true) {
                val currentRoomCode = _uiState.value.multiplayerRoom?.roomCode?.uppercase()?.trim()
                if (currentRoomCode != targetCode && _uiState.value.multiplayerRoom != null) break

                try {
                    val res = AccountService.getRoomFromRailway(targetCode)
                    if (res.success && res.players.isNotEmpty()) {
                        val syncedDeck = DeckRepository.DECKS.find { it.id == res.deckId } ?: _uiState.value.selectedDeck
                        val syncedGameMode = GameMode.values().firstOrNull { it.name == res.gameMode || it.displayName == res.gameMode } ?: _uiState.value.gameMode

                        _uiState.update { state ->
                            val updatedRoom = state.multiplayerRoom?.copy(
                                deckId = res.deckId,
                                gameMode = syncedGameMode,
                                players = res.players,
                                isStarted = res.isStarted || state.multiplayerRoom?.isStarted == true,
                                isFinished = res.hasWinner,
                                winnerNickname = res.winnerNickname
                            ) ?: state.multiplayerRoom

                            val someoneWon = res.hasWinner && !res.winnerNickname.isNullOrBlank()
                            val inActiveGameScreen = state.currentScreen == Screen.BINGO_GAME || state.currentScreen == Screen.MULTIPLAYER_LOBBY

                            state.copy(
                                selectedDeck = syncedDeck,
                                gameMode = syncedGameMode,
                                multiplayerRoom = updatedRoom,
                                showBingoCelebration = (state.showBingoCelebration || someoneWon) && inActiveGameScreen
                            )
                        }

                        // AUTO-LAUNCH PASSENGERS / HOST INTO BINGO GAME SCREEN WHEN ROOM IS STARTED!
                        if (_uiState.value.currentScreen == Screen.MULTIPLAYER_LOBBY && res.isStarted) {
                            startNewGame(
                                deck = syncedDeck,
                                gameMode = syncedGameMode
                            )
                        }
                    }
                } catch (e: Exception) {}

                delay(600) // FAST 600MS POLL DELAY FOR INSTANT PRE-GAME LOBBY SETTINGS SYNC & GAME LAUNCH
            }
        }
    }

    fun startOnlineMatchmaking(deck: Deck = _uiState.value.selectedDeck) {
        val nickname = _uiState.value.playerProfile.nickname

        _uiState.update {
            it.copy(
                selectedDeck = deck,
                isSearchingOpponent = true,
                opponentFoundName = null
            )
        }

        viewModelScope.launch {
            val res = AccountService.joinOnlineMatchmakingOnRailway(nickname, deck.id)
            if (res.success && res.roomCode.isNotBlank()) {
                val roomObj = MultiplayerRoom(
                    roomCode = res.roomCode,
                    hostId = "host_online",
                    deckId = deck.id,
                    region = deck.region,
                    gameMode = _uiState.value.gameMode,
                    players = res.players,
                    isStarted = res.isStarted
                )
                _uiState.update { it.copy(multiplayerRoom = roomObj) }

                // Single unified sync loop handles matchmaking poll and game launch!
                startLiveRoomSync(res.roomCode)
            }
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
