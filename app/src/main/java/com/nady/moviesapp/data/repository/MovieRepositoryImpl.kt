/**
 * File: MovieRepositoryImpl.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.data.repository

import com.nady.moviesapp.data.datasource.MovieStaticDataSource
import com.nady.moviesapp.data.local.MovieDao
import com.nady.moviesapp.data.local.entities.PlaybackProgressEntity
import com.nady.moviesapp.data.local.entities.WatchlistEntity
import com.nady.moviesapp.domain.model.CategoryGenre
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.SearchTrend
import com.nady.moviesapp.domain.model.SpatialAudioTrack
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch

class MovieRepositoryImpl(
    private val movieDao: MovieDao
) : MovieRepository {

    override fun getHeroMovies(): Flow<Resource<List<Movie>>> {
        return movieDao.getAllWatchlist().map { watchlistEntities ->
            val watchlistIds = watchlistEntities.map { it.movieId }.toSet()
            val list = MovieStaticDataSource.allMovies.filter { it.isHero }.map {
                it.copy(isInWatchlist = watchlistIds.contains(it.id))
            }
            Resource.Success(list) as Resource<List<Movie>>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
    }

    override fun getContinueWatching(): Flow<Resource<List<Movie>>> {
        return combine(
            movieDao.getAllPlaybackProgress(),
            movieDao.getAllWatchlist()
        ) { progressEntities, watchlistEntities ->
            val progressMap = progressEntities.associateBy { it.movieId }
            val watchlistIds = watchlistEntities.map { it.movieId }.toSet()

            val baseList = MovieStaticDataSource.allMovies.filter {
                it.id in listOf("neon_rim_nightfall", "singularity_horizon", "shadow_protocol", "solaris_echo")
            }

            val list = baseList.map { movie ->
                val progress = progressMap[movie.id]
                val isInWatchlist = watchlistIds.contains(movie.id)
                if (progress != null) {
                    movie.copy(
                        durationText = "${(progress.progress * 100).toInt()}% • ${progress.timestamp}",
                        isInWatchlist = isInWatchlist
                    )
                } else {
                    movie.copy(isInWatchlist = isInWatchlist)
                }
            }
            Resource.Success(list) as Resource<List<Movie>>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
    }

    override fun getTrendingRanked(): Flow<Resource<List<Movie>>> {
        return movieDao.getAllWatchlist().map { watchlistEntities ->
            val watchlistIds = watchlistEntities.map { it.movieId }.toSet()
            val list = MovieStaticDataSource.allMovies
                .filter { it.isTrending }
                .sortedBy { it.rank }
                .map { it.copy(isInWatchlist = watchlistIds.contains(it.id)) }
            Resource.Success(list) as Resource<List<Movie>>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
    }

    override fun getSpatialAudioTracks(): Flow<Resource<List<SpatialAudioTrack>>> = flow {
        emit(Resource.Success(MovieStaticDataSource.spatialAudioTracks) as Resource<List<SpatialAudioTrack>>)
    }.catch { emit(Resource.Error(it.message ?: "Error", it)) }

    override fun getRecommended(): Flow<Resource<List<Movie>>> {
        return movieDao.getAllWatchlist().map { watchlistEntities ->
            val watchlistIds = watchlistEntities.map { it.movieId }.toSet()
            val list = MovieStaticDataSource.allMovies
                .filter { it.id in listOf("replicant_dawn", "neural_noir", "aero_city_2088", "cold_sleep_0") }
                .map { it.copy(isInWatchlist = watchlistIds.contains(it.id)) }
            Resource.Success(list) as Resource<List<Movie>>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
    }

    override fun getMovieById(id: String): Flow<Resource<Movie?>> {
        return combine(
            movieDao.isMovieInWatchlist(id),
            movieDao.getPlaybackProgress(id)
        ) { inWatchlist, progress ->
            val found = MovieStaticDataSource.allMovies.find { it.id == id }
                ?: MovieStaticDataSource.allMovies.firstOrNull()
            val movie = found?.copy(
                isInWatchlist = inWatchlist,
                episodes = found.episodes.map { ep ->
                    if (progress != null && ep.episodeNumber == progress.episodeNumber) {
                        ep.copy(
                            progressFraction = progress.progress,
                            resumeTimestamp = progress.timestamp
                        )
                    } else ep
                }
            )
            Resource.Success(movie) as Resource<Movie?>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
    }

    override fun getGenreCategories(): Flow<Resource<List<CategoryGenre>>> = flow {
        emit(Resource.Success(MovieStaticDataSource.genreCategories) as Resource<List<CategoryGenre>>)
    }.catch { emit(Resource.Error(it.message ?: "Error", it)) }

    override fun getSearchTrends(): Flow<Resource<List<SearchTrend>>> = flow {
        emit(Resource.Success(MovieStaticDataSource.searchTrends) as Resource<List<SearchTrend>>)
    }.catch { emit(Resource.Error(it.message ?: "Error", it)) }

    override fun searchMovies(query: String, selectedFilter: String): Flow<Resource<List<Movie>>> {
        return movieDao.getAllWatchlist().map { watchlistEntities ->
            val watchlistIds = watchlistEntities.map { it.movieId }.toSet()
            val cleanQuery = query.trim().lowercase()

            val list = MovieStaticDataSource.allMovies
                .filter { movie ->
                    val matchesQuery = cleanQuery.isEmpty() ||
                            movie.title.lowercase().contains(cleanQuery) ||
                            movie.synopsis.lowercase().contains(cleanQuery) ||
                            movie.genres.any { it.lowercase().contains(cleanQuery) }

                    val matchesFilter = when (selectedFilter.lowercase()) {
                        "all", "" -> true
                        "spatial 3d" -> movie.isSpatial3D
                        "cyberpunk" -> movie.genres.any { it.contains("cyber", ignoreCase = true) }
                        "deep space" -> movie.genres.any { it.contains("space", ignoreCase = true) }
                        "imax enhanced" -> movie.audioSpec.contains("imax", ignoreCase = true)
                        "anime" -> movie.genres.any { it.contains("anime", ignoreCase = true) }
                        "dolby atmos" -> movie.audioSpec.contains("atmos", ignoreCase = true)
                        "award winning" -> movie.matchPercentage >= 95
                        else -> true
                    }

                    matchesQuery && matchesFilter
                }
                .map { it.copy(isInWatchlist = watchlistIds.contains(it.id)) }
            Resource.Success(list) as Resource<List<Movie>>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
    }

    override fun getSavedUniverses(): Flow<Resource<List<Movie>>> {
        return movieDao.getAllWatchlist().map { watchlistEntities ->
            val watchlistIds = watchlistEntities.map { it.movieId }.toSet()

            val candidates = if (watchlistIds.isNotEmpty()) {
                MovieStaticDataSource.allMovies.filter { watchlistIds.contains(it.id) }
            } else {
                MovieStaticDataSource.allMovies.filter { it.id in listOf("cold_sleep_0", "aero_city_2088", "neural_noir") }
            }
            val list = candidates.map { it.copy(isInWatchlist = true) }
            Resource.Success(list) as Resource<List<Movie>>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
    }

    override suspend fun toggleWatchlist(movieId: String): Resource<Unit> {
        return try {
            val currentStatus = MovieStaticDataSource.allMovies.any { it.id == movieId && it.isInWatchlist }
            if (currentStatus) {
                movieDao.deleteFromWatchlist(movieId)
            } else {
                movieDao.insertWatchlist(WatchlistEntity(movieId))
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error", e)
        }
    }

    override suspend fun updatePlaybackProgress(
        movieId: String,
        episodeNumber: Int,
        progress: Float,
        timestamp: String
    ): Resource<Unit> {
        return try {
            movieDao.savePlaybackProgress(
                PlaybackProgressEntity(
                    movieId = movieId,
                    episodeNumber = episodeNumber,
                    progress = progress,
                    timestamp = timestamp
                )
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error", e)
        }
    }

    override fun isMovieInWatchlist(movieId: String): Flow<Resource<Boolean>> =
        movieDao.isMovieInWatchlist(movieId).map {
            Resource.Success(it) as Resource<Boolean>
        }.catch { emit(Resource.Error(it.message ?: "Error", it)) }
}
