package com.androidz.base_modules.lib_baseAndroid;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.Keep;

/**
 * 作用描述:
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/1/21
 * 修改日期 2022/1/21
 * 版权 pub
 */
@Keep
public abstract class BaseApplicationLifeCycle implements ApplicationLifeCycle {
    private final String TAG = this.getClass().getSimpleName();

    private int mInitOrder;
    private Context mContext;

    public BaseApplicationLifeCycle() {
    }

    public BaseApplicationLifeCycle(int initOrder) {
        this.mInitOrder = initOrder;
    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onBaseContextAttached(Context base) {
        this.mContext = base;
    }

    @Override
    public int getInitOrder() {
        return mInitOrder;
    }

    public Context getContext() {
        return mContext;
    }
}
