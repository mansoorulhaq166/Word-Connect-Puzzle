package com.wordgame.wordpuzzles.navigation

import com.wordgame.wordpuzzles.R

interface Destinations {
    val route:String
    val icon:Int
    val label:String
}

object DestinationMain:Destinations{
    override val route: String
        get() = "mainscreen"
    override val icon: Int
        get() = R.drawable.app_icon
    override val label: String
        get() = "main"
}
object DestinationGame:Destinations{
    override val route: String
        get() = "gamescreen"
    override val icon: Int
        get() = 0
    override val label: String
        get() = "gameview"
}
object DestinationSettings : Destinations {
    override val route: String
        get() = "settingsscreen"
    override val icon: Int
        get() = R.drawable.icon_settings
    override val label: String
        get() = "Settings" // Set the label for settings
}