package com.androidz.base_modules.lib_baseAndroid;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Process;

import androidx.annotation.Keep;

import com.androidz.base_modules.lib_baseAndroid.utils.HandlerExecutor;

import java.util.concurrent.Executor;


/**
 * 作用描述:
 * 组件描述:
 * 创建人 rentl
 * 创建日期 2022/1/21
 * 修改日期 2022/1/21
 * 版权 pub
 */
@Keep
public final class BaseAndroid {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Application mApplication;
    private static Resources mResources;

    private BaseAndroid() {
    }

    /**
     * 必须最早初始化
     *
     * @param context Application
     */
    public static void installRun(Application context) {
        BaseAndroid.mContext = context;
        BaseAndroid.mApplication = context;
        BaseAndroid.mResources = context.getResources();
    }

    public static boolean isInstall() {
        return mContext != null;
    }

    @Keep
    public static Context getContext() {
        return mContext;
    }

    @Keep
    public static Context getApplication() {
        return mApplication;
    }

    public static Resources getResources() {
        return mResources;
    }

    /**
     * 切记 installRun 之后使用
     */
    public static class StaticCall {
        @SuppressLint("StaticFieldLeak")
        public final static Context sContext;
        public final static Application sApplication;
        public final static Resources sResources;
        public final static String sPackageName;
        public final static ActivityManager sActivityManager;
        public final static int sMyPid;
        /**
         * 闹铃服务
         */
        public final static AlarmManager sAlarmMgr;
        public final static Executor sMainExecutor;

        private StaticCall() {
        }

        static {
            sContext = BaseAndroid.mContext;
            sApplication = BaseAndroid.mApplication;
            sResources = sContext.getResources();
            sPackageName = sContext.getPackageName();
            sActivityManager = (ActivityManager) sContext.getSystemService(Service.ACTIVITY_SERVICE);
            sMyPid = Process.myPid();
            sAlarmMgr = (AlarmManager) sContext.getSystemService(Context.ALARM_SERVICE);
            sMainExecutor = getMainExecutor();
        }

        private static Executor getMainExecutor() {
            return new HandlerExecutor(new Handler(sContext.getMainLooper()));
        }
    }
}
