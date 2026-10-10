package app.vercel.windowwander.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import app.vercel.windowwander.ui.theme.ElectricBlue
import app.vercel.windowwander.ui.theme.ElectricBlueLight
import app.vercel.windowwander.ui.theme.GoldWinner
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.SunnyYellowLight
import app.vercel.windowwander.ui.theme.TravelTeal
import app.vercel.windowwander.ui.theme.VividOrange
import kotlin.random.Random

@Composable
fun HeroRoadTripGraphic(
    modifier: Modifier = Modifier
) {
    val cloudOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        cloudOffset.animateTo(
            targetValue = 100f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E293B),
                        Color(0xFF0F172A),
                        Color(0xFF070C18)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // SUN & SKY GLOW
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SunnyYellowLight, SunnyYellow.copy(alpha = 0.3f), Color.Transparent),
                    center = Offset(width * 0.75f, height * 0.35f),
                    radius = 120f
                ),
                center = Offset(width * 0.75f, height * 0.35f),
                radius = 120f
            )

            // MOUNTAIN SILHOUETTE
            val mountainPath = Path().apply {
                moveTo(0f, height * 0.65f)
                lineTo(width * 0.25f, height * 0.35f)
                lineTo(width * 0.45f, height * 0.55f)
                lineTo(width * 0.7f, height * 0.25f)
                lineTo(width * 0.9f, height * 0.5f)
                lineTo(width, height * 0.4f)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(
                path = mountainPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1E3A8A).copy(alpha = 0.7f), Color(0xFF0F172A))
                )
            )

            // WINDING HIGHWAY ROAD
            val roadPath = Path().apply {
                moveTo(width * 0.1f, height)
                cubicTo(
                    width * 0.3f, height * 0.75f,
                    width * 0.2f, height * 0.55f,
                    width * 0.5f, height * 0.45f
                )
                lineTo(width * 0.55f, height * 0.45f)
                cubicTo(
                    width * 0.35f, height * 0.58f,
                    width * 0.45f, height * 0.8f,
                    width * 0.35f, height
                )
                close()
            }
            drawPath(
                path = roadPath,
                color = Color(0xFF334155)
            )

            // ROAD DASHED CENTER LINE
            val linePath = Path().apply {
                moveTo(width * 0.22f, height)
                cubicTo(
                    width * 0.32f, height * 0.76f,
                    width * 0.25f, height * 0.57f,
                    width * 0.52f, height * 0.46f
                )
            }
            drawPath(
                path = linePath,
                color = SunnyYellow,
                style = Stroke(width = 4f)
            )

            // ADVENTURE RED CAR (VECTOR)
            val carX = width * 0.22f
            val carY = height * 0.72f

            // Car Shadow
            drawOval(
                color = Color.Black.copy(alpha = 0.4f),
                topLeft = Offset(carX - 25f, carY + 12f),
                size = Size(65f, 16f)
            )

            // Car Body
            drawRoundRect(
                color = VividOrange,
                topLeft = Offset(carX - 20f, carY - 8f),
                size = Size(55f, 22f),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Car Cabin Roof
            drawRoundRect(
                color = Color(0xFFC2410C),
                topLeft = Offset(carX - 10f, carY - 20f),
                size = Size(35f, 15f),
                cornerRadius = CornerRadius(6f, 6f)
            )

            // Windows
            drawRoundRect(
                color = Color(0xFFBAE6FD),
                topLeft = Offset(carX - 6f, carY - 17f),
                size = Size(12f, 9f),
                cornerRadius = CornerRadius(2f, 2f)
            )
            drawRoundRect(
                color = Color(0xFFBAE6FD),
                topLeft = Offset(carX + 8f, carY - 17f),
                size = Size(12f, 9f),
                cornerRadius = CornerRadius(2f, 2f)
            )

            // Wheels
            drawCircle(
                color = Color(0xFF020617),
                radius = 7f,
                center = Offset(carX - 8f, carY + 14f)
            )
            drawCircle(
                color = Color(0xFF020617),
                radius = 7f,
                center = Offset(carX + 22f, carY + 14f)
            )

            // LUGGAGE ON ROOF
            drawRoundRect(
                color = SunnyYellow,
                topLeft = Offset(carX - 4f, carY - 28f),
                size = Size(22f, 8f),
                cornerRadius = CornerRadius(3f, 3f)
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
    Canvas(modifier = modifier.size(24.dp)) {
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
                // STAR
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
        val colors = listOf(SunnyYellow, VividOrange, TravelTeal, ElectricBlue, GoldWinner, Color.White)

        for (i in 0..60) {
            val x = random.nextFloat() * size.width
            val y = random.nextFloat() * size.height
            val color = colors[random.nextInt(colors.size)]
            val radius = random.nextFloat() * 6f + 3f

            drawCircle(
                color = color.copy(alpha = random.nextFloat() * 0.8f + 0.2f),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}
