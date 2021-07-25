package com.example.ffmpeglearn

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.ffmpeglearn.base.BaseActivity
import com.example.ffmpeglearn.data.Person
import com.example.ffmpeglearn.databinding.ActivityJniBinding


class JniActivity : BaseActivity() {
    private lateinit var binding: ActivityJniBinding
    private var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJniBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initClick() {
        binding.btnBase.setOnClickListener {
            val stringFromJNI = stringFromJNI()
            Log.e(TAG, stringFromJNI)
            pushData(
                true, 1.toByte(), ',',
                3.toShort(), 4.toLong(), 5.0f, 6.0, "刘宇飞", 28,
                intArrayOf(1, 2, 3), arrayOf("4", "5", "6"), Person("梁小瘦", 28),
                booleanArrayOf(true, false)
            )
        }
        binding.btnGetObject.setOnClickListener {
            val person = getPerson()
            person?.let {
                Log.e(TAG, "获取到对象名称：${it.name} 年龄：${it.age}")
            }
        }
        binding.btnDynamic.setOnClickListener {
            dynamicRegister("我比较高级")
        }
        binding.btnException.setOnClickListener {
            dynamicRegister2("native层抛出异常给Java层")
        }
        binding.btnThread.setOnClickListener {
            synchronizedThread()
        }
        binding.btnBindThread.setOnClickListener {
            test5()
        }
    }

    /**
     * 线程同步测试
     */
    private fun synchronizedThread() {
        for (i in 0..10) {
            Thread {
                count()
                nativeCount()
            }.start()
        }
    }

    private fun count() {
        synchronized(this) {
            count++
            Log.e(TAG, "java count=$count")
        }
    }

    /**
     * Java 将数据传递到 native 中
     */
    private external fun pushData(
        b: Boolean,
        b1: Byte,
        c: Char,
        s: Short,
        l: Long,
        f: Float,
        d: Double,
        name: String?,
        age: Int,
        intArray: IntArray?,
        strArray: Array<String?>?,
        person: Person?,
        bArray: BooleanArray?
    )

    private fun test5() {
        testThread()
    }

    // AndroidUI操作，让C++线程里面来调用
    fun updateUI() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            AlertDialog.Builder(this@JniActivity)
                .setTitle("UI")
                .setMessage("native 运行在主线程，直接更新 UI ...")
                .setPositiveButton("确认", null)
                .show()
        } else {
            runOnUiThread {
                AlertDialog.Builder(this@JniActivity)
                    .setTitle("UI")
                    .setMessage("native运行在子线程切换为主线程更新 UI ...")
                    .setPositiveButton("确认", null)
                    .show()
            }
        }
    }

    private external fun stringFromJNI(): String

    private external fun getPerson(): Person?

    /**
     * 动态注册
     */
    external fun dynamicRegister(name: String)
    external fun dynamicRegister2(name: String)
    external fun nativeCount()
    external fun testThread()
    external fun unThread()

    /**
     * 测试抛出异常
     *
     * @throws NullPointerException
     */
    @Throws(NullPointerException::class)
    private fun testException() {
        Log.e(TAG, "调用了异常方法")
        throw NullPointerException("JniActivity testException NullPointerException")
    }

    override fun initView() {
    }

    override fun onDestroy() {
        super.onDestroy()
        unThread()
    }
}