package com.wordgame.wordpuzzles.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wajahatkarim.flippable.FlipAnimationType
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.FlippableController
import com.wordgame.wordpuzzles.utils.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ShowLetterSnackBar(
    showLetterOnClick: Boolean,
    onDismiss: () -> Unit,
    msg:String
) {
    val coroutineScope = rememberCoroutineScope()
    var snackBarVisible by remember { mutableStateOf(false) }

    val flipController = remember { FlippableController() }
    val touchEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(showLetterOnClick) {
        if (showLetterOnClick) {
            snackBarVisible = true
            coroutineScope.launch {
                delay(2000) // Delay for a few seconds
                snackBarVisible = false
                onDismiss()
            }
        }
    }
    LaunchedEffect(touchEnabled) {
        if (touchEnabled) {
            delay(800) // Delay for auto-flip
            flipController.flip()
        }
    }
    AnimatedVisibility(
        visible = snackBarVisible,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutVertically() + shrinkOut()
    ) {
        if (snackBarVisible) {
            Flippable(
                frontSide = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Snackbar(
                            modifier = Modifier.padding(16.dp),
                            containerColor = Color.Black,
                            content = {
                                Text(
                                    text = msg,
                                    style = TextStyle(
                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            })
                    }
                },
                backSide = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Snackbar(
                            modifier = Modifier.padding(16.dp),
                            containerColor = Color.Black,
                            content = {
                                Text(
                                    text = msg,
                                    style = TextStyle(
                                        textAlign = TextAlign.Center,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        )
                    }
                },
                flipController = flipController,
                flipOnTouch = true,
                flipEnabled = true,
                autoFlip = true,
                autoFlipDurationMs = 800,
                flipAnimationType = FlipAnimationType.VERTICAL_CLOCKWISE,
                cameraDistance = 30.0F,
                onFlippedListener = { _ ->
                    // This is called when any flip animation is finished.
                    // This gives the current side which is visible now in Flippable.
                }
            )
        }
    }
}