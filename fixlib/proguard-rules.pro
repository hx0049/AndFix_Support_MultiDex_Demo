
# Proguard File
# if you minifyEnabled = true,before you patch,yous should add the following sentence in this proguardFile,
#-applymapping mapping.txt
# make sure the app uses the same rule of packaging
# the mapping file should be at the right directory
# or you can put all your proguardFile in the ProGuard Rules For App


#-applymapping mapping.txt
-keep class com.alipay.euler.** {*;}
-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}
