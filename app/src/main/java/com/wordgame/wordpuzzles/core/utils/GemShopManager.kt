package com.wordgame.wordpuzzles.core.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

const val GEMSHOP_FILENAME = "gem_shop"
const val INITIAL_GEMS = 15
const val GEMS_TO_CONSUME = 1

const val KEY_HINTS_TOTAL = "hints_total"
const val FREE_HINTS_INITIAL = 3


const val KEY_BOOSTS_TOTAL = "boosts_total"
const val FREE_BOOSTS_INITIAL = 1


const val KEY_SHOW_CLICKS_TOTAL = "show_clicks_total"
const val FREE_SHOW_CLICK_INITIAL = 1

private const val LAST_SPIN_TIME_KEY = "last_spin_time"
private const val FIVE_MINUTES =
    24 * 60 * 60 * 1000 // 5 minutes for testing, replace with 24 * 60 * 60 * 1000 for a day

object GemShopManager {
    private const val KEY_GEMS_TOTAL = "gems_total"
    private const val KEY_FIRST_INSTALLATION = "first_installation"
    private lateinit var sharedPreferences: SharedPreferences

    fun initializeSharedPrefs(context: Context) {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        sharedPreferences = EncryptedSharedPreferences.create(
            GEMSHOP_FILENAME,
            mainKeyAlias,
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun isFirstInstallation(): Boolean {
        return sharedPreferences.getBoolean(KEY_FIRST_INSTALLATION, true)
    }

    fun setFirstInstallationStatus(isFirstInstallation: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_FIRST_INSTALLATION, isFirstInstallation).apply() }
    }

    fun initializeGemsTotal() {
        sharedPreferences.edit().putInt(KEY_GEMS_TOTAL, INITIAL_GEMS).apply()
    }

    fun getGemsTotal(): Int {
        return sharedPreferences.getInt(KEY_GEMS_TOTAL, 0)
    }

    fun addGems(gems: Int) {
        val currentTotal = getGemsTotal()
        sharedPreferences.edit().putInt(KEY_GEMS_TOTAL, currentTotal + gems).apply()
    }

    fun consumeGems(quantity: Int): Boolean {
        val currentTotal = getGemsTotal()
        if (currentTotal >= quantity) {
            sharedPreferences.edit().putInt(KEY_GEMS_TOTAL, currentTotal - quantity).apply()
            return true
        }
        return false
    }

    fun initializeHintsTotal() {
        sharedPreferences.edit().putInt(KEY_HINTS_TOTAL, FREE_HINTS_INITIAL).apply()
    }

    fun getTotalHints(): Int {
        return sharedPreferences.getInt(KEY_HINTS_TOTAL, 0)
    }

    fun addHints(hints: Int) {
        val currentTotal = getTotalHints()
        val newTotal = currentTotal + hints
        sharedPreferences.edit().putInt(KEY_HINTS_TOTAL, newTotal).apply()
    }

    fun consumeHints(quantity: Int): Boolean {
        val currentTotal = getTotalHints()
        if (currentTotal >= quantity) {
            sharedPreferences.edit().putInt(KEY_HINTS_TOTAL, currentTotal - quantity).apply()
            return true
        }
        return false
    }

    fun initializeBoostsTotal() {
        sharedPreferences.edit().putInt(KEY_BOOSTS_TOTAL, FREE_BOOSTS_INITIAL).apply()
    }

    fun getTotalBoosts(): Int {
        return sharedPreferences.getInt(KEY_BOOSTS_TOTAL, 0)
    }

    fun addBoosts(boosts: Int) {
        val currentTotal = getTotalBoosts()
        val newTotal = currentTotal + boosts
        sharedPreferences.edit().putInt(KEY_BOOSTS_TOTAL, newTotal).apply()
    }

    fun consumeBoosts(quantity: Int): Boolean {
        val currentTotal = getTotalBoosts()
        if (currentTotal >= quantity) {
            sharedPreferences.edit().putInt(KEY_BOOSTS_TOTAL, currentTotal - quantity).apply()
            return true
        }
        return false
    }

    fun initializeShowClickTotal() {
        sharedPreferences.edit().putInt(KEY_SHOW_CLICKS_TOTAL, FREE_SHOW_CLICK_INITIAL).apply()
    }

    fun getTotalShowClicks(): Int {
        return sharedPreferences.getInt(KEY_SHOW_CLICKS_TOTAL, 0)
    }

    fun addShowClicks(showClicks: Int) {
        val currentTotal = getTotalShowClicks()
        val newTotal = currentTotal + showClicks
        sharedPreferences.edit().putInt(KEY_SHOW_CLICKS_TOTAL, newTotal).apply()
    }

    fun consumeShowClicks(quantity: Int): Boolean {
        val currentTotal = getTotalShowClicks()
        if (currentTotal >= quantity) {
            sharedPreferences.edit().putInt(KEY_SHOW_CLICKS_TOTAL, currentTotal - quantity).apply()
            return true
        }
        return false
    }

    fun canSpinWheel(): Boolean {
        val lastSpinTime = sharedPreferences.getLong(LAST_SPIN_TIME_KEY, 0)
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastSpinTime) >= FIVE_MINUTES
    }

    fun spinWheel() {
        sharedPreferences.edit().putLong(LAST_SPIN_TIME_KEY, System.currentTimeMillis()).apply()
    }

    fun useGemsForSpin(): Boolean {
        val currentGems = getGemsTotal()
        if (currentGems >= 5) {
            consumeGems(5)
            getGemsTotal()
            return true
        }
        return false
    }

    fun getRemainingTime(): Long {
        val lastSpinTime = sharedPreferences.getLong(LAST_SPIN_TIME_KEY, 0)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastSpinTime
        val remainingTime = FIVE_MINUTES - elapsedTime
        return remainingTime / 1000 // Convert to seconds
    }
}