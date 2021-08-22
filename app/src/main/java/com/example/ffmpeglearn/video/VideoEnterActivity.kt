package com.example.ffmpeglearn.video

import android.content.Intent
import android.os.Bundle
import com.example.ffmpeglearn.base.BaseActivity
import com.example.ffmpeglearn.databinding.ActivityVideoEnterBinding

class VideoEnterActivity : BaseActivity() {

    private lateinit var mBinding: ActivityVideoEnterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityVideoEnterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun initClick() {
        mBinding.btnNativeWindow.setOnClickListener {
            startActivity(Intent(this, NativeMediaPlayerActivity::class.java))
        }
        mBinding.btnEsPlayer.setOnClickListener {
            startActivity(Intent(this, GlMediaPlayerActivity::class.java))
        }
    }

    override fun initView() {
    }
}