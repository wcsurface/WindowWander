package app.vercel.windowwander.data

import android.content.Context
import android.content.SharedPreferences
import app.vercel.windowwander.model.PlayerProfile
import app.vercel.windowwander.model.Region
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("road_trip_bingo_prefs", Context.MODE_PRIVATE)

    fun getProfile(): PlayerProfile {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val savedDate = prefs.getString("last_play_date", "") ?: ""

        val todayGames = if (savedDate == todayStr) {
            prefs.getInt("daily_games_played", 0)
        } else {
            0
        }

        val badgeString = prefs.getString("earned_badges", "first_ride") ?: "first_ride"
        val badgeList = badgeString.split(",").filter { it.isNotBlank() }

        return PlayerProfile(
            id = prefs.getString("player_id", "guest_" + System.currentTimeMillis()) ?: "guest_player",
            nickname = prefs.getString("nickname", "Road Explorer") ?: "Road Explorer",
            avatarId = prefs.getString("avatar_id", "car_red") ?: "car_red",
            xp = prefs.getInt("xp", 0),
            gamesPlayed = prefs.getInt("games_played", 0),
            bingosWon = prefs.getInt("bingos_won", 0),
            squaresFound = prefs.getInt("squares_found", 0),
            streakDays = prefs.getInt("streak_days", 1),
            earnedBadges = badgeList,
            dailyGamesPlayedToday = todayGames,
            lastPlayDate = savedDate
        )
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
            .putString("last_play_date", todayStr)
            .apply()
    }

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
