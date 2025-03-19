package com.wordgame.wordpuzzles.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wordgame.wordpuzzles.models.GameLetter
import com.wordgame.wordpuzzles.ui.theme.WordLinkTheme

@Preview
@Composable
fun GameLetterItemPreview() {
    WordLinkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val dummyGameLetter = GameLetter(label = "A")
            GameLetterItem(
                modifier = Modifier
                    .padding(16.dp)
                    .size(100.dp),
                letter = dummyGameLetter
            )
        }
    }
}

@Composable
fun GameLetterItem(
    modifier: Modifier = Modifier,
    letter: GameLetter,
    onClick: () -> Unit = {},
    onOffsetObtained: (offset: Offset) -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }
    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(150, easing = FastOutSlowInEasing), label = ""
    )
    val rotationAnim by animateFloatAsState(
        targetValue = if (isPressed) 15f else 0f,
        animationSpec = tween(150, easing = FastOutSlowInEasing), label = ""
    )
    val elevationAnim by animateFloatAsState(
        targetValue = if (isPressed) 2f else 8f,
        animationSpec = tween(150), label = ""
    )

    Box(
        modifier = Modifier
            .then(modifier)
            .shadow(elevationAnim.dp, CircleShape)
            .graphicsLayer {
                scaleX = scaleAnim
                scaleY = scaleAnim
                rotationZ = rotationAnim
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
            .onGloballyPositioned {
                onOffsetObtained(
                    Offset(
                        it.positionInRoot().x,
                        it.positionInRoot().y
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF4A90E2), Color(0xFF145DA0))
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            val scaleSolution by animateFloatAsState(
                targetValue = if (letter.isvisible) 1f else 0f,
                animationSpec = tween(300, easing = FastOutSlowInEasing), label = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scaleSolution)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFFFA726), Color(0xFFE65100))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter.label,
                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                )
            }
        }
    }
}