package com.androidz.android_projects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidz.base_modules.lib_logger.Logg

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logg.d("MainActivity", "setContentView")
    }
}