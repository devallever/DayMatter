package com.zf.daymatter.data;

import android.content.Context;

import com.zf.daymatter.bean.ItemSlidMenuSort;

import java.util.List;

/**
 * Created by Allever on 18/5/26.
 */

public interface DataSource {
    String getEventTitle(int id);

    /**
     * 获取侧滑菜单中列表数据
     * @param context
     * @return
     */
    @Deprecated
    List<ItemSlidMenuSort> getSlidMenuSortData(Context context);
    void getSlidMenuSortData(Context context, DataListener<List<ItemSlidMenuSort>> dataListener);

    /**
     * 第一次启动时初始化数据库，添加默认数据
     * @param context
     */
    void addDefaultSortData(Context context);

    /**
     * 第一次启动时，初始化配置信息
     */
    void addDefaultConfig();

    /**
     * 保存倒计时事件
     * @param
     */
    @Deprecated
    boolean saveEvent(String eventTitle, int year, int month, int day, int weekday, int sortId, boolean isTop, int repeatType, boolean isEnd, int endYear, int endMonth, int endDay, int endWeekday);
    void saveEvent(Event event);

    /**
     * 获取 某分类的倒计时事件
     * @param sortId
     * @return
     */
    @Deprecated
    List<Event> getSortEventList(int sortId);
    void getSortEventList(int sortId, DataListener<List<Event>> dataListener);

    /**
     * 获取所有倒计时事件
     * @return
     */
    @Deprecated
    List<Event> getAllEventList();
    void getAllEventList(final DataListener<List<Event>> dataListener);

    /**
     * 获取分类名称
     * @param sortId
     * @return
     */
    String getSortName(int sortId);

    @Deprecated
    boolean updateEvent(int eventId, String eventTitle, int year, int month, int day, int weekday, int sortId, boolean isTop, int repeatType, boolean isEnd, int endYear, int endMonth, int endDay, int endWeekday);
    /**
     * 修改倒计时事件
     * @param event
     */
    void updateEvent(Event event);

    /**
     * 获取倒计时事件详细信息
     * @param eventId
     * @return
     */
    Event getEvent(int eventId);

    /**
     * 删除倒计时事件
     * @param eventId
     * @return
     */
    boolean deleteEvent(int eventId);

    /**
     * 获取提醒配置信息
     * @return
     */
    Config getRemindConfigData();

    /**
     * 修改提醒配置
     * @param value
     */
    void updateCurrentRemindSwitch(boolean value);
    void updateBeforeRemindSwitch(boolean value);
    void updateCurrentRemindTime(int hour, int min);
    void updateBeforeRemindTiem(int hour, int min);

    /***
     * 获取指定日期的倒计时事件
     * @param year
     * @param month
     * @param day
     * @return
     */
    @Deprecated
    List<Event> getEventListByDate(int year, int month, int day);
    void getEventListByDate(int year, int month, int day, DataListener<List<Event>> dataListener);
}
