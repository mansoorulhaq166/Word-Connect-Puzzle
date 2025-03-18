package com.wordgame.wordpuzzles.models

import androidx.compose.ui.geometry.Offset
import java.util.UUID

data class GameLetter(
    val id:String = UUID.randomUUID().toString(),
    val label:String,
    var isvisible:Boolean = false,
    val offset:Offset = Offset.Zero
)
