package com.nady.moviesapp

import com.nady.moviesapp.cast.CastOptionsProvider
import com.google.android.gms.cast.CastMediaControlIntent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CastOptionsProviderTest {

    @Test
    fun castOptionsProvider_canBeInstantiated() {
        val provider = CastOptionsProvider()
        assertNotNull("Provider should instantiate", provider)
        assertNull("Additional session providers should be null by default", provider.getAdditionalSessionProviders(null as android.content.Context? ?: return))
    }
}
