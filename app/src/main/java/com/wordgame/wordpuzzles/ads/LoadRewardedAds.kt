package com.wordgame.wordpuzzles.ads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.wordgame.wordpuzzles.ads.AdsConfig.REWARDED_ADS

@Composable
fun LoadRewardedAd(
    onAdLoaded: ((RewardedAd) -> Unit)? = null,
    onAdFailed: (() -> Unit)? = null,
    onLoadingProgress: (Any?) -> Unit
) {
    val adRequest = remember { AdRequest.Builder().build() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
          //      Log.d("AdViewReward", "onAdFailedToLoad: " + adError.message)
                onAdFailed?.invoke()
            }

            override fun onAdLoaded(ad: RewardedAd) {
                onAdLoaded?.invoke(ad)
            }
        }
        RewardedAd.load(context, REWARDED_ADS, adRequest, adLoadCallback)
    }
}