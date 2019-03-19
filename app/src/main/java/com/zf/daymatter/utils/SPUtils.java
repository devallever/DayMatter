package com.zf.daymatter.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Allever on 18/5/26.
 */

public class SPUtils {

    private static final String SP_CONFIG = "config";
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    private static final String KEY_CURRENT_DAY_REMIND = "current_day_remind";
    private static final String KEY_BEFORE_DAY_REMIND = "before_day_remind";

    private SPUtils(){}

    public static void setFirstLaunch(Context context, boolean isFirstLaunch){
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_FIRST_LAUNCH, isFirstLaunch);
        editor.apply();
    }

    public static boolean getIsFirstLaunch(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE);
        boolean isFirstLaunch = sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
        return isFirstLaunch;
    }

    public static void setCurrentDayRemind(Context context, boolean isCurrentDayRemind){
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_CURRENT_DAY_REMIND, isCurrentDayRemind);
        editor.apply();
    }

    public static boolean getIsCurrentDayRemind(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE);
        boolean currentDayRemind = sharedPreferences.getBoolean(KEY_CURRENT_DAY_REMIND, true);
        return currentDayRemind;
    }

    public static void setBeforeDayRemind(Context context, boolean isBeforeDayRemind){
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_BEFORE_DAY_REMIND, isBeforeDayRemind);
        editor.apply();
    }

    public static boolean getIsBeforeDayRemind(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CONFIG, Context.MODE_PRIVATE);
        boolean beforeDayRemind = sharedPreferences.getBoolean(KEY_BEFORE_DAY_REMIND, true);
        return beforeDayRemind;
    }


}
