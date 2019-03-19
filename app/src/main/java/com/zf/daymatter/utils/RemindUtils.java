package com.zf.daymatter.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zf.daymatter.receiver.CurrentAlarmReceiver;

import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Allever on 18/6/1.
 */

public class RemindUtils {

    private static final String TAG = "RemindUtils";

    public static final int REQUEST_CODE_CURRENT_REMIND = 1;
    public static final int REQUEST_CODE_BEFORE_REMIND = 2;

    private RemindUtils(){}

    public static void setRemind(Context context, int hour, int min, int uniqueRequestCode, Class clz){
        Log.d(TAG, "setRemind: ");

        if (context == null){
            return;
        }

        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        //设置在几分提醒
        mCalendar.set(Calendar.MINUTE, min);
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        //CurrentAlarmReceiver.class为广播接受者
        Intent intent = new Intent(context ,clz);
        PendingIntent pi = PendingIntent.getBroadcast(context, uniqueRequestCode, intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);

        Log.d(TAG, "startRemind: time = " + mCalendar.getTimeInMillis());
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }

    public static void stopRemind(Context context, int uniqueRequestCode, Class clz){
        Log.d(TAG, "stopRemind: ");
        if (context == null){
            return;
        }

        Intent intent = new Intent(context, clz);
        PendingIntent pi = PendingIntent.getBroadcast(context, uniqueRequestCode, intent, 0);
        AlarmManager am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        //取消提醒
        am.cancel(pi);
    }
}
