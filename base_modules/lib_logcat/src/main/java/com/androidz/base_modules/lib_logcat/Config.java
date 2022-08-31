package com.androidz.base_modules.lib_logcat;

import com.androidz.base_modules.lib_logcat.extend.JLog;

import java.util.Map;

/**
 * Created by lazy on 2017/4/12.
 */
public final class Config {

    /**
     * 存放日志文件的目录全路径
     */
    String logSavePath = "";
    Character logCatLogLevel;
    Character fileLogLevel;
    String topLevelTag;
    JLog jLog;
    int deleteUnusedLogEntriesAfterDays;
    Map<String, Object> fileTags;
    boolean autoSaveLogToFile;
    boolean showStackTraceInfo;
    boolean showFileTimeInfo;
    boolean showFilePidInfo;
    boolean showFileLogLevel;
    boolean showFileLogTag;
    boolean showFileStackTraceInfo;

    Config(Builder builder) {
        this.logSavePath = builder.logSavePath;
        this.logCatLogLevel = builder.logCatLogLevel;
        this.fileLogLevel = builder.fileLogLevel;
        this.topLevelTag = builder.topLevelTag;
        this.jLog = builder.jLog;
        this.autoSaveLogToFile = builder.autoSaveLogToFile;
        this.showStackTraceInfo = builder.showStackTraceInfo;
        this.showFileTimeInfo = builder.showFileTimeInfo;
        this.showFilePidInfo = builder.showFilePidInfo;
        this.showFileLogLevel = builder.showFileLogLevel;
        this.showFileLogTag = builder.showFileLogTag;
        this.showFileStackTraceInfo = builder.showFileStackTraceInfo;
        this.fileTags = builder.fileTags;
        this.deleteUnusedLogEntriesAfterDays = builder.deleteUnusedLogEntriesAfterDays;
    }


}