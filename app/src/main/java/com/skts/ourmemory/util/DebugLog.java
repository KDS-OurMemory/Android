package com.skts.ourmemory.util;

import android.util.Log;

import com.skts.ourmemory.common.GlobalApplication;

public class DebugLog {

    /**
     * Log Level Error
     **/
    public static final void e(String TAG, String message) {
        if (GlobalApplication.DEBUG) {
            Log.e(TAG, buildLogMsg(message));
        }
    }

    /**
     * Log Level Warning
     **/
    public static final void w(String TAG, String message) {
        if (GlobalApplication.DEBUG) {
            Log.w(TAG, buildLogMsg(message));
        }
    }

    /**
     * Log Level Information
     **/
    public static final void i(String TAG, String message) {
        if (GlobalApplication.DEBUG) {
            Log.i(TAG, buildLogMsg(message));
        }
    }

    /**
     * Log Level Debug
     **/
    public static final void d(String TAG, String message) {
        if (GlobalApplication.DEBUG) {
            Log.d(TAG, buildLogMsg(message));
        }
    }

    /**
     * Log Level Verbose
     **/
    public static final void v(String TAG, String message) {
        if (GlobalApplication.DEBUG) {
            Log.v(TAG, buildLogMsg(message));
        }
    }

    public static String buildLogMsg(String message) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        stringBuilder.append(stackTraceElement.getFileName().replace(".java", ""));
        stringBuilder.append("::");
        stringBuilder.append(stackTraceElement.getMethodName());
        stringBuilder.append("]");
        stringBuilder.append("\n");
        stringBuilder.append(message);

        return stringBuilder.toString();
    }
}
