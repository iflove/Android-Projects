package com.androidz.base_modules.lib_logger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidz.base_modules.lib_empty.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}