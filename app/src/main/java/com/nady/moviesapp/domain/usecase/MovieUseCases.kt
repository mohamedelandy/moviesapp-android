/**
 * File: MovieUseCases.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.domain.usecase

import com.nady.moviesapp.domain.model.AppSettings
import com.nady.moviesapp.domain.model.AppThemeMode
import com.nady.moviesapp.domain.model.CategoryGenre
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.SearchTrend
import com.nady.moviesapp.domain.model.SpatialAudioTrack
import com.nady.moviesapp.domain.model.SupportedLanguage
import com.nady.moviesapp.domain.model.UserProfile
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.repository.DownloadsRepository
import com.nady.moviesapp.domain.repository.MovieRepository
import com.nady.moviesapp.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetHeroMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Resource<List<Movie>>> = repository.getHeroMovies()
}

class GetContinueWatchingUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Resource<List<Movie>>> = repository.getContinueWatching()
}

class GetTrendingRankedUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Resource<List<Movie>>> = repository.getTrendingRanked()
}

class GetSpatialAudioTracksUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Resource<List<SpatialAudioTrack>>> = repository.getSpatialAudioTracks()
}

class GetRecommendedUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Resource<List<Movie>>> = repository.getRecommended()
}

class GetMovieDetailsUseCase(private val repository: MovieRepository) {
    operator fun invoke(movieId: String): Flow<Resource<Movie?>> = repository.getMovieById(movieId)
}

class GetGenreCategoriesUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Resource<List<CategoryGenre>>> = repository.getGenreCategories()
}

class GetSearchTrendsUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Resource<List<SearchTrend>>> = repository.getSearchTrends()
}

class SearchMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(query: String, filter: String): Flow<Resource<List<Movie>>> =
        repository.searchMovies(query, filter)
}

class ToggleWatchlistUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: String): Resource<Unit> = repository.toggleWatchlist(movieId)
}

class UpdatePlaybackProgressUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: String, episodeNumber: Int, progress: Float, timestamp: String): Resource<Unit> {
        return repository.updatePlaybackProgress(movieId, episodeNumber, progress, timestamp)
    }
}

class GetSavedUniversesUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Resource<List<Movie>>> = repository.getSavedUniverses()
}

class GetAppSettingsUseCase(private val repository: UserPreferencesRepository) {
    operator fun invoke(): Flow<Resource<AppSettings>> = repository.getAppSettings()
}

class UpdateAppSettingsUseCase(private val repository: UserPreferencesRepository) {
    suspend fun setThemeMode(mode: AppThemeMode): Resource<Unit> = repository.setThemeMode(mode)
    suspend fun setLanguage(language: SupportedLanguage): Resource<Unit> = repository.setLanguage(language)
    suspend fun setSpatialAudioEnabled(enabled: Boolean): Resource<Unit> = repository.setSpatialAudioEnabled(enabled)
    suspend fun setPlaybackSpeed(speed: Float): Resource<Unit> = repository.setPlaybackSpeed(speed)
}

class GetUserProfileUseCase(private val repository: UserPreferencesRepository) {
    operator fun invoke(): Flow<Resource<UserProfile>> = repository.getUserProfile()
}
