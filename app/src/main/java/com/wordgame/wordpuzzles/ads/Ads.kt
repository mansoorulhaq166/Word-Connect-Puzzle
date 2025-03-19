package com.wordgame.wordpuzzles.ads

import com.wordgame.wordpuzzles.BuildConfig

object AdsConfig {
    val APP_ADS_ID = BuildConfig.ADMOB_APP_ID
    val BANNER_ADS = BuildConfig.BANNER_AD_ID
    val REWARDED_ADS = BuildConfig.REWARDED_AD_ID
    val INTERSTITIAL_REWARDED_ADS = BuildConfig.INTERSTITIAL_AD_ID
    val APP_OPEN_ADS = BuildConfig.APP_OPEN_AD_ID

    // Test IDs (These are safe to keep as constants)
    const val TEST_BANNER_ADS = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_REWARDED_ADS = "ca-app-pub-3940256099942544/5224354917"
}