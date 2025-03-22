package com.wordgame.wordpuzzles.core.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingOptionsManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val SETTINGS_FILENAME = "settings"
        private const val MUSIC_OPTION_STATUS = "music_option"
        private const val SOUND_OPTION_STATUS = "sound_option"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        EncryptedSharedPreferences.create(
            SETTINGS_FILENAME,
            mainKeyAlias,
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun isMusicEnabled(): Boolean = sharedPreferences.getBoolean(MUSIC_OPTION_STATUS, true)

    fun setMusicEnabled(isMusicEnabled: Boolean) {
        sharedPreferences.edit { putBoolean(MUSIC_OPTION_STATUS, isMusicEnabled) }
    }

    fun isSoundEnabled(): Boolean = sharedPreferences.getBoolean(SOUND_OPTION_STATUS, true)

    fun setSoundEnabled(isSoundEnabled: Boolean) {
        sharedPreferences.edit { putBoolean(SOUND_OPTION_STATUS, isSoundEnabled) }
    }
}