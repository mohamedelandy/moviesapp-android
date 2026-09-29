/**
 * File: HomeScreen.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nady.moviesapp.presentation.components.LocalAsyncImage
import com.nady.moviesapp.R
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.presentation.components.ContinueWatchingCard
import com.nady.moviesapp.presentation.components.HolographicRankCard
import com.nady.moviesapp.presentation.components.SpatialAudioCard
import com.nady.moviesapp.presentation.components.StandardMovieCard
import com.nady.moviesapp.presentation.viewmodel.HomeViewModel
import com.nady.moviesapp.ui.theme.BrandAccent
import com.nady.moviesapp.ui.theme.BrandAccentGlow
import com.nady.moviesapp.ui.theme.SpatialCyan
import com.nady.moviesapp.ui.theme.spatialColors

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieClick: (String) -> Unit,
    onPlayMovie: (String) -> Unit,
    onOpenLanguageTheme: () -> Unit,
    onOpenWidgets: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heroMovies by viewModel.heroMovies.collectAsState()
    val currentHeroIndex by viewModel.heroIndex.collectAsState()
    val continueWatching by viewModel.continueWatching.collectAsState()
    val trendingRanked by viewModel.trendingRanked.collectAsState()
    val spatialTracks by viewModel.spatialAudioTracks.collectAsState()
    val recommended by viewModel.recommendedMovies.collectAsState()

    val extendedColors = MaterialTheme.spatialColors
    val currentHero = heroMovies.getOrNull(currentHeroIndex) ?: heroMovies.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BrandAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "N",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Column {
                            Text(
                                text = "MOVIES APP",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "SHOWCASE • SPATIAL",
                                color = BrandAccentGlow,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = onOpenWidgets,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                                .testTag("btn_home_widgets")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Widgets,
                                contentDescription = "Widgets Showcase",
                                tint = BrandAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = onOpenLanguageTheme,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                                .testTag("btn_home_language_theme")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = "Language and Theme",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            if (currentHero != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.82f)
                        .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                        .testTag("hero_carousel")
                ) {

                    LocalAsyncImage(
                        model = currentHero.backdropUrl.ifEmpty { currentHero.posterUrl },
                        contentDescription = currentHero.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Black.copy(alpha = 0.55f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f),
                                        Color.Black.copy(alpha = 0.96f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if (currentHero.heroBadge.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color.Black.copy(alpha = 0.65f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        0.8.dp,
                                        Brush.horizontalGradient(
                                            listOf(BrandAccentGlow, SpatialCyan)
                                        ),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = currentHero.heroBadge.uppercase(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Text(
                            text = currentHero.title,
                            style = MaterialTheme.typography.displayMedium,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Black
                        )

                        Text(
                            text = "${currentHero.genres.joinToString(" • ")} • ${currentHero.audioSpec}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.82f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Button(
                                onClick = { onPlayMovie(currentHero.id) },
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(46.dp)
                                    .testTag("btn_hero_play"),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandAccent),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.btn_play),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            Button(
                                onClick = { onMovieClick(currentHero.id) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("btn_hero_details"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.btn_details),
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.toggleWatchlist(currentHero.id) },
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .testTag("btn_hero_watchlist")
                            ) {
                                Icon(
                                    imageVector = if (currentHero.isInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Watchlist",
                                    tint = if (currentHero.isInWatchlist) BrandAccent else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            heroMovies.forEachIndexed { index, _ ->
                                val isSelected = index == currentHeroIndex
                                Box(
                                    modifier = Modifier
                                        .width(if (isSelected) 18.dp else 5.dp)
                                        .height(5.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) BrandAccent else Color.White.copy(alpha = 0.35f)
                                        )
                                        .clickable { viewModel.selectHeroIndex(index) }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (continueWatching.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionHeader(
                    title = stringResource(R.string.section_continue_watching),
                    subtitle = "Pick up where you left off"
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(continueWatching) { movie ->
                        ContinueWatchingCard(
                            movie = movie,
                            progressFraction = if (movie.id == "solaris_echo") 0.68f else 0.45f,
                            onClick = { onPlayMovie(movie.id) }
                        )
                    }
                }
            }
        }

        if (trendingRanked.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(30.dp))
                SectionHeader(
                    title = stringResource(R.string.section_trending_now),
                    subtitle = "Most streamed holographic titles today"
                )
                Spacer(modifier = Modifier.height(14.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(trendingRanked) { movie ->
                        HolographicRankCard(
                            movie = movie,
                            rank = movie.rank,
                            onClick = { onMovieClick(movie.id) }
                        )
                    }
                }
            }
        }

        if (spatialTracks.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = SpatialCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = stringResource(R.string.section_spatial_audio),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Dolby Atmos 128-CH & AirPods Max immersion",
                            style = MaterialTheme.typography.bodySmall,
                            color = extendedColors.textSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(SpatialCyan.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                            .border(0.5.dp, SpatialCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "360° ACTIVE",
                            color = SpatialCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(spatialTracks) { track ->
                        SpatialAudioCard(
                            track = track,
                            onClick = { onPlayMovie("solaris_echo") }
                        )
                    }
                }
            }
        }

        if (recommended.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(30.dp))
                SectionHeader(
                    title = stringResource(R.string.section_because_you_watched),
                    subtitle = "Deep neural affinity matches for your profile"
                )
                Spacer(modifier = Modifier.height(14.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(recommended) { movie ->
                        StandardMovieCard(
                            movie = movie,
                            onClick = { onMovieClick(movie.id) },
                            onToggleWatchlist = { viewModel.toggleWatchlist(movie.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.spatialColors
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = extendedColors.textSecondary
        )
    }
}
