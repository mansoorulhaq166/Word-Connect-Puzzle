package com.wordgame.wordpuzzles.billing

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Billing(selectedId:String) {

    val billingViewModel: BillingViewModel = hiltViewModel()
    val activity = LocalContext.current as Activity

    billingViewModel.makePurchase(activity, selectedID = selectedId)

}

@Composable
fun SetupBilling() {
    val billingViewModel: BillingViewModel = hiltViewModel()
    billingViewModel.setupBilling()
}