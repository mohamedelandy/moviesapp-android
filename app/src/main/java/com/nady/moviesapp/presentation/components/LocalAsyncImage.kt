/**
 * File: LocalAsyncImage.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * Drop-in replacement for [AsyncImage] that understands two kinds of sources:
 *  - plain resource names (e.g. "poster_solaris_hero") resolve against the
 *    bundled res/drawable-nodpi assets, fully offline;
 *  - absolute http(s) URLs are delegated to Coil as before.
 *
 * All static catalog images are bundled locally, so the app renders without
 * any network access; remote loading is kept for backward compatibility.
 */
@Composable
fun LocalAsyncImage(
    model: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val resolved: Any? = when {
        model.isNullOrBlank() -> android.R.drawable.ic_menu_report_image
        !model.contains("://") -> context.resources.getIdentifier(
            model,
            "drawable",
            context.packageName
        ).takeIf { it != 0 } ?: android.R.drawable.ic_menu_report_image
        else -> model
    }
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(resolved)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}
