/**
 * File: MainActivity.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nady.moviesapp.presentation.components.LanguageThemeDialog
import com.nady.moviesapp.presentation.components.LiquidBottomDock
import com.nady.moviesapp.presentation.components.WidgetsShowcaseScreen
import com.nady.moviesapp.presentation.navigation.Screen
import com.nady.moviesapp.presentation.screens.details.MovieDetailsScreen
import com.nady.moviesapp.presentation.screens.explore.ExploreScreen
import com.nady.moviesapp.presentation.screens.home.HomeScreen
import com.nady.moviesapp.presentation.screens.myspace.MySpaceScreen
import com.nady.moviesapp.presentation.screens.player.PlayerScreen
import com.nady.moviesapp.presentation.screens.spatial.SpatialLoungeScreen
import com.nady.moviesapp.presentation.viewmodel.AppThemeViewModel
import com.nady.moviesapp.presentation.viewmodel.ExploreViewModel
import com.nady.moviesapp.presentation.viewmodel.HomeViewModel
import com.nady.moviesapp.presentation.viewmodel.MovieDetailsViewModel
import com.nady.moviesapp.presentation.viewmodel.MySpaceViewModel
import com.nady.moviesapp.presentation.viewmodel.PlayerViewModel
import com.nady.moviesapp.presentation.viewmodel.ViewModelFactory
import com.nady.moviesapp.ui.theme.MoviesAppTheme

class MainActivity : ComponentActivity() {

    private var pendingNavRoute: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        pendingNavRoute = intent?.getStringExtra("nav_route")

        val viewModelFactory = ViewModelFactory(applicationContext)

        setContent {
            val appThemeViewModel: AppThemeViewModel = viewModel(factory = viewModelFactory)
            val appSettings by appThemeViewModel.appSettings.collectAsState()
            val currentThemeMode = appSettings.themeMode
            val currentLanguage = appSettings.language

            val layoutDirection = if (currentLanguage.isRtl) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            MoviesAppTheme(themeMode = currentThemeMode) {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                    MoviesAppMain(
                        viewModelFactory = viewModelFactory,
                        appThemeViewModel = appThemeViewModel,
                        initialNavRoute = pendingNavRoute
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val route = intent.getStringExtra("nav_route")
        if (!route.isNullOrBlank()) {
            pendingNavRoute = route
        }
    }
}

@Composable
fun MoviesAppMain(
    viewModelFactory: ViewModelFactory,
    appThemeViewModel: AppThemeViewModel,
    initialNavRoute: String?
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    var showLanguageThemeDialog by remember { mutableStateOf(false) }
    var showWidgetsModal by remember { mutableStateOf(false) }

    val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
    val exploreViewModel: ExploreViewModel = viewModel(factory = viewModelFactory)
    val detailsViewModel: MovieDetailsViewModel = viewModel(factory = viewModelFactory)
    val playerViewModel: PlayerViewModel = viewModel(factory = viewModelFactory)
    val mySpaceViewModel: MySpaceViewModel = viewModel(factory = viewModelFactory)

    val appSettings by appThemeViewModel.appSettings.collectAsState()
    val currentThemeMode = appSettings.themeMode
    val currentLanguage = appSettings.language

    LaunchedEffect(initialNavRoute) {
        if (!initialNavRoute.isNullOrBlank()) {
            navController.navigate(initialNavRoute) {
                launchSingleTop = true
            }
        }
    }

    val isTopLevelDestination = currentRoute in listOf(
        Screen.Home.route,
        Screen.Explore.route,
        Screen.SpatialLounge.route,
        Screen.MySpace.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = homeViewModel,
                        onMovieClick = { movieId ->
                            navController.navigate(Screen.MovieDetails.createRoute(movieId))
                        },
                        onPlayMovie = { movieId ->
                            navController.navigate(Screen.Player.createRoute(movieId, 1))
                        },
                        onOpenLanguageTheme = { showLanguageThemeDialog = true },
                        onOpenWidgets = { showWidgetsModal = true }
                    )
                }

                composable(Screen.Explore.route) {
                    ExploreScreen(
                        viewModel = exploreViewModel,
                        onMovieClick = { movieId ->
                            navController.navigate(Screen.MovieDetails.createRoute(movieId))
                        }
                    )
                }

                composable(Screen.SpatialLounge.route) {
                    SpatialLoungeScreen(
                        viewModel = homeViewModel,
                        onPlayTrack = { trackId ->
                            navController.navigate(Screen.Player.createRoute("solaris_echo", 1))
                        }
                    )
                }

                composable(Screen.MySpace.route) {
                    MySpaceScreen(
                        viewModel = mySpaceViewModel,
                        onMovieClick = { movieId ->
                            navController.navigate(Screen.MovieDetails.createRoute(movieId))
                        },
                        onPlayMovie = { movieId ->
                            navController.navigate(Screen.Player.createRoute(movieId, 1))
                        },
                        onOpenLanguageTheme = { showLanguageThemeDialog = true },
                        onOpenWidgets = { showWidgetsModal = true }
                    )
                }

                composable(
                    route = Screen.MovieDetails.route,
                    arguments = listOf(
                        navArgument(Screen.MovieDetails.ARG_MOVIE_ID) { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString(Screen.MovieDetails.ARG_MOVIE_ID) ?: "solaris_echo"
                    MovieDetailsScreen(
                        movieId = movieId,
                        viewModel = detailsViewModel,
                        onBack = { navController.popBackStack() },
                        onPlayEpisode = { targetMovieId, episodeNum ->
                            navController.navigate(Screen.Player.createRoute(targetMovieId, episodeNum))
                        }
                    )
                }

                composable(
                    route = Screen.Player.route,
                    arguments = listOf(
                        navArgument(Screen.Player.ARG_MOVIE_ID) { type = NavType.StringType },
                        navArgument(Screen.Player.ARG_EPISODE) {
                            type = NavType.IntType
                            defaultValue = 1
                        }
                    )
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString(Screen.Player.ARG_MOVIE_ID) ?: "solaris_echo"
                    val episodeNumber = backStackEntry.arguments?.getInt(Screen.Player.ARG_EPISODE) ?: 1
                    PlayerScreen(
                        movieId = movieId,
                        episodeNumber = episodeNumber,
                        viewModel = playerViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            AnimatedVisibility(
                visible = isTopLevelDestination,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                LiquidBottomDock(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }

    if (showLanguageThemeDialog) {
        LanguageThemeDialog(
            currentTheme = currentThemeMode,
            currentLanguage = currentLanguage,
            onSelectTheme = { mode -> appThemeViewModel.setThemeMode(mode) },
            onSelectLanguage = { lang -> appThemeViewModel.setLanguage(lang) },
            onDismiss = { showLanguageThemeDialog = false }
        )
    }

    if (showWidgetsModal) {
        WidgetsShowcaseScreen(
            onMovieClick = { movieId ->
                showWidgetsModal = false
                navController.navigate(Screen.MovieDetails.createRoute(movieId))
            },
            onClose = { showWidgetsModal = false }
        )
    }
}
