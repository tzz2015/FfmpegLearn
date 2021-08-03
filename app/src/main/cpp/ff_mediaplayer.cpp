//
// Created by LYF on 2021/8/1.
//
#include <jni.h>
#include <string>
#include <android/log.h>
#include <pthread.h>

#define TAG "native-lib"
// __VA_ARGS__ 代表 ...的可变参数
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG,  __VA_ARGS__);


extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_nativeInit(JNIEnv *env, jobject thiz,
                                                            jstring jurl, jint render_type,
                                                            jobject surface) {
    const char *url = env->GetStringUTFChars(jurl, nullptr);
    LOGE("初始化的url:%s", url)
    env->ReleaseStringUTFChars(jurl, url);
    long code = 1;
    return code;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_nativeUnInit(JNIEnv *env, jobject thiz,
                                                              jlong player_handle) {
    if (player_handle != 0) {
        LOGE("销毁播放器:%ld", player_handle)
    }
}








