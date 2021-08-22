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
        const val MSG_DECODER_INIT_ERROR = 0
        const val MSG_DECODER_READY = 1
        const val MSG_DECODER_DONE = 2
        const val MSG_REQUEST_RENDER = 3
        const val MSG_DECODING_TIME = 4

        const val MEDIA_PARAM_VIDEO_WIDTH = 0x0001
        const val MEDIA_PARAM_VIDEO_HEIGHT = 0x0002
        const val MEDIA_PARAM_VIDEO_DURATION = 0x0003

        const val VIDEO_RENDER_OPENGL = 0
        const val VIDEO_RENDER_ANWINDOW = 1
        const val VIDEO_RENDER_3D_VR = 2

        init {
            System.loadLibrary("native-lib")
        }
    }

    private var mNativePlayerHandle: Long = 0


    fun init(url: String, videoRenderType: Int, surface: Surface) {
        mNativePlayerHandle = nativeInit(url, videoRenderType, surface)
    }

    fun play() {
        native_Play(mNativePlayerHandle)
    }

    fun pause() {
        native_Pause(mNativePlayerHandle)
    }

    fun seekToPosition(position: Float) {
        native_SeekToPosition(mNativePlayerHandle, position)
    }

    fun stop() {
        native_Stop(mNativePlayerHandle)
    }

    fun unInit() {
        nativeUnInit(mNativePlayerHandle)
    }

    fun addEventCallback(callback: EventCallback) {
        mEventCallback = callback
    }

    fun getMediaParams(paramType: Int): Long {
        return native_GetMediaParams(mNativePlayerHandle, paramType)
    }

    private fun playerEventCallback(msgType: Int, msgValue: Float) {
        mEventCallback?.onPlayerEvent(msgType, msgValue)
    }

    private external fun nativeInit(url: String, renderType: Int, surface: Any): Long

    private external fun nativeUnInit(playerHandle: Long)

    private external fun native_Play(playerHandle: Long)

    private external fun native_SeekToPosition(playerHandle: Long, position: Float)

    private external fun native_Pause(playerHandle: Long)

    private external fun native_Stop(playerHandle: Long)

    private external fun native_GetMediaParams(playerHandle: Long, paramType: Int): Long


    //for GL render
    external fun native_OnSurfaceCreated(renderType: Int)
    external fun native_OnSurfaceChanged(renderType: Int, width: Int, height: Int)
    external fun native_OnDrawFrame(renderType: Int)

    //update MVP matrix
    external fun native_SetGesture(
        renderType: Int,
        xRotateAngle: Float,
        yRotateAngle: Float,
        scale: Float
    )

    external fun native_SetTouchLoc(renderType: Int, touchX: Float, touchY: Float)

    interface EventCallback {
        fun onPlayerEvent(msgType: Int, msgValue: Float)
    }

}