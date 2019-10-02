package com.allever.daymatter.mvp.presenter;

import android.content.Context;

import com.allever.daymatter.bean.ItemDayMatter;
import com.allever.daymatter.data.DataListener;
import com.allever.daymatter.data.Event;
import com.allever.daymatter.utils.DateUtils;
import com.allever.daymatter.R;
import com.allever.daymatter.mvp.BasePresenter;
import com.allever.daymatter.mvp.view.IDayMatterListView;
import com.allever.lib.common.util.log.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Allever
 * @date 18/5/21
 */

public class DayMatterListPresenter extends BasePresenter<IDayMatterListView>{
    /**
     * 获取指定分类事件列表
     * */
    public void getDayMatterData(final Context context, int sortId) {
        LogUtils.INSTANCE.d("getDayMatterData: with sortId");
        mDataSource.getSortEventList(sortId, new DataListener<List<Event>>() {
            @Override
            public void onSuccess(List<Event> data) {
                setDayMatterList(context, data);
            }

            @Override
            public void onFail(String msg) {}
        });

    }

    /**
     * 获取所有事件
     * */
    public void getDayMatterData(final Context context) {
        LogUtils.INSTANCE.d( "getDayMatterData: ");
        mDataSource.getAllEventList(new DataListener<List<Event>>() {
            @Override
            public void onSuccess(List<Event> data) {
                setDayMatterList(context, data);
            }

            @Override
            public void onFail(String msg) {}
        });

    }

    private void setDayMatterList(Context context, List<Event> eventList){
        List<ItemDayMatter> dayMatterList = new ArrayList<>();
        ItemDayMatter topDayMatterItem = null;
        for (Event event: eventList){
            ItemDayMatter itemDayMatter = new ItemDayMatter();
            itemDayMatter.setId(event.getId());

            //计算倒计时天数
            int leftDay = DateUtils.calDistanceDayCount(event.getYear(),
                    event.getMonth()-1,
                    event.getDay());
            itemDayMatter.setLeftDay(leftDay);

            itemDayMatter.setTitle(event.getTitle());
            itemDayMatter.setYear(event.getYear());
            itemDayMatter.setMonth(event.getMonth());
            itemDayMatter.setDay(event.getDay());
            itemDayMatter.setWeekDay(event.getWeekDay());
            itemDayMatter.setLastUpdateTime(event.getLastUpdateTime());
            dayMatterList.add(itemDayMatter);

            //
            if (event.isTop()){
                //如果顶部数据为空，则赋值，只执行一次
                if (topDayMatterItem == null){
                    topDayMatterItem = itemDayMatter;
                }else {
                    //如果顶部数据不为空
                    //如果当前事件更新时间比上一个事件数据的更新时间大，则置换顶部数据对象
                    if (itemDayMatter.getLastUpdateTime() > topDayMatterItem.getLastUpdateTime()){
                        topDayMatterItem = itemDayMatter;
                    }
                }
            }
        }

        //如果都不存在置顶数据，则取第一个为置顶数据
        if (topDayMatterItem == null && dayMatterList.size() > 0){
            topDayMatterItem = dayMatterList.get(0);
        }

        //如果没有数据，则设置空视图
        if (dayMatterList.size() == 0){
            mViewRef.get().setEmptyData();
        }else {
            mViewRef.get().setDayMatterList(dayMatterList);

            //以下是设置置顶数据
            //如果倒计时天数大于等于0， 表示还没到
            if (topDayMatterItem.getLeftDay() >= 0){
                //事件标题 ，距离xxx还有
                mViewRef.get().setTvTitle(context.getResources().getString(R.string.distance)
                        + topDayMatterItem.getTitle()
                        + context.getResources().getString(R.string.left));

                //剩余天数
                mViewRef.get().setTvLeftDay(topDayMatterItem.getLeftDay() + "");
            }else {
                //如果倒计时天数小于0， 表示已经过了 xxx已经
                mViewRef.get().setTvTitle(topDayMatterItem.getTitle() + context.getString(R.string.already));
                mViewRef.get().setTvLeftDay((-1 * topDayMatterItem.getLeftDay()) + "");
            }


            //目标日
            StringBuilder builder = new StringBuilder();
            builder.append(context.getString(R.string.target_date));
            builder.append(DateUtils.formatDate_Y_M_D_WEEK_New(context,
                    topDayMatterItem.getYear(),
                    //月份下标值 0-11
                    topDayMatterItem.getMonth()-1,
                    topDayMatterItem.getDay(),
                    topDayMatterItem.getWeekDay()));
            mViewRef.get().setTvTargetDate(builder.toString());
        }
    }
}
