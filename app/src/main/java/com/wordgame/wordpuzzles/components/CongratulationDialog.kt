package com.wordgame.wordpuzzles.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ui.theme.alfa_slab

@Composable
@Preview
fun CongratulationDialogView() {
    CongratulationDialog(
        onDismiss = {},
        painterResource(id = R.drawable.gem_default),
        "Congratulation",
        "+15 Gems"
    )
}

@Composable
fun CongratulationDialog(
    onDismiss: () -> Unit,
    imagePainter: Painter,
    title: String,
    subtitle: String
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Box(
            modifier = Modifier
                .width(190.dp)
                .height(190.dp)
                .background(
                    Color.Transparent,
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Add your image here
                Image(
                    painter = imagePainter,
                    contentDescription = "Congratulation Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Text(
                    text = title,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = Color.Green,
                        fontSize = 16.sp,
                        fontFamily = alfa_slab
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                )

                Text(
                    text = subtitle,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = Color.Green,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}