package app.vercel.windowwander.data

import app.vercel.windowwander.model.DiscoveryEvent
import app.vercel.windowwander.model.GameMode
import app.vercel.windowwander.model.MultiplayerRoom
import app.vercel.windowwander.model.Region
import app.vercel.windowwander.model.RoomPlayer
import kotlin.random.Random

object MultiplayerService {

    private val ROOM_CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"

    // Railway server URL (configured via environment or local default)
    var railwayServerUrl: String = "wss://windowwander-production.up.railway.app/ws"

    fun generateRoomCode(): String {
        return (1..4)
            .map { ROOM_CHARACTERS[Random.nextInt(ROOM_CHARACTERS.length)] }
            .joinToString("")
    }

    fun createLocalQuickPlayRoom(
        hostNickname: String,
        hostAvatarId: String,
        deckId: String,
        region: Region,
        gameMode: GameMode
    ): MultiplayerRoom {
        val code = generateRoomCode()
        val hostPlayer = RoomPlayer(
            id = "host_" + System.currentTimeMillis(),
            nickname = hostNickname,
            avatarId = hostAvatarId,
            isHost = true,
            score = 0
        )

        val botPassenger = RoomPlayer(
            id = "passenger_2",
            nickname = "Backseat Rider",
            avatarId = "car_blue",
            isHost = false,
            score = 0
        )

        return MultiplayerRoom(
            roomCode = code,
            hostId = hostPlayer.id,
            deckId = deckId,
            region = region,
            gameMode = gameMode,
            players = listOf(hostPlayer, botPassenger),
            discoveryFeed = listOf(
                DiscoveryEvent("intro", "Room Created! Code: $code", hostNickname)
            ),
            isStarted = true
        )
    }

    fun joinRoomByCode(
        roomCode: String,
        playerNickname: String,
        playerAvatarId: String
    ): MultiplayerRoom? {
        val upperCode = roomCode.uppercase().trim()
        val newPlayer = RoomPlayer(
            id = "player_" + System.currentTimeMillis(),
            nickname = playerNickname,
            avatarId = playerAvatarId,
            isHost = false
        )

        val mockHost = RoomPlayer(
            id = "host_1",
            nickname = "Road Captain",
            avatarId = "car_yellow",
            isHost = true
        )

        return MultiplayerRoom(
            roomCode = upperCode,
            hostId = mockHost.id,
            deckId = "deck_a",
            region = Region.UNIVERSAL,
            gameMode = GameMode.CLASSIC_LINE,
            players = listOf(mockHost, newPlayer),
            discoveryFeed = listOf(
                DiscoveryEvent("join", "$playerNickname joined room $upperCode!", playerNickname)
            ),
            isStarted = true
        )
    }
}
