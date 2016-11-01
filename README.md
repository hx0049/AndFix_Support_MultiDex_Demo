AndFix is a famous hot-fix project, and it is not so good today. I am a new programmer,and this project takes me about four days Although it can not be taken into our project, it can make me know more about this. So I put it here just to record my program history and hope it is useful for more people.

Way Of Using:
just download the code and import the module of this project to your project, and use Method of YuanAndFix to inject the fix_process to your mainThread.

apatch tool:
The useage of this apatch tool is absolutely same to the original way. All you need to do is to change the code of generate_patch_file.bat to fit your situation.

Warning:
if you make minifyEnabled equals true, you must use the same mappingFile.
The Proguard File should look likes:

-applymapping mapping.txt
-keep class com.alipay.euler.** {*;}
-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}
