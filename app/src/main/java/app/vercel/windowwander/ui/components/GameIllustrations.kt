package app.vercel.windowwander.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.ui.theme.DeepNavy
import app.vercel.windowwander.ui.theme.GoldWinner
import app.vercel.windowwander.ui.theme.GoldWinnerLight
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.SunnyYellowLight
import app.vercel.windowwander.ui.theme.VividOrange
import kotlin.random.Random

// FULL CARTOON ROAD TRIP GAME WORLD BACKGROUND
@Composable
fun ScenicHighwayBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val cloudAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        cloudAnim.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(12000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. VIBRANT SKY GRADIENT (Bright Blue Summer Sky)
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0284C7), // Vibrant Sky Blue
                        Color(0xFF38BDF8), // Light Cyan Horizon
                        Color(0xFFBAE6FD)
                    )
                )
            )

            // 2. SUN GLOW & RAYS
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SunnyYellowLight, SunnyYellow.copy(alpha = 0.5f), Color.Transparent),
                    center = Offset(w * 0.82f, h * 0.12f),
                    radius = 200f
                ),
                center = Offset(w * 0.82f, h * 0.12f),
                radius = 200f
            )

            // 3. PUFFY CARTOON CLOUDS
            val cloudX1 = w * (0.05f + cloudAnim.value * 0.12f)
            val cloudX2 = w * (0.65f - cloudAnim.value * 0.08f)

            drawCircle(color = Color.White.copy(alpha = 0.9f), radius = 32f, center = Offset(cloudX1, h * 0.11f))
            drawCircle(color = Color.White.copy(alpha = 0.9f), radius = 42f, center = Offset(cloudX1 + 28f, h * 0.1f))
            drawCircle(color = Color.White.copy(alpha = 0.9f), radius = 32f, center = Offset(cloudX1 + 56f, h * 0.11f))

            drawCircle(color = Color.White.copy(alpha = 0.9f), radius = 28f, center = Offset(cloudX2, h * 0.16f))
            drawCircle(color = Color.White.copy(alpha = 0.9f), radius = 36f, center = Offset(cloudX2 + 22f, h * 0.15f))

            // 4. DISTANT BLUE MOUNTAINS
            val mtnPath = Path().apply {
                moveTo(0f, h * 0.42f)
                lineTo(w * 0.22f, h * 0.28f)
                lineTo(w * 0.45f, h * 0.38f)
                lineTo(w * 0.72f, h * 0.22f)
                lineTo(w * 0.92f, h * 0.35f)
                lineTo(w, h * 0.3f)
                lineTo(w, h * 0.55f)
                lineTo(0f, h * 0.55f)
                close()
            }
            drawPath(mtnPath, color = Color(0xFF1E3A8A).copy(alpha = 0.65f))

            // 5. BRIGHT GREEN ROLLING HILLS
            val hillPath = Path().apply {
                moveTo(0f, h * 0.48f)
                cubicTo(w * 0.35f, h * 0.42f, w * 0.65f, h * 0.52f, w, h * 0.45f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hillPath, color = Color(0xFF16A34A)) // Vibrant Grass Green

            // 6. ASPHALT HIGHWAY WINDING TO HORIZON
            val roadPath = Path().apply {
                moveTo(w * 0.05f, h)
                cubicTo(
                    w * 0.42f, h * 0.72f,
                    w * 0.28f, h * 0.55f,
                    w * 0.52f, h * 0.45f
                )
                lineTo(w * 0.58f, h * 0.45f)
                cubicTo(
                    w * 0.38f, h * 0.6f,
                    w * 0.55f, h * 0.8f,
                    w * 0.45f, h
                )
                close()
            }
            drawPath(roadPath, color = Color(0xFF334155))

            // 7. YELLOW DASHED CENTER ROAD LINE
            val dashPath = Path().apply {
                moveTo(w * 0.25f, h)
                cubicTo(
                    w * 0.42f, h * 0.74f,
                    w * 0.32f, h * 0.58f,
                    w * 0.55f, h * 0.46f
                )
            }
            drawPath(
                path = dashPath,
                color = SunnyYellow,
                style = Stroke(width = 6f)
            )
        }

        content()
    }
}

