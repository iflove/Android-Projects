package com.androidz.base_modules.lib_logcat;


import androidx.annotation.NonNull;

/**
 * 日志事务
 * Created by lazy on 2017/4/12.
 */

public abstract class LogTransaction {

    public abstract LogTransaction msg(@NonNull final Object msg);

    public abstract LogTransaction msgs(@NonNull final Object... msg);

    public abstract LogTransaction tag(@NonNull final String tag);

    public abstract LogTransaction tags(@NonNull final String... tags);

    public abstract LogTransaction file();

    public abstract LogTransaction file(@NonNull final String fileName);

    public abstract LogTransaction ln();

    public abstract LogTransaction format(@NonNull final String format, Object... args);

    public abstract LogTransaction fmtJSON(@NonNull final String json);

    /**
     * 日志文件是否追加
     */
    public abstract LogTransaction append(boolean append);

    /**
     * 打印堆栈偏移位置
     */
    public abstract LogTransaction stackTrace(int offset);

    public abstract LogTransaction out();

    public abstract LogTransaction out(Boolean logCatShow, Boolean logFileEnable);
}
