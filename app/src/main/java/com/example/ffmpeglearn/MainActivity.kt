package com.example.ffmpeglearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.ffmpeglearn.base.BaseActivity
import com.example.ffmpeglearn.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

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
    }

    override fun initView() {
        binding.tvShow.text = stringFromJNI()
    }

    external fun stringFromJNI(): String


}