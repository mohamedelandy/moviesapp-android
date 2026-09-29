package com.nady.moviesapp

import com.nady.moviesapp.data.datasource.MovieStaticDataSource
import com.nady.moviesapp.domain.agent.MoviesAppFunctions
import com.nady.moviesapp.domain.model.CategoryGenre
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.model.SearchTrend
import com.nady.moviesapp.domain.model.SpatialAudioTrack
import com.nady.moviesapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MoviesAppFunctionsTest {

    private lateinit var appFunctions: MoviesAppFunctions
    private lateinit var fakeMovieRepository: FakeMovieRepository

    @Before
    fun setup() {
        fakeMovieRepository = FakeMovieRepository()
        appFunctions = MoviesAppFunctions(fakeMovieRepository)
    }

    @Test
    fun searchTitles_returnsMatchingMovies() = runTest {
        val results = appFunctions.searchTitles("Solaris")
        assertFalse("Results should not be empty", results.isEmpty())
        assertTrue("Results should contain Solaris Echo", results.any { it.title.contains("Solaris", ignoreCase = true) })
        assertEquals("solaris_echo", results.first().id)
        assertTrue("Should have non-empty synopsis", results.first().synopsis.isNotBlank())
    }

    @Test(expected = IllegalArgumentException::class)
    fun searchTitles_blankQuery_throwsException() = runTest {
        appFunctions.searchTitles("   ")
    }

    @Test
    fun getContinueWatchingList_returnsInProgressItems() = runTest {
        val continueWatching = appFunctions.getContinueWatchingList()
        assertFalse("Continue watching list should not be empty", continueWatching.isEmpty())
        assertNotNull("Should contain valid movie summary", continueWatching.first().title)
    }

    @Test
    fun getSpatialAudioFeatured_returns360AudioTitles() = runTest {
        val spatialTracks = appFunctions.getSpatialAudioFeatured()
        assertFalse("Spatial audio catalog should not be empty", spatialTracks.isEmpty())
        assertTrue("Should have spatial support flag", spatialTracks.first().isSpatialAudioSupported)
        assertEquals("sp_1", spatialTracks.first().id)
    }

    @Test
    fun toggleWatchlist_modifiesWatchlistStatus() = runTest {
        val initialStatus = fakeMovieRepository.isWatchlisted
        val result = appFunctions.toggleWatchlist("solaris_echo")

        assertEquals("solaris_echo", result.movieId)
        assertEquals("Solaris Echo", result.title)
        assertEquals(!initialStatus, result.isInWatchlist)
    }

    @Test(expected = NoSuchElementException::class)
    fun toggleWatchlist_nonExistentMovie_throwsException() = runTest {
        appFunctions.toggleWatchlist("unknown_movie_xyz")
    }

    private class FakeMovieRepository : MovieRepository {
        var isWatchlisted = false
        val testMovie = MovieStaticDataSource.allMovies.first().copy(id = "solaris_echo", title = "Solaris Echo")

        override fun getHeroMovies(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(testMovie)))
        override fun getContinueWatching(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(testMovie)))
        override fun getTrendingRanked(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(testMovie)))
        override fun getSpatialAudioTracks(): Flow<Resource<List<SpatialAudioTrack>>> = flowOf(Resource.Success(MovieStaticDataSource.spatialAudioTracks))
        override fun getRecommended(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(listOf(testMovie)))
        override fun getMovieById(id: String): Flow<Resource<Movie?>> = flowOf(
            Resource.Success(if (id == "solaris_echo") testMovie.copy(isInWatchlist = isWatchlisted) else null)
        )
        override fun getGenreCategories(): Flow<Resource<List<CategoryGenre>>> = flowOf(Resource.Success(emptyList()))
        override fun getSearchTrends(): Flow<Resource<List<SearchTrend>>> = flowOf(Resource.Success(emptyList()))
        override fun searchMovies(query: String, selectedFilter: String): Flow<Resource<List<Movie>>> = flowOf(
            Resource.Success(if (query.contains("Solaris", ignoreCase = true)) listOf(testMovie) else emptyList())
        )
        override fun getSavedUniverses(): Flow<Resource<List<Movie>>> = flowOf(Resource.Success(emptyList()))
        override suspend fun toggleWatchlist(movieId: String): Resource<Unit> {
            isWatchlisted = !isWatchlisted
            return Resource.Success(Unit)
        }
        override suspend fun updatePlaybackProgress(movieId: String, episodeNumber: Int, progress: Float, timestamp: String): Resource<Unit> = Resource.Success(Unit)
        override fun isMovieInWatchlist(movieId: String): Flow<Resource<Boolean>> = flowOf(Resource.Success(isWatchlisted))
    }
}
