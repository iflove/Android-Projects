package com.androidz.base_modules.lib_baseAndroid.utils;

import android.os.SystemClock;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 作用描述:
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/3/17
 * 修改日期 2022/3/17
 * 版权 pub
 */
public final class GcWatchDog {
    static WeakReference<GcWatcher> sGcWatcher = new WeakReference<>(new GcWatcher());
    static final ArrayList<Runnable> sGcWatchers = new ArrayList<>();
    static long sLastGcTime;


    static final class GcWatcher {
        @Override
        protected void finalize() {
            Runnable[] runnables = new Runnable[1];
            sLastGcTime = SystemClock.uptimeMillis();
            synchronized (sGcWatchers) { //锁的粒度尽量小
                runnables = sGcWatchers.toArray(runnables);
            }
            for (Runnable runnable : runnables) {
                if (runnable != null) {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //重建实现继续监听
            sGcWatcher = new WeakReference<>(new GcWatcher());
        }
    }

    /**
     * 添加 GC 监听
     *
     * @param watcher Runnable
     */
    public static void addGcWatcher(Runnable watcher) {
        synchronized (sGcWatchers) {
            sGcWatchers.add(watcher);
        }
    }


    /**
     * 移除 GC 监听
     *
     * @param watcher Runnable
     */
    public static void removeGcWatcher(Runnable watcher) {
        synchronized (sGcWatchers) {
            sGcWatchers.remove(watcher);
        }
    }
}
