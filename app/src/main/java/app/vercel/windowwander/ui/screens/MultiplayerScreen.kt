package app.vercel.windowwander.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.data.DeckRepository
import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.model.GameMode
import app.vercel.windowwander.model.MultiplayerRoom
import app.vercel.windowwander.ui.components.BeveledGameButton
import app.vercel.windowwander.ui.theme.CompletedGreen
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.MidnightNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.VividOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiplayerScreen(
    room: MultiplayerRoom?,
    selectedDeck: Deck,
    selectedGameMode: GameMode = GameMode.CLASSIC_LINE,
    currentUserNickname: String = "You",
    onSelectDeck: (Deck) -> Unit,
    onSelectGameMode: (GameMode) -> Unit = {},
    onCreateRoom: () -> Unit,
    onJoinRoom: (String) -> Unit,
    onStartGame: () -> Unit,
    onBackToHome: () -> Unit
) {
    val scrollState = rememberScrollState()
    var joinCodeInput by remember { mutableStateOf("") }

    val hostChosenDeck = DeckRepository.DECKS.find { it.id == room?.deckId } ?: selectedDeck
    val hostChosenGameMode = room?.gameMode ?: selectedGameMode

    var deckDropdownExpanded by remember { mutableStateOf(false) }
    var gameModeDropdownExpanded by remember { mutableStateOf(false) }

    val isHost = room?.players?.find { it.nickname == currentUserNickname }?.isHost
        ?: (room?.players?.firstOrNull()?.nickname == currentUserNickname || room?.players?.isEmpty() == true)

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

        if (room != null) {
            // ACTIVE MULTIPLAYER HOST LOBBY CARD
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
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 4-LETTER ROOM CODE BADGE
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
                            text = room.roomCode,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = DeepNavy,
                            letterSpacing = 4.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Share this 4-letter code with other passengers in your vehicle!",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // PASSENGERS IN ROOM LIST
                    Text(
                        text = "PASSENGERS IN ROOM (${room.players.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = SunnyYellow,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    room.players.forEach { player ->
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
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(VividOrange),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "🚗", fontSize = 16.sp)
                                }

                                Spacer(modifier = Modifier.width(10.dp))

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
                        // DECK CHOICE DROPDOWN (ONLY HOST CAN CHANGE)
                        ExposedDropdownMenuBox(
                            expanded = deckDropdownExpanded,
                            onExpandedChange = { deckDropdownExpanded = !deckDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = "🎴 Deck: ${hostChosenDeck.title}",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Game Deck Choice (Host Only)", color = SunnyYellow) },
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

                        Spacer(modifier = Modifier.height(12.dp))

                        // RULES CHOICE DROPDOWN (ONLY HOST CAN CHANGE)
                        ExposedDropdownMenuBox(
                            expanded = gameModeDropdownExpanded,
                            onExpandedChange = { gameModeDropdownExpanded = !gameModeDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = "🎯 Rule: ${hostChosenGameMode.displayName}",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Victory Rules Choice (Host Only)", color = SunnyYellow) },
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
                                        onSelectGameMode(GameMode.CLASSIC_LINE)
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
                                        onSelectGameMode(GameMode.TWO_LINES)
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
                                        onSelectGameMode(GameMode.BLACKOUT)
                                        gameModeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    } else {
                        // READ-ONLY DECK AND RULES CARDS FOR JOINED PASSENGERS (UPDATED LIVE WHEN HOST CHANGES)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, SunnyYellow.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Text(
                                    text = "🎴 Deck (Set by Host): ${hostChosenDeck.title}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "🎯 Victory Rules (Set by Host): ${hostChosenGameMode.displayName}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SunnyYellow
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    BeveledGameButton(
                        text = if (isHost) "🚀 LAUNCH GAME NOW ▶" else "⏳ WAITING FOR HOST TO START ▶",
                        onClick = onStartGame,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        textColor = DeepNavy,
                        fontSize = 18
                    )
                }
            }
        } else {
            // CREATE ROOM OR JOIN ROOM SETUP CARDS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MediumNavy)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "1. Select Game Deck & Mode",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = SunnyYellow
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // DECK SELECTOR DROPDOWN
                    ExposedDropdownMenuBox(
                        expanded = deckDropdownExpanded,
                        onExpandedChange = { deckDropdownExpanded = !deckDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = "🎴 Deck: ${hostChosenDeck.title}",
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

                    Spacer(modifier = Modifier.height(12.dp))

                    // GAME MODE SELECTOR DROPDOWN
                    ExposedDropdownMenuBox(
                        expanded = gameModeDropdownExpanded,
                        onExpandedChange = { gameModeDropdownExpanded = !gameModeDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = "🎯 Rule: ${hostChosenGameMode.displayName}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Victory Rules", color = SunnyYellow) },
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
                                    onSelectGameMode(GameMode.CLASSIC_LINE)
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
                                    onSelectGameMode(GameMode.TWO_LINES)
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
                                    onSelectGameMode(GameMode.BLACKOUT)
                                    gameModeDropdownExpanded = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    BeveledGameButton(
                        text = "START GAME ▶",
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

            Spacer(modifier = Modifier.height(16.dp))

            // JOIN ROOM CARD WITH 4-LETTER CODE
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MediumNavy)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "2. Or Join Passenger's Room",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = SunnyYellow
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = joinCodeInput,
                        onValueChange = { if (it.length <= 4) joinCodeInput = it.uppercase() },
                        label = { Text("4-Letter Code", color = SunnyYellow) },
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

                    Button(
                        onClick = { if (joinCodeInput.length >= 4) onJoinRoom(joinCodeInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = CompletedGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "JOIN CAR ROOM ▶",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
