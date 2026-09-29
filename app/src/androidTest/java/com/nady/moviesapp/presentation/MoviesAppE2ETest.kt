package com.nady.moviesapp.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nady.moviesapp.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * End-to-End UI instrumented tests covering critical user flows:
 * - App launch & Home Screen rendering
 * - Multi-tab navigation across Home, Explore, and MySpace
 * - Movie details flow & playback transition
 * - Watchlist interaction
 */
@RunWith(AndroidJUnit4::class)
class MoviesAppE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAppLaunchesAndDisplaysHomeScreen() {
        // Verify Home Screen root and key components are displayed
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("nav_home").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_hero_play").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_hero_details").assertIsDisplayed()
    }

    @Test
    fun testTabNavigationEndToEnd() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()

        // Navigate to Explore
        composeTestRule.onNodeWithTag("nav_explore").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("explore_screen").assertIsDisplayed()

        // Navigate to My Space
        composeTestRule.onNodeWithTag("nav_myspace").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("myspace_screen").assertIsDisplayed()

        // Navigate back to Home
        composeTestRule.onNodeWithTag("nav_home").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }

    @Test
    fun testMovieDetailsAndPlaybackFlow() {
        composeTestRule.waitForIdle()

        // Click Hero details button
        composeTestRule.onNodeWithTag("btn_hero_details").performClick()
        composeTestRule.waitForIdle()

        // Verify Movie Details Screen is displayed
        composeTestRule.onNodeWithTag("movie_details_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_details_play_primary").assertIsDisplayed()

        // Click Play button to enter Player
        composeTestRule.onNodeWithTag("btn_details_play_primary").performClick()
        composeTestRule.waitForIdle()

        // Verify Player Screen is displayed
        composeTestRule.onNodeWithTag("player_screen").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_player_back").assertIsDisplayed()

        // Navigate back to Details
        composeTestRule.onNodeWithTag("btn_player_back").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("movie_details_screen").assertIsDisplayed()

        // Navigate back to Home
        composeTestRule.onNodeWithTag("btn_details_back").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("home_screen").assertIsDisplayed()
    }

    @Test
    fun testWatchlistToggleInDetails() {
        composeTestRule.waitForIdle()

        // Open details
        composeTestRule.onNodeWithTag("btn_hero_details").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("movie_details_screen").assertIsDisplayed()

        // Toggle watchlist button
        composeTestRule.onNodeWithTag("btn_details_watchlist").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_details_watchlist").performClick()
        composeTestRule.waitForIdle()
    }
}
