package app.vercel.windowwander.data

import android.content.Context
import android.content.SharedPreferences
import app.vercel.windowwander.model.PlayerProfile
import app.vercel.windowwander.model.Region
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("road_trip_bingo_prefs", Context.MODE_PRIVATE)

    fun getProfile(): PlayerProfile {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val savedDate = prefs.getString("last_play_date", "") ?: ""

        val (todayGames, bonusGames) = if (savedDate == todayStr) {
            Pair(
                prefs.getInt("daily_games_played", 0),
                prefs.getInt("bonus_games_unlocked", 0)
            )
        } else {
            // Daily reset at midnight!
            Pair(0, 0)
        }

        val badgeString = prefs.getString("earned_badges", "first_ride") ?: "first_ride"
        val badgeList = badgeString.split(",").filter { it.isNotBlank() }

        val defaultGuestNick = "Road Explorer " + Random.nextInt(100, 999)
        val savedNickname = prefs.getString("nickname", null) ?: defaultGuestNick
        if (prefs.getString("nickname", null) == null) {
            prefs.edit().putString("nickname", defaultGuestNick).apply()
        }

        return PlayerProfile(
            id = prefs.getString("player_id", "guest_" + System.currentTimeMillis()) ?: "guest_player",
            nickname = savedNickname,
            avatarId = prefs.getString("avatar_id", "car_red") ?: "car_red",
            xp = prefs.getInt("xp", 0),
            gamesPlayed = prefs.getInt("games_played", 0),
            bingosWon = prefs.getInt("bingos_won", 0),
            squaresFound = prefs.getInt("squares_found", 0),
            streakDays = prefs.getInt("streak_days", 1),
            earnedBadges = badgeList,
            dailyGamesPlayedToday = todayGames,
            bonusGamesUnlockedToday = bonusGames,
            lastPlayDate = todayStr
        )
    }

    fun getProfileForUser(username: String): PlayerProfile {
        val savedXp = prefs.getInt("xp_$username", -1)
        if (savedXp != -1) {
            return PlayerProfile(
                nickname = username,
                xp = savedXp,
                bingosWon = prefs.getInt("bingos_$username", 0),
                squaresFound = prefs.getInt("squares_$username", 0),
                gamesPlayed = prefs.getInt("games_$username", 0)
            )
        }
        return getProfile()
    }

    fun saveProfile(profile: PlayerProfile) {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        prefs.edit()
            .putString("player_id", profile.id)
            .putString("nickname", profile.nickname)
            .putString("avatar_id", profile.avatarId)
            .putInt("xp", profile.xp)
            .putInt("games_played", profile.gamesPlayed)
            .putInt("bingos_won", profile.bingosWon)
            .putInt("squares_found", profile.squaresFound)
            .putInt("streak_days", profile.streakDays)
            .putString("earned_badges", profile.earnedBadges.joinToString(","))
            .putInt("daily_games_played", profile.dailyGamesPlayedToday)
            .putInt("bonus_games_unlocked", profile.bonusGamesUnlockedToday)
            .putString("last_play_date", todayStr)
            // BACK UP USER DATA SPECIFICALLY BY NICKNAME
            .putInt("xp_${profile.nickname}", profile.xp)
            .putInt("bingos_${profile.nickname}", profile.bingosWon)
            .putInt("squares_${profile.nickname}", profile.squaresFound)
            .putInt("games_${profile.nickname}", profile.gamesPlayed)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)
    fun setLoggedIn(loggedIn: Boolean) = prefs.edit().putBoolean("is_logged_in", loggedIn).apply()

    fun getSelectedRegion(): Region {
        val code = prefs.getString("selected_region_code", Region.UNIVERSAL.code) ?: Region.UNIVERSAL.code
        return Region.values().firstOrNull { it.code == code } ?: Region.UNIVERSAL
    }

    fun setSelectedRegion(region: Region) {
        prefs.edit().putString("selected_region_code", region.code).apply()
    }

    fun isSoundEnabled(): Boolean = prefs.getBoolean("sound_enabled", true)
    fun setSoundEnabled(enabled: Boolean) = prefs.edit().putBoolean("sound_enabled", enabled).apply()

    fun isHapticsEnabled(): Boolean = prefs.getBoolean("haptics_enabled", true)
    fun setHapticsEnabled(enabled: Boolean) = prefs.edit().putBoolean("haptics_enabled", enabled).apply()

    fun isConfirmTapEnabled(): Boolean = prefs.getBoolean("confirm_tap", false)
    fun setConfirmTapEnabled(enabled: Boolean) = prefs.edit().putBoolean("confirm_tap", enabled).apply()

    fun isKidsModeEasy(): Boolean = prefs.getBoolean("kids_mode", false)
    fun setKidsModeEasy(enabled: Boolean) = prefs.edit().putBoolean("kids_mode", enabled).apply()

    fun isDarkMode(): Boolean = prefs.getBoolean("dark_mode", false)
    fun setDarkMode(enabled: Boolean) = prefs.edit().putBoolean("dark_mode", enabled).apply()
}
