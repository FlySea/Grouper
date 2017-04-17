package cn.com.libbasic.util;

import android.util.Log;

import cn.com.libbase.LibConfigs;


public class LogUtil {

    public static boolean IS_LOG = LibConfigs.DEBUG;

    public static void d(String tag, String msg) {
        try {
            if (IS_LOG) {
                Log.d(tag, tag + " : " + msg);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void i(String tag, String msg) {
        try {
            if (IS_LOG) {
                Log.i(tag, tag + " : " + msg);
            }
        } catch (Throwable t) {
        }
    }

    public static void v(String tag, String msg) {
        try {
            if (IS_LOG) {
                Log.v(tag, tag + " : " + msg);
            }
        } catch (Throwable t) {
        }
    }

    public static void e(String tag, String msg) {
        try {
            Log.e(tag, tag + " : " + msg);
        } catch (Throwable t) {
        }

    }

    public static void w(String tag, String msg) {
        try {
            if (IS_LOG) {
                Log.w(tag, tag + " : " + msg);
            }
        } catch (Throwable t) {
        }

    }
}
