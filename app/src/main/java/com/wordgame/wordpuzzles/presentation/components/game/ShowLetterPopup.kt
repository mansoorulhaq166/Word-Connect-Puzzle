package com.wordgame.wordpuzzles.presentation.components.game

import androidx.compose.animation.core.Animatable

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.wordgame.wordpuzzles.R

@Preview
@Composable
fun ShowLetterPopupPreview() {
    val hintOffset = Offset(100f, 100f) // Replace with the desired hint offset
    val letterOffset = Offset(200f, 200f) // Replace with the desired letter offset

    ShowLetterPopup(hintOffset = hintOffset, letterOffset = letterOffset)
}

@Composable
fun ShowLetterPopup(
    hintOffset: Offset, letterOffset: Offset,
) {
    val SPEED = 600
    val x = remember { Animatable(hintOffset.x) }
    val y = remember { Animatable(hintOffset.y) }
    LaunchedEffect(letterOffset) {
        x.animateTo(
            targetValue = letterOffset.x,
            animationSpec = tween(durationMillis = SPEED)
        )
    }
    LaunchedEffect(letterOffset) {
        y.animateTo(
            targetValue = letterOffset.y,
            animationSpec = tween(durationMillis = SPEED)
        )
    }
    Box(
        modifier = Modifier
            .size(60.dp)
            .offset {
                IntOffset(x.value.toInt(), y.value.toInt())
            },
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.star)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = 3
        )
        LottieAnimation(composition = composition, progress = { progress })
    }
}