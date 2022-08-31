package com.androidz.base_modules.lib_logger

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.app.Service
import android.content.BroadcastReceiver
import android.util.Log

/**
 * 日志通用简单接口
 * Created by rentianlong on 2020/6/27
 */
interface Logger {
    //常见打印函数
    fun v(tag: String, msg: String)
    fun v(tag: String, msg: String, tr: Throwable)

    fun d(tag: String, msg: String)
    fun d(tag: String, msg: String, tr: Throwable)

    fun i(tag: String, msg: String)
    fun i(tag: String, msg: String, tr: Throwable)

    fun w(tag: String, msg: String)
    fun w(tag: String, msg: String, tr: Throwable)


    fun e(tag: String, msg: String)
    fun e(tag: String, msg: String, tr: Throwable)

    /**
     * 灵活打印函数, 由logID logic handle
     */
    @JvmDefault
    fun println(logID: Int, level: Int, tag: String, msg: String) {
    }

}

/**
 * Logger工厂
 */
interface LoggerFactory {
    fun create(death: Int, vararg args: Any): Logger
}

/**
 * Log 工具
 */
class Logg {

    companion object {

        //isLoggable 默认值
        const val NO_DEUBG = 0

        //由logID
        const val LOG_ID_1 = 1
        const val LOG_ID_2 = 2
        const val LOG_ID_3 = 3
        const val LOG_ID_4 = 4
        const val LOG_ID_5 = 5

        /**
         * 默认Log控制台输出
         */
        private val DEFAULT = object : Logger {

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

        /**
         * NO Log 输出
         */
        val NO_LOG = object : Logger {

            override fun v(tag: String, msg: String) {
            }

            override fun v(tag: String, msg: String, tr: Throwable) {
            }

            override fun d(tag: String, msg: String) {
            }

            override fun d(tag: String, msg: String, tr: Throwable) {
            }

            override fun i(tag: String, msg: String) {
            }

            override fun i(tag: String, msg: String, tr: Throwable) {
            }

            override fun w(tag: String, msg: String) {
            }

            override fun w(tag: String, msg: String, tr: Throwable) {
            }

            override fun e(tag: String, msg: String) {
            }

            override fun e(tag: String, msg: String, tr: Throwable) {
            }

            /**
             * 灵活打印函数, 由logID logic handle
             */
            override fun println(logID: Int, level: Int, tag: String, msg: String) {
            }
        }

        /**
         * shell 开启日志 setprop log.tag.Log D  setprop log.tag.Log 0 关闭日志
         */
        @JvmField
        val NATIVE_LOG_ENABLED = Log.isLoggable("Log", Log.DEBUG)

        /**
         * 开启关闭日志
         */
        @JvmField
        var LOG_ENABLED = NATIVE_LOG_ENABLED

        @JvmStatic
        var log: Logger = DEFAULT

        @JvmStatic
        var loggerFactory: LoggerFactory? = null

        //由logID
        @JvmStatic
        var logPrintln: LogPrintln? = null

        @JvmStatic
        fun v(tag: String, msg: String) {
            log.v(tag, msg)
        }

        @JvmStatic
        fun v(tag: String, msg: String, tr: Throwable) {
            log.v(tag, msg, tr)
        }

        @JvmStatic
        fun d(tag: String, msg: String) {
            log.d(tag, msg)
        }

        @JvmStatic
        fun d(tag: String, msg: String, tr: Throwable) {
            log.d(tag, msg, tr)
        }

        @JvmStatic
        fun i(tag: String, msg: String) {
            log.i(tag, msg)
        }

        @JvmStatic
        fun i(tag: String, msg: String, tr: Throwable) {
            log.i(tag, msg, tr)
        }

        @JvmStatic
        fun w(tag: String, msg: String) {
            log.w(tag, msg)
        }

        @JvmStatic
        fun w(tag: String, msg: String, tr: Throwable) {
            log.w(tag, msg, tr)
        }

        @JvmStatic
        fun e(tag: String, msg: String) {
            log.e(tag, msg)
        }

        @JvmStatic
        fun e(tag: String, msg: String, tr: Throwable) {
            log.e(tag, msg, tr)
        }

        /**
         * 灵活打印函数, 由logID logic handle
         */
        @JvmStatic
        fun println(logID: Int, level: Int, tag: String, msg: String) {
            if (logPrintln != null) {
                when {
                    //VERBOSE
                    (logID == LOG_ID_1) and (level == Log.VERBOSE) -> {
                        logPrintln?.v1(tag, msg)
                    }
                    (logID == LOG_ID_2) and (level == Log.VERBOSE) -> {
                        logPrintln?.v2(tag, msg)
                    }
                    (logID == LOG_ID_3) and (level == Log.VERBOSE) -> {
                        logPrintln?.v3(tag, msg)
                    }
                    (logID == LOG_ID_4) and (level == Log.VERBOSE) -> {
                        logPrintln?.v4(tag, msg)
                    }
                    (logID == LOG_ID_5) and (level == Log.VERBOSE) -> {
                        logPrintln?.v5(tag, msg)
                    }
                    //DEBUG
                    (logID == LOG_ID_1) and (level == Log.DEBUG) -> {
                        logPrintln?.d1(tag, msg)
                    }
                    (logID == LOG_ID_2) and (level == Log.DEBUG) -> {
                        logPrintln?.d2(tag, msg)
                    }
                    (logID == LOG_ID_3) and (level == Log.DEBUG) -> {
                        logPrintln?.d3(tag, msg)
                    }
                    (logID == LOG_ID_4) and (level == Log.DEBUG) -> {
                        logPrintln?.d4(tag, msg)
                    }
                    (logID == LOG_ID_5) and (level == Log.DEBUG) -> {
                        logPrintln?.d5(tag, msg)
                    }

                    //INFO
                    (logID == LOG_ID_1) and (level == Log.INFO) -> {
                        logPrintln?.i1(tag, msg)
                    }
                    (logID == LOG_ID_2) and (level == Log.INFO) -> {
                        logPrintln?.i2(tag, msg)
                    }
                    (logID == LOG_ID_3) and (level == Log.INFO) -> {
                        logPrintln?.i3(tag, msg)
                    }
                    (logID == LOG_ID_4) and (level == Log.INFO) -> {
                        logPrintln?.i4(tag, msg)
                    }
                    (logID == LOG_ID_5) and (level == Log.INFO) -> {
                        logPrintln?.i5(tag, msg)
                    }

                    //WARN
                    (logID == LOG_ID_1) and (level == Log.WARN) -> {
                        logPrintln?.w1(tag, msg)
                    }
                    (logID == LOG_ID_2) and (level == Log.WARN) -> {
                        logPrintln?.w2(tag, msg)
                    }
                    (logID == LOG_ID_3) and (level == Log.WARN) -> {
                        logPrintln?.w3(tag, msg)
                    }
                    (logID == LOG_ID_4) and (level == Log.WARN) -> {
                        logPrintln?.w4(tag, msg)
                    }
                    (logID == LOG_ID_5) and (level == Log.WARN) -> {
                        logPrintln?.w5(tag, msg)
                    }
                    //ASSERT
                    (logID == LOG_ID_1) and (level == Log.ASSERT) -> {
                        logPrintln?.a1(tag, msg)
                    }
                    (logID == LOG_ID_2) and (level == Log.ASSERT) -> {
                        logPrintln?.a2(tag, msg)
                    }
                    (logID == LOG_ID_3) and (level == Log.ASSERT) -> {
                        logPrintln?.a3(tag, msg)
                    }
                    (logID == LOG_ID_4) and (level == Log.ASSERT) -> {
                        logPrintln?.a4(tag, msg)
                    }
                    (logID == LOG_ID_5) and (level == Log.ASSERT) -> {
                        logPrintln?.a5(tag, msg)
                    }
                    else -> logPrintln?.println(logID, level, tag, msg)
                }

            } else {
                log.println(logID, level, tag, msg)
            }
        }


        /**
         * Java层的isLoggable,并不是安卓层
         */
        @JvmStatic
        fun isLoggable(tag: String, level: Int): Boolean {
            return System.getProperty("log.tag.$tag", NO_DEUBG.toString()) == when (level) {
                Log.VERBOSE -> "V"
                Log.DEBUG -> "D"
                Log.ERROR -> "E"
                Log.INFO -> "I"
                Log.WARN -> "W"
                Log.ASSERT -> "A"
                else -> NO_DEUBG
            }
        }

        @JvmStatic
        fun setLoggable(tag: String, level: Int) {
            System.setProperty(
                "log.tag.$tag", when (level) {
                    Log.VERBOSE -> "V"
                    Log.DEBUG -> "D"
                    Log.ERROR -> "E"
                    Log.INFO -> "I"
                    Log.WARN -> "W"
                    Log.ASSERT -> "A"
                    else -> NO_DEUBG.toString()
                }
            )
        }

        /**
         * 获取当前线程堆栈打印msg
         */
        @JvmStatic
        @JvmOverloads
        fun getStackTraceMsg(msg: Any?, index: Int = 6): String {
            return StringBuilder().let {
                getStackTraceElement(index)?.run {
                    it.append("[ (").append(fileName).append(":").append(lineNumber).append(")#")
                        .append(methodName).append(" ] ")
                }
                it.append(msg ?: "null")
            }.toString()
        }

        /**
         * 当前线程的堆栈情况
         */
        private fun getStackTraceElement(index: Int): StackTraceElement? {
            val stackTrace = Thread.currentThread().stackTrace
            return if (index >= stackTrace.size) null else stackTrace[index]
        }
    }
}

var Application.log: Logger
    get() = Logg.log
    set(value) {}

var Activity.log: Logger
    get() = Logg.log
    set(value) {}

var Fragment.log: Logger
    get() = Logg.log
    set(value) {}

var androidx.fragment.app.Fragment.log: Logger
    get() = Logg.log
    set(value) {}

var Service.log: Logger
    get() = Logg.log
    set(value) {}

var BroadcastReceiver.log: Logger
    get() = Logg.log
    set(value) {}

/**
 * 灵活打印函数接口
 */
interface LogPrintln {

