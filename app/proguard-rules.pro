# CoreDraft ProGuard Optimization & Obfuscation Rules

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
-keep class com.reformistsinstitute.coredraft.data.local.** { *; }
-keep class com.reformistsinstitute.coredraft.domain.model.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <methods>;
}

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# AndroidX Compose
-keep class androidx.compose.runtime.** { *; }
-dontwarn androidx.compose.**

# Strip debug logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
}
