package app.vercel.windowwander.ui.screens

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.R
import app.vercel.windowwander.model.PlayerProfile
import app.vercel.windowwander.ui.components.BeveledGameButton
import app.vercel.windowwander.ui.components.GradientPillNavigationBar
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.MidnightNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.VividOrange
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

@Composable
fun LoginScreen(
    profile: PlayerProfile,
    isLoggedIn: Boolean = false,
    onLoginSubmit: (String, String) -> Unit = { _, _ -> },
    onLogOut: () -> Unit = {},
    onNavigateHome: () -> Unit = {},
    onNavigateLogin: () -> Unit = {},
    onNavigateShop: () -> Unit = {},
    onNavigateSettings: () -> Unit = {},
    onBackToHome: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var isRegisterTab by remember { mutableStateOf(false) }

    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var nicknameInput by remember { mutableStateOf("") }

    val effectiveLoggedIn = isLoggedIn || (profile.nickname.isNotBlank() && !profile.nickname.startsWith("Road Explorer"))

    // COIL GIF DECODER LOADER TO PREVENT RUNTIME CRASHES ON GIF ANIMATION
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // MAIN LOBBY DEDICATED DOODLE BACKGROUND ARTWORK
        Image(
            painter = painterResource(id = R.drawable.bg_main_home),
            contentDescription = "Main Lobby Background Artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // DARK SCRIM OVERLAY FOR HIGH CONTRAST & LEGIBILITY
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 104.dp)
        ) {
            // TOP HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (effectiveLoggedIn) "👤 Account Profile" else "🔑 Log In / Join",
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

            // CENTERED OFFICIAL LOGO FLOATING DIRECTLY ON MAIN LOBBY BACKGROUND
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
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

            if (!effectiveLoggedIn) {
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
                        // TAB SWITCHER (LOG IN VS REGISTER)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(MidnightNavy)
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (!isRegisterTab) SunnyYellow else Color.Transparent)
                                    .clickable { isRegisterTab = false }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Log In",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (!isRegisterTab) MidnightNavy else Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isRegisterTab) SunnyYellow else Color.Transparent)
                                    .clickable { isRegisterTab = true }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Create Free Account",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isRegisterTab) MidnightNavy else Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        AnimatedVisibility(visible = isRegisterTab) {
                            Column {
                                OutlinedTextField(
                                    value = nicknameInput,
                                    onValueChange = { nicknameInput = it },
                                    label = { Text("Display Nickname", color = SunnyYellow) },
                                    placeholder = { Text("e.g. RoadCaptain_99", color = Color.Gray) },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = SunnyYellow,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                        OutlinedTextField(
                            value = usernameInput,
                            onValueChange = { usernameInput = it },
                            label = { Text("Username / Email", color = SunnyYellow) },
                            placeholder = { Text("Enter your username", color = Color.Gray) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = SunnyYellow,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("Password", color = SunnyYellow) },
                            placeholder = { Text("Enter your password", color = Color.Gray) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = SunnyYellow,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.4f)
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        BeveledGameButton(
                            text = if (isRegisterTab) "✨ REGISTER ACCOUNT" else "🔑 LOG IN NOW",
                            onClick = {
                                if (usernameInput.isNotBlank() && passwordInput.isNotBlank()) {
                                    onLoginSubmit(usernameInput, passwordInput)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            textColor = MidnightNavy,
                            fontSize = 17
                        )
                    }
                }
            } else {
                // LOGGED-IN ACCOUNT PROFILE CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(22.dp)),
                    shape = RoundedCornerShape(22.dp),
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
                                    Brush.radialGradient(listOf(SunnyYellow, VividOrange))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🐻", fontSize = 38.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = profile.nickname,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Text(
                            text = "Level ${profile.level} Road Explorer • ${profile.xp} XP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SunnyYellow
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // PROMINENT LOG OUT BUTTON
                        BeveledGameButton(
                            text = "🚪 LOG OUT",
                            onClick = onLogOut,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            buttonColor = VividOrange,
                            textColor = Color.White,
                            fontSize = 18
                        )
                    }
                }
            }
        }

        // FLOATING GRADIENT PILL 3D NAVIGATION BAR
        GradientPillNavigationBar(
            currentScreenName = "login",
            isLoggedIn = effectiveLoggedIn,
            onNavigateHome = onNavigateHome,
            onNavigateLogin = onNavigateLogin,
            onNavigateShop = onNavigateShop,
            onNavigateSettings = onNavigateSettings,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
