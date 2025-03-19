package com.wordgame.wordpuzzles.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun AnimatedWordConnectBackgroundPreview() {
    AnimatedWordConnectBackground()
}

@Composable
fun AnimatedWordConnectBackground() {
    val infiniteTransition = rememberInfiniteTransition()
    val gradientShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Tile rotation animation with acceleration/deceleration
    val tileRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Restart
        )
    )

    // Pulsating animation for tile size
    val tilePulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Line animation
    val lineProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Darker colors for the background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A237E), // Dark Indigo
                        Color(0xFF303F9F), // Indigo
                        Color(0xFF0D47A1)  // Dark Blue
                    ),
                    startY = gradientShift
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Connecting word lines with animation
            for (i in 0..4) {
                val startX = canvasWidth * (0.1f + 0.2f * i)
                val startY = canvasHeight * 0.2f
                val endX = canvasWidth * (0.5f - 0.1f * i + 0.1f * lineProgress)
                val endY = canvasHeight * (0.7f + 0.05f * i)

                drawLine(
                    color = Color(0x60ADD8E6), // Light blue with transparency
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 4f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), lineProgress * 30f)
                )
            }

            // Draw letter tiles with pulsating and rotating effects
            val tileColors = listOf(
                Color(0xFF90CAF9), // Light Blue
                Color(0xFFBA68C8), // Purple
                Color(0xFF80CBC4), // Teal
                Color(0xFFFFCC80), // Orange
                Color(0xFFFFAB91)  // Deep Orange
            )

            for (i in 0..6) {
                val offsetX = canvasWidth * (0.2f + 0.1f * i)
                val offsetY = canvasHeight * (0.3f + 0.08f * ((i + 3) % 7))
                val tileSize = Size(canvasWidth / 10f * tilePulse, canvasWidth / 10f * tilePulse)

                rotate(
                    degrees = tileRotation + i * 30f,
                    pivot = Offset(offsetX, offsetY)
                ) {
                    // Draw tile
                    drawRect(
                        color = tileColors[i % tileColors.size].copy(alpha = 0.7f),
                        topLeft = Offset(offsetX - tileSize.width/2, offsetY - tileSize.height/2),
                        size = tileSize
                    )

                    // Add border
                    drawRect(
                        color = Color.White.copy(alpha = 0.5f),
                        topLeft = Offset(offsetX - tileSize.width/2, offsetY - tileSize.height/2),
                        size = tileSize,
                        style = Stroke(width = 2f)
                    )
                }
            }

            val path = Path().apply {
                moveTo(canvasWidth * 0.2f, canvasHeight * 0.3f)
                quadraticTo(
                    canvasWidth * 0.5f, canvasHeight * (0.4f + 0.1f * lineProgress),
                    canvasWidth * 0.8f, canvasHeight * 0.3f
                )
            }

            drawPath(
                path = path,
                color = Color(0x40E3F2FD),
                style = Stroke(
                    width = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), lineProgress * 40f)
                )
            )
        }
    }
}