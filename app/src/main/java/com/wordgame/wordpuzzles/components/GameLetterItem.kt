package com.wordgame.wordpuzzles.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.models.GameLetter

/*
@Preview
@Composable
fun GameLetterItemPreview() {
    WordLinkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Dummy GameLetter data for preview
            val dummyGameLetter = GameLetter(label = "A")
            GameLetterItem(
                modifier = Modifier
                    .padding(16.dp)
                    .size(100.dp),
                letter = dummyGameLetter
            )
        }
    }
}*/

@Composable
fun GameLetterItem(
    modifier: Modifier = Modifier,
    letter: GameLetter,
    onClick: () -> Unit = {},
    onOffsetObtained: (offset: Offset) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onClick() // Call the onClick lambda on tap
                }
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
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.key_solution_default),
            contentDescription = "Background",
            contentScale = ContentScale.Fit
        )
        val scaleSolution by animateFloatAsState(
            targetValue = if (letter.isvisible) 1f else 0f,
            animationSpec = tween(200), label = ""
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scaleSolution),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.key_solution_variant),
                contentDescription = "Background",
                contentScale = ContentScale.Fit
            )
            Text(
                text = letter.label,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}