/**
 * File: MovieStaticDataSource.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 *
 * All images are bundled locally: avatars, posters, backdrops, episode
 * thumbnails, audio covers, and genre art live in res/drawable-nodpi and
 * are referenced by drawable resource name — no network needed.
 *
 * Placeholder art was generated for the six URLs that Google now serves
 * with HTTP 403 (the /aida/ paths): avatar_alex, avatar_alex_alt,
 * poster_solaris_hero, backdrop_solaris_hero, backdrop_aero_city_2088,
 * backdrop_cold_sleep_0. Replace those files with real art (same names)
 * when available. See scripts/download_images.sh.
 */

package com.nady.moviesapp.data.datasource

import com.nady.moviesapp.domain.model.CastMember
import com.nady.moviesapp.domain.model.CategoryGenre
import com.nady.moviesapp.domain.model.DownloadItem
import com.nady.moviesapp.domain.model.Episode
import com.nady.moviesapp.domain.model.Movie
import com.nady.moviesapp.domain.model.SearchTrend
import com.nady.moviesapp.domain.model.SpatialAudioTrack
import com.nady.moviesapp.domain.model.UserProfile

object MovieStaticDataSource {

    const val AVATAR_ALEX = "avatar_alex"
    const val AVATAR_ALEX_ALT = "avatar_alex_alt"

    val userProfile = UserProfile(
        name = "Alex Vance",
        tier = "VIP • Spatial Cinema Tier",
        avatarUrl = AVATAR_ALEX_ALT,
        connectedDevice = "VisionPro Spatial Hub",
        streamHours = "142h",
        sciFiAffinity = "89%",
        losslessAudioQuality = "96k"
    )

    val solarisEchoCast = listOf(
        CastMember(
            id = "c1",
            name = "Chloe Marlowe",
            characterName = "Dr. Lyra Vance",
            avatarUrl = "cast_chloe_marlowe"
        ),
        CastMember(
            id = "c2",
            name = "Liam O'Neill",
            characterName = "Cap. Cole",
            avatarUrl = "cast_liam_oneill"
        ),
        CastMember(
            id = "c3",
            name = "Kenji Arata",
            characterName = "Dir. Sato",
            avatarUrl = "cast_kenji_arata"
        ),
        CastMember(
            id = "c4",
            name = "Elara Vance",
            characterName = "Director",
            avatarUrl = "cast_elara_vance"
        ),
        CastMember(
            id = "c5",
            name = "Gavin Ross",
            characterName = "Composer",
            avatarUrl = "cast_gavin_ross"
        )
    )

    val solarisEchoEpisodes = listOf(
        Episode(
            episodeNumber = 1,
            seasonNumber = 1,
            title = "The Resonance Fold",
            durationText = "54m",
            audioSpecText = "360° Atmos",
            synopsis = "Commander Lyra locks navigational telemetry onto an unnatural acoustic pulse rippling from the binary neutron perimeter.",
            thumbnailUrl = "episode_1_thumb",
            progressFraction = 0.68f,
            resumeTimestamp = "38:14"
        ),
        Episode(
            episodeNumber = 2,
            seasonNumber = 1,
            title = "Event Horizon Whispers",
            durationText = "58m",
            audioSpecText = "360° Atmos",
            synopsis = "A ghost signal originating inside the Chronos' own engine core triggers an emergency temporal quarantine sequence.",
            thumbnailUrl = "episode_2_thumb",
            progressFraction = 0f
        ),
        Episode(
            episodeNumber = 3,
            seasonNumber = 1,
            title = "Singularity Lattice",
            durationText = "51m",
            audioSpecText = "360° Atmos",
            synopsis = "Synthesizing the resonance echoes unearths a living spatial map of parallel timelines, placing Dr. Vance in an impossible ethical dilemma.",
            thumbnailUrl = "episode_3_thumb",
            progressFraction = 0f
        )
    )

