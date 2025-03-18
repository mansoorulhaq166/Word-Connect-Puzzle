package com.wordgame.wordpuzzles.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


const val USER_DATA_FILENAME = "user_data_file"
const val INITIAL_SCORES = 0

object UserDataManager {
    private const val KEY_USER_DATA = "users_data"
    private const val KEY_COMPLETED_WORDS = "completed_words"
    private lateinit var sharedPreferences: SharedPreferences

    fun initializeSharedPrefs(context: Context) {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        sharedPreferences = EncryptedSharedPreferences.create(
            USER_DATA_FILENAME,
            mainKeyAlias,
            context.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getCompletedWords(): List<String> {
        val completedWordsJson = sharedPreferences.getString(KEY_COMPLETED_WORDS, "")
        return if (completedWordsJson!!.isNotEmpty()) {
            Gson().fromJson(completedWordsJson, object : TypeToken<List<String>>() {}.type)
        } else {
            emptyList() // Return an empty list if no data is found
        }
    }

    fun setCompletedWords(newWords: List<String>) {
        val existingWords = HashSet(getCompletedWords()) // Convertinh to a HashSet to remove duplicates
        existingWords.addAll(newWords)
        val completedWordsJson = Gson().toJson(existingWords.toList()) // Converting back to a list
        sharedPreferences.edit().putString(KEY_COMPLETED_WORDS, completedWordsJson).apply()
    }

}