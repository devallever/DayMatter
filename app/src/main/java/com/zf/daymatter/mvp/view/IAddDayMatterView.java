package com.zf.daymatter.mvp.view;

import android.app.DatePickerDialog;
import android.app.DialogFragment;

/**
 * Created by Allever on 18/5/21.
 */

public interface IAddDayMatterView {
    void showProgressDialog();
    void hideProgressDialog();

    void finishSelf();

    void setTvDate(String date);
    void setSort(String sort);
    void setTopSwitch(boolean value);
    void setTvRepeatType(String repeatType);
    void setEndDateSwitch(boolean value);
    void setTvEndDate(String endDate);

    void showDatePickDialog(DatePickerDialog datePickerDialog);
    void showEndDatePickDialog(DatePickerDialog datePickerDialog);

    void showRepeatTypeDialog();

    void setEndDateItemVisible();
    void setEndDateItemGone();
    void setEndDateSwitchVisible();
    void setEndDateSwitchGone();
}
