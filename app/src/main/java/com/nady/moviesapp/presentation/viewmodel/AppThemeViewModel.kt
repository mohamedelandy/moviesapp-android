/**
 * File: AppThemeViewModel.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.viewmodel

import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nady.moviesapp.domain.model.AppSettings
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.model.AppThemeMode
import com.nady.moviesapp.domain.model.SupportedLanguage
import com.nady.moviesapp.domain.usecase.GetAppSettingsUseCase
import com.nady.moviesapp.domain.usecase.UpdateAppSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppThemeViewModel(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase
) : ViewModel() {

    val appSettings: StateFlow<AppSettings> = getAppSettingsUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings(
                themeMode = AppThemeMode.DARK,
                language = SupportedLanguage.ENGLISH,
                isSpatialAudioActive = true
            )
        )

    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch {
            updateAppSettingsUseCase.setThemeMode(mode)
        }
    }

    fun setLanguage(language: SupportedLanguage) {
        viewModelScope.launch {
            updateAppSettingsUseCase.setLanguage(language)
        }
    }

    fun toggleSpatialAudio(enabled: Boolean) {
        viewModelScope.launch {
            updateAppSettingsUseCase.setSpatialAudioEnabled(enabled)
        }
    }

    fun getLayoutDirection(language: SupportedLanguage): LayoutDirection {
        return if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    }
}