    val allMovies = listOf(
        Movie(
            id = "solaris_echo",
            title = "SOLARIS ECHO",
            subtitle = "A Voyage Into The Void",
            synopsis = "When deep space expedition vessel Chronos encounters a sentient resonance fold at the cosmic rim, astrophysicist Dr. Lyra Vance must unravel paradoxical quantum echoes before humanity's temporal baseline collapses.",
            posterUrl = "poster_solaris_hero",
            backdropUrl = "backdrop_solaris_hero",
            heroBadge = "Global Phenomenon #1 in Films",
            audioSpec = "IMAX Enhanced • Dolby Atmos • 8K Spatial",
            genres = listOf("Deep Space", "Hard Sci-Fi", "Cosmic Mystery"),
            matchPercentage = 99,
            releaseYear = "2027",
            ageRating = "TV-MA",
            seasonsCount = "1 Season",
            isSpatialAudioEnabled = true,
            isSpatial3D = true,
            durationText = "54m",
            ratingScore = 9.9,
            cast = solarisEchoCast,
            director = "Elara Vance",
            composer = "Gavin Ross",
            episodes = solarisEchoEpisodes,
            rank = 1,
            isTrending = true,
            isHero = true
        ),
        Movie(
            id = "neon_horizon",
            title = "NEON HORIZON",
            subtitle = "Where Reality Meets The Wire",
            synopsis = "A lone cybernetic operative navigates the treacherous rain-slicked skybridges of Neo-Tokyo 2077, untangling an underground artificial intelligence network threatening the megacity.",
            posterUrl = "poster_neon_horizon",
            backdropUrl = "backdrop_neon_horizon",
            heroBadge = "Chart Topper Today #1",
            audioSpec = "Dolby Vision • Spatial Audio • Ultra HD 4K",
            genres = listOf("Sci-Fi", "Cyberpunk", "Dystopian", "Mind-Bending"),
            matchPercentage = 97,
            releaseYear = "2027",
            ageRating = "TV-MA",
            seasonsCount = "2 Seasons",
            isSpatialAudioEnabled = true,
            isSpatial3D = true,
            durationText = "48m",
            ratingScore = 9.7,
            rank = 1,
            isHero = true
        ),
        Movie(
            id = "chrono_matrix",
            title = "CHRONO MATRIX",
            subtitle = "Realities Collide",
            synopsis = "Fractured floating mirrors reflect glowing red cyberpunk architectures against pitch-black cosmic voids in a mind-bending parallel realities thriller.",
            posterUrl = "poster_chrono_matrix",
            backdropUrl = "poster_chrono_matrix",
            heroBadge = "Critics Choice • Trending #2",
            audioSpec = "Dolby Vision • Dynamic Head-Tracking",
            genres = listOf("Time Travel", "Neo-Noir", "Quantum Thriller"),
            matchPercentage = 95,
            releaseYear = "2027",
            ageRating = "TV-MA",
            seasonsCount = "2 Seasons",
            isSpatialAudioEnabled = true,
            isSpatial3D = true,
            durationText = "52m",
            ratingScore = 9.5,
            rank = 2,
            isHero = true
        ),

        Movie(
            id = "neon_rim_nightfall",
            title = "Neon Rim: Nightfall Circuit",
            subtitle = "Episode 4: Cold Iron",
            synopsis = "A courier trying to survive in a city where memory itself is the most valuable currency.",
            posterUrl = "poster_fracture_2077",
            backdropUrl = "poster_fracture_2077",
            audioSpec = "HDR • Spatial Audio",
            genres = listOf("Anime", "Cyberpunk", "Action"),
            matchPercentage = 98,
            durationText = "24m left",
            rank = 1
        ),
        Movie(
            id = "singularity_horizon",
            title = "Singularity Horizon",
            subtitle = "Episode 8: Event Horizon",
            synopsis = "Gravitational collapse inside an uncharted black hole corridor unlocks human consciousness transfiguration.",
            posterUrl = "poster_singularity_horizon",
            backdropUrl = "poster_singularity_horizon",
            audioSpec = "4K • Atmos",
            genres = listOf("Hard Sci-Fi", "Cosmic"),
            matchPercentage = 94,
            durationText = "12m left",
            rank = 2
        ),
        Movie(
            id = "shadow_protocol",
            title = "Shadow Protocol",
            subtitle = "Episode 2: Ghost Signal",
            synopsis = "An automated orbital military satellite array begins broadcasting an encryption key from 50 years in the future.",
            posterUrl = "poster_shadow_protocol",
            backdropUrl = "poster_shadow_protocol",
            audioSpec = "4K • Spatial Audio",
            genres = listOf("Thriller", "Cybernetic"),
            matchPercentage = 91,
            durationText = "41m left",
            rank = 3
        ),

        Movie(
            id = "fracture_2077",
            title = "Fracture 2077",
            subtitle = "Recently Added",
            synopsis = "Realities shatter in Neo-Tokyo as competing timelines overwrite municipal architecture in real-time.",
            posterUrl = "poster_fracture_2077",
            rank = 1,
            isTrending = true,
            matchPercentage = 99
        ),
        Movie(
            id = "synthetic_heart",
            title = "Synthetic Heart",
            subtitle = "Trending #2",
            synopsis = "A synthetic biology operative discovers an emotional firmware glitch that threatens the conglomerate empire.",
            posterUrl = "poster_synthetic_heart",
            rank = 2,
            isTrending = true,
            matchPercentage = 97
        ),
        Movie(
            id = "orbital_ion",
            title = "Orbital Ion",
            subtitle = "New Season",
            synopsis = "A rogue pilot navigates an ionized orbital plasma storm in deep space with hyper-contrasted cinematic physics.",
            posterUrl = "poster_orbital_ion",
            rank = 3,
            isTrending = true,
            matchPercentage = 94
        ),
        Movie(
            id = "ash_dunes",
            title = "Ash Dunes",
            subtitle = "Chart Topper",
            synopsis = "Armored scouts traverse glowing volcanic ash wastes under a split crimson moon in an epic survival odyssey.",
            posterUrl = "poster_ash_dunes",
            rank = 4,
            isTrending = true,
            matchPercentage = 92
        ),

        Movie(
            id = "replicant_dawn",
            title = "Replicant Dawn",
            subtitle = "Sector 9 Blackout",
            synopsis = "Female cyborg operative in wet dystopian city street at night under neon signs investigates rogue synthetics.",
            posterUrl = "poster_replicant_dawn",
            backdropUrl = "backdrop_replicant_dawn",
            audioSpec = "4K HDR • Spatial",
            genres = listOf("Cyberpunk", "Neo-Noir", "AI"),
            matchPercentage = 98,
            durationText = "18m remaining",
            rank = 2
        ),
        Movie(
            id = "neural_noir",
            title = "Neural Noir",
            subtitle = "Download The Truth",
            synopsis = "High tech cyberpunk detective with glowing cybernetic hand probes holographic data projections in rainy alleys.",
            posterUrl = "poster_neural_noir",
            backdropUrl = "backdrop_neural_noir",
            audioSpec = "Dolby Atmos • 8K",
            genres = listOf("Psychological", "Cybernetic"),
            matchPercentage = 96,
            durationText = "1h 12m remaining",
            rank = 1
        ),
        Movie(
            id = "aero_city_2088",
            title = "Aero City 2088",
            subtitle = "Premiere Nov 14",
            synopsis = "Speeding futuristic aerodynamic flying hypercars stream through colossal mega city canyon towers with fiery sunset skies.",
            posterUrl = "poster_aero_city_2088",
            backdropUrl = "backdrop_aero_city_2088",
            audioSpec = "IMAX Enhanced • 8K",
            genres = listOf("High-Octane", "Dystopian"),
            matchPercentage = 94,
            rank = 3
        ),
        Movie(
            id = "cold_sleep_0",
            title = "Cold Sleep 0",
            subtitle = "Spatial Remastered",
            synopsis = "Female explorer locked in frosted cryogenic stasis pod suspended in purple deep space star nebula confronts subconscious loops.",
            posterUrl = "poster_cold_sleep_0",
            backdropUrl = "backdrop_cold_sleep_0",
            audioSpec = "4K HDR • 360°",
            genres = listOf("Deep Space", "Sci-Fi Thriller"),
            matchPercentage = 91,
            rank = 4
        )
    )

