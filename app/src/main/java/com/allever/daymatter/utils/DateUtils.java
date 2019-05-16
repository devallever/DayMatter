package com.allever.daymatter.utils;

import android.content.Context;
import android.util.Log;

import com.zf.daymatter.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Allever on 18/5/28.
 */

public class DateUtils {
    private DateUtils(){}

    /**
     * 返回数据格式
     * 2018-05-29 星期二
     * @param year 年
     * @param month 月, 具体值1-12
     * @param day 日
     * */
    public static String formatDate_Y_M_D_WEEK(Context context, int year, int month, int day, int weekday){
        Calendar calendar = Calendar.getInstance();
        String formatDate;

        String strWeekDay = "";
        switch (weekday){
            case 1:
                strWeekDay = context.getResources().getString(R.string.sunday);
                break;
            case 2:
                strWeekDay = context.getResources().getString(R.string.monday);
                break;
            case 3:
                strWeekDay = context.getResources().getString(R.string.tuesday);
                break;
            case 4:
                strWeekDay = context.getResources().getString(R.string.wednesday);
                break;
            case 5:
                strWeekDay = context.getResources().getString(R.string.thursday);
                break;
            case 6:
                strWeekDay = context.getResources().getString(R.string.friday);
                break;
            case 7:
                strWeekDay = context.getResources().getString(R.string.saturday);
                break;
            default:
                break;
        }

        formatDate = year + "-" + month + "-" + day + " " + strWeekDay;
        return formatDate;
    }

    /**
     * 返回数据格式
     * 2018-05-29 星期二
     * @param year 年
     * @param month 月, 下标值0-11
     * @param day 日
     * */
    public static String formatDate_Y_M_D_WEEK_New(Context context, int year, int month, int day, int weekday){
        if (context == null){
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String formatDate;

        String strWeekDay = "";
        switch (weekday){
            case 1:
                strWeekDay = context.getResources().getString(R.string.sunday);
                break;
            case 2:
                strWeekDay = context.getResources().getString(R.string.monday);
                break;
            case 3:
                strWeekDay = context.getResources().getString(R.string.tuesday);
                break;
            case 4:
                strWeekDay = context.getResources().getString(R.string.wednesday);
                break;
            case 5:
                strWeekDay = context.getResources().getString(R.string.thursday);
                break;
            case 6:
                strWeekDay = context.getResources().getString(R.string.friday);
                break;
            case 7:
                strWeekDay = context.getResources().getString(R.string.saturday);
                break;
            default:
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(context.getResources().getString(R.string.date_fromat_yyyy_mm_dd_common));
        String yMd = sdf.format(new Date(calendar.getTimeInMillis()));

        formatDate = yMd + " " + strWeekDay;
        return formatDate;
    }

    /**
     * 计算从某天起，几天后的日期
     * @param year 年
     * @param month 月, 具体值1-12
     * @param day 日
     * @param interval 几天后
     * */
    public static String calDayAfter(Context context, int year, int month, int day, int interval){
        if (context == null){
            return "";
        }
        String result;

        //开始时间日历对象
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, month-1, day);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startCalendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, interval);

        SimpleDateFormat sdf = new SimpleDateFormat(context.getResources().getString(R.string.date_fromat_yyyy_mm_dd_common));
        result = sdf.format(new Date(calendar.getTimeInMillis()));

        return result;
    }

    /**
     * 计算从某天起，几天前的日期
     * @param year 年
     * @param month 月, 具体值1-12
     * @param day 日
     * @param interval 几天前
     * */
    public static String calDayBefore(Context context, int year, int month, int day, int interval){
        if (context == null){
            return "";
        }
        String result;

        //开始时间日历对象
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, month-1, day);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startCalendar.getTimeInMillis());
        calendar.add(Calendar.DAY_OF_MONTH, -1 * interval);

        SimpleDateFormat sdf = new SimpleDateFormat(context.getResources().getString(R.string.date_fromat_yyyy_mm_dd_common));
        result = sdf.format(new Date(calendar.getTimeInMillis()));

        return result;
    }

    /**
     * 计算某天与当前的间隔天数
     * */
    public static int calDistanceDayCount(int year, int month, int day){
        Calendar fromCalendar = Calendar.getInstance();


        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(year, month, day);

        double days_d = (toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis())/(1000 * 60 * 60 * 24.0);

        //取整,返回大于或等于参数的最小（最接近负无穷大） double值，等于一个数学整数。
        //10.2 -> 11.0
        //-10.2 -> -10.0
        int days = (int)Math.ceil(days_d);

        return days;
    }
}
