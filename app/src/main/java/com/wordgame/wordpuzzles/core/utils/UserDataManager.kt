package com.wordgame.wordpuzzles.core.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


const val USER_DATA_FILENAME = "user_data_file"

// keeping track of button click on tile
object ShowLetterState {
    val showLetterOnClick = mutableStateOf(false)
}

object UserDataManager {
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
        val existingWords =
            HashSet(getCompletedWords()) // Convert int to a HashSet to remove duplicates
        existingWords.addAll(newWords)
        val completedWordsJson = Gson().toJson(existingWords.toList()) // Converting back to a list
        sharedPreferences.edit { putString(KEY_COMPLETED_WORDS, completedWordsJson).apply() }
    }
}