package com.wordgame.wordpuzzles.ads

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


const val AD_PREFS_FILENAME = "ads"

object AdPrefs {
    private lateinit var sharedPreferences: SharedPreferences
    private const val is_premium_status = "premium_prefs"

    fun initializeSharedPrefs(context: Context) {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        sharedPreferences = EncryptedSharedPreferences.create(
            AD_PREFS_FILENAME,
            mainKeyAlias,
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }


    fun isPremium(): Boolean {
        return sharedPreferences.getBoolean(is_premium_status, false)
    }

    fun setPremium(isPremium: Boolean) {
        sharedPreferences.edit().putBoolean(is_premium_status, isPremium).apply()
    }
}