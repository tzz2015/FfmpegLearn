//
// Created by LYF on 2021/8/1.
//
#include <cstdio>
#include <cstring>
#include <FFMediaPlayer.h>
#include <render/video/VideoGLRender.h>
#include <render/video/VRGLRender.h>
#include <render/audio/OpenSLRender.h>
#include <MediaRecorderContext.h>
#include "util/LogUtil.h"
#include "jni.h"
#include "ASanTestCase.h"

#define TAG "native-lib"
// __VA_ARGS__ 代表 ...的可变参数
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG,  __VA_ARGS__);


extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_nativeInit(JNIEnv *env, jobject obj,
                                                            jstring jurl, jint renderType,
                                                            jobject surface) {
    const char* url = env->GetStringUTFChars(jurl, nullptr);
    auto *player = new FFMediaPlayer();
    player->Init(env, obj, const_cast<char *>(url), renderType, surface);
    env->ReleaseStringUTFChars(jurl, url);
    return reinterpret_cast<jlong>(player);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_nativeUnInit(JNIEnv *env, jobject thiz,
                                                              jlong player_handle) {
    if(player_handle != 0)
    {
        auto *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->UnInit();
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1Play(JNIEnv *env, jobject thiz,
                                                              jlong player_handle) {
    if(player_handle != 0)
    {
        auto *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->Play();
    }

}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1SeekToPosition(JNIEnv *env, jobject thiz,
                                                                        jlong player_handle,
                                                                        jfloat position) {
    if(player_handle != 0)
    {
        auto *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->SeekToPosition(position);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1Pause(JNIEnv *env, jobject thiz,
                                                               jlong player_handle) {
    if(player_handle != 0)
    {
        auto *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->Pause();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1Stop(JNIEnv *env, jobject thiz,
                                                              jlong player_handle) {
    if(player_handle != 0)
    {
        auto *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        ffMediaPlayer->Stop();
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1GetMediaParams(JNIEnv *env, jobject thiz,
                                                                        jlong player_handle,
                                                                        jint param_type) {
    long value = 0;
    if(player_handle != 0)
    {
        auto *ffMediaPlayer = reinterpret_cast<FFMediaPlayer *>(player_handle);
        value = ffMediaPlayer->GetMediaParams(param_type);
    }
    return value;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1OnSurfaceCreated(JNIEnv *env, jobject thiz,
                                                                          jint render_type) {
    switch (render_type)
    {
        case VIDEO_GL_RENDER:
            VideoGLRender::GetInstance()->OnSurfaceCreated();
            break;
        case AUDIO_GL_RENDER:
            AudioGLRender::GetInstance()->OnSurfaceCreated();
            break;
        case VR_3D_GL_RENDER:
            VRGLRender::GetInstance()->OnSurfaceCreated();
            break;
        default:
            break;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1OnSurfaceChanged(JNIEnv *env, jobject thiz,
                                                                          jint render_type,
                                                                          jint width, jint height) {
    switch (render_type)
    {
        case VIDEO_GL_RENDER:
            VideoGLRender::GetInstance()->OnSurfaceChanged(width, height);
            break;
        case AUDIO_GL_RENDER:
            AudioGLRender::GetInstance()->OnSurfaceChanged(width, height);
            break;
        case VR_3D_GL_RENDER:
            VRGLRender::GetInstance()->OnSurfaceChanged(width, height);
            break;
        default:
            break;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1OnDrawFrame(JNIEnv *env, jobject thiz,
                                                                     jint render_type) {
    switch (render_type)
    {
        case VIDEO_GL_RENDER:
            VideoGLRender::GetInstance()->OnDrawFrame();
            break;
        case AUDIO_GL_RENDER:
            AudioGLRender::GetInstance()->OnDrawFrame();
            break;
        case VR_3D_GL_RENDER:
            VRGLRender::GetInstance()->OnDrawFrame();
            break;
        default:
            break;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1SetGesture(JNIEnv *env, jobject thiz,
                                                                    jint render_type,
                                                                    jfloat x_rotate_angle,
                                                                    jfloat y_rotate_angle,
                                                                    jfloat scale) {

}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_ffmpeglearn_media_FFMediaPlayer_native_1SetTouchLoc(JNIEnv *env, jobject thiz,
                                                                     jint render_type,
                                                                     jfloat touch_x,
                                                                     jfloat touch_y) {
    switch (render_type)
    {
        case VIDEO_GL_RENDER:
            VideoGLRender::GetInstance()->SetTouchLoc(touch_x, touch_y);
            break;
        case AUDIO_GL_RENDER:
            AudioGLRender::GetInstance()->SetTouchLoc(touch_x, touch_y);
            break;
        case VR_3D_GL_RENDER:
            VRGLRender::GetInstance()->SetTouchLoc(touch_x, touch_y);
            break;
        default:
            break;
    }
}