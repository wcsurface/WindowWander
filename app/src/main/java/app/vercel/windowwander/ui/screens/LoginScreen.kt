package app.vercel.windowwander.ui.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.R
import app.vercel.windowwander.model.PlayerProfile
import app.vercel.windowwander.ui.components.BeveledGameButton
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

@Composable
fun LoginScreen(
    profile: PlayerProfile,
    isLoggedIn: Boolean,
    onLoginSubmit: (String, String) -> Unit,
    onLogOut: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateShop: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var usernameInput by remember { mutableStateOf(profile.nickname) }
    var passwordInput by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }

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
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 104.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TOP BAR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isLoggedIn) "👤 Account Profile" else if (isRegisterMode) "📝 Create Account" else "🔑 Log In",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Box(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isLoggedIn) CompletedGreen else SunnyYellow)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isLoggedIn) "ACTIVE" else "GUEST",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = DeepNavy
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CENTERED ANIMATED GIF LOGO
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

                if (!isLoggedIn) {
                    // USER / PASSWORD LOG IN & JOIN CARD
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(16.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MediumNavy)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isRegisterMode) "JOIN ROAD TRIP BINGO" else "LOG IN TO YOUR ACCOUNT",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = SunnyYellow,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (isRegisterMode) "Create your explorer account to save your Level, XP, and lifetime Bingo wins!" else "Sign in with your username and password to back up your road trip progress!",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedTextField(
                                value = usernameInput,
                                onValueChange = { usernameInput = it },
                                label = { Text("Username / Email", color = SunnyYellow) },
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
                                text = if (isRegisterMode) "🚀 CREATE ACCOUNT & JOIN" else "🔑 LOG IN",
                                onClick = {
                                    if (usernameInput.isNotBlank()) {
                                        onLoginSubmit(usernameInput, passwordInput)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                textColor = DeepNavy,
                                fontSize = 16
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { isRegisterMode = !isRegisterMode },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                            ) {
                                Text(
                                    text = if (isRegisterMode) "Already have an account? Log In" else "Need an account? Join Now ➔",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                } else {
                    // LOGGED IN USER PROFILE CARD (WITH PROMINENT LOG OUT BUTTON)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(16.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MediumNavy)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "👤 CURRENTLY LOGGED IN",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = SunnyYellow,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = profile.nickname,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )

                            Text(
                                text = "Level ${profile.level} Road Explorer • ${profile.xp} XP",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SunnyYellow
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(CompletedGreen.copy(alpha = 0.25f))
                                    .border(1.5.dp, CompletedGreen, RoundedCornerShape(12.dp))
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✅ Account Synced & Backed Up to Railway DB!",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CompletedGreen
                                )
                            }

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
                currentScreenName = if (isLoggedIn) "logout" else "login",
                isLoggedIn = isLoggedIn,
                onNavigateHome = onNavigateHome,
                onNavigateLogin = onNavigateLogin,
                onNavigateShop = onNavigateShop,
                onNavigateSettings = onNavigateSettings,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
