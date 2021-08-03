package com.example.ffmpeglearn.video

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.widget.SeekBar
import com.example.ffmpeglearn.base.BaseActivity
import com.example.ffmpeglearn.databinding.ActivityNativeMediaPlayerBinding
import com.example.ffmpeglearn.media.FFMediaPlayer
import com.example.ffmpeglearn.media.FFMediaPlayer.Companion.VIDEO_RENDER_ANWINDOW

class NativeMediaPlayerActivity : BaseActivity(), SurfaceHolder.Callback,
    FFMediaPlayer.EventCallback {
    private lateinit var mBinding: ActivityNativeMediaPlayerBinding
    private var mMediaPlayer: FFMediaPlayer? = null
    private val mVideoPath =
        Environment.getExternalStorageDirectory().absolutePath + "/byteflow/one_piece.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityNativeMediaPlayerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun initClick() {
        mBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
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
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        mMediaPlayer?.unInit()
    }

    override fun onPlayerEvent(msgType: Int, msgValue: Float) {
        Log.d(TAG, "onPlayerEvent() called with: msgType = [$msgType], msgValue = [$msgValue]")
    }

}