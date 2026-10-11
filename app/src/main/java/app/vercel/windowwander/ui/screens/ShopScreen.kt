package app.vercel.windowwander.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.R
import app.vercel.windowwander.ui.components.CartoonRoadSignCard
import app.vercel.windowwander.ui.components.GradientPillNavigationBar
import app.vercel.windowwander.ui.components.MainHomeBackground
import app.vercel.windowwander.ui.theme.GoldWinner
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.SunnyYellow

@Composable
fun ShopScreen(
    coins: Int = 1250,
    isLoggedIn: Boolean = false,
    onNavigateHome: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateShop: () -> Unit,
    onNavigateSettings: () -> Unit,
    onBackToHome: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    MainHomeBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 86.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TOP BAR WITH COINS & TITLE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🏪 Road Trip Shop",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Box(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(18.dp))
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF0F172A))
                            .border(2.dp, GoldWinner, RoundedCornerShape(18.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "🪙 $coins Coins",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = SunnyYellow
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CENTERED LOGO
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.road_trip_bingo_animated_logo),
                        contentDescription = "Road Trip Bingo Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ROAD SIGN STYLE UNDER CONSTRUCTION CARD
                CartoonRoadSignCard(
                    title = "🚧 UNDER CONSTRUCTION 🚧",
                    subtitle = "The Road Trip Shop is opening soon!",
                    icon = "🏪",
                    badgeText = "COMING SOON",
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    signColor = Color(0xFFD97706)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // SHOP PREVIEW CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MediumNavy)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎁 What's Coming to the Shop?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = SunnyYellow
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "• Custom Vehicle Avatars & Car Color Skins\n• Unique Tile Stamps & Confetti Effects\n• Exclusive Roadside Badges & Titles\n• Premium Regional Deck Unlocks",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.5.dp, SunnyYellow, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Keep playing games to earn coins!",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SunnyYellow
                            )
                        }
                    }
                }
            }

            // FLOATING GRADIENT PILL 3D NAVIGATION BAR
            GradientPillNavigationBar(
                currentScreenName = "shop",
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
