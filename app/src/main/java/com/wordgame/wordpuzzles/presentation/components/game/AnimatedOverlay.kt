package com.wordgame.wordpuzzles.presentation.components.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.wordgame.wordpuzzles.R

@Composable
@Preview(
    name = "Large", widthDp = 360, heightDp = 740
)
fun AnimatedOverlayP(){
    AnimatedOverlay(animate = true)
  }
@Composable
fun AnimatedOverlay(animate:Boolean) {
    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            modifier = Modifier
                .weight(1f),
            visible = animate,
            enter = slideInVertically(),
            exit = slideOutVertically() + shrinkOut()
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.overlay), contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        }
    }
}