    val spatialAudioTracks = listOf(
        SpatialAudioTrack(
            id = "sp_1",
            title = "Echoes of the Void",
            subtitle = "Active Atmos • 18m Immersion",
            description = "Custom Dolby Atmos 128-channel spatial planetary sound design.",
            coverUrl = "cover_echoes_of_the_void",
            durationText = "18m",
            channels = "128-CH",
            formatBadge = "4K HDR",
            deviceCompatibility = "360° Spatial"
        ),
        SpatialAudioTrack(
            id = "sp_2",
            title = "Synesthesia: Live 2027",
            subtitle = "Acoustic 3D • Live Mix",
            description = "3D acoustic holographic concert mastered for AirPods Pro Max.",
            coverUrl = "cover_synesthesia_live",
            durationText = "34m",
            channels = "AirPods Max",
            formatBadge = "Lossless",
            deviceCompatibility = "Live Stage"
        ),
        SpatialAudioTrack(
            id = "sp_3",
            title = "Chronos: Dark Symphony",
            subtitle = "Dark Symphony • 42m Score",
            description = "Cinematic futuristic orchestra conducted in binaural 360° soundstage.",
            coverUrl = "cover_chronos_dark_symphony",
            durationText = "42m",
            channels = "Orchestra",
            formatBadge = "Master",
            deviceCompatibility = "Binaural 360°"
        )
    )

