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
-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-libraryjars libs

#-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*,!code/allocation/variable
-allowaccessmodification
-repackageclasses ''

-keep class com.google.android.material.** { *; }

-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep class cn.pedant.SweetAlert.Rotate3dAnimation { public <init>(...); }
-keep public class * implements com.ixuea.android.downloader.db.DownloadDBController
-dontwarn com.google.**

#-keepattributes SourceFile,LineNumberTable

-keepattributes InnerClasses
-keepattributes InnerClasses,EnclosingMethod
-keepattributes Signature
-keepattributes *Annotation*