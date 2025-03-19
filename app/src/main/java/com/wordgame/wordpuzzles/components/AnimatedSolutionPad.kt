package com.wordgame.wordpuzzles.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SolutionPadBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "glow_animation")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "glow_pulse"
    )

    val lightSkyBlue = Color(0xFF87CEEB)
    val skyBlue = Color(0xFF56A5EC)
    val deepSkyBlue = Color(0xFF0099CC)

    val woodBrown = Color(0xFFF7EAD9)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        skyBlue.copy(alpha = glowAlpha),
                        lightSkyBlue,
                        deepSkyBlue.copy(alpha = glowAlpha)
                    )
                ), shape = RoundedCornerShape(16.dp)
            )
            .blur(4.dp)
            .border(
                width = 3.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        woodBrown.copy(alpha = 0.9f),
                        woodBrown.copy(alpha = 0.5f),
                        woodBrown.copy(alpha = 0.9f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
    )
}