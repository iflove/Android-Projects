package com.androidz.base_modules.lib_baseAndroid.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.os.Build;

import com.androidz.base_modules.baseAndroid.BaseAndroid;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 作用描述:
 * 组件描述:  #基础组件 # 进程管理
 * 创建人 rentl
 * 创建日期 2022/1/20
 * 修改日期 2022/1/20
 * 版权 pub
 */
public class ProcessHelper {
    private static final String TAG = "ProcessHelper";
    private volatile static Boolean mIsMainProcess;

    private ProcessHelper() {
    }


    public static boolean isMainProcess() {
        if (mIsMainProcess == null) {
            synchronized (ProcessHelper.class) {
                if (mIsMainProcess == null) {
                    String packageName = BaseAndroid.StaticCall.sPackageName;
                    String unixCmdlineProcessName = getUnixCmdlineProcessName();
                    //Logg.d(TAG, "unixCmdlineProcessName=" + unixCmdlineProcessName);
                    if (StringUtils.isNotBlank(unixCmdlineProcessName)) {
                        mIsMainProcess = packageName.equals(unixCmdlineProcessName);
                        return mIsMainProcess;
                    }
                    String curProcessName = getCurProcessName();
                    //Logg.d(TAG, "curProcessName=" + curProcessName);
                    if (StringUtils.isNotBlank(curProcessName)) {
                        mIsMainProcess = packageName.equals(curProcessName);
                        return mIsMainProcess;
                    }
                }
            }
        }
        return mIsMainProcess;
    }

    public static String getCurProcessName() {
        List<ActivityManager.RunningAppProcessInfo> processList = BaseAndroid.StaticCall.sActivityManager.getRunningAppProcesses();
        if (processList == null) {
            return null;
        }
        String processName = null;
        for (ActivityManager.RunningAppProcessInfo processInfo : processList) {
            if (processInfo.pid == BaseAndroid.StaticCall.sMyPid) {
                processName = processInfo.processName;
                return processName;
            }
        }
        return processName;
    }

    public static String getUnixCmdlineProcessName() {
        //cat /proc/1191/cmdline
        File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            return bufferedReader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getUnixStatProcessName() {
        //cat /proc/1191/stat
        //1191 (ng.vscreen.base) S 231 231 0 0 -1 1077936448 391640 984603 308 6 33772 33978 8922 8992 10 -10 99 0 2260 2526752768 80533 4294967295 1 1 0 0 0 0 4612 4096 34040 0 0 0 17 2 0 0 78 0 0 0 0 0 0 0 0 0 0
        File file = new File("/proc/" + android.os.Process.myPid() + "/" + "stat");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            return bufferedReader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getProcessName() throws RuntimeException {
        if (Build.VERSION.SDK_INT >= 28)
            return Application.getProcessName();

        // Using the same technique as Application.getProcessName() for older devices
        // Using reflection since ActivityThread is an internal API

        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");

            // Before API 18, the method was incorrectly named "currentPackageName", but it still returned the process name
            // See https://github.com/aosp-mirror/platform_frameworks_base/commit/b57a50bd16ce25db441da5c1b63d48721bb90687
            String methodName = "currentProcessName";
            @SuppressLint("DiscouragedPrivateApi") Method getProcessName = activityThread.getDeclaredMethod(methodName);
            return (String) getProcessName.invoke(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
