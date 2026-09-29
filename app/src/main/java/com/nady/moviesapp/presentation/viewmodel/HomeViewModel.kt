/**
 * File: HomeViewModel.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.model.SpatialAudioTrack
import com.nady.moviesapp.domain.usecase.GetContinueWatchingUseCase
import com.nady.moviesapp.domain.usecase.GetHeroMoviesUseCase
import com.nady.moviesapp.domain.usecase.GetRecommendedUseCase
import com.nady.moviesapp.domain.usecase.GetSpatialAudioTracksUseCase
import com.nady.moviesapp.domain.usecase.GetTrendingRankedUseCase
import com.nady.moviesapp.domain.usecase.ToggleWatchlistUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val currentHeroIndex: Int = 0,
    val heroMovies: List<Movie> = emptyList(),
    val continueWatching: List<Movie> = emptyList(),
    val trendingRanked: List<Movie> = emptyList(),
    val spatialAudioTracks: List<SpatialAudioTrack> = emptyList(),
    val recommendedMovies: List<Movie> = emptyList(),
    val isAutoLooping: Boolean = true
)

class HomeViewModel(
    private val getHeroMoviesUseCase: GetHeroMoviesUseCase,
    private val getContinueWatchingUseCase: GetContinueWatchingUseCase,
    private val getTrendingRankedUseCase: GetTrendingRankedUseCase,
    private val getSpatialAudioTracksUseCase: GetSpatialAudioTracksUseCase,
    private val getRecommendedUseCase: GetRecommendedUseCase,
    private val toggleWatchlistUseCase: ToggleWatchlistUseCase
) : ViewModel() {

    private val _heroIndex = MutableStateFlow(0)
    val heroIndex: StateFlow<Int> = _heroIndex.asStateFlow()

    val heroMovies: StateFlow<List<Movie>> = getHeroMoviesUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val continueWatching: StateFlow<List<Movie>> = getContinueWatchingUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingRanked: StateFlow<List<Movie>> = getTrendingRankedUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val spatialAudioTracks: StateFlow<List<SpatialAudioTrack>> = getSpatialAudioTracksUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recommendedMovies: StateFlow<List<Movie>> = getRecommendedUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        startHeroCarouselLoop()
    }

    private fun startHeroCarouselLoop() {
        viewModelScope.launch {
            while (true) {
                delay(4500)
                val count = heroMovies.value.size
                if (count > 1) {
                    _heroIndex.value = (_heroIndex.value + 1) % count
                }
            }
        }
    }

    fun selectHeroIndex(index: Int) {
        _heroIndex.value = index
    }

    fun toggleWatchlist(movieId: String) {
        viewModelScope.launch {
            toggleWatchlistUseCase(movieId)
        }
    }
}
