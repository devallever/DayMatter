package com.allever.daymatter.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.allever.daymatter.data.Config;
import com.allever.daymatter.receiver.BeforeAlarmReceiver;
import com.allever.daymatter.receiver.CurrentAlarmReceiver;
import com.allever.daymatter.utils.CommonHalper;
import com.allever.daymatter.utils.RemindUtils;
import com.allever.daymatter.mvp.BasePresenter;
import com.allever.daymatter.mvp.view.ISettingView;
import com.allever.daymatter.utils.TimeUtils;

import java.util.Calendar;

/**
 * Created by Allever on 18/6/1.
 */

public class SettingPresenter extends BasePresenter<ISettingView> {

    private static final String TAG = "SettingPresenter";

    private Calendar mCalendar = Calendar.getInstance();

    public void getRemindConfig() {
        Config config = mDataSource.getRemindConfigData();
        if (config == null){
            return;
        }

        boolean currentSwitch;
        Log.d(TAG, "getRemindConfig: currentRemind = " + config.getCurrentDayRemind());

        //如果当天提醒配置为0， 则当天开关为false
        if (config.getCurrentDayRemind() == 0){
            currentSwitch = false;
        }else {
            currentSwitch = true;
        }
        Log.d(TAG, "getRemindConfig: currentSwitch = " + currentSwitch );
        mViewRef.get().setCurrentRemindSwitch(currentSwitch);

        //如果前天提醒配置为0，则前天开关为false
        boolean beforeSwitch;
        if (config.getBeforeDayRemind() == 0){
            beforeSwitch = false;
        }else {
            beforeSwitch = true;
        }
        mViewRef.get().setBeforeRemindSwitch(beforeSwitch);

        mViewRef.get().setCurrentRemindTime(TimeUtils.INSTANCE.formatTime(config.getCurrentRemindHour(), config.getCurrentRemindMin()));

        mViewRef.get().setBeforeRemindTime(TimeUtils.INSTANCE.formatTime(config.getBeforeRemindHour(), config.getBeforeRemindMin()));

        mViewRef.get().returnRemindConfig(config);

    }

    public void updateCurrentRemindTime(int hourOfDay, int minute) {
        mDataSource.updateCurrentRemindTime(hourOfDay, minute);
    }

    public void updateBeforeRemindTime(int hourOfDay, int minute) {
        mDataSource.updateBeforeRemindTiem(hourOfDay, minute);
    }

    public void updateCurrentDaySwitch(boolean isChecked) {
        Log.d(TAG, "updateCurrentDaySwitch: " + isChecked);
        mDataSource.updateCurrentRemindSwitch(isChecked);
    }

    public void updateBeforeDaySwitch(boolean isChecked) {
        Log.d(TAG, "updateBeforeDaySwitch: " + isChecked);
        mDataSource.updateBeforeRemindSwitch(isChecked);
    }

    public void setCurrentDayRemind(Context context) {
        Config config = mDataSource.getRemindConfigData();
        if (config == null){
            return;
        }

        //检查配置如果是 1， 则设置提醒，先取消原有提醒
        if (config.getCurrentDayRemind() == 1) {
            //取消原有提醒
            RemindUtils.stopRemind(context, RemindUtils.REQUEST_CODE_CURRENT_REMIND, CurrentAlarmReceiver.class);
            //设置提醒
            RemindUtils.setRemind(context,
                    config.getCurrentRemindHour(),
                    config.getCurrentRemindMin(),
                    RemindUtils.REQUEST_CODE_CURRENT_REMIND,
                    CurrentAlarmReceiver.class);
        }else {
            RemindUtils.stopRemind(context, RemindUtils.REQUEST_CODE_CURRENT_REMIND, CurrentAlarmReceiver.class);
        }

    }

    public void setBeforeDayRemind(Context context) {
        Config config = mDataSource.getRemindConfigData();
        if (config == null){
            return;
        }

        //检查配置如果是 1， 则设置提醒，先取消原有提醒
        if (config.getCurrentDayRemind() == 1) {
            //取消原有提醒
            RemindUtils.stopRemind(context, RemindUtils.REQUEST_CODE_BEFORE_REMIND, BeforeAlarmReceiver.class);
            //设置提醒
            RemindUtils.setRemind(context,
                    config.getBeforeRemindHour(),
                    config.getBeforeRemindMin(),
                    RemindUtils.REQUEST_CODE_BEFORE_REMIND,
                    BeforeAlarmReceiver.class);
        }else {
            RemindUtils.stopRemind(context, RemindUtils.REQUEST_CODE_BEFORE_REMIND, BeforeAlarmReceiver.class);
        }

    }

    public void feedback(Activity activity) {
        CommonHalper.INSTANCE.feedback(activity);
    }
}
