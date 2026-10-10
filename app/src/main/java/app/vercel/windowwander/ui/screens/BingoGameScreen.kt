package app.vercel.windowwander.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.engine.WinResult
import app.vercel.windowwander.model.BingoTile
import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.model.GameMode
import app.vercel.windowwander.ui.theme.AccentOrange
import app.vercel.windowwander.ui.theme.CompletedGreen
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.GoldWinner
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.TravelTeal

@Composable
fun BingoGameScreen(
    deck: Deck,
    tiles: List<BingoTile>,
    winResult: WinResult,
    gameMode: GameMode,
    canUndo: Boolean,
    showCelebration: Boolean,
    onTapTile: (Int) -> Unit,
    onUndo: () -> Unit,
    onNewGame: () -> Unit,
    onDismissCelebration: () -> Unit,
    onBackToHome: () -> Unit
) {
    val completedCount = tiles.count { it.isCompleted }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // GAME TOP BAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "◀ Exit",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TravelTeal,
                    modifier = Modifier.clickable { onBackToHome() }
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = deck.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${gameMode.displayName} • $completedCount/25 Sights",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }

                if (canUndo) {
                    Text(
                        text = "↩ Undo",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentOrange,
                        modifier = Modifier.clickable { onUndo() }
                    )
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5x5 BINGO GRID
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tiles) { tile ->
                    BingoTileView(
                        tile = tile,
                        isWinningLine = winResult.winningLineIndices.flatten().contains(tile.index),
                        onTap = { onTapTile(tile.index) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // STATUS & ACTION BAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onNewGame,
                    colors = ButtonDefaults.buttonColors(containerColor = SunnyYellow),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🔄 New Card",
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy,
                        fontSize = 13.sp
                    )
                }

                if (winResult.hasWon) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(GoldWinner)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "🎉 BINGO! (${winResult.winningLinesCount} Line${if (winResult.winningLinesCount > 1) "s" else ""})",
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // SAFETY FOOTER
            Text(
                text = "🚗 Passenger View • Drivers Drive Safely",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }

        // BINGO CELEBRATION OVERLAY
        AnimatedVisibility(
            visible = showCelebration,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🎉 BINGO! 🎉", fontSize = 28.sp, fontWeight = FontWeight.Black, color = SunnyYellow)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Awesome road trip discovery!", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "+100 Bonus XP Earned! ⭐️", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CompletedGreen)
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onDismissCelebration,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = TravelTeal)
                        ) {
                            Text(text = "Keep Playing This Board", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                onDismissCelebration()
                                onNewGame()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Start Next Round 🔄")
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
    onTap: () -> Unit
) {
    val bgColor = when {
        tile.isCenter -> SunnyYellow
        tile.isCompleted && isWinningLine -> GoldWinner
        tile.isCompleted -> CompletedGreen
        else -> MaterialTheme.colorScheme.surface
    }

    val textColor = when {
        tile.isCenter -> DeepNavy
        tile.isCompleted -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(
                width = if (isWinningLine) 2.dp else 1.dp,
                color = if (isWinningLine) SunnyYellow else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onTap() }
            .padding(4.dp),
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
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = DeepNavy
                )
            } else {
                Text(
                    text = tile.prompt.name,
                    fontSize = 9.sp,
                    fontWeight = if (tile.isCompleted) FontWeight.Bold else FontWeight.Medium,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 11.sp,
                    maxLines = 3
                )
                if (tile.isCompleted) {
                    Text(text = "✓", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }
        }
    }
}
