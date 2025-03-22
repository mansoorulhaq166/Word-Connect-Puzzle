package com.wordgame.wordpuzzles.presentation.screens

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.wordgame.wordpuzzles.R
import com.wordgame.wordpuzzles.ads.AdPrefs
import com.wordgame.wordpuzzles.core.utils.GemShopManager
import com.wordgame.wordpuzzles.core.utils.SettingOptionsManager
import com.wordgame.wordpuzzles.core.utils.SoundPlayer
import com.wordgame.wordpuzzles.core.utils.SoundPlayer.pauseBackgroundMusic
import com.wordgame.wordpuzzles.core.utils.SoundPlayer.playBackgroundMusic
import com.wordgame.wordpuzzles.core.utils.UserDataManager
import com.wordgame.wordpuzzles.presentation.components.home.CongratulationDialog
import com.wordgame.wordpuzzles.presentation.navigation.Destination
import com.wordgame.wordpuzzles.presentation.navigation.Destination.Main
import com.wordgame.wordpuzzles.presentation.screens.gameview.GameScreen
import com.wordgame.wordpuzzles.presentation.screens.main.MainScreen
import com.wordgame.wordpuzzles.presentation.screens.settings.SettingsScreen
import com.wordgame.wordpuzzles.presentation.theme.WordLinkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var settingOptionsManager: SettingOptionsManager

    private lateinit var mContext: Context
    private var isBackgroundMusicEnabled: Boolean = true
    private var isSoundEnabled: Boolean = true
    private var currentScreen: String = Main.route
    private var mGems by mutableIntStateOf(0)
    private lateinit var analytics: FirebaseAnalytics
    private var showCongratulationDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this
        installSplashScreen()

        analytics = Firebase.analytics
        GemShopManager.initializeSharedPrefs(mContext)
        UserDataManager.initializeSharedPrefs(mContext)
        AdPrefs.initializeSharedPrefs(mContext)

        // Get settings from injected instances
        isBackgroundMusicEnabled = settingOptionsManager.isMusicEnabled()
        isSoundEnabled = settingOptionsManager.isSoundEnabled()

        mGems = GemShopManager.getGemsTotal()

        if (GemShopManager.isFirstInstallation()) {
            GemShopManager.initializeGemsTotal()
            GemShopManager.initializeHintsTotal()
            GemShopManager.initializeBoostsTotal()
            GemShopManager.initializeShowClickTotal()
            GemShopManager.setFirstInstallationStatus(false)
            showCongratulationDialog = true
        }
        CoroutineScope(Dispatchers.Main).launch {
            SoundPlayer.init()
            if (isBackgroundMusicEnabled) {
                playBackgroundMusic(mContext, R.raw.background)
            }
        }

        setContent {
            WordLinkTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                currentScreen = navBackStackEntry?.destination?.route ?: Main.route
                Box(modifier = Modifier.fillMaxSize()) {
                    if (currentScreen == Main.route) {
                        onBackgroundMusicEnabledChanged(isBackgroundMusicEnabled)
                    }
                    NavHost(
                        navController = navController,
                        startDestination = Main.route
                    ) {
                        composable(Main.route) {
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { -it }) + shrinkOut()
                            ) {
                                MainScreen(
                                    navController
                                )
                                if (showCongratulationDialog) {
                                    SoundPlayer.playSound(this@MainActivity, R.raw.congrats)
                                    CongratulationDialog(
                                        onDismiss = { dismissCongratulationDialog() },
                                        painterResource(id = R.drawable.gem_default),
                                        "Congratulations!",
                                        "+15 Gems"
                                    )
                                }
                            }
                        }
                        composable(Destination.Game.route) {
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { -it }) + shrinkOut()
                            ) {
                                GameScreen(navController)
                            }
                        }
                        composable(Destination.Settings.route) {
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(initialOffsetY = { it }),
                                exit = slideOutVertically(targetOffsetY = { -it }) + shrinkOut()
                            ) {
                                SettingsScreen(
                                    navController
                                )
                            }
                        }
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val listener = View.OnApplyWindowInsetsListener { _, insets ->
                val isSystemBarsVisible = insets.isVisible(WindowInsets.Type.systemBars())
                if (isSystemBarsVisible) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3000)
                        hideSystemUI()
                    }
                }
                insets
            }
            window.decorView.setOnApplyWindowInsetsListener(listener)
        } else {
            val listener = View.OnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(3000)
                        hideSystemUI()
                    }
                }
            }
            window.decorView.setOnSystemUiVisibilityChangeListener(listener)
        }
        hideSystemUI()
    }

    private fun dismissCongratulationDialog() {
        showCongratulationDialog = false
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundPlayer.release()
    }

    private fun onBackgroundMusicEnabledChanged(isEnabled: Boolean) {
        this.isBackgroundMusicEnabled = isEnabled
        if (isEnabled) {
            playBackgroundMusic(mContext, R.raw.background)
        } else {
            pauseBackgroundMusic()
        }
    }

    private fun onSoundEnabledChanged(isEnabled: Boolean) {
        this.isSoundEnabled = isEnabled
    }

    private fun Activity.hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.hide(WindowInsets.Type.systemBars())
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    // Do not let system steal touches for showing the navigation bar
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Hide the nav bar and status bar
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            // Keep the app content behind the bars even if user swipes them up
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            // make navbar translucent - do this already in hideSystemUI() so that the bar
            // is translucent if user swipes it up
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}
/*
                            if (showGemsAnimation) {
//                                ShowLetterPopup(
//                                    hintOffset = Offset(8.0F, 1408.0F), letterOffset = Offset(
//                                        236.0F,
//                                        312.0F
//                                    )
//                                )
//                                Log.d(TAG, "onCreate: Run")
                                showGemsAnimation = false
                            }
*/