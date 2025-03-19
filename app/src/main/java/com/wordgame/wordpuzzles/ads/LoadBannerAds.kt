package com.wordgame.wordpuzzles.ads

import android.util.Log
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.wordgame.wordpuzzles.ads.AdsConfig.BANNER_ADS

@Composable
fun LoadBannerAds() {
    if (!AdPrefs.isPremium()) {
        AndroidView(
            factory = { context ->
                val adView = AdView(context)
                adView.setAdSize(AdSize.BANNER)
                adView.adUnitId = BANNER_ADS
                adView.loadAd(AdRequest.Builder().build())
                adView
            },
            modifier = Modifier.fillMaxWidth()
        ) { adView ->
            Log.d("AdViewTest", "AdView loading...")
            val adViewLayout = adView.parent as? FrameLayout
            adViewLayout?.removeAllViews()
            adViewLayout?.addView(adView)

            adView.adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d("AdViewTest", "AdView loaded successfully!")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("AdViewTest", "AdView failed to load: ${adError.message}")
                }
            }
        }
    }
}