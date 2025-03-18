package com.wordgame.wordpuzzles.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ui.theme.krona

@Composable
fun ButtonPlayMain(text: String, onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(185.dp)
            .height(56.dp)
            .clickable(
                onClick = {
                    isPressed = !isPressed
                    isLoading = true
                    onClick()
                }
            )
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.level_button),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = text,
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = krona
            ),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}