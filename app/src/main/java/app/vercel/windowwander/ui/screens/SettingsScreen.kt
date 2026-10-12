package app.vercel.windowwander.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.R
import app.vercel.windowwander.model.Region
import app.vercel.windowwander.ui.components.GradientPillNavigationBar
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.VividOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentNickname: String,
    selectedRegion: Region,
    soundEnabled: Boolean,
    hapticsEnabled: Boolean,
    confirmTapEnabled: Boolean,
    kidsModeEasy: Boolean,
    darkMode: Boolean,
    isLoggedIn: Boolean = false,
    onUpdateNickname: (String) -> Unit,
    onSelectRegion: (Region) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    onToggleConfirmTap: (Boolean) -> Unit,
    onToggleKidsMode: (Boolean) -> Unit,
    onNavigateHome: () -> Unit = {},
    onNavigateLogin: () -> Unit = {},
    onNavigateShop: () -> Unit = {},
    onNavigateSettings: () -> Unit = {},
    onBackToHome: () -> Unit = {}
) {
    var nicknameInput by remember { mutableStateOf(currentNickname) }
    var regionExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // SETTINGS FULL SCREEN DOODLE ROAD TRIP BACKGROUND
        Image(
            painter = painterResource(id = R.drawable.bg_settings),
            contentDescription = "Settings Background Artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // DARK OVERLAY SCRIM FOR READABILITY OF SETTINGS CARDS
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 104.dp)
        ) {
            // TOP HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚙️ Settings",
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

            // NICKNAME CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MediumNavy),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Player Nickname", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = nicknameInput,
                        onValueChange = { nicknameInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = SunnyYellow,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onUpdateNickname(nicknameInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = VividOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Save Nickname", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // REGION SELECTOR CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MediumNavy),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Manual Region Selection", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                    Text(
                        text = "Generates regional sights even without active GPS",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = regionExpanded,
                        onExpandedChange = { regionExpanded = !regionExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedRegion.displayName,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = regionExpanded,
                            onDismissRequest = { regionExpanded = false }
                        ) {
                            Region.values().forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region.displayName) },
                                    onClick = {
                                        onSelectRegion(region)
                                        regionExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TOGGLES CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MediumNavy),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Sound Effects", fontWeight = FontWeight.Medium, color = Color.White)
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = onToggleSound,
                            colors = SwitchDefaults.colors(checkedThumbColor = SunnyYellow)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Haptic Vibrations", fontWeight = FontWeight.Medium, color = Color.White)
                        Switch(
                            checked = hapticsEnabled,
                            onCheckedChange = onToggleHaptics,
                            colors = SwitchDefaults.colors(checkedThumbColor = SunnyYellow)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Confirm Tap Selections", fontWeight = FontWeight.Medium, color = Color.White)
                        Switch(
                            checked = confirmTapEnabled,
                            onCheckedChange = onToggleConfirmTap,
                            colors = SwitchDefaults.colors(checkedThumbColor = SunnyYellow)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Easier Kids Mode", fontWeight = FontWeight.Medium, color = Color.White)
                        Switch(
                            checked = kidsModeEasy,
                            onCheckedChange = onToggleKidsMode,
                            colors = SwitchDefaults.colors(checkedThumbColor = SunnyYellow)
                        )
                    }
                }
            }
        }

        // FLOATING GRADIENT PILL 3D NAVIGATION BAR
        GradientPillNavigationBar(
            currentScreenName = "settings",
            isLoggedIn = isLoggedIn,
            onNavigateHome = onNavigateHome,
            onNavigateLogin = onNavigateLogin,
            onNavigateShop = onNavigateShop,
            onNavigateSettings = onNavigateSettings,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
