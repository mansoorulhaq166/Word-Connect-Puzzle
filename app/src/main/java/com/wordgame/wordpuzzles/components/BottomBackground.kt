package com.wordgame.wordpuzzles.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wordgame.wordpuzzles.ui.theme.WordLinkTheme

@Preview
@Composable
fun BottomBackgroundPreview() {
    WordLinkTheme {
        BottomBackground()
    }
}

@Composable
fun BottomBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "glow_animation")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "glow_pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3A8DDA).copy(alpha = glowAlpha), // Animated Deep Blue
                        Color(0xFF1565C0)  // Rich Blue
                    )
                ), shape = RoundedCornerShape(16.dp)
            )
    )
}