package com.androidz.android_projects

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.androidz.base_modules.lib_logger.Logg
import com.androidz.base_modules.lib_logger.Printer

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"

    }

    //setprop log.tag.MainActivity D
    val log = Printer(TAG, Log.isLoggable(TAG, Log.DEBUG)).log(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Logg.d("MainActivity", "setContentView")
    }

    override fun onPostResume() {
        super.onPostResume()
    }

    override fun onResume() {
        super.onResume()
        log.d(TAG, "onResume")
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}