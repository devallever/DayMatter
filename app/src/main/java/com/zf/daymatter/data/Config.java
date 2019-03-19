package com.zf.daymatter.data;

import org.litepal.crud.DataSupport;

/**
 * Created by Allever on 18/6/1.
 */

public class Config extends DataSupport {
    private int id;

    private int currentDayRemind;
    private int currentRemindHour;
    private int currentRemindMin;

    private int beforeDayRemind;
    private int beforeRemindHour;
    private int beforeRemindMin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public int getCurrentRemindHour() {
        return currentRemindHour;
    }

    public void setCurrentRemindHour(int currentRemindHour) {
        this.currentRemindHour = currentRemindHour;
    }

    public int getCurrentRemindMin() {
        return currentRemindMin;
    }

    public void setCurrentRemindMin(int currentRemindMin) {
        this.currentRemindMin = currentRemindMin;
    }



    public int getBeforeRemindHour() {
        return beforeRemindHour;
    }

    public void setBeforeRemindHour(int beforeRemindHour) {
        this.beforeRemindHour = beforeRemindHour;
    }

    public int getBeforeRemindMin() {
        return beforeRemindMin;
    }

    public void setBeforeRemindMin(int beforeRemindMin) {
        this.beforeRemindMin = beforeRemindMin;
    }

    public int getCurrentDayRemind() {
        return currentDayRemind;
    }

    public void setCurrentDayRemind(int currentDayRemind) {
        this.currentDayRemind = currentDayRemind;
    }

    public int getBeforeDayRemind() {
        return beforeDayRemind;
    }

    public void setBeforeDayRemind(int beforeDayRemind) {
        this.beforeDayRemind = beforeDayRemind;
    }
}
