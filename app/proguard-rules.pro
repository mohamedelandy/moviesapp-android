# Line numbers and source attributes for crash stack traces
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses,EnclosingMethod

# Domain & Data Models (Keep model classes and their properties for serialization)
-keep class com.nady.moviesapp.domain.model.** { *; }
-keep class com.nady.moviesapp.data.local.entity.** { *; }
-keep class com.nady.moviesapp.data.remote.model.** { *; }

# Moshi rules
-keepclasseswithmembers class * {
    @com.squareup.moshi.Json <fields>;
}
-keepclasseswithmembers class * {
    @com.squareup.moshi.JsonClass class *;
}
-dontwarn com.squareup.moshi.**
-keep class com.squareup.moshi.** { *; }

# Room SQLite Database rules
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Retrofit 2 rules
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions

# Coil image loading
-dontwarn coil.**
-keep class coil.** { *; }

# Media3 & ExoPlayer rules
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# App Widgets
-keep class com.nady.moviesapp.widget.** extends android.appwidget.AppWidgetProvider { *; }

# Google Cast SDK & OptionsProvider
-keep class com.google.android.gms.cast.** { *; }
-dontwarn com.google.android.gms.cast.**
-keep class com.nady.moviesapp.cast.** { *; }

# NavigationEvent & Predictive Back
-keep class androidx.navigationevent.** { *; }
-dontwarn androidx.navigationevent.**

# Agentic AppFunctions & AI Workflows
-keep class com.nady.moviesapp.domain.agent.** { *; }
