package com.allever.daymatter.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Allever on 18/5/21.
 */

public class ItemDayMatter implements Parcelable {
    private int id;
    private String title;
    private int leftDay;
    private int year;
    private int month;
    private int day;
    private int weekDay;
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

    public int getLeftDay() {
        return leftDay;
    }

    public void setLeftDay(int leftDay) {
        this.leftDay = leftDay;
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

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.leftDay);
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
        dest.writeInt(this.weekDay);
        dest.writeLong(this.lastUpdateTime);
    }

    public ItemDayMatter() {
    }

    protected ItemDayMatter(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.leftDay = in.readInt();
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
        this.weekDay = in.readInt();
        this.lastUpdateTime = in.readLong();
    }

    public static final Parcelable.Creator<ItemDayMatter> CREATOR = new Parcelable.Creator<ItemDayMatter>() {
        @Override
        public ItemDayMatter createFromParcel(Parcel source) {
            return new ItemDayMatter(source);
        }

        @Override
        public ItemDayMatter[] newArray(int size) {
            return new ItemDayMatter[size];
        }
    };
}
