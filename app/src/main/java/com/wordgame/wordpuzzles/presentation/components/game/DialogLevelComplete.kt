package com.wordgame.wordpuzzles.presentation.components.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.presentation.theme.alfa_slab


@Composable
@Preview
fun PreviewDialogLevelComplete() {
    DialogLevelComplete(gemsText = "3")
}

@Composable
fun DialogLevelComplete(
    onOkClicked: () -> Unit = {},
    gemsText: String
) {
    Dialog(onDismissRequest = { }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(300.dp),
                    painter = painterResource(id = R.drawable.level_cleared),
                    contentDescription = "Level Cleared",
                    contentScale = ContentScale.Fit
                )
                ButtonContinue {
                    onOkClicked()
                }
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.gem_default),
                        contentDescription = "Gems"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = gemsText,
                        style = TextStyle(
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Green,
                            fontFamily = alfa_slab,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
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
}