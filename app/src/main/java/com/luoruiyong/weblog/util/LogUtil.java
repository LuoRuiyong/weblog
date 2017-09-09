package com.luoruiyong.weblog.util;

import android.util.Log;

/**调试帮助类
 * Created by Administrator on 2017/9/8.
 */

public class LogUtil {

    private static final String TAG = "WeblogDebug";
    private static final int VERBOSE = 1;
    private static final int DEBUG   = 2;
    private static final int INFO    = 3;
    private static final int WARN    = 4;
    private static final int ERROR   = 5;
    private static int level = 1;

    public static void v(String msg){
        if(level <= LogUtil.VERBOSE){
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg){
        if(level <= LogUtil.DEBUG) {
            Log.w(TAG, msg);
        }

    }

    public static void i(String msg){
        if(level <= LogUtil.INFO){
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg){
        if(level <= LogUtil.WARN){
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg){
        if(level <= LogUtil.ERROR){
            Log.e(TAG, msg);
        }
    }
}
