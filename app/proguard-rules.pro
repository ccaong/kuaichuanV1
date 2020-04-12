# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\program\AndroidSdk/tools/proguard/proguard-android.txt
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

-keep class android.support.v7.widget.SearchView { *; }
-keep class com.crashlytics.** { *; }
-keep class com.crashlytics.android.**
-keepattributes SourceFile,LineNumberTable
#-keepattributes *Annotation*

#admob
-keep public class com.google.android.gms.ads.** {
   public *;
}

-keep public class com.google.ads.** {
   public *;
}

#facebook ad
#-keep public class com.facebook.ads.** {
#   public *;
#}

-keep public class com.flurry.android.ads.** {
   public *;
}

-keep public class com.inmobi.ads.** {
   public *;
}

-dontwarn com.facebook.**

-dontwarn com.squareup.okhttp.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn okio.**

##---------------Begin: proguard configuration for Gson ----------
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}

-keepattributes Signature
-keepattributes *Annotation*
-keep public class antivirus.ahundredorso.com.antivirusone.entity.** { *;}

# ±£ÁôÎÒÃÇÊ¹ÓÃµÄËÄ´ó×é¼þ£¬×Ô¶¨ÒåµÄApplicationµÈµÈÕâÐ©Àà²»±»»ìÏý
# ÒòÎªÕâÐ©×ÓÀà¶¼ÓÐ¿ÉÄÜ±»Íâ²¿µ÷ÓÃ
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# ±£ÁôsupportÏÂµÄËùÓÐÀà¼°ÆäÄÚ²¿Àà
-keep class android.support.** {*;}
-dontwarn com.android.support.**
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-keep class com.crashlytics.android.**{
     *;
}
-dontwarn com.crashlytics.android.**


-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep public class * extends android.support.v7.**

# ±£Áô¼Ì³ÐµÄ
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**


-keepclasseswithmembernames class fastsoft.charger.faster.booster.batterysaver.BT_Optimize_screen{ *; }

# ±£ÁôRÏÂÃæµÄ×ÊÔ´
-keep class **.R$* {*;}

# ±£Áô±¾µØnative·½·¨²»±»»ìÏý
-keepclasseswithmembernames class * {
    native <methods>;
}

# ±£ÁôÔÚActivityÖÐµÄ·½·¨²ÎÊýÊÇviewµÄ·½·¨£¬
# ÕâÑùÒÔÀ´ÎÒÃÇÔÚlayoutÖÐÐ´µÄonClick¾Í²»»á±»Ó°Ïì
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

# ±£ÁôÃ¶¾ÙÀà²»±»»ìÏý
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# ±£ÁôÎÒÃÇ×Ô¶¨Òå¿Ø¼þ£¨¼Ì³Ð×ÔView£©²»±»»ìÏý
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# ±£ÁôParcelableÐòÁÐ»¯Àà²»±»»ìÏý
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ±£ÁôSerializableÐòÁÐ»¯µÄÀà²»±»»ìÏý
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ¶ÔÓÚ´øÓÐ»Øµ÷º¯ÊýµÄonXXEvent¡¢**On*ListenerµÄ£¬²»ÄÜ±»»ìÏý
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView´¦Àí£¬ÏîÄ¿ÖÐÃ»ÓÐÊ¹ÓÃµ½webViewºöÂÔ¼´¿É
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, java.lang.String);
}


-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keepattributes InnerClasses
-keepattributes Signature

-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient


-dontwarn org.apache.**
-keep class org.apache.** { *; }
-keep interface org.apache.** { *; }

#-libraryjars libs/original-android--2.3.3.jar
-dontwarn android.**
-keep class android.**{ *; }
-keep interface android.** { *; }

-dontwarn com.android.**
-keep class com.android.**{ *; }
-keep interface com.android.** { *; }

-ignorewarnings

-keep class com.android.internal.telephony.ITelephony { *; }

#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}


#okio
-dontwarn okio.**
-keep class okio.**{*;}


-forceprocessing
-optimizationpasses 99
-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
-dontpreverify
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-target 1.6
-overloadaggressively
-repackageclasses 'g.c'
-allowaccessmodification
-printmapping proguard.map
-dontoptimize

-dontwarn com.google.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

-keepnames class * implements java.io.Serializable

-keepclasseswithmembernames class * {
    native <methods>;
}
 -keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

 -keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

 -keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

 -keepclassmembers enum * {

    public static **[] values();

    public static ** valueOf(java.lang.String);

}

 -keep class * implements android.os.Parcelable {

  public static final android.os.Parcelable$Creator *;

}

-keep public class **.R$*{
    *;
}
-keep class com.google.** {
	*;
}
-keep class com.qq.** {
	*;
}
-keep class com.umeng.** {
	*;
}
-keep class u.aly.** {
	*;
}
-keep class com.chartboost.** {
	*;
}

-keep class com.baidu.** { *;}

-keep class com.inmobi.** { *; }

-dontwarn com.inmobi.**

-keep class com.vungle.** {
	*;
}
-keep class com.facebook.** { *; }
-keepattributes Signature
-keep class com.nineoldandroids.** {
	*;
}
-keep class javax.inject.** {
	*;
}
-keep class dagger.** {
	*;
}
-keep class android.support.v4.** {
	*;
}
-keep class com.common.** {
	*;
}
-keepattributes InnerClasses, EnclosingMethod

-keep class com.ironsource.mobilcore.**{
	*;
}
-keep class com.smartads.Plugins {
	*;
}
-keep class com.smartads.plugin.GameApplication {
	*;
}
-keep class com.smartads.lib.CoreService {
	*;
}
-keep class com.smartads.ads.AdBannerType {
	*;
}
-keep class com.smartads.ads.AdNativeType {
	*;
}

-keep interface com.smartads.ads.mediation.exit.ExitListener {
	*;
}
-keep interface com.smartads.ads.mediation.video.RewardListener {
	*;
}
-keep class com.smartads.ads.mediation.video.MyUnityListener{
   *;
}
-keep class com.smartads.ads.mediation.video.MyVungleListener{
   *;
}
-keep class com.smartads.ads.mediation.video.MyAdColonyListener{
   *;
}
-keep class com.thinkd.xshare.dao.** {*;}

-keep class com.thinkd.xshare.entity.** {*;}