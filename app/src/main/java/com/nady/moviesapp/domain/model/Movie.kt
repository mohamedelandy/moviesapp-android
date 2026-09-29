/**
 * File: Movie.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.domain.model

data class Movie(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val synopsis: String = "",
    val posterUrl: String,
    val backdropUrl: String = "",
    val heroBadge: String = "",
    val audioSpec: String = "Dolby Vision • Spatial Audio • Ultra HD 4K",
    val genres: List<String> = emptyList(),
    val matchPercentage: Int = 95,
    val releaseYear: String = "2027",
    val ageRating: String = "TV-MA",
    val seasonsCount: String = "1 Season",
    val isSpatialAudioEnabled: Boolean = true,
    val isSpatial3D: Boolean = true,
    val durationText: String = "54m",
    val ratingScore: Double = 9.8,
    val cast: List<CastMember> = emptyList(),
    val director: String = "Elara Vance",
    val composer: String = "Gavin Ross",
    val episodes: List<Episode> = emptyList(),
    val rank: Int = 0,
    val rankChange: String = "+0",
    val isTrending: Boolean = false,
    val isHero: Boolean = false,
    val isInWatchlist: Boolean = false
)

data class Episode(
    val episodeNumber: Int,
    val seasonNumber: Int = 1,
    val title: String,
    val durationText: String,
    val audioSpecText: String = "360° Atmos",
    val synopsis: String,
    val thumbnailUrl: String,
    val progressFraction: Float = 0f,
    val resumeTimestamp: String? = null,
    val isDownloaded: Boolean = false
)

data class CastMember(
    val id: String,
    val name: String,
    val characterName: String,
    val avatarUrl: String
)

data class SpatialAudioTrack(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val coverUrl: String,
    val durationText: String,
    val channels: String = "128-CH",
    val formatBadge: String = "4K HDR",
    val deviceCompatibility: String = "AirPods Max"
)

data class CategoryGenre(
    val id: String,
    val name: String,
    val subtitle: String,
    val imageUrl: String,
    val matchRate: String = "98% Match",
    val formatBadge: String = "HDR",
    val titlesCount: String = "140+ Titles",
    val isWide: Boolean = false
)

data class SearchTrend(
    val rank: Int,
    val query: String,
    val isHot: Boolean = false,
    val trendIcon: String = "up"
)

data class DownloadItem(
    val id: String,
    val movieId: String,
    val title: String,
    val subtitle: String,
    val formatBadge: String,
    val fileSizeText: String,
    val posterUrl: String,
    val expiryText: String,
    val progress: Float = 1f
)

data class UserProfile(
    val name: String = "Alex Vance",
    val tier: String = "VIP • Spatial Cinema Tier",
    val avatarUrl: String,
    val connectedDevice: String = "VisionPro Spatial Hub",
    val streamHours: String = "142h",
    val sciFiAffinity: String = "89%",
    val losslessAudioQuality: String = "96k"
)

enum class AppThemeMode {
    SYSTEM, DARK, LIGHT
}

enum class SupportedLanguage(val code: String, val displayName: String, val nativeName: String, val isRtl: Boolean) {
    ENGLISH("en", "English", "English", false),
    ARABIC("ar", "Arabic", "العربية", true),
    GREEK("el", "Greek", "Ελληνικά", false),
    SPANISH("es", "Spanish", "Español", false),
    FRENCH("fr", "French", "Français", false),
    GERMAN("de", "German", "Deutsch", false),
    JAPANESE("ja", "Japanese", "日本語", false);

    companion object {
        fun fromCode(code: String): SupportedLanguage =
            entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
    }
}

data class AppSettings(
    val themeMode: AppThemeMode = AppThemeMode.DARK,
    val language: SupportedLanguage = SupportedLanguage.ENGLISH,
    val isSpatialAudioActive: Boolean = true,
    val playbackSpeed: Float = 1.0f,
    val holographicPriority: Boolean = true
)
