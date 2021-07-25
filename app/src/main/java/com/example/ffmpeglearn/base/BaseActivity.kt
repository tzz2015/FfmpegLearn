package com.example.ffmpeglearn.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
　　* @Description:
　　* @author 刘宇飞
　　* @date  2021/7/24 17:02
　　*/
abstract class BaseActivity : AppCompatActivity() {
    companion object {
        val TAG = javaClass::class.java.name

        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initView()
        initClick()
    }


    abstract fun initClick()

    abstract fun initView()
}