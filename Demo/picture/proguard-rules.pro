-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#所有native的方法不能去混淆
-keepclasseswithmembernames class * {
	native <methods>;
}
#某些构造方法不能混淆
-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.Attributeset);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#不混淆控件的onclick方法
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
#枚举类不能去混淆
-keepclassmembers enum * {
	public static **[] values();
	public static ** valueOf(java.lang.String);
}
#aidl文件不能混淆
-keep class * implements android.os.Parcelable {
	public static final android.os.Parcelabel$Creator *;
}
#内部类不能混淆
#-keepattributes Exceptions,InnerClasses,...
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep class android.support.v4.**{*;}
#重要，別忘了這些，不混淆andfix包，不混淆native方法
-dontwarn android.annotation
-dontwarn com.alipay.euler.**
-keep class com.alipay.euler.** {*;}
-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}

