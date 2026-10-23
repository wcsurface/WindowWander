package com.roadtripbingo.app.data

import com.roadtripbingo.app.model.PlayerProfile
import com.roadtripbingo.app.model.RoomPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object AccountService {

    var vercelBaseUrl: String = "https://windowwander.vercel.app"
    var railwayBaseUrl: String = "https://windowwander-production.up.railway.app"

    data class AuthResult(
        val success: Boolean,
        val profile: PlayerProfile? = null,
        val error: String? = null
    )

    data class RoomResult(
        val success: Boolean,
        val roomCode: String = "",
        val deckId: String = "deck_a",
        val gameMode: String = "CLASSIC_LINE",
        val isStarted: Boolean = false,
        val hasWinner: Boolean = false,
        val winnerNickname: String? = null,
        val players: List<RoomPlayer> = emptyList(),
        val error: String? = null
    )

    private fun parseProfileJson(userObj: JSONObject): PlayerProfile {
        val badgesArr = userObj.optJSONArray("earnedBadges")
        val badgesList = mutableListOf<String>()
        if (badgesArr != null) {
            for (i in 0 until badgesArr.length()) {
                badgesList.add(badgesArr.getString(i))
            }
        }
        if (badgesList.isEmpty()) badgesList.add("first_ride")

        return PlayerProfile(
            id = userObj.optString("id", "usr_1"),
            nickname = userObj.optString("nickname", userObj.optString("username", "Road Explorer")),
            avatarId = "car_red",
            xp = userObj.optInt("xp", 0),
            gamesPlayed = userObj.optInt("gamesPlayed", 0),
            bingosWon = userObj.optInt("bingosWon", 0),
            squaresFound = userObj.optInt("squaresFound", 0),
            streakDays = userObj.optInt("streakDays", 1),
            earnedBadges = badgesList
        )
    }

    private fun parseRoomJson(roomObj: JSONObject): RoomResult {
        val code = roomObj.optString("code", "")
        val deckId = roomObj.optString("deckId", "deck_a")
        val gameMode = roomObj.optString("gameMode", "CLASSIC_LINE")
        val isStarted = roomObj.optBoolean("isStarted", false)
        val hasWinner = roomObj.optBoolean("hasWinner", false)
        val winnerNickname = if (roomObj.has("winnerNickname") && !roomObj.isNull("winnerNickname")) roomObj.optString("winnerNickname") else null
        val playersArr = roomObj.optJSONArray("players")
        val playersList = mutableListOf<RoomPlayer>()

        if (playersArr != null) {
            for (i in 0 until playersArr.length()) {
                val pObj = playersArr.getJSONObject(i)
                val tilesArr = pObj.optJSONArray("completedTiles")
                val tilesList = mutableListOf<Int>()
                if (tilesArr != null) {
                    for (t in 0 until tilesArr.length()) {
                        tilesList.add(tilesArr.getInt(t))
                    }
                }
                if (tilesList.isEmpty()) tilesList.add(12)

                playersList.add(
                    RoomPlayer(
                        id = "p_$i",
                        nickname = pObj.optString("nickname", "Passenger"),
                        avatarId = "car_red",
                        isHost = pObj.optBoolean("isHost", false),
                        completedTiles = tilesList,
                        hasBingo = pObj.optBoolean("hasBingo", false)
                    )
                )
            }
        }

        return RoomResult(
            success = true,
            roomCode = code,
            deckId = deckId,
            gameMode = gameMode,
            isStarted = isStarted,
            hasWinner = hasWinner,
            winnerNickname = winnerNickname,
            players = playersList
        )
    }

    suspend fun login(username: String, pass: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/auth?action=login")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("username", username)
                put("password", pass)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                val userObj = json.getJSONObject("user")
                AuthResult(
                    success = true,
                    profile = parseProfileJson(userObj)
                )
            } else {
                AuthResult(
                    success = false,
                    error = json.optString("error", "Invalid credentials")
                )
            }
        } catch (e: Exception) {
            AuthResult(
                success = false,
                error = "Login error. Please check your password and internet connection."
            )
        }
    }

    suspend fun register(username: String, pass: String, nickname: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/auth?action=register")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("username", username)
                put("password", pass)
                put("nickname", nickname)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                val userObj = json.getJSONObject("user")
                AuthResult(
                    success = true,
                    profile = parseProfileJson(userObj)
                )
            } else {
                AuthResult(
                    success = false,
                    error = json.optString("error", "Registration failed")
                )
            }
        } catch (e: Exception) {
            AuthResult(
                success = false,
                error = "Registration error. Please check your connection."
            )
        }
    }

    suspend fun saveProfileToRailway(profile: PlayerProfile) = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/auth?action=save_profile")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("username", profile.nickname)
                put("level", profile.level)
                put("xp", profile.xp)
                put("earnedBadges", JSONArray(profile.earnedBadges))
                put("streakDays", profile.streakDays)
                put("bingosWon", profile.bingosWon)
                put("gamesPlayed", profile.gamesPlayed)
                put("squaresFound", profile.squaresFound)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            conn.responseCode
        } catch (e: Exception) {
            // Ignore offline save errors
        }
    }

    // LIVE VERCEL / RAILWAY MULTIPLAYER ROOM API
    suspend fun createRoomOnRailway(code: String, hostNickname: String, deckId: String): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/rooms")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("action", "create")
                put("code", code)
                put("hostNickname", hostNickname)
                put("deckId", deckId)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                parseRoomJson(json.getJSONObject("room"))
            } else {
                RoomResult(success = false, error = "Create room failed")
            }
        } catch (e: Exception) {
            RoomResult(success = false, error = e.message)
        }
    }

    suspend fun joinRoomOnRailway(code: String, nickname: String): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/rooms")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("action", "join")
                put("code", code)
                put("nickname", nickname)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                parseRoomJson(json.getJSONObject("room"))
            } else {
                RoomResult(success = false, error = "Join room failed")
            }
        } catch (e: Exception) {
            RoomResult(success = false, error = e.message)
        }
    }

    suspend fun launchGameOnRailway(code: String, deckId: String, gameMode: String): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/rooms")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("action", "launch")
                put("code", code)
                put("deckId", deckId)
                put("gameMode", gameMode)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                parseRoomJson(json.getJSONObject("room"))
            } else {
                RoomResult(success = false, error = "Launch game failed")
            }
        } catch (e: Exception) {
            RoomResult(success = false, error = e.message)
        }
    }

    suspend fun joinOnlineMatchmakingOnRailway(nickname: String, deckId: String): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/rooms")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("action", "matchmaking")
                put("nickname", nickname)
                put("deckId", deckId)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                parseRoomJson(json.getJSONObject("room"))
            } else {
                RoomResult(success = false, error = "Matchmaking failed")
            }
        } catch (e: Exception) {
            RoomResult(success = false, error = e.message)
        }
    }

    suspend fun updateRoomSettingsOnRailway(code: String, deckId: String, gameMode: String): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/rooms")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("action", "update_settings")
                put("code", code)
                put("deckId", deckId)
                put("gameMode", gameMode)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                parseRoomJson(json.getJSONObject("room"))
            } else {
                RoomResult(success = false, error = "Update settings failed")
            }
        } catch (e: Exception) {
            RoomResult(success = false, error = e.message)
        }
    }

    suspend fun resetRoomOnRailway(code: String): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/rooms")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("action", "reset_room")
                put("code", code)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                parseRoomJson(json.getJSONObject("room"))
            } else {
                RoomResult(success = false, error = "Reset room failed")
            }
        } catch (e: Exception) {
            RoomResult(success = false, error = e.message)
        }
    }

    suspend fun updateTilesOnRailway(code: String, nickname: String, completedTiles: List<Int>, hasBingo: Boolean = false): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/rooms")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("action", "update_tiles")
                put("code", code)
                put("nickname", nickname)
                put("completedTiles", JSONArray(completedTiles))
                put("hasBingo", hasBingo)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                parseRoomJson(json.getJSONObject("room"))
            } else {
                RoomResult(success = false, error = "Update tiles failed")
            }
        } catch (e: Exception) {
            RoomResult(success = false, error = e.message)
        }
    }

    suspend fun getRoomFromRailway(code: String): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$vercelBaseUrl/api/rooms")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.doOutput = true

            val body = JSONObject().apply {
                put("action", "get")
                put("code", code)
            }

            conn.outputStream.use { os ->
                os.write(body.toString().toByteArray())
            }

            val statusCode = conn.responseCode
            val stream = if (statusCode in 200..299) conn.inputStream else conn.errorStream
            val responseStr = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseStr)

            if (json.optBoolean("success", false)) {
                parseRoomJson(json.getJSONObject("room"))
            } else {
                RoomResult(success = false, error = "Room not found")
            }
        } catch (e: Exception) {
            RoomResult(success = false, error = e.message)
        }
    }
}
