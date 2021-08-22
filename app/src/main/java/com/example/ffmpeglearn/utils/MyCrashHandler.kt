package com.example.ffmpeglearn.utils

import android.content.Context
import android.os.Environment
import android.os.Looper
import android.os.Process
import android.widget.Toast
import java.io.*
import kotlin.system.exitProcess

/**
　　* @Description:
　　* @author 刘宇飞
　　* @date  2021/8/22 21:30
　　*/
class MyCrashHandler : Thread.UncaughtExceptionHandler {

    private var mContext: Context? = null

    //本类实例
    private var myCrashHandler: MyCrashHandler? = null

    //系统默认的uncatchException
    private var mDefaultException: Thread.UncaughtExceptionHandler? = null


    companion object {

        // 懒汉式单例
        val instance: MyCrashHandler by lazy {
            MyCrashHandler()
        }
    }


    /**
     * 初始化
     * @param context
     */
    fun init(context: Context?) {
        mContext = context
        //系统默认处理类
        mDefaultException = Thread.getDefaultUncaughtExceptionHandler()
        //设置该类为系统默认处理类
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    /**
     * 提示用户出现异常
     * 将异常信息保存
     * @param ex
     * @return
     */
    private fun handleExample(ex: Throwable?): Boolean {
        if (ex == null) return false
        Thread {
            Looper.prepare()
            Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出", Toast.LENGTH_SHORT).show()
            Looper.loop()
        }.start()

        //手机设备参数信息
        collectDeviceInfo(mContext)
        saveCrashInfoToFile(ex)
        return true
    }

    /**
     * 设备信息
     * @param mContext
     */
    private fun collectDeviceInfo(mContext: Context?) {}


    /**
     * 保存错误信息到文件中
     * @param ex
     */
    private fun saveCrashInfoToFile(ex: Throwable) {
        val writer: Writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var exCause = ex.cause
        while (exCause != null) {
            exCause.printStackTrace(printWriter)
            exCause = exCause.cause
        }
        printWriter.close()
        val timeMillis = System.currentTimeMillis()
        //错误日志文件名称
        val fileName = "crash-$timeMillis.log"
        //判断sd卡可正常使用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件存储位置
            val path: String =
                Environment.getExternalStorageDirectory().getPath().toString() + "/crash_logInfo/"
            val fl = File(path)
            //创建文件夹
            if (!fl.exists()) {
                fl.mkdirs()
            }
            try {
                val fileOutputStream = FileOutputStream(path + fileName)
                fileOutputStream.write(writer.toString().toByteArray())
                fileOutputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (!handleExample(e) && mDefaultException != null) { //判断异常是否已经被处理
            mDefaultException!!.uncaughtException(t, e)
        } else {
            try {
                Thread.sleep(3000)
            } catch (e1: InterruptedException) {
                e1.printStackTrace()
            }
            //退出程序
            Process.killProcess(Process.myPid())
            exitProcess(1)
        }
    }
}