/**
 * File: MovieDetailsViewModel.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nady.moviesapp.domain.model.DownloadItem
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.usecase.GetMovieDetailsUseCase
import com.nady.moviesapp.domain.usecase.GetRecommendedUseCase
import com.nady.moviesapp.domain.usecase.ToggleWatchlistUseCase
import com.nady.moviesapp.domain.repository.DownloadsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getRecommendedUseCase: GetRecommendedUseCase,
    private val toggleWatchlistUseCase: ToggleWatchlistUseCase,
    private val downloadsRepository: DownloadsRepository
) : ViewModel() {

    private val _currentMovieId = MutableStateFlow("solaris_echo")
    val currentMovieId: StateFlow<String> = _currentMovieId.asStateFlow()

    private val _selectedTab = MutableStateFlow("Episodes")
    val selectedTab: StateFlow<String> = _selectedTab.asStateFlow()

    private val _selectedSeason = MutableStateFlow("Season 1 (8 Episodes)")
    val selectedSeason: StateFlow<String> = _selectedSeason.asStateFlow()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val movie: StateFlow<Movie?> = _currentMovieId.flatMapLatest { id ->
        getMovieDetailsUseCase(id)
    }.map { (it as? Resource.Success)?.data }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val recommendations: StateFlow<List<Movie>> = getRecommendedUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadMovie(movieId: String) {
        _currentMovieId.value = movieId
    }

    fun selectTab(tabName: String) {
        _selectedTab.value = tabName
    }

    fun selectSeason(season: String) {
        _selectedSeason.value = season
    }

    fun toggleWatchlist() {
        viewModelScope.launch {
            toggleWatchlistUseCase(_currentMovieId.value)
        }
    }

    fun downloadEpisode(episodeTitle: String) {
        viewModelScope.launch {
            val current = movie.value ?: return@launch
            downloadsRepository.addDownload(
                DownloadItem(
                    id = "dl_${System.currentTimeMillis()}",
                    movieId = current.id,
                    title = current.title,
                    subtitle = episodeTitle,
                    formatBadge = "8K Spatial",
                    fileSizeText = "5.1 GB",
                    posterUrl = current.posterUrl,
                    expiryText = "Downloaded • Spatial 360°"
                )
            )
        }
    }
}
