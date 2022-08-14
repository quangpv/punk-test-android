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


-keep class com.afilabs.parcelizer.model.dto.** { *; }
-keep class com.afilabs.parcelizer.model.request.** { *; }
-keep public class * extends java.lang.annotation.Annotation { *; }
-keepattributes SourceFile,LineNumberTable

# Keep Dependency Injection Constructor
-keepclassmembers @android.support.di.Inject public class * {
 public <init>(...);
}

-keepclassmembers class * extends android.support.di.Injectable {
 public <init>(...);
}

# Keep ViewModel Constructor
-keepclassmembers class * extends androidx.lifecycle.ViewModel{
 public <init>(...);
}