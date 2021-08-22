package com.example.ffmpeglearn

import android.app.Application
import com.example.ffmpeglearn.utils.MyCrashHandler

/**
 * 　　* @Description:
 * 　　* @author 刘宇飞
 * 　　* @date  2021/8/22 21:22
 */
internal class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MyCrashHandler.instance.init(this)
    }
}