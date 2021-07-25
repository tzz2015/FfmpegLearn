#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "native-lib"
// __VA_ARGS__ 代表 ...的可变参数
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG,  __VA_ARGS__);


extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ffmpeglearn_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
