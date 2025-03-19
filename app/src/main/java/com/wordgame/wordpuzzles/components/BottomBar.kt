package com.wordgame.wordpuzzles.components

import android.graphics.BlurMaskFilter
import android.util.Log
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.utils.GemShopManager
import com.wordgame.wordpuzzles.utils.TAG

/*
@Composable
@Preview
fun BottomBarPreview() {
    BottomBar(
        0,
        onHintClicked = {},
        onShuffleClicked = {}
    )
}*/

@Composable
fun BottomBar(
    availableHints: Int,
    onHintClicked: (offset: Offset) -> Unit = {},
    onShuffleClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val modifier = Modifier
            .padding(4.dp)
            .size(48.dp)
        // "Hint" button with available hints count

        Box(
            modifier = Modifier
                .padding(4.dp)
        ) {
            ButtonRound(
                modifier = modifier,
                default = R.drawable.hint_default,
                variant = R.drawable.hint_variant
            ) { offset ->
                onHintClicked(offset)
            }
            Badge(
                modifier = Modifier.align(Alignment.TopEnd),
                badgeColor = Color(0xFF2196F3),  // Blue
                count = availableHints,
            )
        }
        ButtonRound(
            modifier = modifier,
            default = R.drawable.shuffle_default,
            variant = R.drawable.shuffle_variant
        ) {
            onShuffleClicked()
        }
    }
}

@Composable
fun Badge(
    modifier: Modifier = Modifier,
    count: Int,
    shape: Shape = CircleShape,
    badgeColor: Color = Color(0xFFFF3D00),
    textColor: Color = Color.White,
    showAnimation: Boolean = true
) {
    val transition = rememberInfiniteTransition(label = "badge_animation")
    val scale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ), label = "badge_scale"
    )

    val glowAlpha by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "badge_glow"
    )

    val rotation by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "badge_rotation"
    )

    val glowColor = remember(badgeColor) {
        badgeColor.copy(red = minOf(1f, badgeColor.red * 1.2f),
            green = minOf(1f, badgeColor.green * 1.2f),
            blue = minOf(1f, badgeColor.blue * 1.2f))
    }

    val shadowColor = remember(badgeColor) {
        badgeColor.copy(alpha = 0.6f)
    }

    Box(
        modifier = modifier
            .size(24.dp)
            .graphicsLayer {
                if (showAnimation) {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                }
                shadowElevation = 8f
                ambientShadowColor = shadowColor
                spotShadowColor = glowColor.copy(alpha = if (showAnimation) glowAlpha else 0.6f)
            }
            .border(
                width = 1.5.dp,
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.9f),
                        Color.White.copy(alpha = 0.2f)
                    )
                ),
                shape = shape
            )
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        badgeColor.copy(alpha = 0.95f),    // Vibrant core
                        badgeColor.adjustBrightness(0.7f), // Slightly darker edge
                        badgeColor.adjustBrightness(0.5f)  // Even darker outer edge
                    ),
                    radius = 25f                          // Tighter gradient for more depth
                ),
                shape = shape
            )
            .then(
                Modifier.drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint()
                        paint.asFrameworkPaint().apply {
                            isAntiAlias = true
                            style = android.graphics.Paint.Style.FILL
                            setShadowLayer(
                                8f,
                                0f,
                                0f,
                                glowColor
                                    .copy(alpha = if (showAnimation) glowAlpha * 0.8f else 0.5f)
                                    .toArgb()
                            )
                        }

                        val frameworkPaint = paint.asFrameworkPaint()
                        frameworkPaint.maskFilter =
                            BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)

                        val radius = size.minDimension / 2
                        canvas.drawCircle(
                            Offset(size.width / 2, size.height / 2),
                            radius * 0.8f,
                            paint
                        )
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (count > 99) "99+" else count.toString(),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color(0xFF0D47A1).copy(alpha = 0.4f),  // Deep blue shadow
                    offset = Offset(0f, 1f),
                    blurRadius = 2f
                )
            ),
            maxLines = 1
        )
    }
}

fun Color.adjustBrightness(factor: Float): Color {
    return copy(
        red = red * factor,
        green = green * factor,
        blue = blue * factor
    )
}