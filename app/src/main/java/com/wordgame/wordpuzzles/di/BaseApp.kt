package com.wordgame.wordpuzzles.di

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.wordgame.wordpuzzles.ads.AdsConfig.APP_OPEN_ADS
import com.wordgame.wordpuzzles.ads.AppOpenAdManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        AppOpenAdManager(this, APP_OPEN_ADS)
    }
}