/**
 * File: UserPreferencesRepository.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.domain.repository

import com.nady.moviesapp.domain.model.AppSettings
import com.nady.moviesapp.domain.model.AppThemeMode
import com.nady.moviesapp.domain.model.SupportedLanguage
import com.nady.moviesapp.domain.model.UserProfile
import com.nady.moviesapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getAppSettings(): Flow<Resource<AppSettings>>
    suspend fun setThemeMode(mode: AppThemeMode): Resource<Unit>
    suspend fun setLanguage(language: SupportedLanguage): Resource<Unit>
    suspend fun setSpatialAudioEnabled(enabled: Boolean): Resource<Unit>
    suspend fun setPlaybackSpeed(speed: Float): Resource<Unit>
    fun getUserProfile(): Flow<Resource<UserProfile>>
}
