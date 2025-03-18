package com.wordgame.wordpuzzles.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ui.theme.alfa_slab

@Preview
@Composable
fun QuitConfirmationDialogPreview() {
//    QuitConfirmationDialog(onConfirmExit = { /*TODO*/ }) {
//    }
    QuitConfirmationDialog(onExitGame = { /*TODO*/ }) {

    }
}

@Composable
fun QuitConfirmationDialog(
    onExitGame: () -> Unit,
    onCancel: () -> Unit
) {

    Dialog(
        onDismissRequest = onCancel
    ) {
        Box(
            modifier = Modifier
                .width(350.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Add your game logo or image here
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "Game Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(150.dp)
                )

                // Add a message to ask if the user wants to exit the game
                Text(
                    text = "Are you sure you want to exit the game?",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = alfa_slab,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                // Add buttons to confirm or cancel exiting the game
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onExitGame()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Exit",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = alfa_slab,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Button(
                        onClick = {
                            onCancel()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            "Cancel",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = alfa_slab,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}