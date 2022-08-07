package com.androidz.base_modules.lib_baseAndroid;

import android.app.Application;
import android.content.Context;
import android.os.Looper;

import androidx.multidex.MultiDex;

import com.didi.drouter.api.DRouter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;


/**
 * 作用描述: BaseAndroidApplication 用于Application 生命周期分发
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/2/9
 * 修改日期 2022/2/9
 * 版权 pub
 */
public class BaseAndroidApplication extends Application {
    private List<ApplicationLifeCycle> mApplicationLifeCycles;

    public BaseAndroidApplication() {
        super();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        BaseAndroid.installRun(this);
    }

    /**
     * 在ApplicationLifeCycle Init……之前
     *
     * @param base Context
     */
    protected void onAttachApplicationLifeCycleBefore(Context base) {
    }

    /**
     * ApplicationLifeCycle Init
     *
     * @param base Context
     */
    protected void onAttachApplicationLifeCycle(Context base) {
        //List<ApplicationLifeCycle> allService = ServiceLoader.build(ApplicationLifeCycle.class).getAllService();

        List<ApplicationLifeCycle> allService = DRouter.build(ApplicationLifeCycle.class).getAllService();
        Collections.sort(allService, (o1, o2) -> Integer.compare(o2.getInitOrder(), o1.getInitOrder()));
        mApplicationLifeCycles = allService;
        for (ApplicationLifeCycle applicationLifeCycle : mApplicationLifeCycles) {
            applicationLifeCycle.onBaseContextAttached(base);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        for (ApplicationLifeCycle applicationLifeCycle : mApplicationLifeCycles) {
            applicationLifeCycle.onCreate();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (ApplicationLifeCycle applicationLifeCycle : mApplicationLifeCycles) {
            applicationLifeCycle.onTerminate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (ApplicationLifeCycle applicationLifeCycle : mApplicationLifeCycles) {
            applicationLifeCycle.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (ApplicationLifeCycle applicationLifeCycle : mApplicationLifeCycles) {
            applicationLifeCycle.onTrimMemory(level);
        }
    }

    @Override
    public Looper getMainLooper() {
        return super.getMainLooper();
    }

    @Override
    public Executor getMainExecutor() {
        return super.getMainExecutor();
    }
}
