package com.wordgame.wordpuzzles.presentation.components.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.wordgame.wordpuzzles.R

@Preview // Use showBackground to display the background color in the preview
@Composable
fun GoodJobPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set a background color for the preview
    ) {
        // Call the GoodJob composable function here, and pass any necessary parameters
        GoodJob(modifier = Modifier.size(200.dp))
    }
}

@Composable
fun GoodJob(modifier: Modifier) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color.Transparent)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.well_done),
                contentDescription = "Level Cleared",
                contentScale = ContentScale.Fit
            )
        }
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.stars_show_letter)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = 3
        )
        LottieAnimation(composition = composition, progress = { progress })
    }
}