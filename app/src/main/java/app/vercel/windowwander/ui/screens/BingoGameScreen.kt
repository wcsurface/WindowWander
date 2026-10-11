package app.vercel.windowwander.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.engine.WinResult
import app.vercel.windowwander.model.BingoTile
import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.model.GameMode
import app.vercel.windowwander.ui.components.BeveledGameButton
import app.vercel.windowwander.ui.components.BingoColumnHeaders
import app.vercel.windowwander.ui.components.ConfettiCelebrationGraphic
import app.vercel.windowwander.ui.components.ScenicHighwayBackground
import app.vercel.windowwander.ui.theme.CompletedGreen
import app.vercel.windowwander.ui.theme.CompletedGreenDark
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.GoldWinner
import app.vercel.windowwander.ui.theme.GoldWinnerLight
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.SunnyYellowLight
import app.vercel.windowwander.ui.theme.VividOrange

@Composable
fun BingoGameScreen(
    deck: Deck,
    tiles: List<BingoTile>,
    winResult: WinResult,
    gameMode: GameMode,
    canUndo: Boolean,
    showCelebration: Boolean,
    showAccountPrompt: Boolean,
    soundEnabled: Boolean = true,
    hapticsEnabled: Boolean = true,
    onTapTile: (Int) -> Unit,
    onUndo: () -> Unit,
    onNewGame: () -> Unit,
    onDismissCelebration: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onDismissAccountPrompt: () -> Unit,
    onBackToHome: () -> Unit
) {
    val completedCount = tiles.count { it.isCompleted }

    ScenicHighwayBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // GAME HEADER BAR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // EXIT BUTTON
                    Box(
                        modifier = Modifier
                            .shadow(6.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(MediumNavy)
                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .clickable { onBackToHome() }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "◀ Exit",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // DECK TITLE & GAME MODE
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = deck.title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = gameMode.displayName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SunnyYellowLight
                        )
                    }

                    // UNDO ACCIDENTAL TAP BUTTON
                    if (canUndo) {
                        Box(
                            modifier = Modifier
                                .shadow(6.dp, RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .background(VividOrange)
                                .clickable { onUndo() }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "↩ Undo",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(60.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // SIGHTS PROGRESS BAR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sights Discovered",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "$completedCount / 25",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = SunnyYellow
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { completedCount / 25f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = CompletedGreen,
                    trackColor = MediumNavy
                )

                Spacer(modifier = Modifier.height(12.dp))

                // B I N G O 3D COLORED HEADERS
                BingoColumnHeaders()

                Spacer(modifier = Modifier.height(6.dp))

                // 5x5 RESPONSIVE BINGO GRID
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(tiles) { tile ->
                        BingoTileView(
                            tile = tile,
                            isWinningLine = winResult.winningLineIndices.flatten().contains(tile.index),
                            hapticsEnabled = hapticsEnabled,
                            onTap = { onTapTile(tile.index) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // GAME ACTION & WIN STATUS BAR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onNewGame,
                        colors = ButtonDefaults.buttonColors(containerColor = MediumNavy),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "🔄 New Board",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }

                    if (winResult.hasWon) {
                        Box(
                            modifier = Modifier
                                .shadow(8.dp, RoundedCornerShape(14.dp))
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.horizontalGradient(listOf(SunnyYellow, GoldWinner))
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "🎉 BINGO! (${winResult.winningLinesCount} Line${if (winResult.winningLinesCount > 1) "s" else ""})",
                                fontWeight = FontWeight.Black,
                                color = DeepNavy,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // PASSENGER SAFETY FOOTER
                Text(
                    text = "🚗 Passenger View • Drivers Drive Safely",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            // BINGO CELEBRATION OVERLAY
            AnimatedVisibility(
                visible = showCelebration && !showAccountPrompt,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.88f)),
                    contentAlignment = Alignment.Center
                ) {
                    ConfettiCelebrationGraphic()

                    Card(
                        modifier = Modifier
                            .padding(20.dp)
                            .shadow(24.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MediumNavy)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(listOf(SunnyYellowLight, GoldWinner))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🏆", fontSize = 36.sp)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "BINGO VICTORY!",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = SunnyYellow
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Great roadside discoveries!",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CompletedGreen.copy(alpha = 0.25f))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "+100 BONUS XP • +50 COINS ⭐️",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CompletedGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            BeveledGameButton(
                                text = "START NEXT ROUND 🔄",
                                onClick = {
                                    onDismissCelebration()
                                    onNewGame()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                textColor = Color.White,
                                fontSize = 16
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = onDismissCelebration,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(text = "Keep Playing Board", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            // ACCOUNT CREATION / GOOGLE SIGN-IN POPUP OVERLAY
            AnimatedVisibility(
                visible = showAccountPrompt,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.88f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(20.dp)
                            .shadow(24.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MediumNavy)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(listOf(Color(0xFF38BDF8), Color(0xFF2563EB)))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "💾", fontSize = 32.sp)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "SAVE YOUR ACHIEVEMENTS!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = SunnyYellow,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Back up your Level, XP, earned badges, and lifetime Bingo wins so you never lose your road trip progress!",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // GOOGLE SIGN IN BUTTON
                            BeveledGameButton(
                                text = "🌐 SIGN IN WITH GOOGLE",
                                onClick = onGoogleSignIn,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                textColor = Color.White,
                                fontSize = 15
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // CREATE FREE ACCOUNT BUTTON
                            BeveledGameButton(
                                text = "✉️ CREATE FREE ACCOUNT",
                                onClick = onGoogleSignIn,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                textColor = DeepNavy,
                                fontSize = 15
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = onDismissAccountPrompt,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text(text = "Continue as Guest (Skip)", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BingoTileView(
    tile: BingoTile,
    isWinningLine: Boolean,
    hapticsEnabled: Boolean,
    onTap: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // SATISFYING SPRING POP ANIMATION ON TAPPING
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.86f
            tile.isCompleted -> 1.05f
            else -> 1.0f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tileSpringScale"
    )

    val bgBrush = when {
        tile.isCenter -> Brush.verticalGradient(listOf(SunnyYellowLight, SunnyYellow))
        tile.isCompleted && isWinningLine -> Brush.verticalGradient(listOf(GoldWinnerLight, GoldWinner))
        tile.isCompleted -> Brush.verticalGradient(listOf(CompletedGreen, CompletedGreenDark))
        else -> Brush.verticalGradient(listOf(Color(0xFF2A364F), MediumNavy))
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .shadow(if (tile.isCompleted) 8.dp else 3.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(bgBrush)
            .border(
                width = if (isWinningLine) 2.5.dp else 1.dp,
                color = when {
                    isWinningLine -> GoldWinnerLight
                    tile.isCompleted -> CompletedGreen
                    else -> Color.White.copy(alpha = 0.25f)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (hapticsEnabled) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    onTap()
                }
            )
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (tile.isCenter) {
                Text(text = "⭐", fontSize = 16.sp)
                Text(
                    text = "FREE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            } else {
                Text(
                    text = tile.prompt.name,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.5.sp,
                    maxLines = 3
                )

                if (tile.isCompleted) {
                    Text(text = "✓", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }
        }
    }
}
