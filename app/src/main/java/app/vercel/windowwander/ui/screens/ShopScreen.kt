package app.vercel.windowwander.ui.screens

import android.os.Build
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.R
import app.vercel.windowwander.data.DeckRepository
import app.vercel.windowwander.model.Deck
import app.vercel.windowwander.ui.components.BeveledGameButton
import app.vercel.windowwander.ui.components.GradientPillNavigationBar
import app.vercel.windowwander.ui.components.MainHomeBackground
import app.vercel.windowwander.ui.theme.CompletedGreen
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.GoldWinner
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.SunnyYellowLight
import app.vercel.windowwander.ui.theme.VividOrange
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

@Composable
fun ShopScreen(
    xp: Int = 250,
    unlockedDeckIds: Set<String> = emptySet(),
    isLoggedIn: Boolean = false,
    onUnlockDeck: (String) -> Unit = {},
    onSelectDeckAndPlay: (Deck) -> Unit = {},
    onNavigateHome: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateShop: () -> Unit,
    onNavigateSettings: () -> Unit,
    onBackToHome: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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

    val holidayDeck = DeckRepository.DECKS.find { it.id == "deck_holiday" }
    val isHolidayUnlocked = "deck_holiday" in unlockedDeckIds

    MainHomeBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(start = 16.dp, end = 16.dp, top = 26.dp, bottom = 104.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TOP BAR WITH XP & TITLE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🏪 Shop",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Box(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(18.dp))
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF0F172A))
                            .border(2.dp, SunnyYellow, RoundedCornerShape(18.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "⭐ $xp XP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = SunnyYellowLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CENTERED ANIMATED GIF LOGO
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
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

                Spacer(modifier = Modifier.height(20.dp))

                // FEATURED ITEM: HOLIDAY ROAD TRIP DECK ($0.99)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(2.5.dp, SunnyYellow)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🎄", fontSize = 32.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Holiday Road Trip Deck",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "30 Festive holiday sights & winter trees",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isHolidayUnlocked) CompletedGreen else VividOrange)
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (isHolidayUnlocked) "UNLOCKED" else "$0.99",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isHolidayUnlocked) {
                            BeveledGameButton(
                                text = "✓ UNLOCKED & READY TO PLAY ▶",
                                onClick = {
                                    holidayDeck?.let { onSelectDeckAndPlay(it) }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                buttonColor = CompletedGreen,
                                bevelColor = Color(0xFF047857),
                                textColor = Color.White,
                                fontSize = 15
                            )
                        } else {
                            BeveledGameButton(
                                text = "🛒 UNLOCK DECK ($0.99) ▶",
                                onClick = { onUnlockDeck("deck_holiday") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                buttonColor = SunnyYellow,
                                bevelColor = GoldWinner,
                                textColor = DeepNavy,
                                fontSize = 15
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
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
