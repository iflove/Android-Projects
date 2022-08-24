package com.androidz.base_modules.lib_logcat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidz.base_modules.lib_logcat.extend.JLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
 *
 * Copyright  2016
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * <ol>
 * <li>输出Log ->控制台   {@link Logcat#v(Object)}</li>
 * <li>输出Log ->控制台   {@link Logcat#v(String, Object)}</li>
 * <li>输出Log ->控制台 (LogTransaction 为Logcat 提供强大而灵活的链式调用api)  {@link  LogTransaction}</li>
 * <p>
 * </ol>
 *
 * @author Lazy
 */
public final class Logcat {
    /**
     * 默认 top-level Tag
     */
    private static final String TAG = "Logcat";

    /**
     * 换行符
     */
    @NonNull
    static final String LINE_SEPARATOR = System.getProperty("line.separator");

    static final String BLANK_STR = " ";

    /**
     * Custom top-level tag
     */
    private static String topLevelTag;

    /**
     * 日志类型标识符(优先级：由低到高排列，取值越小优先级越高)
     */
    public static final char SHOW_VERBOSE_LOG = 0x01;
    public static final char SHOW_DEBUG_LOG = 0x01 << 1;
    public static final char SHOW_INFO_LOG = 0x01 << 2;
    public static final char SHOW_WARN_LOG = 0x01 << 3;
    public static final char SHOW_ERROR_LOG = 0x01 << 4;

    /**
     * 不显示Log
     */
    public static final char NOT_SHOW_LOG = 0;

    /**
     * 日志级别
     */
    private static final String V = "V/";
    private static final String D = "D/";
    private static final String I = "I/";
    private static final String W = "W/";
    private static final String E = "E/";

    /**
     * Tag 分割符号
     */
    private static final String TAG_SEPARATOR = "->";
    private static final String DEFAULT_LOG_DIR = "logs";

    public static final char SHOW_ALL_LOG =
            SHOW_VERBOSE_LOG |
                    SHOW_DEBUG_LOG |
                    SHOW_INFO_LOG |
                    SHOW_WARN_LOG |
                    SHOW_ERROR_LOG;

    /**
     * 默认为五种日志类型均在 LogCat 中输出显示
     */
    private static char logCatShowLogType = SHOW_ALL_LOG;

    /**
     * 默认为五种日志类型均在 日志文件 中输出保存
     */
    private static char fileSaveLogType = SHOW_ALL_LOG;

    /**
     * 存放日志文件的目录全路径
     */
    private static String logFolderPath = "";
    /**
     * Application Context 防止内存泄露
     */
    private static Context context;
    private static String pkgName;

    private static final int JSON_INDENT = 4;
    private static final String LOGFILE_SUFFIX = ".log";

    /**
     * File日志打印日期
     */
    private static final String MM_DD_HH_MM_SS_SSS = "MM-dd HH:mm:ss.SSS";

    /**
     * 默认文件Log 文件名
     */
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 单线程打印Log,打印大量log,主线程耗时
     */
    private static HandlerThread printHandlerThread;
    private static Handler printHandler;
    /**
     * 单线程 用写文件 防止 anr
     */
    private static HandlerThread printFileHandlerThread;
    private static Handler printFileHandler;
    private static Map<String, Object> fileTags;

    private static final int INDEX = 5;
    private static final int MAX_LENGTH = 4000;
    private static JLog jLog;
    private static int deleteUnusedLogEntriesAfterDays;
    private static long FILE_SAVE_TIME;
    private static boolean autoSaveLogToFile;
    private static boolean showStackTraceInfo;
    private static boolean showFileTimeInfo;
    private static boolean showFilePidInfo;
    private static boolean showFileLogLevel;
    private static boolean showFileLogTag;
    private static boolean showFileStackTraceInfo;

    @IntDef({SHOW_VERBOSE_LOG, SHOW_DEBUG_LOG, SHOW_INFO_LOG,
            SHOW_WARN_LOG, SHOW_ERROR_LOG, NOT_SHOW_LOG})
    @Retention(RetentionPolicy.SOURCE)
    private @interface LockLevel {
    }

    private Logcat() {
        throw new UnsupportedOperationException();
    }

    public static void initialize(@NonNull Context context) {
        Logcat.context = context.getApplicationContext();
        pkgName = Logcat.context.getPackageName();
        initialize(context, defaultConfig());
    }

    /**
     * @param context Context
     * @param config  Config
     */
    public static void initialize(@NonNull Context context, @NonNull Config config) {
        Logcat.context = context.getApplicationContext();
        pkgName = Logcat.context.getPackageName();
        initPrintThread();
        if (config.logSavePath == null || "".equals(config.logSavePath.trim())) {
            defaultConfig();
        } else {
            checkSaveLogPath(config.logSavePath);
        }
        if (config.logCatLogLevel != null) {
            logCatShowLogType = config.logCatLogLevel;
        }
        if (config.fileLogLevel != null) {
            fileSaveLogType = config.fileLogLevel;
            if (fileSaveLogType == NOT_SHOW_LOG) {
                if (printFileHandlerThread != null) {
                    printFileHandlerThread.quit();
                    printFileHandler = null;
                }
            } else {
                initPrintFileLogThread();
            }
        }
        topLevelTag = config.topLevelTag;

        if (jLog != null) {
            jLog.release();
        }
        if (config.jLog != null) {
            jLog = config.jLog;
        }

        if (config.fileTags != null) {
            fileTags = config.fileTags;
        }
        deleteUnusedLogEntriesAfterDays = config.deleteUnusedLogEntriesAfterDays;
        FILE_SAVE_TIME = TimeUnit.DAYS.toMillis(deleteUnusedLogEntriesAfterDays);
        autoSaveLogToFile = config.autoSaveLogToFile;
        showStackTraceInfo = config.showStackTraceInfo;
        showFileTimeInfo = config.showFileTimeInfo;
        showFilePidInfo = config.showFilePidInfo;
        showFileLogLevel = config.showFileLogLevel;
        showFileLogTag = config.showFileLogTag;
        showFileStackTraceInfo = config.showFileStackTraceInfo;
    }

    private synchronized static void initPrintThread() {
        if (printHandlerThread == null) {
            printHandlerThread = new HandlerThread("PrintLogThread");
            printHandlerThread.start();
            printHandler = new Handler(printHandlerThread.getLooper());
            Log.i(TAG, printHandlerThread.getName() + "#" + printHandlerThread.getThreadId() + " is starting");
        }
    }

    private synchronized static void initPrintFileLogThread() {
        if (printFileHandlerThread == null) {
            printFileHandlerThread = new HandlerThread("PrintFileLogThread");
            printFileHandlerThread.start();
            printFileHandler = new Handler(printFileHandlerThread.getLooper());
            Log.i(TAG, printFileHandlerThread.getName() + "#" + printFileHandlerThread.getThreadId() + " is starting");
        }
    }

    private static Config defaultConfig() {
        Builder builder = newBuilder();

        // 非循环，只是为了减少分支缩进深度
        do {
            String state = Environment.getExternalStorageState();
            // SD 卡不可写
            if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                Log.w(TAG, "Not allow write SD card!");
                break;
            }

            // 未安装 SD 卡
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                Log.w(TAG, "Not mount SD card!");
                break;
            }

            File externalCacheDir = context.getExternalCacheDir();
            // context.getExternalCacheDir() maybe null
            if (externalCacheDir != null) {
                builder.logSavePath = externalCacheDir.getAbsolutePath() + File.separator + DEFAULT_LOG_DIR;
            } else {
                Log.e(TAG, "externalCacheDir is null!");
                builder.fileLogLevel(NOT_SHOW_LOG);
                break;
            }

            // 只有存在外部 SD 卡且可写入的情况下才允许保存日志文件到指定目录路径下
            // 没有指定日志文件存放位置的话，就写到默认位置，即 当前应用 SD 卡根目录下的 Cache/logs 目录中
            String strSaveLogPath = builder.logSavePath;

            checkSaveLogPath(strSaveLogPath);
        } while (false);

        return new Config(builder);
    }

    private static void checkSaveLogPath(@NonNull String strSaveLogPath) {
        if ("".equals(logFolderPath.trim())) {
            File fileSaveLogFolderPath = new File(strSaveLogPath);
            // 保存日志文件的路径不存在的话，就创建它
            if (!fileSaveLogFolderPath.exists()) {
                boolean mkdirs = fileSaveLogFolderPath.mkdirs();
                if (mkdirs) {
                    Log.i(TAG, "Create log folder success!");
                } else {
                    Log.i(TAG, "Create log folder failed!");
                }
            }

            // 指定日志文件保存的路径，文件名由内部按日期时间形式
            logFolderPath = strSaveLogPath;
        }
    }

    public static LogTransaction v() {
        return new LogStackRecord(LogLevel.Verbose);
    }

    public static LogTransaction d() {
        return new LogStackRecord(LogLevel.Debug);
    }

    public static LogTransaction i() {
        return new LogStackRecord(LogLevel.Info);
    }

    public static LogTransaction w() {
        return new LogStackRecord(LogLevel.Warn);
    }

    public static LogTransaction e() {
        return new LogStackRecord(LogLevel.Error);
    }

    public static void v(Object msg) {
        out(SHOW_VERBOSE_LOG, msg);
    }

    public static void d(Object msg) {
        out(SHOW_DEBUG_LOG, msg);
    }

    public static void i(Object msg) {
        out(SHOW_INFO_LOG, msg);
    }

    public static void w(Object msg) {
        out(SHOW_WARN_LOG, msg);
    }

    public static void e(Object msg) {
        out(SHOW_ERROR_LOG, msg);
    }

    public static void v(String tag, Object msg) {
        out(SHOW_VERBOSE_LOG, msg, tag);
    }

    public static void d(String tag, Object msg) {
        out(SHOW_DEBUG_LOG, msg, tag);
    }

    public static void i(String tag, Object msg) {
        out(SHOW_INFO_LOG, msg, tag);
    }

    public static void w(String tag, Object msg) {
        out(SHOW_WARN_LOG, msg, tag);
    }

    public static void e(String tag, Object msg) {
        out(SHOW_ERROR_LOG, msg, tag);
    }

    /**
     * 输出日志
     */
    private static void out(@LockLevel final int logLevel, Object msg, String... tags) {
        if (NOT_SHOW_LOG != (logLevel & logCatShowLogType)) {
            printLog(getStackTraceElement(INDEX), logLevel, msg, null, tags);
        }
        if (autoSaveLogToFile || canWriteLogToFile(tags)) {
            writeLog(logLevel, msg, null, "", true, 0, tags);
        }
    }

    /**
     * 输出日志
     */
    static void out(@LockLevel final int logLevel, @Nullable String jsonText, Object msg, final String filesName, final boolean append, int stackTraceOffset, String... tags) {
        if (NOT_SHOW_LOG != (logLevel & logCatShowLogType)) {
            printLog(getStackTraceElement(INDEX + stackTraceOffset), logLevel, msg, jsonText, tags);
        }
        boolean hasFile = filesName != null;
        if (hasFile) {
            writeLog(logLevel, msg, jsonText, filesName, append, stackTraceOffset, tags);
        } else {
            if (autoSaveLogToFile || canWriteLogToFile(tags)) {
                writeLog(logLevel, msg, jsonText, "", true, stackTraceOffset, tags);
            }
        }
    }

    private static boolean canWriteLogToFile(String... tagArgs) {
        StringBuilder tagBuilder = new StringBuilder();
        boolean hasTop = topLevelTag != null;
        if (hasTop) {
            tagBuilder.append(topLevelTag);
        }
        if (tagArgs != null) {
            for (int i = 0; i < tagArgs.length; i++) {
                if (i == 0 && hasTop) {
                    tagBuilder.append(TAG_SEPARATOR);
                }
                tagBuilder.append(tagArgs[i]);
                if (i < tagArgs.length - 1) {
                    tagBuilder.append(TAG_SEPARATOR);
                }
            }
        }
        if (tagBuilder.length() > 0 && fileTags != null && !fileTags.isEmpty()) {
            return fileTags.containsKey(tagBuilder.toString());
        }
        return false;
    }

    private static void printLog(final StackTraceElement stackTraceElement, final int type, final Object objectMsg, final @Nullable String jsonText, final @Nullable String... tagArgs) {
        checkPrintHandler();
        //linux thread ID
        Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
        final long threadId = Process.myTid();
        printHandler.post(new Runnable() {
            @Override
            public void run() {
                String msg;
                if (logCatShowLogType == NOT_SHOW_LOG) {
                    return;
                }

                String fileName = stackTraceElement.getFileName();
                String methodName = stackTraceElement.getMethodName();
                int lineNumber = stackTraceElement.getLineNumber();

                StringBuilder tagBuilder = new StringBuilder();
                boolean hasTop = topLevelTag != null;
                if (hasTop) {
                    tagBuilder.append(topLevelTag);
                }
                if (tagArgs == null || tagArgs.length == 0) {
                    if (!hasTop) {
                        tagBuilder.append(TAG);
                    }

                } else {
                    for (int i = 0; i < tagArgs.length; i++) {
                        if (i == 0 && hasTop) {
                            tagBuilder.append(TAG_SEPARATOR);
                        }
                        tagBuilder.append(tagArgs[i]);
                        if (i < tagArgs.length - 1) {
                            tagBuilder.append(TAG_SEPARATOR);
                        }
                    }
                }

                methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

                StringBuilder stringBuilder = new StringBuilder();
                if (showStackTraceInfo) {
                    stringBuilder.append("[ (").append(fileName).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");
                }

                if (objectMsg == null) {
                    msg = "null";
                } else {
                    msg = objectMsg.toString();
                }
                stringBuilder.append(msg);

                String tag = tagBuilder.toString();
                String logStr = stringBuilder.toString();
                printJLog(threadId, tag, logStr, type, jsonText);
                //Android Log No Format
                if (!TextUtils.isEmpty(jsonText)) {
                    logStr = stringBuilder.append(BLANK_STR).append(jsonText).toString();
                }

                int tagLength = tag.getBytes().length;
                int totalSize = tagLength + logStr.getBytes().length;
                if (totalSize > MAX_LENGTH) {
                    int index = 0;
                    int length = logStr.length();
                    //Android log 限制包括 tag 的长度,以及为了截取字符串最高效率打印中文字符限制 note:中文字一般3个字节
                    int max = (MAX_LENGTH - tagLength) / 3;
                    int countOfSub = length / max;
                    if (countOfSub > 0) {
                        for (int i = 0; i < countOfSub; i++) {
                            String sub = logStr.substring(index, index + max);
                            printLog(type, tag, sub);
                            index += max;
                        }
                        printLog(type, tag, logStr.substring(index, length));
                        return;
                    }
                }
                printLog(type, tag, logStr);
            }
        });
    }

    private static void checkPrintHandler() {
        if (printHandler == null) {
            synchronized (Logcat.class) {
                if (printHandler == null) {
                    initPrintThread();
                }
            }
        }
    }

    private static void printJLog(long threadId, final String tag, final String logStr, final int type, @Nullable final String jsonText) {
        if (jLog != null) {
            // 得到当前日期时间的指定格式字符串
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MM_DD_HH_MM_SS_SSS);
            String strDateTimeLogHead = simpleDateFormat.format(new Date());
            int pid = Process.myPid();
            // 将标签、日期时间头、日志信息体结合起来
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(strDateTimeLogHead)
                    .append(BLANK_STR)
                    .append(String.format("%s-%s/%s", pid, threadId, pkgName))
                    .append(BLANK_STR);

            switch (type) {
                case SHOW_VERBOSE_LOG:
                    stringBuilder.append(V);
                    break;
                case SHOW_DEBUG_LOG:
                    stringBuilder.append(D);
                    break;
                case SHOW_INFO_LOG:
                    stringBuilder.append(I);
                    break;
                case SHOW_WARN_LOG:
                    stringBuilder.append(W);
                    break;
                case SHOW_ERROR_LOG:
                    stringBuilder.append(E);
                    break;
                default:
                    break;
            }
            if (TextUtils.isEmpty(jsonText)) {
                jLog.println(stringBuilder.append(tag).append(":").append(BLANK_STR).append(logStr).toString());
            } else {
                String indentJSON = null;
                try {
                    if (jsonText.startsWith("{")) {
                        JSONObject jsonObject = new JSONObject(jsonText);
                        indentJSON = jsonObject.toString(JSON_INDENT);
                    } else if (jsonText.startsWith("[")) {
                        JSONArray jsonArray = new JSONArray(jsonText);
                        indentJSON = jsonArray.toString(JSON_INDENT);
                    }
                    stringBuilder.append(indentJSON);
                    stringBuilder.append(LINE_SEPARATOR);
                } catch (JSONException e) {
                    Log.e("JSONException/", e.getCause().getMessage() + LINE_SEPARATOR + jsonText);
                    return;
                }
                jLog.println(stringBuilder.append(tag).append(":").append(BLANK_STR).append(logStr).append(LINE_SEPARATOR).append(indentJSON).append(LINE_SEPARATOR).toString());
            }
        }
    }

    private static void printLog(int type, String tag, String logStr) {
        switch (type) {
            case SHOW_VERBOSE_LOG:
                Log.v(tag, logStr);
                break;
            case SHOW_DEBUG_LOG:
                Log.d(tag, logStr);
                break;
            case SHOW_INFO_LOG:
                Log.i(tag, logStr);
                break;
            case SHOW_WARN_LOG:
                Log.w(tag, logStr);
                break;
            case SHOW_ERROR_LOG:
                Log.e(tag, logStr);
                break;
            default:
                break;
        }
    }

    /**
     * 写Log 到文件
     */
    private static void writeLog(@LockLevel final int logLevel, final Object msg, @Nullable final String jsonText,
                                 @Nullable final String logFileName, final boolean append, int stackTraceOffset, final String... tag) {
        if (NOT_SHOW_LOG != (logLevel &
                fileSaveLogType)) {
            //当前线程的堆栈情况
            final StackTraceElement stackTraceElement = getStackTraceElement(INDEX + 1 + stackTraceOffset);
            //linux thread ID
            Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT);
            final long threadId = Process.myTid();
            if (printFileHandler != null) {
                printFileHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        fileLog(stackTraceElement, logLevel, msg, jsonText, logFileName, append, threadId, tag);
                    }
                });
            }
        }
    }

    private static void fileLog(StackTraceElement stackTraceElement, int type, Object
            objectMsg, @Nullable String jsonText, @Nullable String logFileName, final boolean append, long threadId, @Nullable String... tagArgs) {
        String msg;
        if (fileSaveLogType == NOT_SHOW_LOG) {
            return;
        }

        String fileName = stackTraceElement.getFileName();
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        int lineNumber = stackTraceElement.getLineNumber();

        StringBuilder tagBuilder = new StringBuilder();
        boolean hasTop = topLevelTag != null;
        if (hasTop) {
            tagBuilder.append(topLevelTag);
        }
        if (tagArgs == null || tagArgs.length == 0) {
            if (!hasTop) {
                tagBuilder.append(TAG);
            }

        } else {
            for (int i = 0; i < tagArgs.length; i++) {
                if (i == 0 && hasTop) {
                    tagBuilder.append(TAG_SEPARATOR);
                }
                tagBuilder.append(tagArgs[i]);
                if (i < tagArgs.length - 1) {
                    tagBuilder.append(TAG_SEPARATOR);
                }
            }
        }


        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();

        // 得到当前日期时间的指定格式字符串
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MM_DD_HH_MM_SS_SSS);
        String strDateTimeLogHead = simpleDateFormat.format(new Date());
        int pid = Process.myPid();
        // 将标签、日期时间头、日志信息体结合起来
        if (showFileTimeInfo) {
            stringBuilder
                    .append(strDateTimeLogHead)
                    .append(BLANK_STR);
        }

        if (showFilePidInfo) {
            stringBuilder
                    .append(String.format("%s-%s/%s", pid, threadId, pkgName))
                    .append(BLANK_STR);
        }

        if (showFileLogLevel) {
            switch (type) {
                case SHOW_VERBOSE_LOG:
                    stringBuilder.append(V);
                    break;
                case SHOW_DEBUG_LOG:
                    stringBuilder.append(D);
                    break;
                case SHOW_INFO_LOG:
                    stringBuilder.append(I);
                    break;
                case SHOW_WARN_LOG:
                    stringBuilder.append(W);
                    break;
                case SHOW_ERROR_LOG:
                    stringBuilder.append(E);
                    break;
                default:
                    break;
            }
            if (!showFileLogTag && !showFileStackTraceInfo) {
                stringBuilder.append(BLANK_STR);
            }
        }

        if (showFileLogTag) {
            stringBuilder.append(tagBuilder.toString());
            if (!showFileStackTraceInfo) {
                stringBuilder.append(":");
                stringBuilder.append(BLANK_STR);
            }
        }

        if (showFileStackTraceInfo) {
            if (showFileTimeInfo || showFilePidInfo || showFileLogLevel || showFileLogTag) {
                if (!showFileLogLevel) {
                    stringBuilder.append(":");
                }
                stringBuilder.append(BLANK_STR);
            }

            stringBuilder.append("[ (").append(fileName).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");
        }


        if (objectMsg == null) {
            msg = "null";
        } else {
            msg = objectMsg.toString();
        }
        stringBuilder.append(msg);
        stringBuilder.append(LINE_SEPARATOR);
        if (!TextUtils.isEmpty(jsonText)) {
            String indentJSON = null;
            try {
                if (jsonText.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(jsonText);
                    indentJSON = jsonObject.toString(JSON_INDENT);
                } else if (jsonText.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(jsonText);
                    indentJSON = jsonArray.toString(JSON_INDENT);
                }
                stringBuilder.append(indentJSON);
                stringBuilder.append(LINE_SEPARATOR);
            } catch (JSONException e) {
                Log.e("JSONException/", e.getCause().getMessage() + LINE_SEPARATOR + jsonText);
                return;
            }
        }

        switch (type) {
            case SHOW_VERBOSE_LOG:
                saveLogToFile(stringBuilder.toString(), logFileName, append);
                break;
            case SHOW_DEBUG_LOG:
                saveLogToFile(stringBuilder.toString(), logFileName, append);
                break;
            case SHOW_INFO_LOG:
                saveLogToFile(stringBuilder.toString(), logFileName, append);
                break;
            case SHOW_WARN_LOG:
                saveLogToFile(stringBuilder.toString(), logFileName, append);
                break;
            case SHOW_ERROR_LOG:
                saveLogToFile(stringBuilder.toString(), logFileName, append);
                break;
            default:
                break;
        }
    }

    /**
     * 将msg 写入日志文件
     *
     * @param msg         msg
     * @param logFileName log 文件名
     */
    private static void saveLogToFile(String msg, @Nullable String logFileName, final boolean append) {
        if (TextUtils.isEmpty(logFileName)) {
            // 得到当前日期时间的指定格式字符串
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fileSimpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
            String strDateTimeFileName = fileSimpleDateFormat.format(new Date());
            logFileName = strDateTimeFileName + LOGFILE_SUFFIX;
        }
        FileWriter objFilerWriter = null;
        BufferedWriter objBufferedWriter = null;

        do { // 非循环，只是为了减少分支缩进深度
            String state = Environment.getExternalStorageState();
            // 未安装 SD 卡
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                Log.i(TAG, "Not mount SD card!");
                break;
            }


            File rootPath = new File(logFolderPath);
            if (rootPath.exists()) {
                File[] listFiles = rootPath.listFiles();
                Date now = new Date();
                boolean delete = false;
                List<String> delPaths = new ArrayList<>();
                for (File file : listFiles) {
                    long lastModified = file.lastModified();
                    String name = file.getName();
                    if (now.getTime() - lastModified >= FILE_SAVE_TIME) {
                        delete = file.delete();
                        if (delete) {
                            Log.i(TAG, "deleteUnusedLogEntries is " + name);
                            delPaths.add(file.getAbsolutePath());
                        }
                    }
                }
                if (delete) {
                    MediaScannerConnection.scanFile(context, delPaths.toArray(new String[0]), null, null);
                }

                File fileLogFilePath = new File(logFolderPath, logFileName);
                // 如果日志文件不存在，则创建它
                if (!fileLogFilePath.exists()) {
                    try {
                        boolean createSuccess = fileLogFilePath.createNewFile();
                        if (createSuccess) {
                            MediaScannerConnection.scanFile(context, new String[]{fileLogFilePath.getAbsolutePath()}, null, null);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }

                // 如果执行到这步日志文件还不存在，就不写日志到文件了
                if (!fileLogFilePath.exists()) {
                    Log.i(TAG, "Create log file failed!");
                    break;
                }

                try {
                    // 续写不覆盖
                    objFilerWriter = new FileWriter(fileLogFilePath, append);
                } catch (IOException e1) {
                    Log.i(TAG, "New FileWriter Instance failed");
                    e1.printStackTrace();
                    break;
                }

                objBufferedWriter = new BufferedWriter(objFilerWriter);

                try {
                    objBufferedWriter.write(msg);
                    objBufferedWriter.flush();
                } catch (IOException e) {
                    Log.i(TAG, "objBufferedWriter.write or objBufferedWriter.flush failed");
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "LogTransaction savePaht invalid!");
            }


        } while (false);

        if (null != objBufferedWriter) {
            try {
                objBufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != objFilerWriter) {
            try {
                objFilerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 当前线程的堆栈情况
     */
    private static StackTraceElement getStackTraceElement(int index) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[index];
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }
}