    fun println(logID: Int, level: Int, tag: String, msg: String)

    @JvmDefault
    fun v1(tag: String, msg: String) {
    }

    @JvmDefault
    fun v2(tag: String, msg: String) {
    }

    @JvmDefault
    fun v3(tag: String, msg: String) {
    }

    @JvmDefault
    fun v4(tag: String, msg: String) {
    }

    @JvmDefault
    fun v5(tag: String, msg: String) {
    }

    @JvmDefault
    fun d1(tag: String, msg: String) {
    }

    @JvmDefault
    fun d2(tag: String, msg: String) {
    }

    @JvmDefault
    fun d3(tag: String, msg: String) {
    }

    @JvmDefault
    fun d4(tag: String, msg: String) {
    }

    @JvmDefault
    fun d5(tag: String, msg: String) {
    }

    @JvmDefault
    fun i1(tag: String, msg: String) {
    }

    @JvmDefault
    fun i2(tag: String, msg: String) {
    }

    @JvmDefault
    fun i3(tag: String, msg: String) {
    }

    @JvmDefault
    fun i4(tag: String, msg: String) {
    }

    @JvmDefault
    fun i5(tag: String, msg: String) {
    }

    @JvmDefault
    fun w1(tag: String, msg: String) {
    }

    @JvmDefault
    fun w2(tag: String, msg: String) {
    }

    @JvmDefault
    fun w3(tag: String, msg: String) {
    }

    @JvmDefault
    fun w4(tag: String, msg: String) {
    }

    @JvmDefault
    fun w5(tag: String, msg: String) {
    }

    @JvmDefault
    fun a1(tag: String, msg: String) {
    }

    @JvmDefault
    fun a2(tag: String, msg: String) {
    }

    @JvmDefault
    fun a3(tag: String, msg: String) {
    }

    @JvmDefault
    fun a4(tag: String, msg: String) {
    }

    @JvmDefault
    fun a5(tag: String, msg: String) {
    }
}

