package com.wordgame.wordpuzzles.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

const val SETTINGS_FILENAME = "settings"

object SettingOptionsManager {
    private const val settings_options_prefs = "settings_prefs"
    private const val MUSIC_OPTION_STATUS = "music_option"
    private const val SOUND_OPTION_STATUS = "sound_option"
    private lateinit var sharedPreferences: SharedPreferences

    fun initializeSharedPrefs(context: Context) {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        sharedPreferences = EncryptedSharedPreferences.create(
            SETTINGS_FILENAME,
            mainKeyAlias,
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun isMusicEnabled(): Boolean {
        return sharedPreferences.getBoolean(MUSIC_OPTION_STATUS, true)
    }

    fun setMusicEnabled(isMusicEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(MUSIC_OPTION_STATUS, isMusicEnabled).apply()
    }

    fun isSoundEnabled(): Boolean {
        return sharedPreferences.getBoolean(SOUND_OPTION_STATUS, true)
    }

    fun setSoundEnabled(isSoundEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(SOUND_OPTION_STATUS, isSoundEnabled).apply()
    }
}