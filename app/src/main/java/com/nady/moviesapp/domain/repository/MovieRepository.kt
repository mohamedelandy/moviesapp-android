/**
 * File: MovieRepository.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.domain.repository

import com.nady.moviesapp.domain.model.CategoryGenre
import com.nady.moviesapp.domain.model.Episode
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.SearchTrend
import com.nady.moviesapp.domain.model.SpatialAudioTrack
import com.nady.moviesapp.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getHeroMovies(): Flow<Resource<List<Movie>>>
    fun getContinueWatching(): Flow<Resource<List<Movie>>>
    fun getTrendingRanked(): Flow<Resource<List<Movie>>>
    fun getSpatialAudioTracks(): Flow<Resource<List<SpatialAudioTrack>>>
    fun getRecommended(): Flow<Resource<List<Movie>>>
    fun getMovieById(id: String): Flow<Resource<Movie?>>
    fun getGenreCategories(): Flow<Resource<List<CategoryGenre>>>
    fun getSearchTrends(): Flow<Resource<List<SearchTrend>>>
    fun searchMovies(query: String, selectedFilter: String): Flow<Resource<List<Movie>>>
    fun getSavedUniverses(): Flow<Resource<List<Movie>>>
    suspend fun toggleWatchlist(movieId: String): Resource<Unit>
    suspend fun updatePlaybackProgress(movieId: String, episodeNumber: Int, progress: Float, timestamp: String): Resource<Unit>
    fun isMovieInWatchlist(movieId: String): Flow<Resource<Boolean>>
}
