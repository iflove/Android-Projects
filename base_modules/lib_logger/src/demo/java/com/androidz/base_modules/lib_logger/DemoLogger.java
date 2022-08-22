package com.androidz.base_modules.lib_logger;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * 作用描述: 组件描述
 * 组件描述: #基础组件 #组件名 （子组件）
 * 组件版本: v1
 * 创建人 rentl
 * 创建日期 2022/8/22 23:16
 * 修改日期 2022/8/22 23:16
 * 版权 pub
 */
public class DemoLogger {
    public static void main() {
        Logg.setLog(new Logger() {
            @Override
            public void v(@NonNull String tag, @NonNull String msg) {
                Log.v(tag, msg);
            }

            @Override
            public void v(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void d(@NonNull String tag, @NonNull String msg) {

            }

            @Override
            public void d(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void i(@NonNull String tag, @NonNull String msg) {

            }

            @Override
            public void i(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void w(@NonNull String tag, @NonNull String msg) {

            }

            @Override
            public void w(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void e(@NonNull String tag, @NonNull String msg) {

            }

            @Override
            public void e(@NonNull String tag, @NonNull String msg, @NonNull Throwable tr) {

            }

            @Override
            public void println(int logID, int level, @NonNull String tag, @NonNull String msg) {
                Logger.super.println(logID, level, tag, msg);
            }
        });

        //日志开关
        Logger log = new Printer("xxa", false).getLog();
        log.v("xxa", "Printer 控制a");
        log.v("xxa", "Printer 控制b");
    }
}
