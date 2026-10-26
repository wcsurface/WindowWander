package com.roadtripbingo.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roadtripbingo.app.R
import com.roadtripbingo.app.model.PlayerProfile
import com.roadtripbingo.app.ui.components.GradientPillNavigationBar
import com.roadtripbingo.app.ui.theme.CompletedGreen
import com.roadtripbingo.app.ui.theme.GoldWinner
import com.roadtripbingo.app.ui.theme.MediumNavy
import com.roadtripbingo.app.ui.theme.MidnightNavy
import com.roadtripbingo.app.ui.theme.SunnyYellow

data class LeaderboardEntry(
    val rank: Int,
    val nickname: String,
    val avatarEmoji: String,
    val level: Int,
    val value: Int,
    val isCurrentUser: Boolean = false
)

@Composable
fun LeaderboardScreen(
    profile: PlayerProfile,
    isLoggedIn: Boolean = false,
    onNavigateHome: () -> Unit = {},
    onNavigateLogin: () -> Unit = {},
    onNavigateShop: () -> Unit = {},
    onNavigateSettings: () -> Unit = {},
    onBackToHome: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf(0) } // 0 = Total Points (XP), 1 = Items Found

    var globalXpLeaderboard by remember { mutableStateOf<List<LeaderboardEntry>>(emptyList()) }
    var globalItemsLeaderboard by remember { mutableStateOf<List<LeaderboardEntry>>(emptyList()) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        val (xpItems, squaresItems) = com.roadtripbingo.app.data.AccountService.getGlobalLeaderboardOnRailway()
        
        val currentCleanNick = profile.nickname.lowercase().trim()

        val defaultFeaturedXp = listOf(
            LeaderboardEntry(1, if (currentCleanNick == "will") "Will (You)" else "Will", "🚗", 8, 750, isCurrentUser = (currentCleanNick == "will")),
            LeaderboardEntry(2, if (currentCleanNick == "testaccount") "TestAccount (You)" else "TestAccount", "🚗", 7, 600, isCurrentUser = (currentCleanNick == "testaccount"))
        )

        val defaultFeaturedItems = listOf(
            LeaderboardEntry(1, if (currentCleanNick == "will") "Will (You)" else "Will", "🚗", 8, 42, isCurrentUser = (currentCleanNick == "will")),
            LeaderboardEntry(2, if (currentCleanNick == "testaccount") "TestAccount (You)" else "TestAccount", "🚗", 7, 32, isCurrentUser = (currentCleanNick == "testaccount"))
        )

        if (xpItems.isNotEmpty()) {
            val formattedXp = xpItems.mapIndexed { idx, item ->
                val isMe = item.nickname.lowercase().trim() == currentCleanNick || item.username.lowercase().trim() == currentCleanNick
                LeaderboardEntry(
                    rank = idx + 1,
                    nickname = if (isMe) "${item.nickname} (You)" else item.nickname,
                    avatarEmoji = "🚗",
                    level = item.level,
                    value = item.xp,
                    isCurrentUser = isMe
                )
            }
            globalXpLeaderboard = formattedXp
        } else {
            globalXpLeaderboard = defaultFeaturedXp
        }

        if (squaresItems.isNotEmpty()) {
            val formattedItems = squaresItems.mapIndexed { idx, item ->
                val isMe = item.nickname.lowercase().trim() == currentCleanNick || item.username.lowercase().trim() == currentCleanNick
                LeaderboardEntry(
                    rank = idx + 1,
                    nickname = if (isMe) "${item.nickname} (You)" else item.nickname,
                    avatarEmoji = "🚗",
                    level = item.level,
                    value = item.squaresFound,
                    isCurrentUser = isMe
                )
            }
            globalItemsLeaderboard = formattedItems
        } else {
            globalItemsLeaderboard = defaultFeaturedItems
        }
    }

    val currentList = if (selectedCategory == 0) globalXpLeaderboard else globalItemsLeaderboard

    Box(modifier = Modifier.fillMaxSize()) {
        // LEADERBOARD DEDICATED DOODLE BACKGROUND ARTWORK
        Image(
            painter = painterResource(id = R.drawable.bg_leaderboard),
            contentDescription = "Leaderboard Background Artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // DARK SCRIM OVERLAY FOR HIGH CONTRAST & LEGIBILITY
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 26.dp, bottom = 16.dp)
        ) {
            // TOP HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏆 Leaderboards",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MediumNavy)
                        .clickable { onBackToHome() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Back",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CATEGORY SWITCHER TABS (TOTAL POINTS VS ITEMS FOUND)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MediumNavy)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedCategory == 0) SunnyYellow else Color.Transparent)
                        .clickable { selectedCategory = 0 }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⭐ Total Points (XP)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (selectedCategory == 0) MidnightNavy else Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedCategory == 1) SunnyYellow else Color.Transparent)
                        .clickable { selectedCategory = 1 }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👀 Items Found",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (selectedCategory == 1) MidnightNavy else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LEADERBOARD LIST
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 104.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(currentList) { idx, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(if (item.isCurrentUser) 10.dp else 4.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isCurrentUser) Color(0xFF1E293B) else MediumNavy
                        ),
                        border = if (item.isCurrentUser) BorderStroke(2.dp, SunnyYellow) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // RANK BADGE
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (item.rank) {
                                                1 -> GoldWinner
                                                2 -> Color(0xFF94A3B8) // Silver
                                                3 -> Color(0xFFB45309) // Bronze
                                                else -> Color(0xFF0F172A)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${item.rank}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(text = item.avatarEmoji, fontSize = 22.sp)

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = item.nickname,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Lvl ${item.level} Explorer",
                                        fontSize = 11.sp,
                                        color = SunnyYellow
                                    )
                                }
                            }

                            // SCORE / STAT VALUE
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF0F172A))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${item.value} ${if (selectedCategory == 0) "XP" else "Sights"}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CompletedGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        // FLOATING GRADIENT PILL 3D NAVIGATION BAR
        GradientPillNavigationBar(
            currentScreenName = "leaderboard",
            isLoggedIn = isLoggedIn,
            onNavigateHome = onNavigateHome,
            onNavigateLogin = onNavigateLogin,
            onNavigateShop = onNavigateShop,
            onNavigateSettings = onNavigateSettings,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
