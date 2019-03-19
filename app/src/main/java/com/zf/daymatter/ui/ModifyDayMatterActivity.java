package com.zf.daymatter.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zf.daymatter.R;
import com.zf.daymatter.dialog.RepeatTypeDialog;
import com.zf.daymatter.dialog.SortDialog;
import com.zf.daymatter.mvp.BaseActivity;
import com.zf.daymatter.mvp.presenter.ModifyDayMatterPresenter;
import com.zf.daymatter.mvp.view.IModifyDayMatterView;
import com.zf.daymatter.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Allever on 18/5/22.
 */

public class ModifyDayMatterActivity extends BaseActivity<IModifyDayMatterView, ModifyDayMatterPresenter> implements IModifyDayMatterView {

    private static final String TAG = "ModifyDayMatterActivity";

    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";

    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.id_modify_day_matter_btn_delete)
    Button mBtnDelete;
    @BindView(R.id.id_modify_day_matter_btn_save)
    Button mBtnSave;
    @BindView(R.id.id_input_et_title)
    EditText mEtEventTitle;
    @BindView(R.id.id_input_tv_date)
    TextView mTvDate;
    @BindView(R.id.id_input_tv_sort)
    TextView mTvSort;
    @BindView(R.id.id_input_tv_sort_selector)
    TextView mTvSortSelector;
    @BindView(R.id.id_input_tv_is_top)
    TextView mTvIsTop;
    @BindView(R.id.id_input_switch_is_top)
    SwitchCompat mSwitchIsTop;
    @BindView(R.id.id_input_tv_repeat)
    TextView mTvRepeat;
    @BindView(R.id.id_input_tv_repeat_selector)
    TextView mTvRepeatSelector;
    @BindView(R.id.id_input_tv_is_end_date)
    TextView mTvIsEndDate;
    @BindView(R.id.id_input_switch_end_date)
    SwitchCompat mSwitchEndDate;
    @BindView(R.id.id_input_tv_end_date)
    TextView mTvEndDate;
    @BindView(R.id.id_modify_day_matter_ll_root)
    LinearLayout mLlRoot;
    @BindView(R.id.id_input_rl_end_date_switch_container)
    RelativeLayout mRlEndDateSwitchContainer;
    @BindView(R.id.id_input_rl_end_date_container)
    RelativeLayout mRlEndDateContainer;

    private RepeatTypeDialog mRepeatDialog;
    private SortDialog mSortDialog;
    private ProgressDialog mProgressDialog;


    private int mEventId;
    private String mEventTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_day_matter);

        ButterKnife.bind(this);

        mEventId = getIntent().getIntExtra(EXTRA_EVENT_ID,0);
        if (mEventId == 0){
            return;
        }

        initToolbar(mToolbar, R.string.modify_day_matter);

        initDialog();

        setListener();

        mPresenter.getEventData(this, mEventId);
    }

    private void setListener() {
        mSwitchIsTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.setmTop(isChecked);
            }
        });

        mSwitchEndDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.setmEndDateSwitch(isChecked);
            }
        });
    }

    private void initDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.saving));

        /**
         * 重复类型对话框
         * 当选择不重复，结束时间开关项可见
         * 当选择其他重复类型，结束时间开关可见
         */
        mRepeatDialog = RepeatTypeDialog.newInsance(new RepeatTypeDialog.OptionListener() {
            @Override
            public void onItemClick(DialogFragment dialog, int repeatType) {
                //当修改了重复类型后，保存修改的值
                mPresenter.setmRepeatType(repeatType);

                dialog.dismiss();
                Log.d(TAG, "onItemClick: repeat" + repeatType);
                switch (repeatType) {
                    case Constants.REPEAT_TYPE_NO_REPEAT:
                        setTvRepeatType(getString(R.string.no_repeat));

                        //结束日期可见
                        setEndDateSwitchVisible();
                        mPresenter.setmEndDateSwitch(mSwitchEndDate.isChecked());
                        break;
                    case Constants.REPEAT_TYPE_PER_WEEK:
                        setTvRepeatType(getString(R.string.per_week_repeat));

                        //结束日期可见
                        setEndDateSwitchGone();
                        setEndDateItemGone();
                        break;
                    case Constants.REPEAT_TYPE_PER_MONTH:
                        setTvRepeatType(getString(R.string.per_month_repeat));

                        //结束日期可见
                        setEndDateSwitchGone();
                        setEndDateItemGone();
                        break;
                    case Constants.REPEAT_TYPE_PER_YEAR:
                        setTvRepeatType(getString(R.string.per_year_repeat));

                        //结束日期可见
                        setEndDateSwitchGone();
                        setEndDateItemGone();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onCancel(DialogFragment dialog) {
                dialog.dismiss();
            }
        });

        mSortDialog = SortDialog.newInsance(new SortDialog.OptionListener() {
            @Override
            public void onItemClick(DialogFragment dialog, String sortName, int sortId) {
                //当修改了事件类型后，保存修改的值
                mPresenter.setmSortId(sortId);
                setSort(sortName);
                dialog.dismiss();
            }

            @Override
            public void onCancel(DialogFragment dialog) {
                dialog.dismiss();
            }
        });


    }

    @Override
    protected ModifyDayMatterPresenter createPresenter() {
        return new ModifyDayMatterPresenter();
    }

    @OnClick({R.id.id_modify_day_matter_btn_delete,
            R.id.id_modify_day_matter_btn_save,
            R.id.id_input_tv_date,
            R.id.id_input_tv_sort,
            R.id.id_input_tv_is_top,
            R.id.id_input_tv_repeat,
            R.id.id_input_tv_is_end_date,
            R.id.id_input_tv_end_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            //日期
            case R.id.id_input_tv_date:
                //弹出日历选择器
                mPresenter.openDatePicker(this);
                break;

            //分类
            case R.id.id_input_tv_sort:
                //打开选择分类界面
                mSortDialog.show(getSupportFragmentManager(),TAG);
                break;

            //置顶项
            case R.id.id_input_tv_is_top:
                mSwitchIsTop.setChecked(!mSwitchIsTop.isChecked());
                break;

            //重复
            case R.id.id_input_tv_repeat:
                //打开重复类型对话框
                showRepeatTypeDialog();
                break;

            //结束时间项
            case R.id.id_input_tv_is_end_date:
                mSwitchEndDate.setChecked(!mSwitchEndDate.isChecked());
                break;

            //选择结束时间
            case R.id.id_input_tv_end_date:
                //如果结束开关为打开状态,则打开日历选择器
                if (mSwitchEndDate.isChecked()) {
                    mPresenter.openEndDatePicker(this);
                } else {
                    showToast(getResources().getString(R.string.please_open_end_date_switch));
                }
                break;

            case R.id.id_modify_day_matter_btn_delete:
                mPresenter.deleteDayMatter(mEventId);
                break;
            case R.id.id_modify_day_matter_btn_save:
                mEventTitle = mEtEventTitle.getText().toString();
                //如果标题不为空，则保存事件
                if (!TextUtils.isEmpty(mEventTitle)) {
                    //打开progressDialog
                    mPresenter.updateEvent(mEventId, mEventTitle);

                } else {
                    showToast(getResources().getString(R.string.please_input_event_title));
                }
                break;
            default:
                break;
        }
    }

    public static void startSelf(Context context, int eventId){
        Intent intent = new Intent(context, ModifyDayMatterActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        context.startActivity(intent);
    }

    @Override
    public void showProgressDialog() {
        //mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        //mProgressDialog.dismiss();
    }

    @Override
    public void finishSelf() {
        finish();
    }

    @Override
    public void setTvDate(String date) {
        mTvDate.setText(date);
    }

    @Override
    public void setSort(String sort) {
        mTvSortSelector.setText(sort);
    }

    @Override
    public void setTopSwitch(boolean value) {
        mSwitchIsTop.setChecked(value);
    }

    @Override
    public void setTvRepeatType(String repeatType) {
        mTvRepeatSelector.setText(repeatType);
    }

    @Override
    public void setEndDateSwitch(boolean value) {
        mSwitchEndDate.setChecked(value);
    }

    @Override
    public void setTvEndDate(String endDate) {
        mTvEndDate.setText(endDate);
    }

    @Override
    public void showDatePickDialog(DatePickerDialog datePickerDialog) {
        datePickerDialog.show();
    }

    @Override
    public void showEndDatePickDialog(DatePickerDialog datePickerDialog) {
        datePickerDialog.show();
    }

    @Override
    public void showRepeatTypeDialog() {
        mRepeatDialog.show(getSupportFragmentManager(), TAG);
    }

    @Override
    public void setEndDateItemVisible() {
        mRlEndDateContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEndDateItemGone() {
        mRlEndDateContainer.setVisibility(View.GONE);
    }

    @Override
    public void setEndDateSwitchVisible() {
        mRlEndDateSwitchContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEndDateSwitchGone() {
        mRlEndDateSwitchContainer.setVisibility(View.GONE);
    }

    @Override
    public void setEtTetle(String title) {
        mEtEventTitle.setText(title);
    }
}
