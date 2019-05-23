package com.allever.daymatter.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.allever.daymatter.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;
import com.allever.daymatter.R;
import com.allever.daymatter.data.Config;
import com.allever.daymatter.mvp.BaseFragment;
import com.allever.daymatter.mvp.presenter.SettingPresenter;
import com.allever.daymatter.mvp.view.ISettingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Allever on 18/6/1.
 */

public class SettingFragment extends BaseFragment<ISettingView, SettingPresenter> implements ISettingView {

    private static final String TAG = "SettingFragment";

    Unbinder unbinder;

    //当天提醒开关
    @BindView(R.id.id_fg_remind_switch_current_day)
    SwitchCompat mSwitchCurrentDay;

    //当天提醒项-主要用于设置监听
    @BindView(R.id.id_fg_remind_rl_current_day_switch_container)
    RelativeLayout mRlCurrentDaySwitchContainer;

    //当天提醒时间
    @BindView(R.id.id_fg_remind_tv_current_day_remind_time)
    TextView mTvCurrentDayRemindTime;

    //当天提醒时间项-主要用于设置监听
    @BindView(R.id.id_fg_remind_rl_current_day_remind_time_container)
    RelativeLayout mRlCurrentDayRemindTimeContainer;

    //前天提醒开关
    @BindView(R.id.id_fg_remind_switch_before_day)
    SwitchCompat mSwitchBeforeDay;

    //前天提醒开关选项-主要用于设置监听
    @BindView(R.id.id_fg_remind_rl_before_day_switch_container)
    RelativeLayout mRlBeforeDaySwitchContainer;

    //前天提醒时间
    @BindView(R.id.id_fg_remind_tv_before_day_remind_time)
    TextView mTvBeforeDayRemindTime;

    //前天提醒时间选项-主要用于设置监听
    @BindView(R.id.id_fg_remind_rl_before_day_remind_time_container)
    RelativeLayout mRlBeforeDayRemindTimeContainer;

    @BindView(R.id.id_fg_remind_rl_before_day_remind_about_container)
    ViewGroup mAboutContainer;
    @BindView(R.id.id_fg_remind_rl_before_day_remind_feedback_container)
    ViewGroup mFeedbackContainer;


    //当天提醒时间选择器
    private TimePickerDialog mCurrentTimePicker;

    //前天提醒时间选择器
    private TimePickerDialog mBeforeTimePicker;

    private Config mConfig;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_remind, container, false);

        unbinder = ButterKnife.bind(this, view);

        //获取提醒配置
        mPresenter.getRemindConfig();

        //设置当前提醒
        mPresenter.setCurrentDayRemind(getActivity());

        //设置前天提醒
        mPresenter.setBeforeDayRemind(getActivity());

        setListener();

        initDialog();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    private void initDialog() {
        if (mConfig == null) {
            return;
        }
        mCurrentTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //保存到数据库
                mPresenter.updateCurrentRemindTime(hourOfDay, minute);
                //
                setCurrentRemindTime(TimeUtils.INSTANCE.formatTime(hourOfDay, minute));

                //更新提醒
                mPresenter.setCurrentDayRemind(getActivity());

            }
        }, mConfig.getCurrentRemindHour(), mConfig.getCurrentRemindMin(), true);

        mBeforeTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //保存到数据库
                mPresenter.updateBeforeRemindTime(hourOfDay, minute);
                //
                setBeforeRemindTime(TimeUtils.INSTANCE.formatTime(hourOfDay, minute));

                //更新提醒
                mPresenter.setBeforeDayRemind(getActivity());
            }
        }, mConfig.getBeforeRemindHour(), mConfig.getBeforeRemindMin(), true);
    }

    private void setListener() {
        mSwitchCurrentDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: current switch");
                //保存到数据库
                mPresenter.updateCurrentDaySwitch(isChecked);

                //更新提醒
                mPresenter.setCurrentDayRemind(getActivity());
            }
        });

        mSwitchBeforeDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: before");
                //保存到数据库
                mPresenter.updateBeforeDaySwitch(isChecked);

                //更新提醒
                mPresenter.setBeforeDayRemind(getActivity());
            }
        });
    }

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setCurrentRemindSwitch(boolean value) {
        mSwitchCurrentDay.setChecked(value);
    }

    @Override
    public void setCurrentRemindTime(String time) {
        mTvCurrentDayRemindTime.setText(time);
    }

    @Override
    public void setBeforeRemindSwitch(boolean value) {
        mSwitchBeforeDay.setChecked(value);
    }

    @Override
    public void setBeforeRemindTime(String time) {
        mTvBeforeDayRemindTime.setText(time);
    }

    @Override
    public void returnRemindConfig(Config config) {
        if (config != null){
            mConfig = config;
        }
    }

    @OnClick({R.id.id_fg_remind_rl_current_day_switch_container,
            R.id.id_fg_remind_rl_current_day_remind_time_container,
            R.id.id_fg_remind_rl_before_day_switch_container,
            R.id.id_fg_remind_rl_before_day_remind_time_container,
            R.id.id_fg_remind_rl_before_day_remind_about_container,
            R.id.id_fg_remind_rl_before_day_remind_feedback_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //当天提醒
            case R.id.id_fg_remind_rl_current_day_switch_container:
                mSwitchCurrentDay.setChecked(!mSwitchCurrentDay.isChecked());
                break;
            //当天提醒时间
            case R.id.id_fg_remind_rl_current_day_remind_time_container:
                if (mCurrentTimePicker != null){
                    mCurrentTimePicker.show();
                }
                break;
            //前天提醒
            case R.id.id_fg_remind_rl_before_day_switch_container:
                mSwitchBeforeDay.setChecked(!mSwitchBeforeDay.isChecked());
                break;
            //前天提醒时间
            case R.id.id_fg_remind_rl_before_day_remind_time_container:
                if (mBeforeTimePicker != null){
                    mBeforeTimePicker.show();
                }
                break;
            case R.id.id_fg_remind_rl_before_day_remind_about_container:
                AboutActivity.Companion.actionStart(getActivity());
                break;
            case R.id.id_fg_remind_rl_before_day_remind_feedback_container:
                mPresenter.feedback(getActivity());
                break;
            default:
                break;
        }
    }
}
