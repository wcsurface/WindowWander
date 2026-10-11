package app.vercel.windowwander.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.model.PlayerProfile
import app.vercel.windowwander.model.Region
import app.vercel.windowwander.ui.components.ArcadeHudBar
import app.vercel.windowwander.ui.components.BeveledGameButton
import app.vercel.windowwander.ui.components.CartoonRoadSignCard
import app.vercel.windowwander.ui.components.HeroRoadTripGraphic
import app.vercel.windowwander.ui.components.ScenicHighwayBackground
import app.vercel.windowwander.ui.theme.CompletedGreen
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
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
    var showPlayOptionModal by remember { mutableStateOf(false) }

    ScenicHighwayBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TOP ARCADE HUD BAR (AVATAR, LEVEL, COINS, SETTINGS)
                ArcadeHudBar(
                    nickname = profile.nickname,
                    level = profile.level,
                    xp = profile.xp,
                    coins = 1250,
                    onOpenStats = onOpenStats,
                    onOpenSettings = onOpenSettings
                )

                Spacer(modifier = Modifier.height(12.dp))

                // SCENIC ROAD TRIP HERO SCENE
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
                                text = "CARTOON ROAD TRIP BINGO",
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

                // HUGE 3D BEVELED GOLDEN PLAY BUTTON -> OPENS IN-CAR PLAY MODAL
                BeveledGameButton(
                    text = "⚡ PLAY NOW ▶",
                    onClick = { showPlayOptionModal = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    buttonColor = SunnyYellow,
                    bevelColor = Color(0xFFD97706),
                    textColor = DeepNavy,
                    fontSize = 22
                )

                Spacer(modifier = Modifier.height(14.dp))

                // GAME MODES CARTOON ROAD SIGN CARDS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // MULTIPLAYER ROAD SIGN
                    CartoonRoadSignCard(
                        title = "Multiplayer",
                        subtitle = "Play in car room",
                        icon = "🌐",
                        badgeText = "LIVE",
                        onClick = { showPlayOptionModal = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp),
                        signColor = Color(0xFF2563EB)
                    )

                    // DECK LIBRARY ROAD SIGN
                    CartoonRoadSignCard(
                        title = "Deck Library",
                        subtitle = "8 Themed Decks",
                        icon = "🎴",
                        badgeText = "8 DECKS",
                        onClick = onChooseDeck,
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp),
                        signColor = Color(0xFF0D9488)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // TRAVEL LOCATION REGION SELECTOR
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(18.dp))
                        .clickable { onOpenSettings() },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MediumNavy)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MediumNavy)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(CompletedGreen.copy(alpha = 0.3f), MediumNavy)
                                )
                            )
                            .border(1.5.dp, CompletedGreen, RoundedCornerShape(18.dp))
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

                // SAFETY BANNER
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = VividOrange.copy(alpha = 0.2f)),
                    border = BorderStroke(1.dp, VividOrange)
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
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Privacy Policy & Terms of Service",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.clickable { onOpenPrivacy() }
                )
            }

            // IN-CAR PLAY MODAL (START OR JOIN A GAME)
            AnimatedVisibility(
                visible = showPlayOptionModal,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.88f))
                        .clickable { showPlayOptionModal = false },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(20.dp)
                            .shadow(24.dp, RoundedCornerShape(24.dp))
                            .clickable(enabled = false) {},
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MediumNavy)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🚗 IN-CAR PLAY",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = SunnyYellow
                            )
                            Text(
                                text = "Play bingo with passengers in your car!",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                            )

                            // START A GAME BUTTON
                            BeveledGameButton(
                                text = "🚗 START A GAME (HOST)",
                                onClick = {
                                    showPlayOptionModal = false
                                    onPlayOnline()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                buttonColor = SunnyYellow,
                                bevelColor = Color(0xFFD97706),
                                textColor = DeepNavy,
                                fontSize = 16
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // JOIN A GAME BUTTON
                            BeveledGameButton(
                                text = "📱 JOIN A GAME (CODE)",
                                onClick = {
                                    showPlayOptionModal = false
                                    onPlayOnline()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                buttonColor = Color(0xFF2563EB),
                                bevelColor = Color(0xFF1D4ED8),
                                textColor = Color.White,
                                fontSize = 16
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // SOLO PLAY BUTTON
                            Button(
                                onClick = {
                                    showPlayOptionModal = false
                                    onQuickPlay()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text(text = "Play Solo Single-Player ▶", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
