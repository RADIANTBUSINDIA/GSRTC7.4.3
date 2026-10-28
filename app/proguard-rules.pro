# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Install\Users\Swastiik\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keepclassmembers class * {
 @android.webkit.JavascriptInterface <methods>;
 }
 -keepattributes JavascriptInterface
 -keepattributes *Annotation*

# ==============================================================================
# PayPhi / ICICI Orange PG / Sapphire Customer SDK Rules
# (From Android Customer SDK Integration.pdf - Section 3: Proguard rules for release builds)
# ==============================================================================

# 1. SDK Core Rules
-keep class com.payphi.** { *; }
-dontwarn com.payphi.**
-keep class com.gsrtc.mobileweb.ui.activities.payment_gateway.** { *; }

# 2. Don't warn attributes needed (Security Providers, OkHttp, Kotlin, Annotations)
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn kotlinx.parcelize.Parcelize
-dontwarn kotlin.Unit
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# 3. Keep class attributes (Coroutines, Retrofit Response)
-keep,allowoptimization,allowshrinking,allowobfuscation class kotlin.coroutines.Continuation
-keep,allowoptimization,allowshrinking,allowobfuscation class retrofit2.Response

# 4. Global metadata & reflection attributes
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepattributes EnclosingMethod,InnerClasses,Signature,SourceFile,LineNumberTable

# 5. Retrofit (Network interfaces)
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# 6. GSON (JSON parsing)
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}



