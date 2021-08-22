package com.example.ffmpeglearn.video

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.widget.SeekBar
import com.example.ffmpeglearn.base.BaseActivity
import com.example.ffmpeglearn.databinding.ActivityNativeMediaPlayerBinding
import com.example.ffmpeglearn.media.FFMediaPlayer
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MEDIA_PARAM_VIDEO_DURATION
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MEDIA_PARAM_VIDEO_HEIGHT
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MEDIA_PARAM_VIDEO_WIDTH
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_DECODER_DONE
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_DECODER_INIT_ERROR
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_DECODER_READY
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_DECODING_TIME
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.MSG_REQUEST_RENDER
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.VIDEO_RENDER_ANWINDOW

class NativeMediaPlayerActivity : BaseActivity(), SurfaceHolder.Callback,
    FFMediaPlayer.EventCallback {
    private lateinit var mBinding: ActivityNativeMediaPlayerBinding
    private var mMediaPlayer: FFMediaPlayer? = null
    private val mVideoPath =
        Environment.getExternalStorageDirectory().absolutePath + "/byteflow/one_piece.mp4"
    private var mIsTouch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityNativeMediaPlayerBinding.inflate(layoutInflater)
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
        mBinding.surfaceView.holder.addCallback(this)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        if (mMediaPlayer == null)
            mMediaPlayer = FFMediaPlayer()
        mMediaPlayer?.addEventCallback(this)
        mMediaPlayer?.init(mVideoPath, VIDEO_RENDER_ANWINDOW, surfaceHolder.surface)
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, w: Int, h: Int) {
        Log.e(
            TAG,
            "surfaceChanged() called with: surfaceHolder = [$surfaceHolder], format = [$format], w = [$w], h = [$h]"
        )
        mMediaPlayer?.play()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        mMediaPlayer?.unInit()
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

    private fun onDecoderReady() {
        mMediaPlayer?.run {

            val videoWidth = getMediaParams(MEDIA_PARAM_VIDEO_WIDTH).toInt()
            val videoHeight = getMediaParams(MEDIA_PARAM_VIDEO_HEIGHT).toInt()
            if (videoHeight * videoWidth != 0) mBinding.surfaceView.setAspectRatio(
                videoWidth,
                videoHeight
            )
            val duration = getMediaParams(MEDIA_PARAM_VIDEO_DURATION).toInt()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mBinding.seekBar.min = 0
            }
            mBinding.seekBar.max = duration
        }

    }

}