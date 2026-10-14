package app.vercel.windowwander.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PlayerProfile(
    val id: String = "guest_player",
    val nickname: String = "Road Explorer",
    val avatarId: String = "car_red",
    val xp: Int = 0,
    val gamesPlayed: Int = 0,
    val bingosWon: Int = 0,
    val squaresFound: Int = 0,
    val streakDays: Int = 1,
    val earnedBadges: List<String> = listOf("first_ride"),
    val dailyGamesPlayedToday: Int = 0,
    val bonusGamesUnlockedToday: Int = 0,
    val lastPlayDate: String = ""
) {
    val level: Int get() = (xp / 100) + 1
    val xpInCurrentLevel: Int get() = xp % 100
    val maxDailyGamesAllowed: Int get() = 2 + bonusGamesUnlockedToday
    val remainingGamesToday: Int get() = (maxDailyGamesAllowed - dailyGamesPlayedToday).coerceAtLeast(0)
    val hasReachedDailyLimit: Boolean get() = dailyGamesPlayedToday >= maxDailyGamesAllowed

    companion object {
        fun getTodayDateString(): String {
            return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        }
    }
}

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false
)
