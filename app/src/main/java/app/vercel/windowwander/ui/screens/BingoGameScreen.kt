package app.vercel.windowwander.ui.screens

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.ToneGenerator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.engine.WinResult
import app.vercel.windowwander.model.BingoTile
import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.model.GameMode
import app.vercel.windowwander.model.MultiplayerRoom
import app.vercel.windowwander.ui.components.BeveledGameButton
import app.vercel.windowwander.ui.components.BingoColumnHeaders
import app.vercel.windowwander.ui.components.ConfettiCelebrationGraphic
import app.vercel.windowwander.ui.components.ScenicHighwayBackground
import app.vercel.windowwander.ui.theme.CompletedGreen
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.GoldWinner
import app.vercel.windowwander.ui.theme.GoldWinnerLight
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.SunnyYellowLight
import app.vercel.windowwander.ui.theme.VividOrange
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// CHEERFUL BUBBLY GAME POP AUDIO SYNTHESIZER
fun playCheerfulBubblyPop() {
    try {
        val sampleRate = 22050
        val numSamples = (sampleRate * 0.12).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val freq = 520.0 + (t / 0.12) * 820.0 // Bouncy rising pitch 520Hz -> 1340Hz
            val volume = (1.0 - (t / 0.12)).coerceIn(0.0, 1.0)
            val sampleValue = (Math.sin(2.0 * Math.PI * freq * t) * 26000.0 * volume).toInt()
            samples[i] = sampleValue.toShort()
        }

        val track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            samples.size * 2,
            AudioTrack.MODE_STATIC
        )

        track.write(samples, 0, samples.size)
        track.play()
    } catch (e: Exception) {
        try {
            val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 90)
            toneGen.startTone(ToneGenerator.TONE_DTMF_8, 80)
        } catch (ex: Exception) {}
    }
}

// 4-NOTE CHEERFUL TRIUMPHANT VICTORY FANFARE (C5 -> E5 -> G5 -> C6)
fun playVictoryFanfare() {
    try {
        val sampleRate = 22050
        val totalSecs = 0.65
        val numSamples = (sampleRate * totalSecs).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val (freq, volume) = when {
                t < 0.09 -> Pair(523.25, 1.0 - (t / 0.09) * 0.3) // C5
                t < 0.18 -> Pair(659.25, 1.0 - ((t - 0.09) / 0.09) * 0.3) // E5
                t < 0.28 -> Pair(783.99, 1.0 - ((t - 0.18) / 0.10) * 0.3) // G5
                else -> {
                    val fade = (1.0 - ((t - 0.28) / 0.37)).coerceIn(0.0, 1.0)
                    Pair(1046.50, fade) // C6 High Hold!
                }
            }
            val sampleValue = (Math.sin(2.0 * Math.PI * freq * t) * 28000.0 * volume).toInt()
            samples[i] = sampleValue.toShort()
        }

        val track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            samples.size * 2,
            AudioTrack.MODE_STATIC
        )

        track.write(samples, 0, samples.size)
        track.play()
    } catch (e: Exception) {}
}

