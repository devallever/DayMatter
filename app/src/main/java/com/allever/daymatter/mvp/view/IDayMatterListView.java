package com.allever.daymatter.mvp.view;

import com.allever.daymatter.bean.ItemDayMatter;

import java.util.List;

/**
 * Created by Allever on 18/5/21.
 */

public interface IDayMatterListView {
    void setDayMatterList(List<ItemDayMatter> dayMatterList);

    void setEmptyData();

    void setTvTitle(String title);
    void setTvLeftDay(String leftDay);
    void setTvTargetDate(String tvTargetDate);
}
