package com.example.ffmpeglearn.video

import android.os.Bundle
import com.example.ffmpeglearn.base.BaseActivity
import com.example.ffmpeglearn.databinding.ActivityNativeMediaPlayerBinding

class NativeMediaPlayerActivity : BaseActivity() {
    private lateinit var mBinding: ActivityNativeMediaPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityNativeMediaPlayerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun initClick() {
    }

    override fun initView() {
    }

}