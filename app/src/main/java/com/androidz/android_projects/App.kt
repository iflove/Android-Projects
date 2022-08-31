package com.androidz.android_projects

import android.content.Context
import android.os.Build
import com.androidz.android_projects.utility.LoggHelper
import com.androidz.base_modules.lib_baseAndroid.BaseAndroid
import com.androidz.base_modules.lib_baseAndroid.BaseAndroidApplication
import com.androidz.base_modules.lib_baseAndroid.utils.ProcessHelper
import com.androidz.base_modules.lib_baseAndroid.utils.ResUtil
import com.androidz.base_modules.lib_baseAndroid.utils.StorageUtils
import com.androidz.base_modules.lib_logcat.Builder
import com.androidz.base_modules.lib_logcat.Logcat
import com.androidz.base_modules.lib_logcat.extend.JLog
import com.androidz.base_modules.lib_logger.Logg
import com.didi.drouter.api.DRouter


/**
 * 作用描述: 组件描述
 * 组件描述: #基础组件 #组件名 （子组件）
 * 组件版本: v1
 * 创建人 rentl
 * 创建日期 2022/8/20 20:07
 * 修改日期 2022/8/20 20:07
 * 版权 pub
 */
class App : BaseAndroidApplication() {
    companion object {

        private const val TAG = "App"
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        BaseAndroid.installRun(this)
        initLog()
        DRouter.init(this);
        onAttachApplicationLifeCycleBefore(this)
        onAttachApplicationLifeCycle(this)
    }

    override fun onCreate() {
        super.onCreate()
        injectObj()
        Logg.d(TAG, "onCreate")
    }

    private fun injectObj() {
        ResUtil.inject(this, R::class.java.`package`.name)
    }

    private fun initLog() {
        val builder: Builder = Logcat.newBuilder()
        //设置Log 保存的文件夹
        builder.logSavePath(StorageUtils.getDiskCacheDir(this, "log"))
        //设置输出日志等级
        if (BuildConfig.DEBUG) {
            //设置输出文件日志等级
            builder.fileLogLevel(Logcat.SHOW_ALL_LOG)
        } else {
            builder.logCatLogLevel(Logcat.SHOW_INFO_LOG.code or Logcat.SHOW_WARN_LOG.code or Logcat.SHOW_ERROR_LOG.code)
            //设置输出文件日志等级
            builder.fileLogLevel(Logcat.SHOW_INFO_LOG.code or Logcat.SHOW_WARN_LOG.code or Logcat.SHOW_ERROR_LOG.code)
        }
        //不显示日志
        //builder.fileLogLevel(Logcat.NOT_SHOW_LOG);

        builder.topLevelTag("AndroidZ")
        //删除过了几天无用日志条目
        builder.deleteUnusedLogEntriesAfterDays(7)
        //输出到Java控制台服务端
        if (ProcessHelper.isMainProcess()) {
            builder.dispatchLog(JLog("192.168.3.11", 5036))
        }
        //是否自动保存日志到文件中
        builder.autoSaveLogToFile(true)
        //是否显示打印日志调用堆栈信息
        builder.showStackTraceInfo(true)
        //是否显示文件日志的时间
        builder.showFileTimeInfo(true)
        //是否显示文件日志的进程以及Linux线程
        builder.showFilePidInfo(true)
        //是否显示文件日志级别
        builder.showFileLogLevel(true)
        //是否显示文件日志标签
        builder.showFileLogTag(true)
        //是否显示文件日志调用堆栈信息
        builder.showFileStackTraceInfo(true)
        //添加该标签,日志将被写入文件
        builder.addTagToFile(TAG)
        Logcat.initialize(this, builder.build())

        LoggHelper.init()

        Logcat.d().file("devProp.txt").append(true).tag("").ln()
            .msg("MODEL:").msg(Build.MODEL).ln()
            .msg("SDK_INT:").msg(Build.VERSION.SDK_INT).ln()
            .out(false, true)

    }

    override fun onLowMemory() {
        super.onLowMemory()
        Logg.d(TAG, "onLowMemory")
    }
}