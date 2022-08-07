package com.androidz.base_modules.lib_baseAndroid;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.Keep;

/**
 * 作用描述:
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/1/20
 * 修改日期 2022/1/20
 * 版权 pub
 */
@Keep
public interface ApplicationLifeCycle {
    /**
     * Same as {@link Application#onCreate()}.
     */
    void onCreate();

    /**
     * Same as {@link Application#onLowMemory()}.
     */
    void onLowMemory();

    /**
     * Same as {@link Application#onTrimMemory(int level)}.
     *
     * @param level
     */
    void onTrimMemory(int level);

    /**
     * Same as {@link Application#onTerminate()}.
     */
    void onTerminate();

    /**
     * Same as {@link Application#onConfigurationChanged(Configuration newconfig)}.
     */
    void onConfigurationChanged(Configuration newConfig);

    /**
     * Same as {@link Application#attachBaseContext(Context context)}.
     */
    void onBaseContextAttached(Context base);

    int getInitOrder();
}
