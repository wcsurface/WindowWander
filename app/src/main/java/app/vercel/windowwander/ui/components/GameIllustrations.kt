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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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

// 3D BEVELLED PLAY BUTTON COMPONENT
@Composable
fun BeveledGameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = SunnyYellow,
    bevelColor: Color = Color(0xFFD97706),
    textColor: Color = DeepNavy,
    fontSize: Int = 18
) {
    Box(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bevelColor)
            .clickable { onClick() }
            .padding(bottom = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(buttonColor, buttonColor.copy(alpha = 0.85f))
                    )
                )
                .border(1.5.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
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

// B I N G O 3D COLUMN HEADERS
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
                    .height(36.dp)
                    .shadow(6.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(color)
                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = char,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    }
}

// SCENIC ROAD TRIP GAME WORLD SCENE
@Composable
fun HeroRoadTripGraphic(
    modifier: Modifier = Modifier
) {
    val carAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        carAnim.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .shadow(16.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0284C7), // Sky Blue
                        Color(0xFF38BDF8), // Light Sky
                        Color(0xFF0284C7), // Horizon
                        Color(0xFF0F172A)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // SUN GLOW
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SunnyYellowLight, SunnyYellow.copy(alpha = 0.4f), Color.Transparent),
                    center = Offset(w * 0.78f, h * 0.28f),
                    radius = 110f
                ),
                center = Offset(w * 0.78f, h * 0.28f),
                radius = 110f
            )

            // MOUNTAINS & HILLS
            val mountainPath = Path().apply {
                moveTo(0f, h * 0.6f)
                lineTo(w * 0.25f, h * 0.3f)
                lineTo(w * 0.5f, h * 0.55f)
                lineTo(w * 0.75f, h * 0.22f)
                lineTo(w, h * 0.5f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = mountainPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E3A8A), Color(0xFF0F172A))
                )
            )

            // WINDING HIGHWAY ROAD
            val roadPath = Path().apply {
                moveTo(w * 0.12f, h)
                cubicTo(
                    w * 0.35f, h * 0.72f,
                    w * 0.22f, h * 0.52f,
                    w * 0.52f, h * 0.42f
                )
                lineTo(w * 0.58f, h * 0.42f)
                cubicTo(
                    w * 0.38f, h * 0.56f,
                    w * 0.52f, h * 0.8f,
                    w * 0.42f, h
                )
                close()
            }
            drawPath(
                path = roadPath,
                color = Color(0xFF334155)
            )

            // ROAD DASHED CENTER LINE
            val linePath = Path().apply {
                moveTo(w * 0.26f, h)
                cubicTo(
                    w * 0.38f, h * 0.74f,
                    w * 0.28f, h * 0.55f,
                    w * 0.55f, h * 0.43f
                )
            }
            drawPath(
                path = linePath,
                color = SunnyYellow,
                style = Stroke(width = 5f)
            )

            // ADVENTURE CAR WITH LUGGAGE
            val carX = w * (0.24f + carAnim.value * 0.08f)
            val carY = h * 0.74f

            // Car Shadow
            drawOval(
                color = Color.Black.copy(alpha = 0.45f),
                topLeft = Offset(carX - 30f, carY + 14f),
                size = Size(75f, 18f)
            )

            // Car Body
            drawRoundRect(
                color = VividOrange,
                topLeft = Offset(carX - 22f, carY - 8f),
                size = Size(64f, 24f),
                cornerRadius = CornerRadius(10f, 10f)
            )

            // Cabin Roof
            drawRoundRect(
                color = Color(0xFFC2410C),
                topLeft = Offset(carX - 10f, carY - 22f),
                size = Size(40f, 16f),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Windows
            drawRoundRect(
                color = Color(0xFFBAE6FD),
                topLeft = Offset(carX - 5f, carY - 18f),
                size = Size(14f, 11f),
                cornerRadius = CornerRadius(3f, 3f)
            )
            drawRoundRect(
                color = Color(0xFFBAE6FD),
                topLeft = Offset(carX + 11f, carY - 18f),
                size = Size(14f, 11f),
                cornerRadius = CornerRadius(3f, 3f)
            )

            // Wheels
            drawCircle(color = Color(0xFF020617), radius = 8f, center = Offset(carX - 8f, carY + 16f))
            drawCircle(color = Color(0xFF020617), radius = 8f, center = Offset(carX + 28f, carY + 16f))

            // Luggage
            drawRoundRect(
                color = SunnyYellow,
                topLeft = Offset(carX - 4f, carY - 32f),
                size = Size(26f, 10f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}

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
