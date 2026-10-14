package app.vercel.windowwander.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.R
import app.vercel.windowwander.model.Region
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.GoldWinner
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.SunnyYellowLight
import app.vercel.windowwander.ui.theme.VividOrange
import kotlin.random.Random

// SHINY 3D GRADIENT PILL-SHAPED FLOATING NAVIGATION BAR (SET 6)
@Composable
fun GradientPillNavigationBar(
    currentScreenName: String = "home",
    isLoggedIn: Boolean = false,
    onNavigateHome: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateShop: () -> Unit,
    onNavigateSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .shadow(20.dp, RoundedCornerShape(40.dp))
            .clip(RoundedCornerShape(40.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF0F52BA), // Royal Blue
                        Color(0xFF1E824C), // Emerald Green
                        Color(0xFF512E5F)  // Deep Purple
                    )
                )
            )
            .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(40.dp))
            .padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. HOME
            PillNavItem(
                iconRes = R.drawable.nav_home_icon,
                label = "Home",
                isSelected = currentScreenName == "home",
                iconSize = 60,
                onClick = onNavigateHome,
                modifier = Modifier.weight(1f)
            )

            // DIVIDER 1
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(42.dp)
                    .background(Color.White.copy(alpha = 0.25f))
            )

            // 2. LOG IN / LOG OUT
            PillNavItem(
                iconRes = R.drawable.nav_login_icon,
                label = if (isLoggedIn) "Log Out" else "Log In",
                isSelected = currentScreenName == "login" || currentScreenName == "logout",
                iconSize = 52,
                onClick = onNavigateLogin,
                modifier = Modifier.weight(1f)
            )

            // DIVIDER 2
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(42.dp)
                    .background(Color.White.copy(alpha = 0.25f))
            )

            // 3. SHOP
            PillNavItem(
                iconRes = R.drawable.nav_shop_icon,
                label = "Shop",
                isSelected = currentScreenName == "shop",
                iconSize = 52,
                onClick = onNavigateShop,
                modifier = Modifier.weight(1f)
            )

            // DIVIDER 3
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(42.dp)
                    .background(Color.White.copy(alpha = 0.25f))
            )

            // 4. SETTINGS
            PillNavItem(
                iconRes = R.drawable.nav_settings_icon,
                label = "Settings",
                isSelected = currentScreenName == "settings",
                iconSize = 52,
                onClick = onNavigateSettings,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PillNavItem(
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    iconSize: Int = 52,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 2.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(60.dp)
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF38BDF8).copy(alpha = 0.85f),
                                    Color(0xFF0284C7).copy(alpha = 0.45f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(iconSize.dp)
            )
        }

        Spacer(modifier = Modifier.height(1.dp))

        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Black,
            style = TextStyle(
                color = Color.White,
                shadow = if (isSelected) Shadow(
                    color = Color(0xFF38BDF8),
                    offset = Offset(0f, 0f),
                    blurRadius = 16f
                ) else null
            )
        )
    }
}

// MAIN LOBBY FULL SCREEN ARTWORK BACKGROUND
@Composable
fun MainHomeBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_main_home),
            contentDescription = "Main Home Lobby Background Artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.15f))
        )

        content()
    }
}

// DECK-AWARE FULL SCREEN ARTWORK BACKGROUND FOR GAMEPLAY SCREENS
@Composable
fun ScenicHighwayBackground(
    deckId: String = "deck_c",
    region: Region = Region.UNIVERSAL,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val bgResId = when {
        deckId == "deck_a" -> R.drawable.bg_classic_roadtrip
        deckId == "deck_b" -> R.drawable.bg_weird_roadtrip
        deckId == "deck_c" || region == Region.MOUNTAIN_WEST -> R.drawable.bg_rocky_mountains
        deckId == "deck_d" || region == Region.SOUTHWEST -> R.drawable.bg_desert_southwest
        deckId == "deck_e" || region == Region.NORTHEAST -> R.drawable.bg_northeast
        deckId == "deck_f" || region == Region.SOUTHEAST || region == Region.TEXAS_SOUTH_CENTRAL -> R.drawable.bg_south
        deckId == "deck_g" || region == Region.PACIFIC_SOUTHWEST || region == Region.PACIFIC_NORTHWEST -> R.drawable.bg_west_coast
        deckId == "deck_h" || region == Region.MIDWEST -> R.drawable.bg_heartland
        else -> R.drawable.bg_classic_roadtrip
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = bgResId),
            contentDescription = "Scenic Deck Background Artwork",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.15f))
        )

        content()
    }
}

// ARCADE HUD STATUS BAR (NAME, LEVEL & TITLE ON ONE SINGLE LINE)
@Composable
fun ArcadeHudBar(
    nickname: String = "Road Explorer",
    level: Int = 3,
    xp: Int = 250,
    onOpenStats: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // USER NICKNAME ON LINE 1, LEVEL & TITLE ON LINE 2 BELOW USER NAME
        Box(
            modifier = Modifier
                .shadow(10.dp, RoundedCornerShape(22.dp))
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF0F172A))
                .border(2.5.dp, SunnyYellow, RoundedCornerShape(22.dp))
                .clickable { onOpenStats() }
                .padding(horizontal = 16.dp, vertical = 7.dp)
        ) {
            Column {
                Text(
                    text = nickname,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Lvl $level Explorer",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SunnyYellow
                )
            }
        }

        // XP TOTAL PILL
        Box(
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(18.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF0F172A))
                .border(2.dp, SunnyYellow, RoundedCornerShape(18.dp))
                .clickable { onOpenStats() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⭐", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$xp XP",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = SunnyYellowLight
                )
            }
        }
    }
}

