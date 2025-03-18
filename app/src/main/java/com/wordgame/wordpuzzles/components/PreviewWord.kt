package com.wordgame.wordpuzzles.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ui.screens.gameview.GameScreenViewModel
import com.wordgame.wordpuzzles.ui.theme.WordLink_WordPreview

@Composable
fun PreviewWord(
    modifier: Modifier,
    viewmodel: GameScreenViewModel,
) {

    val word = remember {
        viewmodel.wordtopreview
    }
    val showScale by animateFloatAsState(
        targetValue = if (word.value.isNotEmpty()) 1f else 0f,
        animationSpec = tween(200), label = ""
    )
    Box(
        modifier = Modifier
            .scale(showScale)
            .clip(RoundedCornerShape(16.dp))
            .background(WordLink_WordPreview.copy(alpha = 0.5f))
            .padding(start = 8.dp, end = 8.dp)
            .then(modifier),
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Add an icon before the text
            Icon(
                painter = painterResource(id = R.drawable.gem_default),
                contentDescription = null, // Provide a meaningful description
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = word.value,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}