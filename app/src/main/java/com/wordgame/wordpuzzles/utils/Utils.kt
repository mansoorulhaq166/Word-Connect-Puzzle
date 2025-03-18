package com.wordgame.wordpuzzles.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.wordgame.wordpuzzles.R

object Utils {
    fun shareApp(context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Word Connect From PlayStore")
        val appUrl = "https://play.google.com/store/apps/details?id=${context.packageName}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Word Connect From PlayStore: $appUrl")
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun rateApp(context: Context) {
        val packageName = context.packageName
        val uri = "market://details?id=$packageName"
        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        try {
            context.startActivity(playStoreIntent)
        } catch (e: ActivityNotFoundException) {
            // If the Play Store app is not installed, open the Play Store website
            val webUri = "https://play.google.com/store/apps/details?id=$packageName"
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUri))
            context.startActivity(webIntent)
        }
    }

    fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    }

    fun getIconResource(iconName: String): Int {
        return when (iconName) {
            "gem" -> R.drawable.gem_default
            "hint" -> R.drawable.hint_default
            "boost" -> R.drawable.boost_default
            // Add more cases for other icon names
            else -> R.drawable.gem_default
        }
    }
}