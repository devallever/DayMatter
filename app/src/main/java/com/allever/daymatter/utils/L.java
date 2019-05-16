package com.allever.daymatter.utils;

import android.util.Log;

/**
 * Created by Allever on 18/5/21.
 */

public class L {
    private static final String TAG = "L";
    public static boolean isDebug = true;

    public static void d(String msg){
        if (isDebug){
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg){
        if (isDebug){
            Log.e(TAG, msg, new Throwable());
        }
    }
}
