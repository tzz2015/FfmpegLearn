package com.example.ffmpeglearn

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.example.ffmpeglearn.base.BaseActivity
import com.example.ffmpeglearn.databinding.ActivityMainBinding
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val RC_CAMERA_AND_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClick()
    }

    override fun initClick() {
        binding.btnJni.setOnClickListener {
            startActivity(Intent(this, JniActivity::class.java))
        }
        binding.btnPermissions.setOnClickListener { requestAllPermissions() }
    }

    /**
     * 申请所需要的所有权限
     */


    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private fun requestAllPermissions() {
        val perms = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "", RC_CAMERA_AND_LOCATION, *perms)
        }
    }


    override fun initView() {
        binding.tvShow.text = stringFromJNI()
    }

    external fun stringFromJNI(): String


}