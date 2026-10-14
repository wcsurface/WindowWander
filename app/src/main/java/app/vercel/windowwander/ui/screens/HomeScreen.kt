package app.vercel.windowwander.ui.screens

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.R
import app.vercel.windowwander.data.DeckRepository
import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.model.PlayerProfile
import app.vercel.windowwander.model.Region
import app.vercel.windowwander.ui.components.ArcadeHudBar
import app.vercel.windowwander.ui.components.BeveledGameButton
import app.vercel.windowwander.ui.components.CartoonRoadSignCard
import app.vercel.windowwander.ui.components.GradientPillNavigationBar
import app.vercel.windowwander.ui.components.MainHomeBackground
import app.vercel.windowwander.ui.theme.CompletedGreen
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.VividOrange
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    profile: PlayerProfile,
    selectedRegion: Region,
    selectedDeck: Deck,
    isSearchingOpponent: Boolean,
    opponentFoundName: String?,
    isLoggedIn: Boolean = false,
    onQuickPlay: () -> Unit,
    onPlayInCarHost: () -> Unit,
    onPlayInCarJoin: (String) -> Unit,
    onStartOnlineMatchmaking: (Deck) -> Unit,
    onCancelMatchmaking: () -> Unit,
    onChooseDeck: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onOpenLogin: () -> Unit,
    onOpenShop: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPrivacy: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showInCarPlayModal by remember { mutableStateOf(false) }
    var showOnlineMultiplayerModal by remember { mutableStateOf(false) }
    var showJoinCodeInput by remember { mutableStateOf(false) }
    var joinCodeInputText by remember { mutableStateOf("") }
    var currentDeckChoice by remember { mutableStateOf(selectedDeck) }
    var deckDropdownExpanded by remember { mutableStateOf(false) }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    MainHomeBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 104.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TOP ARCADE HUD BAR (NAME, LEVEL & XP PILLS)
                ArcadeHudBar(
                    nickname = profile.nickname,
                    level = profile.level,
                    xp = profile.xp,
                    onOpenStats = onOpenStats
                )

                Spacer(modifier = Modifier.height(16.dp))

                // CENTERED TRANSPARENT ANIMATED GIF LOGO
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(R.drawable.road_trip_bingo_animated_logo)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = "Road Trip Bingo Animated Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // LARGE CAPSULE PLAY NOW BUTTON (HEIGHT 68DP)
                BeveledGameButton(
                    text = "⚡ PLAY NOW ▶",
                    onClick = { showInCarPlayModal = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    textColor = Color(0xFF0F172A),
                    fontSize = 24
                )

                Spacer(modifier = Modifier.height(18.dp))

                // TWO CARTOON ROAD SIGN CARDS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // PROFILE ROAD SIGN (TEAL GREEN COLOR WITH 18SP TITLE & 12.5SP SUBTITLE)
                    CartoonRoadSignCard(
                        title = "Profile",
                        subtitle = "Stats & Badges",
                        icon = "👤",
                        badgeText = null,
                        titleFontSize = 18f,
                        subtitleFontSize = 12.5f,
                        onClick = onOpenStats,
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp),
                        signColor = Color(0xFF0D9488)
                    )

                    // DECKS ROAD SIGN (18SP TITLE & 12.5SP SUBTITLE MATCHING LEADERBOARD)
                    CartoonRoadSignCard(
                        title = "Decks",
                        subtitle = "8 Decks!",
                        icon = "🎴",
                        badgeText = null,
                        titleFontSize = 18f,
                        subtitleFontSize = 12.5f,
                        onClick = onChooseDeck,
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp),
                        signColor = Color(0xFF047857)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // LEADERBOARD BUTTON (EXACT SAME 68DP HEIGHT AS PLAY NOW BUTTON, TOP-LEFT CUP & TEXT)
                CartoonRoadSignCard(
                    title = "🏆 LEADERBOARD",
                    subtitle = "Points & Items Found Rankings",
                    icon = "",
                    badgeText = "RANKS",
                    titleFontSize = 18f,
                    subtitleFontSize = 12f,
                    onClick = onOpenLeaderboard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    signColor = Color(0xFF1D4ED8)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // PROMINENT LARGE ORANGE SAFETY BANNER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(14.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(VividOrange)
                        .border(2.5.dp, Color(0xFFC2410C), RoundedCornerShape(24.dp))
                        .padding(vertical = 18.dp, horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Passengers Play. Drivers Drive.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // FLOATING GRADIENT PILL 3D NAVIGATION BAR (SET 6)
            GradientPillNavigationBar(
                currentScreenName = "home",
                isLoggedIn = isLoggedIn,
                onNavigateHome = { },
                onNavigateLogin = onOpenLogin,
                onNavigateShop = onOpenShop,
                onNavigateSettings = onOpenSettings,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            // SAME-CAR PLAY MODAL (HOST GAME OR JOIN GAME WITH CODE/QR)
            AnimatedVisibility(
                visible = showInCarPlayModal,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.88f))
                        .clickable {
                            showInCarPlayModal = false
                            showJoinCodeInput = false
                        },
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
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
                            )

                            if (!showJoinCodeInput) {
                                // 1. HOST A GAME
                                BeveledGameButton(
                                    text = "🚗 START A GAME (HOST)",
                                    onClick = {
                                        showInCarPlayModal = false
                                        onPlayInCarHost()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    textColor = DeepNavy,
                                    fontSize = 16
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // 2. JOIN A GAME
                                BeveledGameButton(
                                    text = "📱 JOIN A GAME (CODE)",
                                    onClick = { showJoinCodeInput = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    textColor = DeepNavy,
                                    fontSize = 16
                                )
                            } else {
                                // CODE INPUT FOR JOINING HOST'S GAME LOBBY
                                Text(
                                    text = "Enter 4-Letter Code from Host:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                OutlinedTextField(
                                    value = joinCodeInputText,
                                    onValueChange = { if (it.length <= 4) joinCodeInputText = it.uppercase() },
                                    placeholder = { Text("e.g. ROAD", color = Color.Gray) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = SunnyYellow,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                                    )
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                BeveledGameButton(
                                    text = "JOIN CAR ROOM ▶",
                                    onClick = {
                                        if (joinCodeInputText.length >= 4) {
                                            showInCarPlayModal = false
                                            showJoinCodeInput = false
                                            onPlayInCarJoin(joinCodeInputText)
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    textColor = DeepNavy,
                                    fontSize = 16
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = { showJoinCodeInput = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                ) {
                                    Text(text = "◀ Back", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // ONLINE MULTIPLAYER POPUP MODAL
            AnimatedVisibility(
                visible = showOnlineMultiplayerModal && !isSearchingOpponent,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.88f))
                        .clickable { showOnlineMultiplayerModal = false },
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
                                text = "🌐 ONLINE MULTIPLAYER",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = SunnyYellow
                            )
                            Text(
                                text = "Play Classic Bingo against online passengers across America!",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                            )

                            // REGION / DECK SELECTOR BUTTON
                            ExposedDropdownMenuBox(
                                expanded = deckDropdownExpanded,
                                onExpandedChange = { deckDropdownExpanded = !deckDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = "🎴 Deck: ${currentDeckChoice.title}",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Game Deck", color = SunnyYellow) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = deckDropdownExpanded) },
                                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = SunnyYellow,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                                    )
                                )

                                ExposedDropdownMenu(
                                    expanded = deckDropdownExpanded,
                                    onDismissRequest = { deckDropdownExpanded = false }
                                ) {
                                    DeckRepository.DECKS.forEach { deck ->
                                        DropdownMenuItem(
                                            text = { Text("🎴 ${deck.title} (${deck.audience})") },
                                            onClick = {
                                                currentDeckChoice = deck
                                                deckDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // FIXED GAME MODE BADGE
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0F172A))
                                    .border(1.5.dp, Color(0xFF2563EB), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🎯 Rule: Classic Bingo (Line Win)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // START GAME BUTTON
                            BeveledGameButton(
                                text = "START GAME ▶",
                                onClick = {
                                    showOnlineMultiplayerModal = false
                                    onStartOnlineMatchmaking(currentDeckChoice)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                textColor = DeepNavy,
                                fontSize = 18
                            )
                        }
                    }
                }
            }

            // SEARCHING FOR OPPONENT STATUS OVERLAY
            AnimatedVisibility(
                visible = isSearchingOpponent,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(24.dp)
                            .shadow(24.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MediumNavy)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(listOf(SunnyYellow, VividOrange))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = if (opponentFoundName != null) "🎉" else "🔍", fontSize = 32.sp)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            if (opponentFoundName == null) {
                                Text(
                                    text = "SEARCHING FOR OPPONENT...",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SunnyYellow,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Matching with another passenger traveling in America...",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(18.dp))
                                Button(
                                    onClick = onCancelMatchmaking,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                ) {
                                    Text(text = "Cancel Search", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                                }
                            } else {
                                Text(
                                    text = "OPPONENT FOUND!",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CompletedGreen,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Match: $opponentFoundName",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Loading Classic Bingo board...",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
