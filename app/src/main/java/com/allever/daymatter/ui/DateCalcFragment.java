package com.allever.daymatter.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.zf.daymatter.R;
import com.allever.daymatter.mvp.BaseFragment;
import com.allever.daymatter.mvp.presenter.DateCalcPresenter;
import com.allever.daymatter.mvp.view.IDateCalcView;
import com.allever.daymatter.utils.DateUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Allever on 18/5/21.
 */

public class DateCalcFragment extends BaseFragment<IDateCalcView, DateCalcPresenter> implements IDateCalcView {

    //求几天前几天后的开始时间
    @BindView(R.id.id_fg_date_cal_tv_start_before_after_day)
    TextView mTvStartBeforeAfterDay;

    //几天后的输入框
    @BindView(R.id.id_fg_date_cal_et_day_after)
    EditText mEtDayAfter;

    //显示几天后的日期
    @BindView(R.id.id_fg_date_cal_tv_display_after)
    TextView mTvDisplayAfter;

    //几天前的输入框
    @BindView(R.id.id_fg_date_cal_et_day_before)
    EditText mEtDayBefore;

    //显示几天前的日期
    @BindView(R.id.id_fg_date_cal_tv_display_before)
    TextView mTvDisplayBefore;

    //求相隔天数的开始时间
    @BindView(R.id.id_fg_date_cal_tv_start_distance_day)
    TextView mTvStartDistanceDay;

    //显示间隔天数
    @BindView(R.id.id_fg_date_cal_tv_display_distance_count)
    TextView mTvDisplayDistanceCount;

    Unbinder unbinder;

    //几天前几天后开始时间选择器
    private DatePickerDialog mAfterBeforeStartDatePicker;

    //相隔天数开始时间选择器
    private DatePickerDialog mDistanceStartDatePicker;

    private Calendar mAfterBeforeCalendar;
    private int mAfterBeforeStartYear;
    private int mAfterBeforeStartMonth;
    private int mAfterBeforeStartDay;
    private int mAfterBeforeStartWeek;

    private Calendar mDistanceCalendar;
    private int mDistanceStartYear;
    private int mDistanceStartMonth;
    private int mDistanceStartDay;
    private int mDistanceStartWeek;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_calc, container, false);

        unbinder = ButterKnife.bind(this, view);

        initData();

        initView();

        initDialog();

        setListener();

        return view;
    }

    private void initData(){
        mAfterBeforeCalendar = Calendar.getInstance();
        mAfterBeforeStartYear = mAfterBeforeCalendar.get(Calendar.YEAR);
        mAfterBeforeStartMonth = mAfterBeforeCalendar.get(Calendar.MONTH);
        mAfterBeforeStartDay = mAfterBeforeCalendar.get(Calendar.DAY_OF_MONTH);
        mAfterBeforeStartWeek = mAfterBeforeCalendar.get(Calendar.DAY_OF_WEEK);

        mDistanceCalendar = Calendar.getInstance();
        mDistanceStartYear = mAfterBeforeStartYear;
        mDistanceStartMonth = mAfterBeforeStartMonth;
        mDistanceStartDay = mAfterBeforeStartDay;
        mDistanceStartWeek = mAfterBeforeStartWeek;

    }

    private void initView(){
        String displayTvAfterBeforeStart = DateUtils.formatDate_Y_M_D_WEEK_New(getActivity(),
                mAfterBeforeStartYear,
                mAfterBeforeStartMonth,
                mAfterBeforeStartDay,
                mAfterBeforeStartWeek);
        mTvStartBeforeAfterDay.setText(displayTvAfterBeforeStart);

        String displayTvDistanceStart = DateUtils.formatDate_Y_M_D_WEEK_New(getActivity(),
                mDistanceStartYear,
                mDistanceStartMonth,
                mDistanceStartDay,
                mDistanceStartWeek);
        mTvStartDistanceDay.setText(displayTvDistanceStart);
    }

    private void initDialog(){
        //几天前几天后的日历选择器
        mAfterBeforeStartDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //1.刷新界面
                //重新赋值
                mAfterBeforeCalendar.set(year,month,dayOfMonth);
                mAfterBeforeStartYear = year;
                mAfterBeforeStartMonth = month;
                mAfterBeforeStartDay = dayOfMonth;

                int week = mAfterBeforeCalendar.get(Calendar.DAY_OF_WEEK);

                String display = DateUtils.formatDate_Y_M_D_WEEK_New(getActivity(),year,month,dayOfMonth,week);
                mTvStartBeforeAfterDay.setText(display);

                //2.重新计算几天后
                String intervalAfterText = mEtDayAfter.getText().toString();
                //如果输入框内容不为空，则计算几天后的日期
                if (!TextUtils.isEmpty(intervalAfterText)){
                    int interval = Integer.parseInt(intervalAfterText);
                    mTvDisplayAfter.setText(DateUtils.calDayAfter(getActivity(), year,month+1, dayOfMonth, interval));
                }

                //3.重新计算几天前
                String intervalBeforeText = mEtDayBefore.getText().toString();
                //如果输入框内容不为空，则计算几天后的日期
                if (!TextUtils.isEmpty(intervalBeforeText)){
                    int interval = Integer.parseInt(intervalBeforeText);
                    mTvDisplayBefore.setText(DateUtils.calDayBefore(getActivity(), year,month+1, dayOfMonth, interval));
                }
            }
        },mAfterBeforeStartYear, mAfterBeforeStartMonth, mAfterBeforeStartDay);

        //计算间隔天数的日历选择器
        mDistanceStartDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //1.刷新界面
                mDistanceCalendar.set(year,month,dayOfMonth);
                mDistanceStartYear = year;
                mDistanceStartMonth = month;
                mDistanceStartDay = dayOfMonth;

                int week = mDistanceCalendar.get(Calendar.DAY_OF_WEEK);

                String display = DateUtils.formatDate_Y_M_D_WEEK(getActivity(),year,month+1,dayOfMonth,week);
                mTvStartDistanceDay.setText(display);

                //2.重新计算间隔天数
                mTvDisplayDistanceCount.setText(DateUtils.calDistanceDayCount(mDistanceStartYear,mDistanceStartMonth,mDistanceStartDay) + "");
            }
        }, mDistanceStartYear, mDistanceStartMonth, mDistanceStartDay);
    }

    private void setListener(){
        mTvStartBeforeAfterDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开日历选择器
                mAfterBeforeStartDatePicker.show();
            }
        });

        mEtDayAfter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //计算几天后
                String intervalText = mEtDayAfter.getText().toString();
                //如果输入框内容不为空，则计算几天后的日期
                if (!TextUtils.isEmpty(intervalText)){
                    int interval = Integer.parseInt(intervalText);
                    mTvDisplayAfter.setText(DateUtils.calDayAfter(getActivity(),
                            mAfterBeforeStartYear,
                            mAfterBeforeStartMonth+1,
                            mAfterBeforeStartDay,
                            interval));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mEtDayBefore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //todo 计算几天前
                String intervalText = mEtDayBefore.getText().toString();
                //如果输入框内容不为空，则计算几天后的日期
                if (!TextUtils.isEmpty(intervalText)){
                    int interval = Integer.parseInt(intervalText);
                    mTvDisplayBefore.setText(DateUtils.calDayBefore(getActivity(),
                            mAfterBeforeStartYear,
                            mAfterBeforeStartMonth+1,
                            mAfterBeforeStartDay,
                            interval));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        mTvStartDistanceDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开日历选择器
                mDistanceStartDatePicker.show();
            }
        });

    }

    @Override
    protected DateCalcPresenter createPresenter() {
        return new DateCalcPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
