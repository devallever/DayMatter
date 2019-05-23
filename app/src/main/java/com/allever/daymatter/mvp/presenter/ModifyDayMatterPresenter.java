package com.allever.daymatter.mvp.presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.DatePicker;

import com.allever.daymatter.data.Event;
import com.allever.daymatter.event.EventDayMatter;
import com.allever.daymatter.utils.Constants;
import com.allever.daymatter.utils.DateUtils;
import com.allever.daymatter.R;
import com.allever.daymatter.mvp.BasePresenter;
import com.allever.daymatter.mvp.view.IModifyDayMatterView;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by Allever on 18/5/22.
 */

public class ModifyDayMatterPresenter extends BasePresenter<IModifyDayMatterView> {

    private static final String TAG = "ModifyDayMatterPresente";

    private Event mEvent;

    private Calendar mCalendar;

    public ModifyDayMatterPresenter(){
        mCalendar = Calendar.getInstance();
    }

    public void openDatePicker(final Context context) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //保存返回的数据
                mEvent.setYear(year);
                mEvent.setMonth(month + 1);
                mEvent.setDay(dayOfMonth);
                mCalendar.set(year,month,dayOfMonth);
                mEvent.setWeekDay(mCalendar.get(Calendar.DAY_OF_WEEK));
                String displayDate = DateUtils.formatDate_Y_M_D_WEEK_New(context,
                        mEvent.getYear(),
                        mEvent.getMonth()-1,
                        mEvent.getDay(),
                        mEvent.getWeekDay());
                mViewRef.get().setTvDate(displayDate);
            }
        }, mEvent.getYear(), mEvent.getMonth()-1, mEvent.getDay());
        mViewRef.get().showDatePickDialog(datePickerDialog);
    }

    public void openEndDatePicker(final Context context) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //保存返回的数据
                mEvent.setEndYear(year);
                mEvent.setEndMonth(month + 1);
                mEvent.setEndDay(dayOfMonth);
                mCalendar.set(year,month,dayOfMonth);
                mEvent.setEndWeekday(mCalendar.get(Calendar.DAY_OF_WEEK));

                String displayDate = DateUtils.formatDate_Y_M_D_WEEK(context,
                        mEvent.getEndYear(),
                        mEvent.getEndMonth() - 1,
                        mEvent.getEndDay(),
                        mEvent.getEndWeekday());
                mViewRef.get().setTvEndDate(displayDate);

            }
        }, mEvent.getEndYear(), mEvent.getEndMonth() - 1, mEvent.getEndDay());
        mViewRef.get().showEndDatePickDialog(datePickerDialog);
    }

    public void updateEvent(int eventId, String eventTitle) {
        mViewRef.get().showProgressDialog();
        mDataSource.updateEvent(mEvent);

        mViewRef.get().finishSelf();

        //通知外界刷新界面
        EventDayMatter eventDayMatter = new EventDayMatter();
        eventDayMatter.setEvent(Constants.EVENT_MODIFY_DAY_MATTER);
        eventDayMatter.setEventId(eventId);
        eventDayMatter.setSortId(mEvent.getSortId());
        EventBus.getDefault().post(eventDayMatter);
    }


    public void setmSortId(int mSortId) {
        this.mEvent.setSortId(mSortId);
    }

    public void setmTop(boolean mTop) {
        mEvent.setTop(mTop);
    }

    public void setmRepeatType(int mRepeatType) {
        mEvent.setRepeatType(mRepeatType);
    }

    public void setmEndDateSwitch(boolean mEndDateSwitch) {
        mEvent.setEndSwitch(mEndDateSwitch);
        //如果开启结束时间开关,则显示结束时间选项
        if (mEndDateSwitch){
            mViewRef.get().setEndDateItemVisible();
        }else {
            mViewRef.get().setEndDateItemGone();
        }
    }

    public void getEventData(Context context, int eventId) {
        Event event = mDataSource.getEvent(eventId);
        mEvent = event;

        //设置界面
        IModifyDayMatterView mView = getView();
        mView.setEtTetle(event.getTitle());

        mView.setTvDate(DateUtils.formatDate_Y_M_D_WEEK_New(context,
                event.getYear(),
                event.getMonth()-1,
                event.getDay(),
                event.getWeekDay()));

        String sortName = mDataSource.getSortName(event.getSortId());
        if (!TextUtils.isEmpty(sortName)){
            mView.setSort(sortName);
        }else {
            mView.setSort(context.getResources().getString(R.string.sort_life));
        }

        mView.setTopSwitch(event.isTop());

        switch (event.getRepeatType()){
            case Constants.REPEAT_TYPE_NO_REPEAT:
                mView.setTvRepeatType(context.getString(R.string.no_repeat));
                //设置结束时间可见
                mView.setEndDateSwitchVisible();
                if (event.isEndSwitch()){
                    mView.setEndDateItemVisible();
                }else {
                    mView.setEndDateItemGone();
                }
                break;
            case Constants.REPEAT_TYPE_PER_WEEK:
                mView.setTvRepeatType(context.getString(R.string.per_week_repeat));
                mView.setEndDateItemGone();
                mView.setEndDateSwitchGone();
                break;
            case Constants.REPEAT_TYPE_PER_MONTH:
                mView.setTvRepeatType(context.getString(R.string.per_month_repeat));
                mView.setEndDateItemGone();
                mView.setEndDateSwitchGone();
                break;
            case Constants.REPEAT_TYPE_PER_YEAR:
                mView.setTvRepeatType(context.getString(R.string.per_year_repeat));
                mView.setEndDateItemGone();
                mView.setEndDateSwitchGone();
                break;
            default:
                break;
        }


        mView.setEndDateSwitch(event.isEndSwitch());

        mView.setTvEndDate(DateUtils.formatDate_Y_M_D_WEEK_New(context,
                event.getEndYear(),
                event.getEndMonth()- 1,
                event.getEndDay(),
                event.getEndWeekday()));
    }

    public void deleteDayMatter(int eventId) {
        //从数据库中删除
        mDataSource.deleteEvent(eventId);

        EventDayMatter eventDayMatter = new EventDayMatter();
        eventDayMatter.setSortId(mEvent.getSortId());
        eventDayMatter.setEvent(Constants.EVENT_DELETE_DAY_MATTER);

        //刷新界面
        EventBus.getDefault().post(eventDayMatter);

        //1.结束自己
        mViewRef.get().finishSelf();
    }
}
