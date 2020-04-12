# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/jikai/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes SourceFile,LineNumberTable

-keep class com.bestgo.adsplugin.ads.AdAppHelper {
    public *;
}

-keep class com.bestgo.adsplugin.ads.AdStateListener {
    public *;
}

-keep interface com.bestgo.adsplugin.ads.RewardedListener {
    public *;
}

-keep class com.bestgo.adsplugin.ads.AdType {
    public *;
}

-keep class com.bestgo.adsplugin.ads.AdConfig {
    public *;
}
-keep class com.bestgo.adsplugin.ads.AdConfig$AdCtrl {
    public *;
}
-keep class com.bestgo.adsplugin.ads.analytics.** {
    public *;
}
-keep class com.bestgo.adsplugin.ads.NewsListener {
    public *;
}
-keep class com.bestgo.adsplugin.daemon.Daemon {
    public *;
}
-keep interface com.bestgo.adsplugin.ads.activity.ShowAdFilter {
    *;
}
-keep class org.jsoup.** {*;}

-keep class com.facebook.ads.AdListener {*;}
-keep class com.facebook.ads.InterstitialAdListener {*;}

-dontwarn com.flurry.sdk.**