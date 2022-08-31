package com.androidz.base_modules.lib_logcat;

import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lazy on 2017/4/12.
 */

final class LogStackRecord extends LogTransaction {
    static final int OP_NULL = 0;

    static final int OP_TAG = 1;
    static final int OP_TAGS = 2;
    static final int OP_MSG = 3;
    static final int OP_MSGS = 4;
    static final int OP_FILE = 5;
    static final int OP_LN = 6;
    static final int OP_FORMAT = 7;
    static final int OP_JSON_FORMAT = 8;
    static final int OP_FILE_APPEND = 9;
    static final int OP_STACKTRACE_OFFSET = 10;

    private final LogLevel logLevel;

    LogStackRecord(LogLevel logLevel) {
        this.logLevel = logLevel;
    }


    @Override
    public LogTransaction msg(@NonNull Object msg) {
        doOp(OP_MSG, msg);
        return this;
    }

    @Override
    public LogTransaction msgs(@NonNull Object... msg) {
        doOp(OP_MSGS, msg);
        return this;
    }

    @Override
    public LogTransaction tag(@NonNull String tag) {
        doOp(OP_TAG, tag);
        return this;
    }

    @Override
    public LogTransaction tags(@NonNull String... tags) {
        doOp(OP_TAGS, tags);
        return this;
    }

    @Override
    public LogTransaction file() {
        doOp(OP_FILE, "");
        return this;
    }

    @Override
    public LogTransaction file(@NonNull String fileName) {
        doOp(OP_FILE, fileName);
        return this;
    }

    @Override
    public LogTransaction ln() {
        doOp(OP_LN, Logcat.LINE_SEPARATOR);
        return this;
    }

    @Override
    public LogTransaction format(@NonNull String format, Object... args) {
        doOp(OP_FORMAT, new Pair<>(format, Arrays.asList(args)));
        return this;
    }

    @Override
    public LogTransaction fmtJSON(@NonNull String json) {
        doOp(OP_JSON_FORMAT, json);
        return this;
    }

    @Override
    public LogTransaction append(boolean append) {
        doOp(OP_FILE_APPEND, append);
        return this;
    }

    @Override
    public LogTransaction stackTrace(int offset) {
        doOp(OP_STACKTRACE_OFFSET, offset);
        return this;
    }

    @Override
    public LogTransaction out(Boolean logCatShow, Boolean logFileEnable) {
        List<Object> msgsList = new ArrayList<>();
        List<String> tagsList = new ArrayList<>();
        String filesName = null;
        String jsonText = null;
        boolean fileAppend = true;
        int stackTraceOffset = 0;

        while (mHead != null) {

            switch (mHead.cmd) {
                case OP_MSG:
                    msgsList.add(mHead.obj);
                    break;
                case OP_MSGS:
                    msgsList.addAll(Arrays.asList((Object[]) mHead.obj));
                    break;
                case OP_TAG:
                    tagsList.add((String) mHead.obj);
                    break;
                case OP_TAGS:
                    tagsList.addAll(Arrays.asList((String[]) mHead.obj));
                    break;
                case OP_FILE:
                    filesName = ((String) mHead.obj);
                    break;
                case OP_LN:
                    msgsList.add(mHead.obj);
                    break;
                case OP_FORMAT:
                    Pair<String, List<String>> pair = (Pair<String, List<String>>) mHead.obj;
                    String format = String.format(pair.first, pair.second.toArray());
                    msgsList.add(format);
                    break;
                case OP_JSON_FORMAT:
                    jsonText = ((String) mHead.obj);
                    break;
                case OP_FILE_APPEND:
                    fileAppend = ((boolean) mHead.obj);
                    break;
                case OP_STACKTRACE_OFFSET:
                    stackTraceOffset = ((int) mHead.obj);
                    break;
                default:
                    break;
            }

            mHead = mHead.next;
            if (mHead != null) {
                mHead.prev = null;
            }
        }
        mTail = null;
        mNumOp = 0;

        boolean hasMsg = !msgsList.isEmpty();
        boolean hasTag = !tagsList.isEmpty();

        StringBuilder builder = new StringBuilder();
        for (Object o : msgsList) {
            String str = o.toString();
            builder.append(str);
            if (!Logcat.LINE_SEPARATOR.equals(str)) {
                builder.append(Logcat.BLANK_STR);
            }
        }
        String[] tags = new String[tagsList.size()];
        tagsList.toArray(tags);
        String msg = builder.toString();
        Logcat.out(logLevel.value, jsonText, msg, filesName, fileAppend, stackTraceOffset, logCatShow, logFileEnable, tags);
        return this;
    }

    @Override
    public LogTransaction out() {
        return out(null, null);
    }

    static final class Op {
        Op next;
        Op prev;
        int cmd;
        Object obj;
    }


    Op mHead;
    Op mTail;
    int mNumOp;


    private void doOp(int opCmd, Object object) {
        if (object == null) {
            object = "null";
        }
        Op op = new Op();
        op.cmd = opCmd;
        op.obj = object;
        addOp(op);
    }

    void addOp(Op op) {
        if (mHead == null) {
            mHead = mTail = op;
        } else {
            op.prev = mTail;
            mTail.next = op;
            mTail = op;
        }

        mNumOp++;
    }

}
