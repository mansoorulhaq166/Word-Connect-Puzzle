package com.wordgame.wordpuzzles.utils

import androidx.compose.runtime.mutableStateOf

const val TAG = "WordLink"
const val SMALL_SIZE_HEIGHT = 600
const val LEVEL_CLEAR_DELAY = 300L

// keeping track of button click on tile
object ShowLetterState {
    val showLetterOnClick = mutableStateOf(false)
}

class GlobalHelper {
}