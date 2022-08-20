package com.androidz.base_modules.lib_baseAndroid.utils;

import androidx.annotation.NonNull;

import com.androidz.base_modules.lib_baseAndroid.BuildConfig;


/**
 * 作用描述: debug组件消灭if判断,混淆时会移除
 * 组件描述: #基础组件 #debug组件 （debug）
 * 创建人 rentl
 * 创建日期 2021/5/25
 * 修改日期 2021/5/25
 * 版权 pub
 */
public final class DebugRunner {
    private static boolean mOpenDebug;

    /**
     * DEBUG调试下 运行
     */
    public static void run(@NonNull Runnable run) {
        if (mOpenDebug) {
            run.run();
            return;
        }
        if (!BuildConfig.DEBUG) {
            return;
        }
        run.run();
    }

    public static void openDebug() {
        mOpenDebug = true;
    }
}
