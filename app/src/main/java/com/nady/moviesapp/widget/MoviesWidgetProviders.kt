/**
 * File: MoviesWidgetProviders.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.nady.moviesapp.MainActivity
import com.nady.moviesapp.R

class MoviesSmallWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("nav_route", "player/solaris_echo?episode=1")
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views = RemoteViews(context.packageName, R.layout.widget_movies_small).apply {
                setTextViewText(R.id.widget_small_title, "SOLARIS ECHO")
                setTextViewText(R.id.widget_small_subtitle, "Resume S1:E1 • 38:14")
                setOnClickPendingIntent(R.id.widget_small_root, pendingIntent)
                setOnClickPendingIntent(R.id.widget_small_play_btn, pendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

class MoviesMediumWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val playIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("nav_route", "player/solaris_echo?episode=1")
            }
            val playPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId * 10,
                playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val nextIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("nav_route", "player/solaris_echo?episode=2")
            }
            val nextPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId * 10 + 1,
                nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views = RemoteViews(context.packageName, R.layout.widget_movies_medium).apply {
                setTextViewText(R.id.widget_medium_title, "SOLARIS ECHO")
                setTextViewText(R.id.widget_medium_ep, "S1:E1 • The Resonance Fold (360° Atmos)")
                setTextViewText(R.id.widget_medium_time, "38:14 / 54m (68%)")
                setProgressBar(R.id.widget_medium_progress, 100, 68, false)
                setOnClickPendingIntent(R.id.widget_medium_root, playPendingIntent)
                setOnClickPendingIntent(R.id.widget_medium_play_btn, playPendingIntent)
                setOnClickPendingIntent(R.id.widget_medium_next_btn, nextPendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

class MoviesLargeWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val rootIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("nav_route", "home")
            }
            val rootPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId * 20,
                rootIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val playIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("nav_route", "player/solaris_echo?episode=1")
            }
            val playPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId * 20 + 1,
                playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val neonIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("nav_route", "movie_details/neon_horizon")
            }
            val neonPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId * 20 + 2,
                neonIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val chronoIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("nav_route", "movie_details/chrono_matrix")
            }
            val chronoPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId * 20 + 3,
                chronoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views = RemoteViews(context.packageName, R.layout.widget_movies_large).apply {
                setTextViewText(R.id.widget_large_hero_title, "SOLARIS ECHO")
                setTextViewText(R.id.widget_large_hero_desc, "IMAX Enhanced • Dolby Atmos • S1:E1")
                setProgressBar(R.id.widget_large_progress, 100, 68, false)
                setOnClickPendingIntent(R.id.widget_large_root, rootPendingIntent)
                setOnClickPendingIntent(R.id.widget_large_play, playPendingIntent)
                setOnClickPendingIntent(R.id.widget_large_movie1, neonPendingIntent)
                setOnClickPendingIntent(R.id.widget_large_movie2, chronoPendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
