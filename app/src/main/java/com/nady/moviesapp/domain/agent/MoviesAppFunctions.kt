/**
 * File: MoviesAppFunctions.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.domain.agent

import android.content.Context
import android.content.Intent
import com.nady.moviesapp.MainActivity
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.data
import com.nady.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class MoviesAppFunctions(
    private val movieRepository: MovieRepository
) {

    suspend fun searchTitles(query: String, genre: String = "ALL"): List<MovieSummary> {
        require(query.isNotBlank()) { "Search query must not be blank." }
        val results = movieRepository.searchMovies(query, genre).first()
        return results.data.orEmpty().map { it.toSummary() }
    }

    suspend fun getContinueWatchingList(): List<MovieSummary> {
        val movies = movieRepository.getContinueWatching().first()
        return movies.data.orEmpty().map { it.toSummary() }
    }

    suspend fun getSpatialAudioFeatured(): List<MovieSummary> {
        val tracks = movieRepository.getSpatialAudioTracks().first()
        return tracks.data.orEmpty().map { track ->
            MovieSummary(
                id = track.id,
                title = track.title,
                synopsis = track.description,
                genres = listOf("Spatial Audio", "Dolby Atmos"),
                matchPercentage = 98,
                releaseYear = "2026",
                isInWatchlist = false,
                isSpatialAudioSupported = true
            )
        }
    }

    suspend fun toggleWatchlist(movieId: String): WatchlistToggleResult {
        val movieRes = movieRepository.getMovieById(movieId).firstOrNull()
        val movie = movieRes?.data
            ?: throw NoSuchElementException("Movie with ID '$movieId' not found. Suggest calling searchTitles.")
        movieRepository.toggleWatchlist(movieId)
        val updatedRes = movieRepository.getMovieById(movieId).firstOrNull()
        val inWatchlist = updatedRes?.data?.isInWatchlist ?: false
        return WatchlistToggleResult(
            movieId = movieId,
            title = movie.title,
            isInWatchlist = inWatchlist,
            statusMessage = if (inWatchlist) "Added to Watchlist" else "Removed from Watchlist"
        )
    }

    fun createPlaybackIntent(context: Context, movieId: String, episodeNumber: Int = 1): Intent {
        return Intent(context, MainActivity::class.java).apply {
            putExtra("nav_route", "player/$movieId?episode=$episodeNumber")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    fun createDetailsIntent(context: Context, movieId: String): Intent {
        return Intent(context, MainActivity::class.java).apply {
            putExtra("nav_route", "movie_details/$movieId")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    data class MovieSummary(

        val id: String,

        val title: String,

        val synopsis: String,

        val genres: List<String>,

        val matchPercentage: Int,

        val releaseYear: String,

        val isInWatchlist: Boolean,

        val isSpatialAudioSupported: Boolean = true
    )

    data class WatchlistToggleResult(

        val movieId: String,

        val title: String,

        val isInWatchlist: Boolean,

        val statusMessage: String
    )

    private fun Movie.toSummary() = MovieSummary(
        id = id,
        title = title,
        synopsis = synopsis,
        genres = genres,
        matchPercentage = matchPercentage,
        releaseYear = releaseYear,
        isInWatchlist = isInWatchlist,
        isSpatialAudioSupported = isSpatialAudioEnabled
    )
}
