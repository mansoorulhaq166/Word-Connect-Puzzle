package com.wordgame.wordpuzzles.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ads.LoadBannerAds
import com.wordgame.wordpuzzles.components.settings.SettingActionItem
import com.wordgame.wordpuzzles.components.settings.SettingSwitchItem
import com.wordgame.wordpuzzles.components.settings.SettingsTopBar
import com.wordgame.wordpuzzles.ui.theme.colorPallet1
import com.wordgame.wordpuzzles.ui.theme.colorPallet2
import com.wordgame.wordpuzzles.utils.SettingOptionsManager
import com.wordgame.wordpuzzles.utils.Utils.rateApp
import com.wordgame.wordpuzzles.utils.Utils.shareApp

@Composable
fun SettingsScreen(
    navController: NavController,
    onBackgroundMusicEnabledChanged: (Boolean) -> Unit,
    onSoundEnabledChanged: (Boolean) -> Unit,
) {
    // Track the sound/music states
    var isSoundEnabled by remember { mutableStateOf(SettingOptionsManager.isSoundEnabled()) }
    var isBackgroundMusicEnabled by remember { mutableStateOf(SettingOptionsManager.isMusicEnabled()) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            SettingsTopBar(navController)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        // Background image
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorPallet2,
                                colorPallet1
                            )
                        )
                    )
            )

            // Main content column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 64.dp)
                    .padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sound setting
                SettingSwitchItem(
                    label = "Sound",
                    checked = isSoundEnabled,
                    onCheckedChange = { isChecked ->
                        isSoundEnabled = isChecked
                        onSoundEnabledChanged(isChecked)
                        SettingOptionsManager.setSoundEnabled(isChecked)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Music setting
                SettingSwitchItem(
                    label = "Music",
                    checked = isBackgroundMusicEnabled,
                    onCheckedChange = { isChecked ->
                        isBackgroundMusicEnabled = isChecked
                        onBackgroundMusicEnabledChanged(isChecked)
                        SettingOptionsManager.setMusicEnabled(isChecked)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Share
                SettingActionItem(
                    icon = painterResource(id = R.drawable.share_icon),
                    text = "Share"
                ) {
                    shareApp(context)
                }

                Spacer(modifier = Modifier.height(8.dp))

                SettingActionItem(
                    icon = painterResource(id = R.drawable.rate_icon),
                    text = "Rate Us"
                ) {
                    rateApp(context)
                }
            }

            // Bottom banner ad area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Column {
                    LoadBannerAds()
                }
            }
        }
    }
}