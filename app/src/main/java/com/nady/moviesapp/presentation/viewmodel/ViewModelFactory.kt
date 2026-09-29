/**
 * File: ViewModelFactory.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nady.moviesapp.data.local.AppDatabase
import com.nady.moviesapp.data.repository.DownloadsRepositoryImpl
import com.nady.moviesapp.data.repository.MovieRepositoryImpl
import com.nady.moviesapp.data.repository.UserPreferencesRepositoryImpl
import com.nady.moviesapp.domain.usecase.GetAppSettingsUseCase
import com.nady.moviesapp.domain.usecase.GetContinueWatchingUseCase
import com.nady.moviesapp.domain.usecase.GetGenreCategoriesUseCase
import com.nady.moviesapp.domain.usecase.GetHeroMoviesUseCase
import com.nady.moviesapp.domain.usecase.GetMovieDetailsUseCase
import com.nady.moviesapp.domain.usecase.GetRecommendedUseCase
import com.nady.moviesapp.domain.usecase.GetSavedUniversesUseCase
import com.nady.moviesapp.domain.usecase.GetSearchTrendsUseCase
import com.nady.moviesapp.domain.usecase.GetSpatialAudioTracksUseCase
import com.nady.moviesapp.domain.usecase.GetTrendingRankedUseCase
import com.nady.moviesapp.domain.usecase.GetUserProfileUseCase
import com.nady.moviesapp.domain.usecase.SearchMoviesUseCase
import com.nady.moviesapp.domain.usecase.ToggleWatchlistUseCase
import com.nady.moviesapp.domain.usecase.UpdateAppSettingsUseCase
import com.nady.moviesapp.domain.usecase.UpdatePlaybackProgressUseCase

class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val applicationContext = context.applicationContext
    private val database by lazy { AppDatabase.getInstance(applicationContext) }
    private val movieDao by lazy { database.movieDao() }

    private val movieRepository by lazy { MovieRepositoryImpl(movieDao) }
    private val userPreferencesRepository by lazy { UserPreferencesRepositoryImpl(movieDao) }
    private val downloadsRepository by lazy { DownloadsRepositoryImpl(movieDao) }

    val moviesAppFunctions by lazy { com.nady.moviesapp.domain.agent.MoviesAppFunctions(movieRepository) }

    private val getHeroMoviesUseCase by lazy { GetHeroMoviesUseCase(movieRepository) }
    private val getContinueWatchingUseCase by lazy { GetContinueWatchingUseCase(movieRepository) }
    private val getTrendingRankedUseCase by lazy { GetTrendingRankedUseCase(movieRepository) }
    private val getSpatialAudioTracksUseCase by lazy { GetSpatialAudioTracksUseCase(movieRepository) }
    private val getRecommendedUseCase by lazy { GetRecommendedUseCase(movieRepository) }
    private val getMovieDetailsUseCase by lazy { GetMovieDetailsUseCase(movieRepository) }
    private val getGenreCategoriesUseCase by lazy { GetGenreCategoriesUseCase(movieRepository) }
    private val getSearchTrendsUseCase by lazy { GetSearchTrendsUseCase(movieRepository) }
    private val searchMoviesUseCase by lazy { SearchMoviesUseCase(movieRepository) }
    private val toggleWatchlistUseCase by lazy { ToggleWatchlistUseCase(movieRepository) }
    private val updatePlaybackProgressUseCase by lazy { UpdatePlaybackProgressUseCase(movieRepository) }
    private val getSavedUniversesUseCase by lazy { GetSavedUniversesUseCase(movieRepository) }
    private val getAppSettingsUseCase by lazy { GetAppSettingsUseCase(userPreferencesRepository) }
    private val updateAppSettingsUseCase by lazy { UpdateAppSettingsUseCase(userPreferencesRepository) }
    private val getUserProfileUseCase by lazy { GetUserProfileUseCase(userPreferencesRepository) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AppThemeViewModel::class.java) -> {
                AppThemeViewModel(getAppSettingsUseCase, updateAppSettingsUseCase) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    getHeroMoviesUseCase,
                    getContinueWatchingUseCase,
                    getTrendingRankedUseCase,
                    getSpatialAudioTracksUseCase,
                    getRecommendedUseCase,
                    toggleWatchlistUseCase
                ) as T
            }
            modelClass.isAssignableFrom(ExploreViewModel::class.java) -> {
                ExploreViewModel(
                    searchMoviesUseCase,
                    getGenreCategoriesUseCase,
                    getSearchTrendsUseCase,
                    getTrendingRankedUseCase
                ) as T
            }
            modelClass.isAssignableFrom(MovieDetailsViewModel::class.java) -> {
                MovieDetailsViewModel(
                    getMovieDetailsUseCase,
                    getRecommendedUseCase,
                    toggleWatchlistUseCase,
                    downloadsRepository
                ) as T
            }
            modelClass.isAssignableFrom(PlayerViewModel::class.java) -> {
                PlayerViewModel(
                    getMovieDetailsUseCase,
                    updatePlaybackProgressUseCase
                ) as T
            }
            modelClass.isAssignableFrom(MySpaceViewModel::class.java) -> {
                MySpaceViewModel(
                    getUserProfileUseCase,
                    getContinueWatchingUseCase,
                    getSavedUniversesUseCase,
                    downloadsRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
