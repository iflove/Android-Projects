package com.androidz.base_modules.lib_baseAndroid.utils;

import android.os.SystemClock;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

/**
 * 作用描述: 对于低频WeakHashMap 进行GC回收
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/3/18
 * 修改日期 2022/3/18
 * 版权 pub
 */
public class GcWeakHashMap implements Runnable {
    private static GcWeakHashMap mGcWeakHashMap;
    final ArrayList<WeakReference<WeakHashMap<Object, Object>>> sGcWatchers = new ArrayList<>();
    long mLastGcTime;

    private GcWeakHashMap() {
        GcWatchDog.addGcWatcher(this);
    }

    public static GcWeakHashMap get() {
        if (mGcWeakHashMap == null) {
            synchronized (GcWeakHashMap.class) {
                if (mGcWeakHashMap == null) {
                    mGcWeakHashMap = new GcWeakHashMap();
                }
            }
        }

        return mGcWeakHashMap;
    }

    /**
     * 添加 GC 监听
     *
     * @param watcher Runnable
     */
    public void addGcWatcher(WeakHashMap watcher) {
        synchronized (sGcWatchers) {
            sGcWatchers.add(new WeakReference<>(watcher));
        }
    }

    @Override
    public void run() {
        long gcTime = SystemClock.uptimeMillis();
        if (gcTime - this.mLastGcTime < 5000) {
            return;
        }
        this.mLastGcTime = gcTime;
        ArrayList<WeakReference<WeakHashMap<Object, Object>>> runnables;
        synchronized (sGcWatchers) { //锁的粒度尽量小
            List<WeakReference<WeakHashMap<Object, Object>>> collect = sGcWatchers.stream().filter(weakHashMapWeakReference -> weakHashMapWeakReference.get() == null).collect(Collectors.toList());
            sGcWatchers.removeAll(collect);
            runnables = new ArrayList<>(sGcWatchers);
        }
        for (WeakReference<WeakHashMap<Object, Object>> runnable : runnables) {
            WeakHashMap<Object, Object> weakHashMap = runnable.get();
            if (weakHashMap != null) {
                weakHashMap.size();
                //Logg.d("test", "回收");
            }
        }
    }
}
