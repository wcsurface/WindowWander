package app.vercel.windowwander.model

data class RoomPlayer(
    val id: String,
    val nickname: String,
    val avatarId: String,
    val isHost: Boolean = false,
    val score: Int = 0,
    val hasBingo: Boolean = false,
    val completedTiles: List<Int> = listOf(12)
)

data class DiscoveryEvent(
    val promptId: String,
    val promptName: String,
    val claimedByNickname: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class MultiplayerRoom(
    val roomCode: String,
    val hostId: String,
    val deckId: String,
    val region: Region = Region.UNIVERSAL,
    val gameMode: GameMode = GameMode.CLASSIC_LINE,
    val players: List<RoomPlayer> = emptyList(),
    val discoveryFeed: List<DiscoveryEvent> = emptyList(),
    val isStarted: Boolean = false,
    val isFinished: Boolean = false,
    val winnerNickname: String? = null
)
