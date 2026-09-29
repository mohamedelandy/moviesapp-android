package com.nady.moviesapp.presentation

import com.nady.moviesapp.domain.model.CategoryGenre
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.Resource
import com.nady.moviesapp.domain.model.SearchTrend
import com.nady.moviesapp.domain.model.SpatialAudioTrack
import com.nady.moviesapp.domain.repository.MovieRepository
import com.nady.moviesapp.domain.usecase.GetContinueWatchingUseCase
import com.nady.moviesapp.domain.usecase.GetHeroMoviesUseCase
import com.nady.moviesapp.domain.usecase.GetRecommendedUseCase
import com.nady.moviesapp.domain.usecase.GetSpatialAudioTracksUseCase
import com.nady.moviesapp.domain.usecase.GetTrendingRankedUseCase
import com.nady.moviesapp.domain.usecase.ToggleWatchlistUseCase
import com.nady.moviesapp.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    // A dedicated scheduler, deliberately NOT shared with runTest. HomeViewModel.init
    // launches an unbounded `while (true) { delay(4500) }` carousel loop on
    // Dispatchers.Main. If runTest drained this same scheduler, that loop would
    // re-schedule itself forever and the test would never finish.
    private val testDispatcher = StandardTestDispatcher()

    private val fakeMovie = Movie(
        id = "movie-1",
        title = "Solaris Echo",
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

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(
            getHeroMoviesUseCase = GetHeroMoviesUseCase(fakeRepository),
            getContinueWatchingUseCase = GetContinueWatchingUseCase(fakeRepository),
            getTrendingRankedUseCase = GetTrendingRankedUseCase(fakeRepository),
            getSpatialAudioTracksUseCase = GetSpatialAudioTracksUseCase(fakeRepository),
            getRecommendedUseCase = GetRecommendedUseCase(fakeRepository),
            toggleWatchlistUseCase = ToggleWatchlistUseCase(fakeRepository)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `viewModel exposes heroMovies and trendingRanked from repository`() {
        // The stateIn flows use SharingStarted.WhileSubscribed, so they only start
        // collecting once something observes them. Collecting on a plain coroutine
        // (not inside runTest) keeps the unbounded carousel loop off runTest's
        // scheduler, so this test cannot hang.
        val subscribers = CoroutineScope(Dispatchers.Main + Job()).let { scope ->
            listOf(
                scope.launch { viewModel.heroMovies.collect {} },
                scope.launch { viewModel.trendingRanked.collect {} }
            )
        }

        // Bounded advance: the carousel loop re-schedules itself forever, so
        // advanceUntilIdle() would never return.
        testDispatcher.scheduler.advanceTimeBy(100)

        assertEquals(1, viewModel.heroMovies.value.size)
        assertEquals("Solaris Echo", viewModel.heroMovies.value[0].title)
        assertEquals(1, viewModel.trendingRanked.value.size)

        subscribers.forEach { it.cancel() }
    }
}
