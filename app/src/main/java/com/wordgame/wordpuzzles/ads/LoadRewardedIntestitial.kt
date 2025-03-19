package com.wordgame.wordpuzzles.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.wordgame.wordpuzzles.ads.AdsConfig.INTERSTITIAL_REWARDED_ADS

@Composable
fun LoadRewardedInterstitial(
    onAdClosed: () -> Unit,
    onUserEarnedReward: () -> Unit
) {

    // Load the rewarded interstitial ad
    val context = LocalContext.current
    val adUnitId = INTERSTITIAL_REWARDED_ADS
    val rewardedInterstitialAdState = remember { mutableStateOf<RewardedInterstitialAd?>(null) }
    val isAdLoading = rewardedInterstitialAdState.value == null
    val isError = remember { mutableStateOf(false) }

    if (rewardedInterstitialAdState.value == null) {
        MobileAds.initialize(context) { _ ->
            loadAd(context, adUnitId, rewardedInterstitialAdState, isError)
        }
    }

    if (!isError.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isAdLoading) {
                CircularProgressIndicator()
            } else {
                val rewardedInterstitialAd = rewardedInterstitialAdState.value
                if (rewardedInterstitialAd != null) {
                    val activity = LocalActivity.current as Activity
                    rewardedInterstitialAd.show(activity) {
                        onAdClosed()
                        onUserEarnedReward()
                    }
                }
            }
        }
    } else {
        loadAd(context, adUnitId, rewardedInterstitialAdState, isError)
    }
}

fun loadAd(
    context: Context,
    adUnitId: String,
    rewardedInterstitialAdState: MutableState<RewardedInterstitialAd?>,
    isError: MutableState<Boolean>
) {
    RewardedInterstitialAd.load(context, adUnitId, AdRequest.Builder().build(),
        object : RewardedInterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedInterstitialAd) {
                Log.d("RewardedInter", "Ad was loaded.")
                rewardedInterstitialAdState.value = ad
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("RewardedInterError", adError.toString())
                rewardedInterstitialAdState.value = null
                isError.value = true
                Log.d("RewardedInterIsError", isError.value.toString())
            }
        })
}