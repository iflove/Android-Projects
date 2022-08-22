package com.androidz.base_modules.lib_logger

import android.app.Application
import android.util.Log

/**
 * 作用描述: 组件描述
 * 组件描述: #基础组件 #组件名 （子组件）
 * 组件版本: v1
 * 创建人 rentl
 * 创建日期 2022/8/22 23:11
 * 修改日期 2022/8/22 23:11
 * 版权 pub
 */
class App : Application() {
    companion object {
        private const val TAG = "App"
    }

    override fun onCreate() {
        super.onCreate()
        //开启关闭日志
        Logg.LOG_ENABLED = true //全局变量

        DemoLogger.main()
        //自定义Logger 处理
        Logg.log = object : Logger {

            override fun v(tag: String, msg: String) {
                Log.v(tag, msg)
            }

            override fun v(tag: String, msg: String, tr: Throwable) {
                Log.v(tag, msg, tr)
            }

            override fun d(tag: String, msg: String) {
                Log.d(tag, msg)
            }

            override fun d(tag: String, msg: String, tr: Throwable) {
                Log.d(tag, msg, tr)
            }

            override fun i(tag: String, msg: String) {
                Log.i(tag, msg)
            }

            override fun i(tag: String, msg: String, tr: Throwable) {
                Log.i(tag, msg, tr)
            }

            override fun w(tag: String, msg: String) {
                Log.w(tag, msg)
            }

            override fun w(tag: String, msg: String, tr: Throwable) {
                Log.w(tag, msg, tr)
            }

            override fun e(tag: String, msg: String) {
                Log.e(tag, msg)
            }

            override fun e(tag: String, msg: String, tr: Throwable) {
                Log.e(tag, msg, tr)
            }

            /**
             * 灵活打印函数, 由logID logic handle
             */
            override fun println(logID: Int, level: Int, tag: String, msg: String) {
                Log.println(level, tag, msg)
            }
        }

        //动态开关变量
        Logg.setLoggable(TAG, Log.DEBUG)
        val loggable = Logg.isLoggable(TAG, Log.DEBUG)

        //堆栈信息获取
        val stackTraceMsg = Logg.getStackTraceMsg("test getStackTraceMsg", 4)
        Logg.d(TAG, "stackTraceMsg: $stackTraceMsg")
    }
}