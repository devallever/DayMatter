package com.zf.daymatter.mvp.presenter;

import android.content.Context;

import com.zf.daymatter.R;
import com.zf.daymatter.data.Event;
import com.zf.daymatter.mvp.BasePresenter;
import com.zf.daymatter.mvp.view.IDayMatterDetailFgView;
import com.zf.daymatter.utils.DateUtils;

/**
 * Created by Allever on 18/5/22.
 */

public class DayMatterDetailFgPresenter extends BasePresenter<IDayMatterDetailFgView> {

    public void getDayMatterData(Context context, int id) {

        Event event = mDataSource.getEvent(id);

        if (event == null){
            return;
        }

        IDayMatterDetailFgView iView = mViewRef.get();
        String title;
        int leftDay = DateUtils.calDistanceDayCount(event.getYear(), event.getMonth()-1, event.getDay());

        //如果剩余天数大于等于0， 表示还没到
        if (leftDay >= 0){
            title = context.getString(R.string.distance) + event.getTitle() + context.getString(R.string.has);
            iView.setTvTitle(title);
            iView.setTvLeftDay(leftDay + "");
            iView.setTitleBackgroundColor(context.getResources().getColor(R.color.colorDefault));
        }else {
            //如果剩余天数小于0， 表示已经过了
            title = event.getTitle() +context.getString(R.string.already);
            iView.setTvTitle(title);
            iView.setTvLeftDay((-1 * leftDay) + "");
            iView.setTitleBackgroundColor(context.getResources().getColor(R.color.orange_500));
        }
        String date = DateUtils.formatDate_Y_M_D_WEEK_New(context,
                event.getYear(),
                event.getMonth()-1,
                event.getDay(),
                event.getWeekDay());
        iView.setTvDate(context.getString(R.string.target_date) + date);
    }
}
