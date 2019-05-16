package com.allever.daymatter.utils;

/**
 * Created by Allever on 18/5/28.
 */

public class Constants {

    //私有构造
    private Constants(){}

    //倒计时事件重复类型
    public static final int REPEAT_TYPE_NO_REPEAT = 0;
    public static final int REPEAT_TYPE_PER_WEEK = 1;
    public static final int REPEAT_TYPE_PER_MONTH = 2;
    public static final int REPEAT_TYPE_PER_YEAR = 3;

    //EventBus 事件类型
    public static final String EVENT_SELECT_DISPLAY_SORT_LIST = "EVENT_SELECT_DISPLAY_SORT_LIST";
    public static final String EVENT_ADD_DAY_MATTER = "EVENT_ADD_DAY_MATTER";
    public static final String EVENT_MODIFY_DAY_MATTER = "EVENT_MODIFY_DAY_MATTER";
    public static final String EVENT_DELETE_DAY_MATTER = "EVENT_DELETE_DAY_MATTER";

    public static final int SORT_LIFE = 1;
    public static final int SORT_WORK = 2;
    public static final int SORT_MEMORY_DAY = 3;


}