// GENTLE PLAYFUL DESCENDING "WAH-WAH" SAD TROMBONE FOR GAME OVER
fun playGameOverSadTrombone() {
    try {
        val sampleRate = 22050
        val totalSecs = 0.85
        val numSamples = (sampleRate * totalSecs).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val (freq, volume) = when {
                t < 0.14 -> Pair(622.25, 0.8) // Eb5
                t < 0.28 -> Pair(587.33, 0.8) // D5
                t < 0.42 -> Pair(554.37, 0.8) // Db5
                else -> {
                    val slideProgress = (t - 0.42) / 0.43
                    val slideFreq = 523.25 - slideProgress * 140.0 // Slide down 523Hz -> 383Hz
                    val fade = (1.0 - slideProgress).coerceIn(0.0, 1.0)
                    Pair(slideFreq, fade * 0.9)
                }
            }
            val period = sampleRate / freq
            val saw = (2.0 * (i % period) / period - 1.0)
            val sampleValue = (saw * 22000.0 * volume).toInt()
            samples[i] = sampleValue.toShort()
        }

        val track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            samples.size * 2,
            AudioTrack.MODE_STATIC
        )

        track.write(samples, 0, samples.size)
        track.play()
    } catch (e: Exception) {}
}

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
    multiplayerRoom: MultiplayerRoom? = null,
    currentUserNickname: String = "You",
    onTapTile: (Int) -> Unit,
    onUndo: () -> Unit,
    onNewGame: () -> Unit,
    onNextRound: () -> Unit = {},
    onDismissCelebration: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onDismissAccountPrompt: () -> Unit,
    onBackToHome: () -> Unit
) {
    val scrollState = rememberScrollState()
    val completedCount = tiles.count { it.isCompleted }
    var globalExplosionCenter by remember { mutableStateOf<Offset?>(null) }
    var rootBoxWindowOffset by remember { mutableStateOf(Offset.Zero) }

    val otherRealPlayers = multiplayerRoom?.players?.filter { it.nickname != currentUserNickname } ?: emptyList()

    // TRIGGER FANFARE OR SAD TROMBONE AUDIO EFFECT WHEN CELEBRATION OVERLAY OPENS
    val winnerName = multiplayerRoom?.winnerNickname
    val isSelfWinner = winnerName.isNullOrBlank() || winnerName == currentUserNickname
    val isHost = multiplayerRoom?.players?.find { it.nickname == currentUserNickname }?.isHost
        ?: (multiplayerRoom?.players?.firstOrNull()?.nickname == currentUserNickname || multiplayerRoom == null)

    val isRoomFinished = multiplayerRoom?.isFinished == true || multiplayerRoom?.winnerNickname != null
    val canStartNextRound = isHost || !isRoomFinished

    LaunchedEffect(showCelebration) {
        if (showCelebration && soundEnabled) {
            if (isSelfWinner) {
                playVictoryFanfare()
            } else {
                playGameOverSadTrombone()
            }
        }
    }

    ScenicHighwayBackground(deckId = deck.id, region = deck.region) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp, top = 22.dp, bottom = 12.dp)
                .onGloballyPositioned { coordinates ->
                    rootBoxWindowOffset = coordinates.boundsInWindow().topLeft
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
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

                    // GAME MODE BADGE (DECK TITLE REMOVED TO CLEAR CAMERA CUTOUT)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = gameMode.displayName,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black,
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

                // B I N G O GLOSSY 3D GEL BUTTON HEADERS
                BingoColumnHeaders()

                Spacer(modifier = Modifier.height(6.dp))

                // 5x5 RESPONSIVE BINGO GRID
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    userScrollEnabled = false
                ) {
                    items(tiles) { tile ->
                        BingoTileView(
                            tile = tile,
                            isWinningLine = winResult.winningLineIndices.flatten().contains(tile.index),
                            soundEnabled = soundEnabled,
                            hapticsEnabled = hapticsEnabled,
                            onTapWithCenter = { windowCenter ->
                                val relativeCenter = Offset(
                                    x = windowCenter.x - rootBoxWindowOffset.x,
                                    y = windowCenter.y - rootBoxWindowOffset.y
                                )
                                globalExplosionCenter = relativeCenter
                                onTapTile(tile.index)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // BINGO WIN STATUS BADGE (NO NEW BOARD BUTTON DURING GAMEPLAY)
                if (winResult.hasWon) {
                    Box(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(14.dp))
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.horizontalGradient(listOf(SunnyYellow, GoldWinner))
                            )
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "🎉 BINGO! (${winResult.winningLinesCount} Line${if (winResult.winningLinesCount > 1) "s" else ""})",
                            fontWeight = FontWeight.Black,
                            color = DeepNavy,
                            fontSize = 14.sp
                        )
                    }
                }

                // LIVE REAL CONNECTED PASSENGER BOARDS SECTION (DISPLAYED IN ROWS OF EXACTLY 2 BOARDS SIDE-BY-SIDE)
                if (otherRealPlayers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CONNECTED PASSENGERS (${otherRealPlayers.size})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = SunnyYellow
                            )
                            Text(
                                text = "⚡ Live Room Sync",
                                fontSize = 11.sp,
                                color = CompletedGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // CHUNK PASSENGERS INTO PAIRS OF 2 BOARDS PER ROW
                        otherRealPlayers.chunked(2).forEach { pair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                pair.forEach { realPlayer ->
                                    OpponentMiniBoardCard(
                                        nickname = realPlayer.nickname,
                                        avatarEmoji = "🚗",
                                        completedIndices = realPlayer.completedTiles,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (pair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // PASSENGER SAFETY FOOTER
                Text(
                    text = "🚗 Passenger View • Drivers Drive Safely",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            // SCREEN-WIDE STAR & CONFETTI EXPLOSION OVERLAY
            globalExplosionCenter?.let { centerPos ->
                GlobalStarExplosionOverlay(
                    center = centerPos,
                    onAnimationFinished = { globalExplosionCenter = null }
                )
            }

            // BINGO CELEBRATION OVERLAY (TRIGGERED FOR SELF WINNER OR OTHER PASSENGER WINNER)
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
                                Text(text = if (isSelfWinner) "🏆" else "🏁", fontSize = 36.sp)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = if (isSelfWinner) "BINGO VICTORY!" else "GAME OVER!",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Black,
                                color = SunnyYellow
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (isSelfWinner) "Great roadside discoveries!" else "🎉 $winnerName won the Bingo match!",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CompletedGreen.copy(alpha = 0.25f))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = if (isSelfWinner) "+100 BONUS XP ⭐️" else "MATCH FINISHED 🏁",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CompletedGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // START NEXT ROUND BUTTON (DIMMED/DISABLED FOR PASSENGERS UNTIL HOST STARTS NEXT ROUND)
                            BeveledGameButton(
                                text = if (canStartNextRound) "START NEXT ROUND 🔄" else "⏳ WAITING FOR HOST TO START NEXT ROUND...",
                                onClick = {
                                    if (canStartNextRound) {
                                        onDismissCelebration()
                                        onNextRound()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                buttonColor = if (canStartNextRound) SunnyYellow else MediumNavy,
                                textColor = if (canStartNextRound) DeepNavy else Color.White.copy(alpha = 0.5f),
                                fontSize = if (canStartNextRound) 16 else 12
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // RETURN HOME BUTTON
                            OutlinedButton(
                                onClick = {
                                    onDismissCelebration()
                                    onBackToHome()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(text = "Return Home", fontWeight = FontWeight.Bold, color = Color.White)
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

// OPPONENT LIVE MINI-BOARD CARD COMPOSABLE
@Composable
fun OpponentMiniBoardCard(
    nickname: String,
    avatarEmoji: String,
    completedIndices: List<Int>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MediumNavy),
        border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = avatarEmoji, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = nickname,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1
                    )
                }
                Text(
                    text = "${completedIndices.size}/25",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = SunnyYellow
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 5x5 MINI GRID PREVIEW
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                userScrollEnabled = false
            ) {
                items(25) { index ->
                    val isDone = completedIndices.contains(index)
                    val isCenter = index == 12

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                when {
                                    isCenter -> SunnyYellow
                                    isDone -> CompletedGreen
                                    else -> Color(0xFF2A364F)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCenter) {
                            Text(text = "⭐", fontSize = 6.sp)
                        } else if (isDone) {
                            Text(text = "✓", fontSize = 6.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// GLOSSY 3D GEL BUTTON BINGO TILE COMPOSABLE
@Composable
fun BingoTileView(
    tile: BingoTile,
    isWinningLine: Boolean,
    soundEnabled: Boolean = true,
    hapticsEnabled: Boolean = true,
    onTapWithCenter: (Offset) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var tileCenterOffset by remember { mutableStateOf(Offset.Zero) }

    // DYNAMIC 3-LINE PROMPT FORMATTING & LONGEST WORD FIT FONT CALCULATION
    val (formattedText, fontSizeSp) = remember(tile.prompt.name) {
        val words = tile.prompt.name.split(" ").filter { it.isNotBlank() }
        val longestWordLen = words.maxOfOrNull { it.length } ?: 0

        val linesText = when {
            words.size <= 1 -> tile.prompt.name
            words.size == 2 -> "${words[0]}\n${words[1]}"
            words.size == 3 -> "${words[0]}\n${words[1]}\n${words[2]}"
            words.size == 4 -> "${words[0]} ${words[1]}\n${words[2]}\n${words[3]}"
            words.size == 5 -> "${words[0]} ${words[1]}\n${words[2]}\n${words[3]} ${words[4]}"
            else -> {
                val line1 = words.take(2).joinToString(" ")
                val line2 = words.drop(2).dropLast(2).joinToString(" ")
                val line3 = words.takeLast(2).joinToString(" ")
                "$line1\n$line2\n$line3"
            }
        }

        val lines = linesText.split("\n")
        val maxLineLen = lines.maxOfOrNull { it.length } ?: linesText.length

        val calculatedSp = when {
            longestWordLen >= 13 -> 8.0f
            longestWordLen >= 11 -> 8.8f  // e.g. "Construction" (12 chars) -> 8.8sp fits on 1 line without wrapping!
            longestWordLen >= 10 -> 9.5f
            maxLineLen <= 5 && lines.size <= 2 -> 14.5f
            maxLineLen <= 7 && lines.size <= 2 -> 13.5f
            maxLineLen <= 9 && lines.size <= 2 -> 12.5f
            maxLineLen <= 9 && lines.size == 3 -> 12.0f
            maxLineLen <= 12 && lines.size <= 2 -> 11.0f
            maxLineLen <= 12 && lines.size == 3 -> 10.0f
            maxLineLen <= 16 && lines.size == 3 -> 9.0f
            else -> 8.0f
        }

        Pair(linesText, calculatedSp)
    }

    // FAST & SMOOTH PRESS ANIMATION (100-160ms)
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.94f
            tile.isCompleted -> 1.02f
            else -> 1.0f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "tilePressScale"
    )

    // BASE GEL GRADIENT COLORS (DARKENED RICH COBALT BLUE FOR UNDISCOVERED TILES)
    val baseGradient = when {
        tile.isCenter -> listOf(
            Color(0xFFFFF066), // Bright Luminous Yellow Top
            Color(0xFFF59E0B), // Vibrant Gold
            Color(0xFFD97706)  // Deeper Gold Bottom
        )
        tile.isCompleted -> listOf(
            Color(0xFF4ADE80), // Bright Mint Green Top
            Color(0xFF10B981), // Luminous Emerald Green
            Color(0xFF047857)  // Deeper Forest Green Bottom
        )
        else -> listOf(
            Color(0xFF0284C7), // Saturated Royal Blue Top
            Color(0xFF0369A1), // Deep Rich Cobalt Blue
            Color(0xFF075985)  // Dark Saturated Navy Blue Bottom
        )
    }

    // BORDER GEL RIM BRUSH
    val rimBorderBrush = when {
        tile.isCenter -> Brush.verticalGradient(
            listOf(Color.White, Color(0xFFFDE047), Color(0xFFB45309))
        )
        tile.isCompleted -> Brush.verticalGradient(
            listOf(Color.White, Color(0xFF86EFAC), Color(0xFF065F46))
        )
        else -> Brush.verticalGradient(
            listOf(Color.White.copy(alpha = 0.85f), Color(0xFF0284C7), Color(0xFF075985).copy(alpha = 0.6f))
        )
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .onGloballyPositioned { coordinates ->
                val bounds = coordinates.boundsInWindow()
                tileCenterOffset = Offset(bounds.left + bounds.width / 2f, bounds.top + bounds.height / 2f)
            }
            .shadow(
                elevation = if (isPressed) 2.dp else if (tile.isCenter || tile.isCompleted) 10.dp else 6.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = when {
                    tile.isCenter -> SunnyYellow
                    tile.isCompleted -> CompletedGreen
                    else -> Color(0xFF0369A1)
                }
            )
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.verticalGradient(baseGradient))
            .border(
                width = if (tile.isCompleted || tile.isCenter) 2.5.dp else 2.dp,
                brush = rimBorderBrush,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (soundEnabled && !tile.isCompleted) {
                        playCheerfulBubblyPop()
                    }
                    if (hapticsEnabled) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    onTapWithCenter(tileCenterOffset)
                }
            )
    ) {
        // TOP CRESCENT GLOSS REFLECTION HIGHLIGHT OVERLAY (INFLATED 3D GEL LOOK)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.55f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                )
        )

        // TILE CONTENT (MAXIMUM FONT SIZE SPLIT ACROSS MAX 3 LINES WITH SHADOW)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            if (tile.isCenter) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "⭐", fontSize = 22.sp)
                    Text(
                        text = "FREE",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.6f),
                                offset = Offset(0f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                }
            } else {
                Text(
                    text = formattedText,
                    fontSize = fontSizeSp.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = (fontSizeSp * 1.15f).sp,
                    maxLines = 3,
                    softWrap = false,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.85f),
                            offset = Offset(0f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
            }
        }
    }
}

// SCREEN-WIDE UNBOUNDED STAR & SHOCKWAVE PARTICLE EXPLOSION OVERLAY
@Composable
fun GlobalStarExplosionOverlay(
    center: Offset,
    onAnimationFinished: () -> Unit
) {
    val burstAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        burstAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(900, easing = LinearEasing)
        )
        onAnimationFinished()
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val progress = burstAnim.value
        val random = Random(42)

        // MASSIVE EXPANDING GOLDEN SHOCKWAVE RING
        val ringRadius = progress * 400f
        val ringAlpha = (1f - progress).coerceIn(0f, 1f)
        drawCircle(
            color = SunnyYellow.copy(alpha = ringAlpha * 0.85f),
            radius = ringRadius,
            center = center,
            style = Stroke(width = 8f * (1f - progress))
        )

        // SECONDARY MINT GREEN SHOCKWAVE RING
        val innerRingRadius = progress * 280f
        drawCircle(
            color = Color(0xFF34D399).copy(alpha = ringAlpha * 0.7f),
            radius = innerRingRadius,
            center = center,
            style = Stroke(width = 5f * (1f - progress))
        )

        // 48 MASSIVE RADIANT PARTICLES SHOOTING ACROSS THE SCREEN
        val particleColors = listOf(
            SunnyYellow, VividOrange, GoldWinnerLight, Color(0xFF38BDF8),
            CompletedGreen, Color(0xFFE11D48), Color.White, Color(0xFFA855F7), Color(0xFFFDE047)
        )

        for (i in 0..48) {
            val angle = (i * (360f / 48f) + random.nextFloat() * 10f) * (Math.PI / 180f).toFloat()
            val speed = 120f + random.nextFloat() * 320f
            val distance = speed * progress

            val px = center.x + cos(angle) * distance
            val py = center.y + sin(angle) * distance

            // SLOW FADE OUT AS PARTICLES EXPLODE
            val alpha = (1f - progress).coerceIn(0f, 1f)
            val color = particleColors[i % particleColors.size].copy(alpha = alpha)
            val particleRadius = (16f * (1f - progress * 0.4f)).coerceAtLeast(3f)

            drawCircle(color = color, radius = particleRadius, center = Offset(px, py))
        }
    }
}