// MOCKUP MATCHING CREAM/GOLD CAPSULE BUTTON
@Composable
fun BeveledGameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = Color(0xFFFFFBEB),
    bevelColor: Color = Color(0xFFD97706),
    textColor: Color = DeepNavy,
    fontSize: Int = 22
) {
    Box(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = SunnyYellow)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF0F172A))
            .clickable { onClick() }
            .padding(bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(buttonColor, buttonColor.copy(alpha = 0.9f))
                    )
                )
                .border(3.dp, Color(0xFF0F172A), RoundedCornerShape(30.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Black,
                color = textColor,
                letterSpacing = 1.sp
            )
        }
    }
}

// CARTOON HIGHWAY MILEAGE ROAD SIGN CARD
@Composable
fun CartoonRoadSignCard(
    title: String,
    subtitle: String,
    icon: String,
    badgeText: String? = null,
    titleFontSize: Float = 18f,
    subtitleFontSize: Float = 12.5f,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    signColor: Color = Color(0xFF15803D)
) {
    Box(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF0F172A))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.verticalGradient(listOf(signColor, signColor.copy(alpha = 0.75f)))
                )
                .border(2.5.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(22.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // TOP LEFT: TITLE & SUBTITLE
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top
                ) {
                    if (icon.isNotEmpty()) {
                        Text(text = icon, fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                    }

                    Text(
                        text = title,
                        fontSize = titleFontSize.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = subtitleFontSize.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                // TOP RIGHT BADGE
                if (!badgeText.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(VividOrange)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// B I N G O GLOSSY 3D GEL BUTTON HEADERS
@Composable
fun BingoColumnHeaders(
    modifier: Modifier = Modifier
) {
    val headers = listOf(
        Pair("B", listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8), Color(0xFF1E3A8A))), // Blue Gel
        Pair("I", listOf(Color(0xFFEF4444), Color(0xFFDC2626), Color(0xFF991B1B))), // Red Gel
        Pair("N", listOf(Color(0xFFA855F7), Color(0xFF7C3AED), Color(0xFF581C87))), // Purple Gel
        Pair("G", listOf(Color(0xFF10B981), Color(0xFF059669), Color(0xFF064E3B))), // Green Gel
        Pair("O", listOf(Color(0xFFF97316), Color(0xFFD97706), Color(0xFF78350F)))  // Orange Gel
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        headers.forEach { (char, gradientColors) ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.verticalGradient(gradientColors))
                    .border(2.dp, Color.White.copy(alpha = 0.65f), RoundedCornerShape(16.dp))
            ) {
                // TOP CRESCENT GLOSS REFLECTION HIGHLIGHT
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .padding(horizontal = 3.dp, vertical = 2.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.White.copy(alpha = 0.68f), Color.White.copy(alpha = 0.05f))
                            )
                        )
                )

                // BINGO LETTER
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.4f),
                                offset = Offset(0f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                }
            }
        }
    }
}

// CATEGORY ICON DRAWING
@Composable
fun CategoryIconGraphic(
    category: String,
    modifier: Modifier = Modifier,
    tint: Color = SunnyYellow
) {
    Canvas(modifier = modifier.size(22.dp)) {
        val w = size.width
        val h = size.height

        when (category.lowercase()) {
            "mountain", "rockies" -> {
                val path = Path().apply {
                    moveTo(w * 0.1f, h * 0.85f)
                    lineTo(w * 0.5f, h * 0.15f)
                    lineTo(w * 0.9f, h * 0.85f)
                    close()
                }
                drawPath(path, color = tint)
            }
            "desert", "cactus" -> {
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.42f, h * 0.1f),
                    size = Size(w * 0.16f, h * 0.8f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                drawRoundRect(
                    color = tint,
                    topLeft = Offset(w * 0.15f, h * 0.35f),
                    size = Size(w * 0.7f, h * 0.14f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
            "tree", "nature" -> {
                val path = Path().apply {
                    moveTo(w * 0.5f, h * 0.1f)
                    lineTo(w * 0.15f, h * 0.7f)
                    lineTo(w * 0.85f, h * 0.7f)
                    close()
                }
                drawPath(path, color = tint)
                drawRect(
                    color = tint,
                    topLeft = Offset(w * 0.42f, h * 0.7f),
                    size = Size(w * 0.16f, h * 0.2f)
                )
            }
            else -> {
                drawCircle(color = tint, radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.5f))
            }
        }
    }
}

// CONFETTI PARTICLES
@Composable
fun ConfettiCelebrationGraphic(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val random = Random(42)
        val colors = listOf(SunnyYellow, VividOrange, Color(0xFF0D9488), Color(0xFF2563EB), GoldWinner, Color.White)

        for (i in 0..70) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height
            val color = colors[random.nextInt(colors.size)]
            val radius = random.nextFloat() * 7f + 3f

            drawCircle(
                color = color.copy(alpha = random.nextFloat() * 0.85f + 0.15f),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}
