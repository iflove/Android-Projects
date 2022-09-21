package com.androidz.android_projects.utility

import android.util.Log
import com.androidz.base_modules.lib_logcat.Logcat
import com.androidz.base_modules.lib_logger.Logg
import com.androidz.base_modules.lib_logger.Logger
import com.androidz.base_modules.lib_logger.LoggerFactory

/**
 * 作用描述: 组件描述
 * 组件描述: #基础组件 #组件名 （子组件）
 * 组件版本: v1
 * 创建人 rentl
 * 创建日期 2022/8/29 23:04
 * 修改日期 2022/8/29 23:04
 * 版权 pub
 */
object LoggHelper {
    fun init() {
        Logg.log = logger(3)

        //工厂
        Logg.loggerFactory = object : LoggerFactory {
            override fun create(death: Int, vararg args: Any): Logger {
                return logger(death)
            }
        }
    }

    fun logger(death: Int) = object : Logger {
        override fun v(tag: String, msg: String) {
            Logcat.v().tag(tag).msg(msg).stackTrace(death).out()
        }

        override fun v(tag: String, msg: String, tr: Throwable) {
            Logcat.v().tag(tag).msg(msg + '\n' + Log.getStackTraceString(tr)).stackTrace(death).out()
        }

        override fun d(tag: String, msg: String) {
            Logcat.d().tag(tag).msg(msg).stackTrace(death).out()
        }

        override fun d(tag: String, msg: String, tr: Throwable) {
            Logcat.d().tag(tag).msg(msg + '\n' + Log.getStackTraceString(tr)).stackTrace(death).out()
        }

        override fun i(tag: String, msg: String) {
            Logcat.i().tag(tag).msg(msg).stackTrace(death).out()
        }

        override fun i(tag: String, msg: String, tr: Throwable) {
            Logcat.i().tag(tag).msg(msg + '\n' + Log.getStackTraceString(tr)).stackTrace(death).out()
        }

        override fun w(tag: String, msg: String) {
            Logcat.w().tag(tag).msg(msg).stackTrace(death).out()
        }

        override fun w(tag: String, msg: String, tr: Throwable) {
            Logcat.w().tag(tag).msg(msg + '\n' + Log.getStackTraceString(tr)).stackTrace(death).out()
        }

        override fun e(tag: String, msg: String) {
            Logcat.e().tag(tag).msg(msg).stackTrace(death).out()
        }

        override fun e(tag: String, msg: String, tr: Throwable) {
            Logcat.e().tag(tag).msg(msg + '\n' + Log.getStackTraceString(tr)).stackTrace(death).out()
        }

    }
}