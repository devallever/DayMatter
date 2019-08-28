package com.allever.daymatter.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.allever.daymatter.R;
import com.allever.daymatter.bean.ItemDayMatter;
import com.allever.daymatter.event.EventDayMatter;
import com.allever.daymatter.mvp.BaseFragment;
import com.allever.daymatter.mvp.presenter.DayMatterDetailFgPresenter;
import com.allever.daymatter.mvp.view.IDayMatterDetailFgView;
import com.allever.daymatter.utils.Constants;
import com.allever.daymatter.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Allever on 18/5/22.
 */

public class DayMatterDetailFragment extends BaseFragment<IDayMatterDetailFgView, DayMatterDetailFgPresenter> implements IDayMatterDetailFgView {
    @BindView(R.id.id_fg_day_matter_detail_tv_title)
    TextView mTvTitle;
    @BindView(R.id.id_fg_day_matter_detail_fl_title_container)
    FrameLayout mFlTitleContainer;
    @BindView(R.id.id_fg_day_matter_detail_tv_left_day)
    TextView mTvLeftDay;
    @BindView(R.id.id_fg_day_matter_detail_tv_date)
    TextView mTvDate;

    Unbinder unbinder;

    private ItemDayMatter mItemDayMatter;

    public DayMatterDetailFragment() {}

    @SuppressLint("ValidFragment")
    public DayMatterDetailFragment(ItemDayMatter itemDayMatter) {
        mItemDayMatter = itemDayMatter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_day_matter_detail, container, false);

        EventBus.getDefault().register(this);

        unbinder = ButterKnife.bind(this, view);

        initView();

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

    private void initView(){
        int leftDay = mItemDayMatter.getLeftDay();
        if (leftDay >= 0){
            mTvTitle.setText(getActivity().getString(R.string.distance) + mItemDayMatter.getTitle() + getActivity().getString(R.string.has));
            mTvLeftDay.setText(leftDay + "");
            mFlTitleContainer.setBackgroundColor(getActivity().getResources().getColor(R.color.colorDefault));
        }else {
            mTvTitle.setText(mItemDayMatter.getTitle() + getActivity().getString(R.string.already));
            mTvLeftDay.setText((-1 * leftDay) + "");
            mFlTitleContainer.setBackgroundColor(getActivity().getResources().getColor(R.color.orange_500));
        }
        String date = DateUtils.formatDate_Y_M_D_WEEK_New(getActivity(),
                mItemDayMatter.getYear(),
                mItemDayMatter.getMonth()-1,
                mItemDayMatter.getDay(),
                mItemDayMatter.getWeekDay());
        mTvDate.setText(getActivity().getString(R.string.target_date) + date);
    }

    @Override
    protected DayMatterDetailFgPresenter createPresenter() {
        return new DayMatterDetailFgPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDayMatterReceive(EventDayMatter event){
        String eventType = event.getEvent();
        switch (eventType){
            case Constants.EVENT_MODIFY_DAY_MATTER:
                //刷新界面
                if (mItemDayMatter.getId() == event.getEventId()){
                    //mPresenter.g
                    mPresenter.getDayMatterData(getActivity(), mItemDayMatter.getId());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setTvTitle(String title) {
        mTvTitle.setText(title);
    }

    @Override
    public void setTvLeftDay(String leftDay) {
        mTvLeftDay.setText(leftDay);
    }

    @Override
    public void setTvDate(String date) {
        mTvDate.setText(date);
    }

    @Override
    public void setTitleBackgroundColor(int color) {
        mFlTitleContainer.setBackgroundColor(color);
    }
}
