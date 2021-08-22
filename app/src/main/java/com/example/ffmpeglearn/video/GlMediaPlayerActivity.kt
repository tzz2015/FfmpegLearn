package com.example.ffmpeglearn.video

import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.SeekBar
import com.example.ffmpeglearn.base.BaseActivity
import com.example.ffmpeglearn.databinding.ActivityGlMediaPlayerBinding
import com.example.ffmpeglearn.media.FFMediaPlayer
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MEDIA_PARAM_VIDEO_DURATION
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MEDIA_PARAM_VIDEO_HEIGHT
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MEDIA_PARAM_VIDEO_WIDTH
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_DECODER_DONE
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_DECODER_INIT_ERROR
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_DECODER_READY
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_DECODING_TIME
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_REQUEST_RENDER
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.VIDEO_GL_RENDER
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.VIDEO_RENDER_OPENGL
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GlMediaPlayerActivity : BaseActivity(), GLSurfaceView.Renderer, FFMediaPlayer.EventCallback,
    MyGlSurfaceView.OnGestureCallback {
    private lateinit var mBinding: ActivityGlMediaPlayerBinding
    private var mMediaPlayer: FFMediaPlayer? = null
    private val mVideoPath =
        Environment.getExternalStorageDirectory().absolutePath + "/byteflow/one_piece.mp4"
    private var mIsTouch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityGlMediaPlayerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

    }


    override fun onResume() {
        super.onResume()
        mMediaPlayer?.play()
    }

    override fun onPause() {
        super.onPause()
        mMediaPlayer?.pause()
    }


    override fun initClick() {
        mBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                mIsTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mMediaPlayer?.seekToPosition(mBinding.seekBar.progress.toFloat())
                mIsTouch = false
            }

        })
    }

    override fun initView() {
        mBinding.surfaceView.setEGLContextClientVersion(3)
        mBinding.surfaceView.setRenderer(this)
        mBinding.surfaceView.addOnGestureCallback(this)
        mBinding.surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        mMediaPlayer = FFMediaPlayer()
        mMediaPlayer?.addEventCallback(this)
        mMediaPlayer?.init(mVideoPath, VIDEO_RENDER_OPENGL, null)
    }


    override fun onPlayerEvent(msgType: Int, msgValue: Float) {
        Log.d(TAG, "onPlayerEvent() called with: msgType = [$msgType], msgValue = [$msgValue]")
        runOnUiThread {
            when (msgType) {
                MSG_DECODER_INIT_ERROR -> {
                }
                MSG_DECODER_READY -> onDecoderReady()
                MSG_DECODER_DONE -> {
                }
                MSG_REQUEST_RENDER -> {
                }
                MSG_DECODING_TIME -> if (!mIsTouch) mBinding.seekBar.progress = msgValue.toInt()
                else -> {
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.unInit()
    }

    private fun onDecoderReady() {
        mMediaPlayer?.run {

            val videoWidth = mMediaPlayer!!.getMediaParams(MEDIA_PARAM_VIDEO_WIDTH).toInt()
            val videoHeight = mMediaPlayer!!.getMediaParams(MEDIA_PARAM_VIDEO_HEIGHT).toInt()
            if (videoHeight * videoWidth != 0) mBinding.surfaceView.setAspectRatio(
                videoWidth,
                videoHeight
            )

            val duration = mMediaPlayer!!.getMediaParams(MEDIA_PARAM_VIDEO_DURATION).toInt()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBinding.seekBar.min = 0
            }
            mBinding.seekBar.max = duration
        }

    }

    override fun onGesture(xRotateAngle: Int, yRotateAngle: Int, scale: Float) {
        mMediaPlayer?.native_SetGesture(
            VIDEO_GL_RENDER,
            xRotateAngle.toFloat(),
            yRotateAngle.toFloat(),
            scale
        )
    }

    override fun onTouchLoc(touchX: Float, touchY: Float) {
        mMediaPlayer?.native_SetTouchLoc(VIDEO_GL_RENDER, touchX, touchY)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        mMediaPlayer?.native_OnSurfaceCreated(VIDEO_GL_RENDER)
    }

    override fun onSurfaceChanged(p0: GL10?, w: Int, h: Int) {
        mMediaPlayer?.native_OnSurfaceChanged(VIDEO_GL_RENDER, w, h)
    }

    override fun onDrawFrame(p0: GL10?) {
        mMediaPlayer?.native_OnDrawFrame(VIDEO_GL_RENDER)
    }


}