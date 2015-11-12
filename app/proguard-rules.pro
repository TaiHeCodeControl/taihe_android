# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files (x86)\Android\android-studio\sdk/tools/proguard/proguard-android.txt
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

#忽略警告,避免打包时某些警告出现
-ignorewarnings
#指定代码的压缩级别
-optimizationpasses 5
#是否使用大小写混合
-dontusemixedcaseclassnames
#是否混淆第三方jar
-dontskipnonpubliclibraryclasses
#不优化输入类文件
-dontoptimize
#不混淆输入的类文件
-dontobfuscate
#混淆时是否做预校验
-dontpreverify
#不压缩输入类文件
-dontshrink
#混淆时是否记录日志
-verbose
#混淆时所使用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/mergin/*
#保护给定的可选属性
-keepattributes *Annotation*
#如果引用了v4或者v7包
-dontwarn android.support.**
#保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#-keep public class * extends com.sqlcrypt.database
#-keep public class * extends com.sqlcrypt.database.sqlite
#-dontwarn com.chinaway.framework.swordfish.**
#-keep public class com.chinaway.framework.swordfish.**{*;}

-keepclasseswithmembernames class * {#保持native方法不被混淆
    native <methods>;
}
-keepclasseswithmembernames class * {#保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {#保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持类成员
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {#保持枚举enum类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {#保持Parcelable不被混淆
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    !static !transient <fields>;
#    !private <fields>;
#    !private <methods>;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#如果引用V4包可以添加 下面这行
-keep public class * extends android.support.v4.app.Fragment

#-libraryjars libs/gson-2.2.4.jar
#-libraryjars libs/swordfish.jar
#-libraryjars libs/xUtils-2.6.14.jar
#-libraryjars libs/afinal_0.5.1_bin.jar
#-libraryjars libs/commons-codec-1.5.jar
#-libraryjars libs/commons-io-2.4.jar
#-libraryjars libs/httpclient-android-4.3.3.jar
#-libraryjars libs/httpmime-4.3.5.jar
#-libraryjars libs/jikmediaplayer.jar
#-libraryjars libs/jikmediawidget.jar
#-libraryjars libs/polyvSDK.jar
#-libraryjars libs/locSDK_6.05.jar
#-libraryjars libs/umeng-analytics-v5.5.3.jar
#-libraryjars libs/AMap_Services_V2.3.1.jar
#-libraryjars libs/universal-image-loader-1.9.4.jar

#以libaray的形式应用了一个图片加载框架，不想混淆keep掉
#-keep class com.handmark.pulltorefresh.library.** {*; }


