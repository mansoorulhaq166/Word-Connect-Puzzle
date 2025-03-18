package com.wordgame.wordpuzzles.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.utils.GemShopManager
import com.wordgame.wordpuzzles.utils.TAG

/*
@Composable
@Preview
fun BottomBarPreview() {
    BottomBar(
        0,
        onHintClicked = {},
        onShuffleClicked = {}
    )
}*/

@Composable
fun BottomBar(
    availableHints: Int,
    onHintClicked: (offset: Offset) -> Unit = {},
    onShuffleClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val modifier = Modifier
            .padding(4.dp)
            .size(48.dp)
        // "Hint" button with available hints count

        Box(
            modifier = Modifier
                .padding(4.dp)
        ) {
            ButtonRound(
                modifier = modifier,
                default = R.drawable.hint_default,
                variant = R.drawable.hint_variant
            ) { offset ->
                onHintClicked(offset)
            }
            Badge(
                modifier = Modifier.align(Alignment.TopEnd),
                count = availableHints,
            )
        }
        ButtonRound(
            modifier = modifier,
            default = R.drawable.shuffle_default,
            variant = R.drawable.shuffle_variant
        ) {
            onShuffleClicked()
        }
    }
}

@Composable
fun Badge(
    modifier: Modifier = Modifier,
    count: Int,
    shape: Shape = CircleShape,
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .background(
                Color(
                    android.graphics.Color
                        .parseColor("#A3C2FF")
                ), shape
            )
    ) {
        //  if (count > 0) {
        Text(
            text = count.toString(),
            color = Color.Blue,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
        //    } else {
//            Icon(
//                painter = painterResource(id = R.drawable.gem_default),
//                contentDescription = "zero hints",
//                tint = Color.White,
//                modifier = Modifier.fillMaxSize()
//                    .padding(2.dp)
//            )
//        }
    }
}