package app.vercel.windowwander.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.data.DeckRepository
import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.model.GameMode
import app.vercel.windowwander.model.MultiplayerRoom
import app.vercel.windowwander.ui.components.BeveledGameButton
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.ElectricBlue
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.MidnightNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.TravelTeal
import app.vercel.windowwander.ui.theme.VividOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiplayerScreen(
    room: MultiplayerRoom?,
    selectedDeck: Deck,
    onSelectDeck: (Deck) -> Unit,
    onCreateRoom: () -> Unit,
    onJoinRoom: (String) -> Unit,
    onStartGame: () -> Unit,
    onBackToHome: () -> Unit
) {
    var roomInputCode by remember { mutableStateOf("") }
    var selectedGameMode by remember { mutableStateOf(GameMode.CLASSIC_LINE) }

    var deckDropdownExpanded by remember { mutableStateOf(false) }
    var gameModeDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .padding(16.dp)
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🌐 In-Car Multiplayer",
                fontSize = 22.sp,
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

        if (room == null) {
            // START A GAME CARD (HOST)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = MediumNavy),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(SunnyYellow.copy(alpha = 0.2f), MediumNavy)))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "🚗 START A GAME (HOST)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Creates a room code & QR code for passengers in your car!",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    BeveledGameButton(
                        text = "START NEW ROOM ▶",
                        onClick = onCreateRoom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        buttonColor = SunnyYellow,
                        bevelColor = Color(0xFFD97706),
                        textColor = DeepNavy,
                        fontSize = 16
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // JOIN A GAME CARD (CODE)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = MediumNavy),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(ElectricBlue.copy(alpha = 0.2f), MediumNavy)))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "📱 JOIN A GAME (CODE)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = roomInputCode,
                        onValueChange = { roomInputCode = it.uppercase().take(4) },
                        label = { Text("Enter 4-Letter Room Code", color = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = SunnyYellow,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    BeveledGameButton(
                        text = "JOIN CAR ROOM ▶",
                        onClick = { if (roomInputCode.isNotBlank()) onJoinRoom(roomInputCode) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        buttonColor = Color(0xFF2563EB),
                        bevelColor = Color(0xFF1D4ED8),
                        textColor = Color.White,
                        fontSize = 16
                    )
                }
            }
        } else {
            // ACTIVE CAR ROOM LOBBY WITH QR CODE & ROOM CODE
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = MediumNavy),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(SunnyYellow.copy(alpha = 0.3f), MediumNavy)))
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "ROOM CODE", fontSize = 12.sp, fontWeight = FontWeight.Black, color = SunnyYellow)
                    Text(
                        text = room.roomCode,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 4.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // QR CODE DISPLAY BADGE FOR SCANNING
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📷\n[ QR CODE ]\nScan to Join", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Scan QR Code or enter code '${room.roomCode}' to join!",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Passengers Connected (${room.players.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(room.players) { player ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MediumNavy),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${if (player.isHost) "👑 " else "🚗 "}${player.nickname}",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Ready",
                                fontSize = 12.sp,
                                color = TravelTeal,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 1. DECK SELECTION BUTTON (DEFAULTED TO SAVED OR LOCAL REGION DECK)
            ExposedDropdownMenuBox(
                expanded = deckDropdownExpanded,
                onExpandedChange = { deckDropdownExpanded = !deckDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = "🎴 Deck: ${selectedDeck.title}",
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
                                onSelectDeck(deck)
                                deckDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. GAME MODE SELECTION BUTTON (DEFAULTED TO CLASSIC BINGO)
            ExposedDropdownMenuBox(
                expanded = gameModeDropdownExpanded,
                onExpandedChange = { gameModeDropdownExpanded = !gameModeDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = "🎯 Mode: ${selectedGameMode.displayName}",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Winning Condition", color = SunnyYellow) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gameModeDropdownExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = SunnyYellow,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                    )
                )

                ExposedDropdownMenu(
                    expanded = gameModeDropdownExpanded,
                    onDismissRequest = { gameModeDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text("Classic Line Bingo", fontWeight = FontWeight.Bold)
                                Text("Complete a horizontal, vertical, or diagonal line.", fontSize = 11.sp, color = Color.Gray)
                            }
                        },
                        onClick = {
                            selectedGameMode = GameMode.CLASSIC_LINE
                            gameModeDropdownExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text("Two Lines Bingo", fontWeight = FontWeight.Bold)
                                Text("Complete two winning lines.", fontSize = 11.sp, color = Color.Gray)
                            }
                        },
                        onClick = {
                            selectedGameMode = GameMode.TWO_LINES
                            gameModeDropdownExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text("Blackout Bingo", fontWeight = FontWeight.Bold)
                                Text("Find all 24 discoverable items and fill your card.", fontSize = 11.sp, color = Color.Gray)
                            }
                        },
                        onClick = {
                            selectedGameMode = GameMode.BLACKOUT
                            gameModeDropdownExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BeveledGameButton(
                text = "START BINGO BOARD ▶",
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                buttonColor = VividOrange,
                bevelColor = Color(0xFFC2410C),
                textColor = Color.White,
                fontSize = 18
            )
        }
    }
}
