package com.example.ffmpeglearn.media

import android.view.Surface

/**
　　* @Description:
　　* @author 刘宇飞
　　* @date  2021/8/1 9:57
　　*/
class FFMediaPlayer {
    private var mEventCallback: EventCallback? = null

    companion object {
        val VIDEO_RENDER_OPENGL = 0
        val VIDEO_RENDER_ANWINDOW = 1
        val VIDEO_RENDER_3D_VR = 2

        init {
            System.loadLibrary("native-lib")
        }
    }

    private var mNativePlayerHandle: Long = 0


    fun init(url: String, videoRenderType: Int, surface: Surface) {
        mNativePlayerHandle = nativeInit(url, videoRenderType, surface)
    }

    fun unInit() {
        nativeUnInit(mNativePlayerHandle)
    }

    fun addEventCallback(callback: EventCallback) {
        mEventCallback = callback
    }

    private fun playerEventCallback(msgType: Int, msgValue: Float) {
        mEventCallback?.onPlayerEvent(msgType, msgValue)
    }

    private external fun nativeInit(url: String, renderType: Int, surface: Any): Long

    private external fun nativeUnInit(playerHandle: Long)


    interface EventCallback {
        fun onPlayerEvent(msgType: Int, msgValue: Float)
    }

}