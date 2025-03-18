package com.wordgame.wordpuzzles.billing

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.wordgame.wordpuzzles.billing.BillingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(private val billingRepository: BillingRepository) :
    ViewModel() {
    fun makePurchase(activity: Activity, selectedID:String) {
        billingRepository.makePurchase(activity,selectedID)
    }
    fun setupBilling() {
        billingRepository.setupBilling()
    }
}