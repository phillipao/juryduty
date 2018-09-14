# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/poertel/adt-bundle-linux-x86_64-20130219/sdk/tools/proguard/proguard-android.txt
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
-dontwarn org.w3c.dom.bootstrap.**
-dontwarn java.nio.file.**
-dontwarn java.beans.**
-dontwarn com.google.android.gms.**
-dontwarn sun.misc.Unsafe
-dontwarn org.joda.convert.**

# For Jackson (used by Duty.java and Instructions.java
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepnames class com.fasterxml.jackson.** { *; }
 -dontwarn com.fasterxml.jackson.databind.**
 -keep class org.codehaus.** { *; }
 -keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
 public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }

 -keep public class com.philoertel.juryduty.** {
   public void set*(***);
   public *** get*();
 }

# For AWS. Source: https://github.com/aws/aws-sdk-android/blob/master/Proguard.md
# Class names are needed in reflection
-keepnames class com.amazonaws.**
# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler
# The following are referenced but aren't required to run
-dontwarn com.fasterxml.jackson.**
-dontwarn org.apache.commons.logging.**
# Android 6.0 release removes support for the Apache HTTP client
-dontwarn org.apache.http.**
# The SDK has several references of Apache HTTP client
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**

# Needed for BroadcastReceivers, such as NoDataAlarmReceiver. Source:
# http://proguard.sourceforge.net/manual/examples.html#serializable.
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
