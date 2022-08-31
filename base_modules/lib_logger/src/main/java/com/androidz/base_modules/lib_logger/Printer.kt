package com.androidz.base_modules.lib_logger

/**
 * 作用描述: 打印包装,方便调试 结合Logcat 使用堆栈信息定位会丟准确位置
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/6/15
 * 修改日期 2022/6/15
 * 版权 pub
 */
class Printer(val tag: String, val isPrinter: Boolean = false) {
    private lateinit var create: Logger

    fun log(death: Int, vararg args: Any): Logger {
        if (isPrinter) {
            val log = Logg.loggerFactory?.create(death, args)
            if (log == null) {
                return Logg.NO_LOG
            } else {
                create = log
            }
            return create
        } else { //no
            return Logg.NO_LOG
        }
    }

}