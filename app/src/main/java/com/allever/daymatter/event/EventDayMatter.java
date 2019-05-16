package com.allever.daymatter.event;

/**
 * Created by Allever on 18/5/28.
 */

public class EventDayMatter {
    private String event;
    private int sortId;
    private int eventId;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
