package com.example.ffmpeglearn.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.EasyPermissions

/**
　　* @Description:
　　* @author 刘宇飞
　　* @date  2021/7/24 17:02
　　*/
abstract class BaseActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }


    abstract fun initClick()

    abstract fun initView()
}