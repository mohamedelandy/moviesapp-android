/**
 * File: UserPreferencesRepositoryImpl.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.data.repository

import com.nady.moviesapp.data.datasource.MovieStaticDataSource
import com.nady.moviesapp.data.local.MovieDao
import com.nady.moviesapp.data.local.entities.AppSettingEntity
import com.nady.moviesapp.domain.model.AppSettings
import com.nady.moviesapp.domain.model.AppThemeMode
import com.nady.moviesapp.domain.model.SupportedLanguage
import com.nady.moviesapp.domain.model.UserProfile
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch

class UserPreferencesRepositoryImpl(
    private val movieDao: MovieDao
) : UserPreferencesRepository {

    override fun getAppSettings(): Flow<Resource<AppSettings>> {
        return movieDao.getSetting("app_settings").map { entity ->
            val settings = if (entity != null) {
                val parts = entity.value.split("|")
                val theme = parts.getOrNull(0)?.let {
                    runCatching { AppThemeMode.valueOf(it) }.getOrDefault(AppThemeMode.DARK)
                } ?: AppThemeMode.DARK

                val lang = parts.getOrNull(1)?.let {
                    SupportedLanguage.fromCode(it)
                } ?: SupportedLanguage.ENGLISH

                val spatial = parts.getOrNull(2)?.toBooleanStrictOrNull() ?: true
                val speed = parts.getOrNull(3)?.toFloatOrNull() ?: 1.0f

                AppSettings(
                    themeMode = theme,
                    language = lang,
                    isSpatialAudioActive = spatial,
                    playbackSpeed = speed
                )
            } else {
                AppSettings(
                    themeMode = AppThemeMode.DARK,
                    language = SupportedLanguage.ENGLISH,
                    isSpatialAudioActive = true,
                    playbackSpeed = 1.0f
                )
            }
            Resource.Success(settings) as Resource<AppSettings>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
    }

    override suspend fun setThemeMode(mode: AppThemeMode): Resource<Unit> {
        return try {
            val current = getCurrentSettings()
            saveSettings(current.copy(themeMode = mode))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error", e)
        }
    }

    override suspend fun setLanguage(language: SupportedLanguage): Resource<Unit> {
        return try {
            val current = getCurrentSettings()
            saveSettings(current.copy(language = language))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error", e)
        }
    }

    override suspend fun setSpatialAudioEnabled(enabled: Boolean): Resource<Unit> {
        return try {
            val current = getCurrentSettings()
            saveSettings(current.copy(isSpatialAudioActive = enabled))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error", e)
        }
    }

    override suspend fun setPlaybackSpeed(speed: Float): Resource<Unit> {
        return try {
            val current = getCurrentSettings()
            saveSettings(current.copy(playbackSpeed = speed))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error", e)
        }
    }

    override fun getUserProfile(): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Success(MovieStaticDataSource.userProfile) as Resource<UserProfile>)
    }.catch { emit(Resource.Error(it.message ?: "Error", it)) }

    private var cachedSettings = AppSettings()

    private fun getCurrentSettings(): AppSettings = cachedSettings

    private suspend fun saveSettings(settings: AppSettings) {
        cachedSettings = settings
        val value = "${settings.themeMode.name}|${settings.language.code}|${settings.isSpatialAudioActive}|${settings.playbackSpeed}"
        movieDao.saveSetting(AppSettingEntity(key = "app_settings", value = value))
    }
}
