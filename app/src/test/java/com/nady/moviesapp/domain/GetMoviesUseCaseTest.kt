package com.nady.moviesapp.domain

import com.nady.moviesapp.domain.model.CategoryGenre
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.model.SearchTrend
import com.nady.moviesapp.domain.model.SpatialAudioTrack
import com.nady.moviesapp.domain.repository.MovieRepository
import com.nady.moviesapp.domain.usecase.GetHeroMoviesUseCase
import com.nady.moviesapp.domain.usecase.GetTrendingRankedUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetMoviesUseCaseTest {

    private val fakeMovie = Movie(
        id = "1",
        title = "Inception",
        posterUrl = "https://example.com/poster.jpg"
    )

    private val fakeRepository = object : MovieRepository {
        override fun getHeroMovies(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(fakeMovie)))
        override fun getContinueWatching(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(fakeMovie)))
        override fun getTrendingRanked(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(fakeMovie)))
        override fun getSpatialAudioTracks(): Flow<Resource<List<SpatialAudioTrack>>> = flowOf(Resource.Success(emptyList()))
        override fun getRecommended(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(fakeMovie)))
        override fun getMovieById(id: String): Flow<Resource<Movie?>> = flowOf(Resource.Success(fakeMovie))
        override fun getGenreCategories(): Flow<Resource<List<CategoryGenre>>> = flowOf(Resource.Success(emptyList()))
        override fun getSearchTrends(): Flow<Resource<List<SearchTrend>>> = flowOf(Resource.Success(emptyList()))
        override fun searchMovies(query: String, selectedFilter: String): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(fakeMovie)))
        override fun getSavedUniverses(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(fakeMovie)))
        override suspend fun toggleWatchlist(movieId: String): Resource<Unit> = Resource.Success(Unit)
        override suspend fun updatePlaybackProgress(movieId: String, episodeNumber: Int, progress: Float, timestamp: String): Resource<Unit> = Resource.Success(Unit)
        override fun isMovieInWatchlist(movieId: String): Flow<Resource<Boolean>> = flowOf(Resource.Success(true))
    }

    private val getHeroMoviesUseCase = GetHeroMoviesUseCase(fakeRepository)
    private val getTrendingRankedUseCase = GetTrendingRankedUseCase(fakeRepository)

    @Test
    fun `getHeroMovies returns success resource with movies`() = runTest {
        val result = getHeroMoviesUseCase().first()
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
        assertEquals("Inception", result.data[0].title)
    }

    @Test
    fun `getTrendingRanked returns trending movies`() = runTest {
        val result = getTrendingRankedUseCase().first()
        assertTrue(result is Resource.Success)
        assertEquals(1, (result as Resource.Success).data.size)
        assertEquals("Inception", result.data[0].title)
    }
}