    val genreCategories = listOf(
        CategoryGenre(
            id = "g_cyberpunk",
            name = "Cyberpunk Neo-Noir",
            subtitle = "140+ immersive dystopian detective chronicles",
            imageUrl = "genre_cyberpunk_neo_noir",
            matchRate = "98% Match",
            formatBadge = "HDR",
            titlesCount = "140+ Titles",
            isWide = true
        ),
        CategoryGenre(
            id = "g_quantum",
            name = "Quantum Sci-Fi",
            subtitle = "Sub-Space Paradoxes",
            imageUrl = "genre_quantum_scifi",
            matchRate = "8K Spatial",
            formatBadge = "IMAX",
            titlesCount = "72 Titles",
            isWide = false
        ),
        CategoryGenre(
            id = "g_dystopian",
            name = "Dystopian Epics",
            subtitle = "Hyperspeed urban sagas",
            imageUrl = "genre_dystopian_epics",
            matchRate = "#1 Trend",
            formatBadge = "4K",
            titlesCount = "95 Titles",
            isWide = false
        ),
        CategoryGenre(
            id = "g_deepspace",
            name = "Deep Space Horror",
            subtitle = "Cryo-nightmares & cosmic dread in spatial audio",
            imageUrl = "genre_deep_space_horror",
            matchRate = "Lossless",
            formatBadge = "Dolby Atmos",
            titlesCount = "56 Titles",
            isWide = true
        )
    )

    val searchTrends = listOf(
        SearchTrend(1, "Solaris Echo: Finale", isHot = true, trendIcon = "up"),
        SearchTrend(2, "Replicant Dawn", isHot = true, trendIcon = "up"),
        SearchTrend(3, "Neural Noir 8K", isHot = false, trendIcon = "flat"),
        SearchTrend(4, "Stellar Drift", isHot = false, trendIcon = "up")
    )

    val initialDownloads = listOf(
        DownloadItem(
            id = "dl_1",
            movieId = "cold_sleep_0",
            title = "Cold Sleep 0",
            subtitle = "Spatial 360°",
            formatBadge = "3D",
            fileSizeText = "4.8 GB",
            posterUrl = "poster_cold_sleep_0",
            expiryText = "Expires in 28 days • Dolby Atmos"
        ),
        DownloadItem(
            id = "dl_2",
            movieId = "aero_city_2088",
            title = "Aero City 2088",
            subtitle = "8K IMAX Spatial",
            formatBadge = "8K",
            fileSizeText = "6.2 GB",
            posterUrl = "poster_aero_city_2088",
            expiryText = "Verified Offline • High Bitrate"
        )
    )
}
