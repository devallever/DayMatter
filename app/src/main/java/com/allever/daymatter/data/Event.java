package com.allever.daymatter.data;

import org.litepal.crud.DataSupport;

/**
 * Created by Allever on 18/5/21.
 */

public class Event extends DataSupport {
    private int id;

    //事件标题
    private String title;

    private int year;
    private int month;
    private int day;
    private int weekDay;

    //分类
    private int sortId;

    //是否置顶
    private boolean top;

    /**
     * 重复类型
     * 0：不重复
     * 1：每周重复
     * 2：每月
     * 3：每年*/
    private int repeatType;

    //是否结束
    private boolean endSwitch;

    private int endYear;
    private int endMonth;
    private int endDay;
    private int endWeekday;

    private long lastUpdateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }


    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    public boolean isEndSwitch() {
        return endSwitch;
    }

    public void setEndSwitch(boolean endSwitch) {
        this.endSwitch = endSwitch;
    }

    public int getEndWeekday() {
        return endWeekday;
    }

    public void setEndWeekday(int endWeekday) {
        this.endWeekday = endWeekday;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public static class Sort extends DataSupport{

        private int id;

        //分类名称
        private String name;

        //是否默认分类
        private boolean defaultSort;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDefaultSort() {
            return defaultSort;
        }

        public void setDefaultSort(boolean defaultSort) {
            this.defaultSort = defaultSort;
        }
    }
}
