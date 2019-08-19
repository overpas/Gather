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

#Enums

-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Chatkit

-keep class * extends com.stfalcon.chatkit.messages.MessageHolders$OutcomingTextMessageViewHolder {
     public <init>(android.view.View, java.lang.Object);
     public <init>(android.view.View);
 }
-keep class * extends com.stfalcon.chatkit.messages.MessageHolders$IncomingTextMessageViewHolder {
     public <init>(android.view.View, java.lang.Object);
     public <init>(android.view.View);
 }
-keep class * extends com.stfalcon.chatkit.messages.MessageHolders$IncomingImageMessageViewHolder {
     public <init>(android.view.View, java.lang.Object);
     public <init>(android.view.View);
 }
-keep class * extends com.stfalcon.chatkit.messages.MessageHolders$OutcomingImageMessageViewHolder {
     public <init>(android.view.View, java.lang.Object);
     public <init>(android.view.View);
 }

#Butterknife

# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
