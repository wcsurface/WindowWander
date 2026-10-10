package app.vercel.windowwander.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.model.PlayerProfile
import app.vercel.windowwander.model.Region
import app.vercel.windowwander.ui.components.BeveledGameButton
import app.vercel.windowwander.ui.components.HeroRoadTripGraphic
import app.vercel.windowwander.ui.theme.CompletedGreen
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.ElectricBlue
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.MidnightNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.SunnyYellowLight
import app.vercel.windowwander.ui.theme.TravelTeal
import app.vercel.windowwander.ui.theme.VividOrange

@Composable
fun HomeScreen(
    profile: PlayerProfile,
    selectedRegion: Region,
    onQuickPlay: () -> Unit,
    onPlayOnline: () -> Unit,
    onChooseDeck: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPrivacy: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOP APP HUD BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // PROFILE BADGE WITH LEVEL & XP
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(MediumNavy)
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                    .clickable { onOpenStats() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(SunnyYellow, VividOrange))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⭐",
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = profile.nickname,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Level ${profile.level} • ${profile.xp} XP",
                        fontSize = 10.sp,
                        color = SunnyYellowLight,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // SETTINGS BUTTON
            IconButton(
                onClick = onOpenSettings,
                modifier = Modifier
                    .size(40.dp)
                    .shadow(6.dp, CircleShape)
                    .clip(CircleShape)
                    .background(MediumNavy)
                    .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Text(text = "⚙️", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SCENIC GAME WORLD HERO SECTION
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeroRoadTripGraphic()

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(VividOrange)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "ROAD TRIP EDITION",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "ROAD TRIP BINGO",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.shadow(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // HUGE 3D BEVELED GOLDEN PLAY BUTTON
        BeveledGameButton(
            text = "⚡ PLAY NOW ▶",
            onClick = onQuickPlay,
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            buttonColor = SunnyYellow,
            bevelColor = Color(0xFFD97706),
            textColor = DeepNavy,
            fontSize = 20
        )

        Spacer(modifier = Modifier.height(14.dp))

        // MULTIPLAYER & DECK LIBRARY CARDS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // MULTIPLAYER CARD
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .clickable { onPlayOnline() },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MediumNavy)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(ElectricBlue.copy(alpha = 0.35f), MediumNavy)
                            )
                        )
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "🌐", fontSize = 24.sp)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricBlue)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "LIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Column {
                        Text(
                            text = "Multiplayer",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Play in car room",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // DECK LIBRARY CARD
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .clickable { onChooseDeck() },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MediumNavy)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(TravelTeal.copy(alpha = 0.35f), MediumNavy)
                            )
                        )
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "🎴", fontSize = 24.sp)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TravelTeal)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = "8 DECKS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Column {
                        Text(
                            text = "Deck Library",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Explore themes",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TRAVEL REGION SELECTOR CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenSettings() },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MediumNavy)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📍", fontSize = 22.sp, modifier = Modifier.padding(end = 10.dp))
                    Column {
                        Text(
                            text = "CURRENT LOCATION REGION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SunnyYellow
                        )
                        Text(
                            text = selectedRegion.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                Text(
                    text = "Change >",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TravelTeal
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // DAILY CHALLENGE PASS CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MediumNavy)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(CompletedGreen.copy(alpha = 0.25f), MediumNavy)
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎯", fontSize = 20.sp, modifier = Modifier.padding(end = 6.dp))
                            Text(
                                text = "DAILY ROAD TRIP PASS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = CompletedGreen
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CompletedGreen)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "+150 XP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Spot a yellow vehicle, semi-truck, and an out-of-state plate today!",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CRITICAL SAFETY BANNER
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = VividOrange.copy(alpha = 0.15f)),
            border = BorderStroke(1.dp, VividOrange.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🚗", fontSize = 24.sp, modifier = Modifier.padding(end = 10.dp))
                Column {
                    Text(
                        text = "Passengers play. Drivers drive.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = VividOrange
                    )
                    Text(
                        text = "Never operate or look at the screen while driving.",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PRIVACY & TERMS LINK
        Text(
            text = "Privacy Policy & Terms of Service",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.4f),
            modifier = Modifier.clickable { onOpenPrivacy() }
        )
    }
}
