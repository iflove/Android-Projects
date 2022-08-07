package com.androidz.base_modules.baseAndroid

import android.app.Application
import android.content.Context
import com.androidz.base_modules.lib_baseAndroid.BaseAndroid
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

/**
 * 作用描述:
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/5/11
 * 修改日期 2022/5/11
 * 版权 pub
 */

/**
 * 内联simpleName
 */
inline fun <reified T> simpleName(): String {
    return T::class.java.simpleName
}

val application: Application
    get() = BaseAndroid.StaticCall.sApplication

val context: Context
    get() = BaseAndroid.StaticCall.sContext

fun Int.fileSize(): String {
    return toLong().fileSize()
}

fun Long.fileSize(): String {
    val size = this
    if (size <= 0) return "0"
    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}

fun Long.fileSizeSimple(): String {
    val size = this
    if (size <= 0) return "0"
    val units = arrayOf("B", "K", "M", "G", "T")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + "" + units[digitGroups]
}


//class BaseAndroidKt {
//
//    //未完善
//    // 保存 CoroutineScope
//    private var scopeRef: AtomicReference<Any> = AtomicReference()
//
//    // 自定义的 CoroutineScope
//    val appGlobalScope: CoroutineScope
//        get() {
//            while (true) {
//                val existing = scopeRef.get() as CoroutineScope?
//                if (existing != null) {
//                    return existing
//                }
//                val newScope = SafeCoroutineScope(Dispatchers.Main.immediate)
//                if (scopeRef.compareAndSet(null, newScope)) {
//                    return newScope
//                }
//            }
//        }
//
//    // 不会崩溃的 CoroutineScope
//    private class SafeCoroutineScope(context: CoroutineContext) : CoroutineScope, Closeable {
//        override val coroutineContext: CoroutineContext =
//            SupervisorJob() + context + UncaughtCoroutineExceptionHandler()
//
//        override fun close() {
//            coroutineContext.cancelChildren()
//        }
//    }
//
//    // 自定义 CoroutineExceptionHandler
//    private class UncaughtCoroutineExceptionHandler : CoroutineExceptionHandler,
//        AbstractCoroutineContextElement(CoroutineExceptionHandler) {
//        override fun handleException(context: CoroutineContext, exception: Throwable) {
//            // 处理异常
//        }
//    }
//
//}