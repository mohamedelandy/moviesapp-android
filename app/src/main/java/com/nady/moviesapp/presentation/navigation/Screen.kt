/**
 * File: Screen.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Explore : Screen("explore")
    data object SpatialLounge : Screen("spatial_lounge")
    data object Downloads : Screen("downloads")
    data object MySpace : Screen("my_space")
    data object MovieDetails : Screen("movie_details/{movieId}") {
        const val ARG_MOVIE_ID = "movieId"
        fun createRoute(movieId: String): String = "movie_details/$movieId"
    }
    data object Player : Screen("player/{movieId}?episode={episode}") {
        const val ARG_MOVIE_ID = "movieId"
        const val ARG_EPISODE = "episode"
        fun createRoute(movieId: String, episode: Int = 1): String = "player/$movieId?episode=$episode"
    }
    data object WidgetsShowcase : Screen("widgets_showcase")
}
