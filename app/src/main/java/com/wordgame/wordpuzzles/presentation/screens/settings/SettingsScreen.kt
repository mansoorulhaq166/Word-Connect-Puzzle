package com.wordgame.wordpuzzles.presentation.screens.settings

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ads.LoadBannerAds
import com.wordgame.wordpuzzles.core.utils.Utils.rateApp
import com.wordgame.wordpuzzles.core.utils.Utils.shareApp
import com.wordgame.wordpuzzles.presentation.components.settings.SettingActionItem
import com.wordgame.wordpuzzles.presentation.components.settings.SettingSwitchItem
import com.wordgame.wordpuzzles.presentation.components.settings.SettingsTopBar
import com.wordgame.wordpuzzles.presentation.theme.colorPallet1
import com.wordgame.wordpuzzles.presentation.theme.colorPallet2

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Collecting state from ViewModel
    val isSoundEnabled by viewModel.isSoundEnabled.collectAsState()
    val isMusicEnabled by viewModel.isMusicEnabled.collectAsState()

    Scaffold(
        topBar = { SettingsTopBar(navController) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(colorPallet2, colorPallet1)))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 2.dp, bottom = 64.dp, start = 28.dp, end = 28.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sound setting
                SettingSwitchItem(
                    label = "Sound",
                    checked = isSoundEnabled,
                    onCheckedChange = { viewModel.setSoundEnabled(it) }
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Music setting
                SettingSwitchItem(
                    label = "Music",
                    checked = isMusicEnabled,
                    onCheckedChange = { viewModel.setMusicEnabled(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Share app
                SettingActionItem(
                    icon = painterResource(id = R.drawable.share_icon),
                    text = "Share"
                ) { shareApp(context) }

                Spacer(modifier = Modifier.height(8.dp))

                // Rate app
                SettingActionItem(
                    icon = painterResource(id = R.drawable.rate_icon),
                    text = "Rate Us"
                ) { rateApp(context) }
            }

            // Bottom banner ad
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                LoadBannerAds()
            }
        }
    }
}