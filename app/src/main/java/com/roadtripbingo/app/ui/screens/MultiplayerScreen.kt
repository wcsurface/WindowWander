package com.roadtripbingo.app.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roadtripbingo.app.data.DeckRepository
import com.roadtripbingo.app.model.Deck
import com.roadtripbingo.app.model.GameMode
import com.roadtripbingo.app.model.MultiplayerRoom
import com.roadtripbingo.app.ui.components.BeveledGameButton
import com.roadtripbingo.app.ui.theme.CompletedGreen
import com.roadtripbingo.app.ui.theme.DeepNavy
import com.roadtripbingo.app.ui.theme.MediumNavy
import com.roadtripbingo.app.ui.theme.MidnightNavy
import com.roadtripbingo.app.ui.theme.SunnyYellow
import com.roadtripbingo.app.ui.theme.VividOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiplayerScreen(
    room: MultiplayerRoom?,
    selectedDeck: Deck,
    selectedGameMode: GameMode = GameMode.CLASSIC_LINE,
    currentUserNickname: String = "You",
    unlockedDeckIds: Set<String> = emptySet(),
    onSelectDeck: (Deck) -> Unit,
    onSelectGameMode: (GameMode) -> Unit,
    onCreateRoom: () -> Unit,
    onJoinRoom: (String) -> Unit,
    onStartGame: () -> Unit,
    onBackToHome: () -> Unit
) {
    val scrollState = rememberScrollState()
    var deckDropdownExpanded by remember { mutableStateOf(false) }
    var rulesDropdownExpanded by remember { mutableStateOf(false) }

    val hostChosenDeck = DeckRepository.DECKS.find { it.id == room?.deckId } ?: selectedDeck
    val hostChosenGameMode = room?.gameMode ?: selectedGameMode

    val isHost = room?.players?.find { it.nickname == currentUserNickname }?.isHost
        ?: (room?.players?.firstOrNull()?.nickname == currentUserNickname || room == null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .verticalScroll(scrollState)
            .padding(start = 16.dp, end = 16.dp, top = 38.dp, bottom = 16.dp)
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🚗 Road Trip Bingo!",
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

        // ROOM LOBBY CONTAINER
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(22.dp)),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MediumNavy)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ROOM LOBBY CODE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // PROMINENT 4-LETTER CODE BADGE
                Box(
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(listOf(SunnyYellow, VividOrange))
                        )
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = room?.roomCode ?: "ROAD",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = DeepNavy,
                        letterSpacing = 4.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Share this code with other passengers in your vehicle!",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // CONNECTED PASSENGERS LIST
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "PASSENGERS IN ROOM (${room?.players?.size ?: 1})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = SunnyYellow
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val playerList = room?.players ?: emptyList()
                    playerList.forEach { player ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F172A))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🚗", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = player.nickname,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                if (player.isHost) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SunnyYellow)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "HOST",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = DeepNavy
                                        )
                                    }
                                }
                            }

                            Text(
                                text = "READY ✓",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CompletedGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    if (isHost) {
                        // DECK CHOICE DROPDOWN (ONLY SHOWS UNLOCKED DECKS)
                        ExposedDropdownMenuBox(
                            expanded = deckDropdownExpanded,
                            onExpandedChange = { deckDropdownExpanded = !deckDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = "🎴 Card: ${hostChosenDeck.title}",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Game Card Choice (Host Only)", color = SunnyYellow) },
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
                                val availableDecks = DeckRepository.DECKS.filter { deck ->
                                    deck.isFree || deck.id in unlockedDeckIds
                                }
                                availableDecks.forEach { deck ->
                                    DropdownMenuItem(
                                        text = { Text("🎴 ${deck.title} (${deck.audience})") },
                                        onClick = {
                                            onSelectDeck(deck)
                                            deckDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // RULES CHOICE DROPDOWN (ONLY HOST CAN CHANGE)
                        ExposedDropdownMenuBox(
                            expanded = rulesDropdownExpanded,
                            onExpandedChange = { rulesDropdownExpanded = !rulesDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = "🎯 Rule: ${hostChosenGameMode.displayName}",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Game Rule Choice (Host Only)", color = SunnyYellow) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = rulesDropdownExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = SunnyYellow,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = rulesDropdownExpanded,
                                onDismissRequest = { rulesDropdownExpanded = false }
                            ) {
                                GameMode.values().forEach { mode ->
                                    DropdownMenuItem(
                                        text = { Text("🎯 ${mode.displayName} (${mode.description})") },
                                        onClick = {
                                            onSelectGameMode(mode)
                                            rulesDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // LAUNCH GAME BUTTON (HOST ONLY)
                        BeveledGameButton(
                            text = "🚀 LAUNCH GAME NOW ▶",
                            onClick = onStartGame,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            textColor = DeepNavy,
                            fontSize = 18
                        )
                    } else {
                        // PASSENGER WAITING BANNER
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.5.dp, SunnyYellow, RoundedCornerShape(14.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "⏳ WAITING FOR HOST TO START ▶\n(Card: ${hostChosenDeck.title})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = SunnyYellow,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
