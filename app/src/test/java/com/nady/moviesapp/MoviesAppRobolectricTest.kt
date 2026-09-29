package com.nady.moviesapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.nady.moviesapp.data.datasource.MovieStaticDataSource
import com.nady.moviesapp.domain.model.AppThemeMode
import com.nady.moviesapp.domain.model.SupportedLanguage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Movies App Showcase", appName)
    }

    @Test
    fun `verify movie static repository contains featured titles and episodes`() {
        val movies = MovieStaticDataSource.allMovies
        assertTrue(movies.isNotEmpty())

        val solaris = movies.find { it.id == "solaris_echo" }
        assertNotNull("Solaris Echo must exist in catalog", solaris)
        assertEquals(3, solaris?.episodes?.size)
        assertTrue(solaris?.cast?.isNotEmpty() == true)
        assertTrue(solaris?.audioSpec?.contains("Dolby") == true)
    }

    @Test
    fun `verify RTL detection for supported languages`() {
        assertTrue(SupportedLanguage.ARABIC.isRtl)
        assertTrue(!SupportedLanguage.ENGLISH.isRtl)
        assertTrue(!SupportedLanguage.GREEK.isRtl)
    }

    @Test
    fun `verify spatial audio lounge tracks availability`() {
        val tracks = MovieStaticDataSource.spatialAudioTracks
        assertTrue("Spatial tracks must be populated", tracks.isNotEmpty())
        assertEquals("sp_1", tracks.first().id)
    }
}
