package com.wordgame.wordpuzzles.components

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.ads.rewarded.RewardedAd
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ads.LoadRewardedAd
import com.wordgame.wordpuzzles.ui.screens.gameview.GameScreenViewModel
import kotlinx.coroutines.delay
//
//@Composable
//@Preview
//fun ButtonWatchPreview() {
//    ButtonWatchAd()
//}

@Composable
fun ButtonWatchAd(
    modifier: Modifier = Modifier,
    showHand: Boolean = false,
    onGemsEarned: () -> Unit
) {
    var loadAd by remember {
        mutableStateOf(false)
    }
    var rewardedAd: RewardedAd? by remember {
        mutableStateOf(null)
    }
    var loadingProgress by remember {
        mutableFloatStateOf(0f)
    }
    val activity = LocalContext.current as? Activity
    val context = LocalContext.current
    val viewModel: GameScreenViewModel = hiltViewModel()

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                delayMillis = 3000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "",
    )
    Column(
        modifier = Modifier
            .then(modifier),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    rotationX = rotationAnimation,
                ),
        ) {
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        loadAd = true
                    },
                painter = painterResource(id = R.drawable.button_watch_ad),
                contentDescription = "Ad",
                contentScale = ContentScale.Fit
            )
        }
        if (showHand) {
            Box(
                modifier = Modifier
                    .size(60.dp),
            ) {
                val composition by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(R.raw.hand)
                )
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = 10
                )
                LottieAnimation(composition = composition, progress = { progress })
            }
        }
        if (loadAd) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 8.dp
            )
        }
    }
    LaunchedEffect(loadingProgress) {
        if (loadAd && loadingProgress < 1f) {
            delay(100) // Delay between progress updates
            loadingProgress += 0.05f // Gradually increase progress
        }
    }
    if (loadAd) {
        LoadRewardedAd(
            onAdLoaded = { ad ->
                activity?.let {
                    rewardedAd = ad
                    rewardedAd?.show(it) {
                        viewModel.incrementGems(3)
                        loadAd = false
                        onGemsEarned()
                    }
                }
            },
            onAdFailed = {
                Toast.makeText(context, "No Ads Available", Toast.LENGTH_SHORT).show()
                loadAd = false
            }
        ) { progress ->
            loadingProgress = progress as Float
        }
    }
}