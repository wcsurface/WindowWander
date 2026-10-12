package app.vercel.windowwander.data

import app.vercel.windowwander.model.MultiplayerRoom
import app.vercel.windowwander.model.PlayerProfile
import app.vercel.windowwander.model.RoomPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object AccountService {

    var railwayBaseUrl: String = "https://windowwander-production.up.railway.app"

    data class AuthResult(
        val success: Boolean,
        val profile: PlayerProfile? = null,
        val error: String? = null
    )

    data class RoomResult(
        val success: Boolean,
        val roomCode: String = "",
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
        val playersArr = roomObj.optJSONArray("players")
        val playersList = mutableListOf<RoomPlayer>()

        if (playersArr != null) {
            for (i in 0 until playersArr.length()) {
                val pObj = playersArr.getJSONObject(i)
                playersList.add(
                    RoomPlayer(
                        id = "p_$i",
                        nickname = pObj.optString("nickname", "Passenger"),
                        avatarId = "car_red",
                        isHost = pObj.optBoolean("isHost", false)
                    )
                )
            }
        }

        return RoomResult(
            success = true,
            roomCode = code,
            players = playersList
        )
    }

    suspend fun login(username: String, pass: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$railwayBaseUrl/api/auth/login")
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
                success = true,
                profile = PlayerProfile(
                    nickname = username
                )
            )
        }
    }

    suspend fun register(username: String, pass: String, nickname: String): AuthResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$railwayBaseUrl/api/auth/register")
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
                success = true,
                profile = PlayerProfile(
                    nickname = nickname.ifBlank { username }
                )
            )
        }
    }

    suspend fun saveProfileToRailway(profile: PlayerProfile) = withContext(Dispatchers.IO) {
        try {
            val url = URL("$railwayBaseUrl/api/auth/save-profile")
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

    // LIVE RAILWAY MULTIPLAYER ROOM API
    suspend fun createRoomOnRailway(hostNickname: String, deckId: String): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$railwayBaseUrl/api/rooms/create")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            conn.doOutput = true

            val body = JSONObject().apply {
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
            val url = URL("$railwayBaseUrl/api/rooms/join")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 4000
            conn.readTimeout = 4000
            conn.doOutput = true

            val body = JSONObject().apply {
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

    suspend fun getRoomFromRailway(code: String): RoomResult = withContext(Dispatchers.IO) {
        try {
            val url = URL("$railwayBaseUrl/api/rooms/get")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.doOutput = true

            val body = JSONObject().apply {
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
