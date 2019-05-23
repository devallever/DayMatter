package com.allever.daymatter.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.allever.daymatter.R;
import com.allever.daymatter.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Allever on 18/5/28.
 */

public class RepeatTypeDialog extends DialogFragment {

    private static OptionListener mOptionListener;

    @BindView(R.id.id_dialog_repeat_type_rb_no_repeat)
    RadioButton mRbNoRepeat;
    @BindView(R.id.id_dialog_repeat_type_rb_per_week)
    RadioButton mRbPerWeek;
    @BindView(R.id.id_dialog_repeat_type_rb_per_month)
    RadioButton mRbPerMonth;
    @BindView(R.id.id_dialog_repeat_type_rb_per_year)
    RadioButton mRbPerYear;
    @BindView(R.id.id_dialog_repeat_type_tv_cancel)
    TextView mTvCancel;

    Unbinder unbinder;

    public static RepeatTypeDialog newInsance(OptionListener optionListener) {
        RepeatTypeDialog repeatTypeDialog = new RepeatTypeDialog();
        repeatTypeDialog.setCancelable(true);
        mOptionListener = optionListener;
        return repeatTypeDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_repeat_type, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.id_dialog_repeat_type_rb_no_repeat,
            R.id.id_dialog_repeat_type_rb_per_week,
            R.id.id_dialog_repeat_type_rb_per_month,
            R.id.id_dialog_repeat_type_rb_per_year,
            R.id.id_dialog_repeat_type_tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_dialog_repeat_type_rb_no_repeat:
                if (mOptionListener != null) {
                    mOptionListener.onItemClick(this, Constants.REPEAT_TYPE_NO_REPEAT);
                }
                break;
            case R.id.id_dialog_repeat_type_rb_per_week:
                if (mOptionListener != null) {
                    mOptionListener.onItemClick(this, Constants.REPEAT_TYPE_PER_WEEK);
                }
                break;
            case R.id.id_dialog_repeat_type_rb_per_month:
                if (mOptionListener != null) {
                    mOptionListener.onItemClick(this, Constants.REPEAT_TYPE_PER_MONTH);
                }
                break;
            case R.id.id_dialog_repeat_type_rb_per_year:
                if (mOptionListener != null) {
                    mOptionListener.onItemClick(this, Constants.REPEAT_TYPE_PER_YEAR);
                }
                break;
            case R.id.id_dialog_repeat_type_tv_cancel:
                if (mOptionListener != null){
                    mOptionListener.onCancel(this);
                }
                break;
            default:
                break;
        }
    }

    public interface OptionListener {
        void onItemClick(DialogFragment dialog, int repeatType);

        void onCancel(DialogFragment dialog);
    }

}
