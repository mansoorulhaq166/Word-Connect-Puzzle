package com.wordgame.wordpuzzles.presentation.navigation

import androidx.annotation.DrawableRes
import com.wordgame.wordpuzzles.R

sealed class Destination(val route: String, @DrawableRes val icon: Int, val label: String) {
    object Main : Destination(
        route = "main_screen",
        icon = R.drawable.app_icon,
        label = "Main"
    )

    object Game : Destination(
        route = "game_screen",
        icon = 0, // No icon for game
        label = "Game"
    )

    object Settings : Destination(
        route = "settings_screen",
        icon = R.drawable.icon_settings,
        label = "Settings"
    )
}