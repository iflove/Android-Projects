package com.androidz.base_modules.lib_logger

/**
 * 作用描述: 打印包装,方便调试
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/6/15
 * 修改日期 2022/6/15
 * 版权 pub
 */
class Printer(val tag: String, val isPrinter: Boolean = false) {

    val log by lazy {
        if (isPrinter) {
            Logg.log
        } else {
            //no 
            object : Logger {
                override fun d(tag: String, msg: String) {

                }

                override fun d(tag: String, msg: String, tr: Throwable) {

                }

                override fun e(tag: String, msg: String) {

                }

                override fun e(tag: String, msg: String, tr: Throwable) {

                }

                override fun i(tag: String, msg: String) {

                }

                override fun i(tag: String, msg: String, tr: Throwable) {

                }

                override fun v(tag: String, msg: String) {

                }

                override fun v(tag: String, msg: String, tr: Throwable) {

                }

                override fun w(tag: String, msg: String) {

                }

                override fun w(tag: String, msg: String, tr: Throwable) {

                }

                override fun println(logID: Int, level: Int, tag: String, msg: String) {
                    super.println(logID, level, tag, msg)
                }
            }
        }
    }

}