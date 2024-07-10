# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontnote retrofit2.Platform
 # Platform used when running on Java 8 VMs. Will not be used at runtime.
 -dontwarn retrofit2.Platform$Java8
 # Retain generic type information for use by reflection by converters and adapters.
 -keepattributes Signature
 # Retain declared checked exceptions for use by a Proxy instance.
 -keepattributes Exceptions
 ##---------------End: proguard configuration for RETROFIT  ----------
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
-dontwarn javax.annotation.**
-keep class androidx.lifecycle.LiveData { *; }
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}

-keep @com.squareup.moshi.JsonQualifier @interface *

# Enum field names are used by the integrated EnumJsonAdapter.
# values() is synthesized by the Kotlin compiler and is used by EnumJsonAdapter indirectly
# Annotate enums with @JsonClass(generateAdapter = false) to use them with Moshi.
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
    **[] values();
}

# Keep helper method to avoid R8 optimisation that would keep all Kotlin Metadata when unwanted
-keepclassmembers class com.squareup.moshi.internal.Util {
    private static java.lang.String getKotlinMetadataClassName();
}
-dontwarn okhttp3.**
-dontwarn retrofit2.Platform$Java8
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep ToJson/FromJson-annotated methods
-keepclassmembers class * {
  @com.squareup.moshi.FromJson <methods>;
  @com.squareup.moshi.ToJson <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep class com.annas.playground.data.remote.model** {<fields>;}
-keep class com.annas.playground.data.local.dto** {<fields>;}
-keep class com.annas.playground.data.domain.model** {<fields>;}
-keep class com.annas.playground.data.local.model** {<fields>;}

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }
-keepnames @kotlin.Metadata class com.annas.playground.data.remote.model.**
-keepnames @kotlin.Metadata class com.annas.playground.data.local.dto.**
-keepnames @kotlin.Metadata class com.annas.playground.data.domain.model.**
-keepnames @kotlin.Metadata class com.annas.playground.data.local.model.**

-keepclassmembers class com.annas.playground.data.remote.model.**  { *; }
-keepclassmembers class com.annas.playground.data.local.dto.**  { *; }
-keepclassmembers class com.annas.playground.data.domain.model.**  { *; }
-keepclassmembers class com.annas.playground.data.local.model.**  { *; }
-keep class kotlin.** { *; }
-keepclassmembers class my.models.package.** {
  <init>(...);
  <fields>;
}

