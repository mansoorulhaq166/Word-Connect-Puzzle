package com.wordgame.wordpuzzles.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.google.common.collect.ImmutableList
import com.wordgame.wordpuzzles.ads.AdPrefs
import com.wordgame.wordpuzzles.core.utils.GemShopManager
import javax.inject.Inject

class BillingRepository @Inject constructor(private val context: Context) :
    PurchasesUpdatedListener {
    private lateinit var billingClient: BillingClient
    private lateinit var productDetailsForNoAds: ProductDetails
    private lateinit var productDetailsForMiniPack: ProductDetails
    private lateinit var productDetailsForMegaPack: ProductDetails
    private lateinit var purchase: Purchase
    private var onProductDetailsInitialized: (() -> Unit)? = null
    private val completedProductSkus = mutableSetOf<String>()

    init {
        setupBilling()
    }

    fun setupBilling(): Boolean {
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails {
                        val queryPurchasesParams = QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                        billingClient.queryPurchasesAsync(queryPurchasesParams) { billingResult1, purchases ->
                            if (billingResult1.responseCode == BillingClient.BillingResponseCode.OK) {
                                for (purchase in purchases) {
                                    if (purchase.products.contains(NO_ADS) && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //       Log.i(TAG, "OnBillingSetupFinish failed")
                    return
                }
            }

            override fun onBillingServiceDisconnected() {
                //   Log.i(TAG, "OnBillingSetupFinish connection lost")
                return
            }
        })
        return true
    }

    fun queryProductDetails(onInitialized: () -> Unit) {
        onProductDetailsInitialized = onInitialized

        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                ImmutableList.of(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(NO_ADS)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(MINI_PACK)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build(),
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(MEGA_PACK)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { _, productDetailsList ->
            if (productDetailsList.isNotEmpty()) {
                productDetailsForNoAds = productDetailsList[2]
                productDetailsForMiniPack = productDetailsList[1]
                productDetailsForMegaPack = productDetailsList[0]
                onProductDetailsInitialized?.invoke() // Callback to signal initialization
            } else {
                //     Log.i(TAG, "onProductDetailsResponse: No products")
            }
        }
    }

    fun makePurchase(activity: Activity, selectedProductId: String) {
        val productDetails = when (selectedProductId) {
            NO_ADS -> productDetailsForNoAds
            MINI_PACK -> productDetailsForMiniPack
            MEGA_PACK -> productDetailsForMegaPack
            else -> null
        }
        val productDetailsParamsList = listOf(
            productDetails?.let {
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(it)
                    .build()
            }
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    private fun completePurchase(item: Purchase) {
        purchase = item

        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            val product = purchase.products.firstOrNull()
            if (product != null && !completedProductSkus.contains(product)) {
                completedProductSkus.add(product)
                when (product) {
                    NO_ADS -> AdPrefs.setPremium(true)
                    MINI_PACK -> {
                        GemShopManager.addGems(50)
                        GemShopManager.getGemsTotal()
                        consumePurchase(purchase)
                    }

                    MEGA_PACK -> {
                        Log.d("TAG", "completePurchase: ")
                        GemShopManager.addGems(125)
                        GemShopManager.getGemsTotal()
                        consumePurchase(purchase)
                    }

                    else -> {
                        // Handle other products if needed
                    }
                }
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                completePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            //    Log.i(TAG, "onPurchasesUpdated: Purchase Canceled")
        } else {
            //    Log.i(TAG, "onPurchasesUpdated: Error: ${billingResult.debugMessage}")
        }
    }

    private fun consumePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.consumeAsync(consumeParams) { billingResult, _ ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                // Consume successful, handle as needed
            } else {
                // Consume failed, handle error
            }
        }
    }
}