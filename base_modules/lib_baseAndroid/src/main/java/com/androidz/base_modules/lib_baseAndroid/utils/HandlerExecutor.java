package com.androidz.base_modules.lib_baseAndroid.utils;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/**
 * 作用描述: HandlerExecutor
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/2/15
 * 修改日期 2022/2/15
 * 版权 pub
 */
public class HandlerExecutor implements Executor {
    private final Handler mHandler;

    public HandlerExecutor(@NonNull Handler handler) {
        if (handler == null) {
            throw new NullPointerException();
        }
        mHandler = handler;
    }

    @Override
    public void execute(Runnable command) {
        if (!mHandler.post(command)) {
            throw new RejectedExecutionException(mHandler + " is shutting down");
        }
    }
}