package com.wordgame.wordpuzzles.models

import androidx.compose.ui.geometry.Offset
import java.util.UUID

data class KeyPadButton(
    val id:String = UUID.randomUUID().toString(),
    val label:String,
    var center:Offset = Offset(0f,0f)
)
