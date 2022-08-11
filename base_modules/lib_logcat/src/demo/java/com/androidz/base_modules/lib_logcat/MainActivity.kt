package com.androidz.base_modules.lib_logcat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidz.base_modules.lib_empty.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Logcat.d("hello logcat")
    }
}