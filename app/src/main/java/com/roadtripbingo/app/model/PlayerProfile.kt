package com.roadtripbingo.app.model

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
    val isRoadWarriorUnlocked: Boolean = false,
    val isRoadWarriorPlusUnlocked: Boolean = false,
    val lastPlayDate: String = ""
) {
    val level: Int get() = (xp / 100) + 1
    val xpInCurrentLevel: Int get() = xp % 100
    val hasUnlimitedGames: Boolean get() = isRoadWarriorUnlocked || isRoadWarriorPlusUnlocked
    val maxDailyGamesAllowed: Int get() = if (hasUnlimitedGames) 999999 else 2
    val hasReachedDailyLimit: Boolean get() = !hasUnlimitedGames && dailyGamesPlayedToday >= 2

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
