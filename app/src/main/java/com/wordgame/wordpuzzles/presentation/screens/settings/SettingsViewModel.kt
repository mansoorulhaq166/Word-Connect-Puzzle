package com.wordgame.wordpuzzles.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wordgame.wordpuzzles.core.utils.SettingOptionsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingOptionsManager
) : ViewModel() {

    private val _isSoundEnabled = MutableStateFlow(settingsManager.isSoundEnabled())
    val isSoundEnabled: StateFlow<Boolean> get() = _isSoundEnabled

    private val _isMusicEnabled = MutableStateFlow(settingsManager.isMusicEnabled())
    val isMusicEnabled: StateFlow<Boolean> get() = _isMusicEnabled

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _isSoundEnabled.value = enabled
            settingsManager.setSoundEnabled(enabled)
        }
    }

    fun setMusicEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _isMusicEnabled.value = enabled
            settingsManager.setMusicEnabled(enabled)
        }
    }
}