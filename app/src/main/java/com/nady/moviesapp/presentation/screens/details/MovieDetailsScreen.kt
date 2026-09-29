/**
 * File: MovieDetailsScreen.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.presentation.screens.details

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material.icons.filled.Cast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nady.moviesapp.presentation.components.LocalAsyncImage
import com.nady.moviesapp.R
import com.nady.moviesapp.domain.model.Episode
import com.nady.moviesapp.presentation.components.StandardMovieCard
import com.nady.moviesapp.presentation.viewmodel.MovieDetailsViewModel
import com.nady.moviesapp.ui.theme.BrandAccent
import com.nady.moviesapp.ui.theme.BrandAccentGlow
import com.nady.moviesapp.ui.theme.SpatialCyan
import com.nady.moviesapp.ui.theme.SpatialPurple
import com.nady.moviesapp.ui.theme.spatialColors

@Composable
fun MovieDetailsScreen(
    movieId: String,
    viewModel: MovieDetailsViewModel,
    onBack: () -> Unit,
    onPlayEpisode: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var backProgress by remember { mutableStateOf(0f) }

    PredictiveBackHandler(enabled = true) { progressFlow ->
        try {
            progressFlow.collect { backEvent ->
                backProgress = backEvent.progress
            }
            onBack()
        } catch (e: java.util.concurrent.CancellationException) {
            backProgress = 0f
        }
    }

    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }

    val movie by viewModel.movie.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()
    val extendedColors = MaterialTheme.spatialColors

    val targetMovie = movie ?: return

    val tabEpisodes = stringResource(R.string.tab_episodes)
    val tabMoreLikeThis = stringResource(R.string.tab_more_like_this)
    val tabTrailers = stringResource(R.string.tab_trailers_extras)

    val tabs = listOf(tabEpisodes, tabMoreLikeThis, tabTrailers)

    val scale = 1f - (backProgress * 0.12f)
    val alpha = 1f - (backProgress * 0.35f)
    val cornerRadius = (backProgress * 24).dp

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                clip = backProgress > 0f
                shape = RoundedCornerShape(cornerRadius)
            }
            .testTag("movie_details_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
            ) {
                LocalAsyncImage(
                    model = targetMovie.backdropUrl.ifEmpty { targetMovie.posterUrl },
                    contentDescription = targetMovie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )

                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .align(Alignment.TopStart)
                        .testTag("btn_details_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp)
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = { onPlayEpisode(targetMovie.id, 1) },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                            .border(0.5.dp, SpatialCyan.copy(alpha = 0.5f), CircleShape)
                            .testTag("btn_details_cast")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cast,
                            contentDescription = "Cast Movie",
                            tint = SpatialCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                            .border(0.5.dp, SpatialCyan, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = SpatialCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "360° SPATIAL",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = targetMovie.title,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${targetMovie.matchPercentage}% Match",
                        color = Color(0xFF46D369),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )

                    Text(
                        text = targetMovie.releaseYear,
                        color = extendedColors.textSecondary,
                        fontSize = 13.sp
                    )

                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = targetMovie.ageRating,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = targetMovie.seasonsCount,
                        color = extendedColors.textSecondary,
                        fontSize = 13.sp
                    )

                    Box(
                        modifier = Modifier
                            .background(BrandAccent.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .border(0.5.dp, BrandAccent, RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "IMAX 8K",
                            color = BrandAccentGlow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onPlayEpisode(targetMovie.id, 1) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_details_play_primary"),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.btn_play_episode_1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.downloadEpisode("Season 1 Batch") },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_details_download"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = stringResource(R.string.btn_download_season),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.toggleWatchlist() },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("btn_details_watchlist")
                    ) {
                        Icon(
                            imageVector = if (targetMovie.isInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Watchlist",
                            tint = if (targetMovie.isInWatchlist) BrandAccent else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = {  },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = targetMovie.synopsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (targetMovie.cast.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.cast_and_crew),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        items(targetMovie.cast) { member ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(72.dp)
                            ) {
                                LocalAsyncImage(
                                    model = member.avatarUrl,
                                    contentDescription = member.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, extendedColors.glassBorder, CircleShape)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = member.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = member.characterName,
                                    fontSize = 10.sp,
                                    color = extendedColors.textSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                tabs.forEach { tab ->
                    val isSelected = tab == selectedTab ||
                            (tab == stringResource(R.string.tab_episodes) && selectedTab == "Episodes")
                    Column(
                        modifier = Modifier
                            .clickable { viewModel.selectTab(tab) }
                            .padding(bottom = 6.dp)
                    ) {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isSelected) BrandAccent else extendedColors.textSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .height(2.dp)
                                    .width(36.dp)
                                    .background(BrandAccent)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (selectedTab.contains("Episode", ignoreCase = true) || selectedTab == tabEpisodes) {
            items(targetMovie.episodes) { ep ->
                EpisodeItemCard(
                    episode = ep,
                    onPlay = { onPlayEpisode(targetMovie.id, ep.episodeNumber) },
                    onDownload = { viewModel.downloadEpisode(ep.title) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        } else if (selectedTab.contains("More", ignoreCase = true) || selectedTab == tabMoreLikeThis) {
            items(recommendations.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    pair.forEach { rec ->
                        Box(modifier = Modifier.weight(1f)) {
                            StandardMovieCard(
                                movie = rec,
                                onClick = { viewModel.loadMovie(rec.id) },
                                width = 160
                            )
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Spatial Trailer: The Void Awakens (4K HDR • 360°)",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onPlayEpisode(targetMovie.id, 1) }
                    ) {
                        LocalAsyncImage(
                            model = targetMovie.backdropUrl,
                            contentDescription = "Trailer",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center)
                                .background(BrandAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Trailer",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeItemCard(
    episode: Episode,
    onPlay: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedColors = MaterialTheme.spatialColors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, extendedColors.glassBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onPlay)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .width(110.dp)
                .height(72.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black)
        ) {
            LocalAsyncImage(
                model = episode.thumbnailUrl,
                contentDescription = episode.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Center)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            if (episode.progressFraction > 0f) {
                LinearProgressIndicator(
                    progress = { episode.progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .align(Alignment.BottomCenter),
                    color = BrandAccent,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${episode.episodeNumber}. ${episode.title}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onDownload,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download Episode",
                        tint = extendedColors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = episode.durationText,
                    fontSize = 11.sp,
                    color = extendedColors.textSecondary
                )
                Text(
                    text = "•",
                    fontSize = 11.sp,
                    color = extendedColors.textSecondary
                )
                Text(
                    text = episode.audioSpecText,
                    fontSize = 11.sp,
                    color = SpatialCyan,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = episode.synopsis,
                style = MaterialTheme.typography.bodySmall,
                color = extendedColors.textMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
        }
    }
}