// ARCADE HUD STATUS BAR (COINS, XP, ENERGY, LEVEL)
@Composable
fun ArcadeHudBar(
    nickname: String,
    level: Int,
    xp: Int = 0,
    coins: Int = 1250,
    onOpenStats: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // AVATAR & LEVEL PILL
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .shadow(10.dp, RoundedCornerShape(22.dp))
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF0F172A))
                .border(2.5.dp, SunnyYellow, RoundedCornerShape(22.dp))
                .clickable { onOpenStats() }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(SunnyYellow, VividOrange))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐻", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = nickname,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Lvl $level Explorer",
                    fontSize = 10.sp,
                    color = SunnyYellowLight,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // CURRENCY & SETTINGS PILLS
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // COINS PILL
            Box(
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF0F172A))
                    .border(2.dp, GoldWinner, RoundedCornerShape(18.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🪙", fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$coins",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldWinnerLight
                    )
                }
            }

            // SETTINGS BUTTON
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFF0F172A))
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                    .clickable { onOpenSettings() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⚙️", fontSize = 18.sp)
            }
        }
    }
}

// 3D GLOSSY BEVELED CARTOON BUTTON
@Composable
fun BeveledGameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = SunnyYellow,
    bevelColor: Color = Color(0xFFD97706),
    textColor: Color = DeepNavy,
    fontSize: Int = 22
) {
    Box(
        modifier = modifier
            .shadow(20.dp, RoundedCornerShape(22.dp), spotColor = buttonColor)
            .clip(RoundedCornerShape(22.dp))
            .background(bevelColor)
            .clickable { onClick() }
            .padding(bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(buttonColor, buttonColor.copy(alpha = 0.88f))
                    )
                )
                .border(2.5.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
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
    badgeText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    signColor: Color = Color(0xFF15803D) // Highway Green
) {
    Box(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF064E3B))
            .clickable { onClick() }
            .padding(bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(listOf(signColor, Color(0xFF047857)))
                )
                .border(2.5.dp, Color.White, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = icon, fontSize = 28.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SunnyYellow)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = DeepNavy
                        )
                    }
                }

                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

// B I N G O 3D COLORED HEADERS
@Composable
fun BingoColumnHeaders(
    modifier: Modifier = Modifier
) {
    val headers = listOf(
        Pair("B", Color(0xFF2563EB)), // Electric Blue
        Pair("I", Color(0xFFDC2626)), // Crimson Red
        Pair("N", Color(0xFF7C3AED)), // Vivid Purple
        Pair("G", Color(0xFF059669)), // Emerald Green
        Pair("O", Color(0xFFD97706))  // Amber Gold
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        headers.forEach { (char, color) ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .shadow(10.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(color)
                    .border(2.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    }
}

// HERO SCENE
@Composable
fun HeroRoadTripGraphic(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .shadow(16.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0284C7),
                        Color(0xFF38BDF8),
                        Color(0xFF0F172A)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SunnyYellowLight, SunnyYellow.copy(alpha = 0.4f), Color.Transparent),
                    center = Offset(w * 0.78f, h * 0.28f),
                    radius = 110f
                ),
                center = Offset(w * 0.78f, h * 0.28f),
                radius = 110f
            )

            val mtnPath = Path().apply {
                moveTo(0f, h * 0.6f)
                lineTo(w * 0.25f, h * 0.3f)
                lineTo(w * 0.5f, h * 0.55f)
                lineTo(w * 0.75f, h * 0.22f)
                lineTo(w, h * 0.5f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(mtnPath, color = Color(0xFF1E3A8A))

            val carX = w * 0.28f
            val carY = h * 0.74f

            drawOval(color = Color.Black.copy(alpha = 0.45f), topLeft = Offset(carX - 30f, carY + 14f), size = Size(75f, 18f))
            drawRoundRect(color = VividOrange, topLeft = Offset(carX - 22f, carY - 8f), size = Size(64f, 24f), cornerRadius = CornerRadius(10f, 10f))
            drawRoundRect(color = Color(0xFFC2410C), topLeft = Offset(carX - 10f, carY - 22f), size = Size(40f, 16f), cornerRadius = CornerRadius(8f, 8f))
            drawCircle(color = Color(0xFF020617), radius = 8f, center = Offset(carX - 8f, carY + 16f))
            drawCircle(color = Color(0xFF020617), radius = 8f, center = Offset(carX + 28f, carY + 16f))
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
