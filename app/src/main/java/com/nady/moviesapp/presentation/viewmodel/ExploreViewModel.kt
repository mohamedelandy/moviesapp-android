/**
 * File: ExploreViewModel.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nady.moviesapp.domain.model.CategoryGenre
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.SearchTrend
import com.nady.moviesapp.domain.usecase.GetGenreCategoriesUseCase
import com.nady.moviesapp.domain.usecase.GetSearchTrendsUseCase
import com.nady.moviesapp.domain.usecase.GetTrendingRankedUseCase
import com.nady.moviesapp.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getGenreCategoriesUseCase: GetGenreCategoriesUseCase,
    private val getSearchTrendsUseCase: GetSearchTrendsUseCase,
    private val getTrendingRankedUseCase: GetTrendingRankedUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    val genreCategories: StateFlow<List<CategoryGenre>> = getGenreCategoriesUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchTrends: StateFlow<List<SearchTrend>> = getSearchTrendsUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topRankedSpatial: StateFlow<List<Movie>> = getTrendingRankedUseCase().mapNotNull { (it as? Resource.Success)?.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchResults: StateFlow<List<Movie>> = combine(_searchQuery, _selectedFilter) { query, filter ->
        Pair(query, filter)
    }.flatMapLatest { (query, filter) ->
        searchMoviesUseCase(query, filter)
    }.mapNotNull { (it as? Resource.Success)?.data }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelect(filter: String) {
        _selectedFilter.value = filter
    }

    fun onTrendClick(trendQuery: String) {
        _searchQuery.value = trendQuery
    }
}
