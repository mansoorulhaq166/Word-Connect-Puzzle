package com.wordgame.wordpuzzles.presentation.components.main

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordgame.wordpuzzles.presentation.theme.krona
import kotlinx.coroutines.delay

@Composable
fun ButtonPlayMain(text: String, onClick: () -> Unit) {
    var isLoading by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    val infiniteTransition = rememberInfiniteTransition(label = "button_animation")

    val primaryColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF6A11CB), // Dark Purple
        targetValue = Color(0xFF2575FC), // Blue
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ), label = "primary_gradient_animation"
    )

    val secondaryColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFFF6B6B), // Soft Red
        targetValue = Color(0xFF45A247), // Greenish
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ), label = "secondary_gradient_animation"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scale_animation"
    )

    var isPressed by remember { mutableStateOf(false) }
    val pressedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(150, easing = EaseOutCubic),
        label = "press_animation"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed) 5f else 20f,
        animationSpec = tween(150),
        label = "shadow_animation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(220.dp)
            .height(60.dp)
            .scale(scale * pressedScale)
            .shadow(
                elevation = shadowElevation.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = secondaryColor
            )
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint()
                    val frameworkPaint = paint.asFrameworkPaint()
                    frameworkPaint.color = secondaryColor.copy(alpha = 0.4f).toArgb()
                    frameworkPaint.setShadowLayer(
                        20f,
                        0f,
                        0f,
                        secondaryColor.copy(alpha = 0.6f).toArgb()
                    )
                    canvas.drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        28.dp.toPx(),
                        28.dp.toPx(),
                        paint
                    )
                }
            }
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(primaryColor, secondaryColor)
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .border(
                width = 3.dp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.White.copy(alpha = 0.8f), secondaryColor)
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    isPressed = true
                    isLoading = true
                    onClick()
                }
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(28.dp),
                strokeWidth = 3.dp
            )
        } else {
            Text(
                text = text.uppercase(),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = krona,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(1f, 1f),
                        blurRadius = 3f
                    )
                )
            )
        }
    }

    // Reset pressed state and loading simulation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(200)
            isPressed = false
        }
    }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(2000) // Simulate async operation
            isLoading = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonPlayMain() {
    ButtonPlayMain(text = "Play", onClick = {})
}