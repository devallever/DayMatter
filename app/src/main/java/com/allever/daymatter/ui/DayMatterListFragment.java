package com.allever.daymatter.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.umeng.analytics.MobclickAgent;
import com.allever.daymatter.R;
import com.allever.daymatter.adapter.DayMatterListAdapter;
import com.allever.daymatter.bean.ItemDayMatter;
import com.allever.daymatter.event.EventDayMatter;
import com.allever.daymatter.mvp.BaseFragment;
import com.allever.daymatter.mvp.presenter.DayMatterListPresenter;
import com.allever.daymatter.mvp.view.IDayMatterListView;
import com.allever.daymatter.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Allever on 18/5/21.
 */

public class DayMatterListFragment extends BaseFragment<IDayMatterListView, DayMatterListPresenter> implements IDayMatterListView {

    private static final String TAG = "DayMatterListFragment";

    @BindView(R.id.id_fg_day_matter_list_tv_title)
    TextView mTvTitle;
    @BindView(R.id.id_fg_day_matter_list_tv_date)
    TextView mTvDate;
    @BindView(R.id.d_fg_day_matter_list_tv_left_day)
    TextView mTvLeftDay;
    @BindView(R.id.id_fg_day_matter_list_rv)
    RecyclerView mRv;
    @BindView(R.id.id_main_cv_no_data)
    CardView mCvNoData;
    @BindView(R.id.id_fg_day_matter_list_ll_list_container)
    LinearLayout mLlListContainer;

    private Unbinder unbinder;

    private DayMatterListAdapter mAdapter;

    private ArrayList<ItemDayMatter> mData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        EventBus.getDefault().register(this);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_day_matter_list, container, false);

        unbinder = ButterKnife.bind(this, view);

        //初始化控件并设置监听
        initView();

        //获取全部事件列表数据
        mPresenter.getDayMatterData(getActivity());

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

    private void initView() {
        mAdapter = new DayMatterListAdapter(getActivity(), mData);
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRv.setAdapter(mAdapter);

        //设置列表监听器
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转事件详细信息界面
                DayMatterDetailActivity.startSelf(getActivity(), mData, position);
            }
        });
    }

    @Override
    protected DayMatterListPresenter createPresenter() {
        return new DayMatterListPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.id_main_cv_no_data)
    public void onViewClicked() {
        EditDayMatterActivity.startSelf(mActivity, false, -1);
    }

    @Override
    public void setDayMatterList(List<ItemDayMatter> dayMatterList) {

        //如果数据为空时，则不做处理，默认显示一个空数据时的界面
        if (dayMatterList == null){
            return;
        }

        //如果有数据，则隐藏空视图，显示列表视图
        mCvNoData.setVisibility(View.GONE);
        mLlListContainer.setVisibility(View.VISIBLE);

        //设置列表数据
        mData.clear();
        mData.addAll(dayMatterList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setEmptyData() {
        mCvNoData.setVisibility(View.VISIBLE);
        mLlListContainer.setVisibility(View.GONE);
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
    public void setTvTargetDate(String tvTargetDate) {
        mTvDate.setText(tvTargetDate);
    }


    /**
     * 响应倒计时事件操作
     * 添加、删除、修改事件都需要刷新列表
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDayMatterOptionReceive(EventDayMatter event){
        if (event == null){
            return;
        }

        String eventType = event.getEvent();
        int sortId = event.getSortId();
        Log.d(TAG, "onRefreshDayMatterData: sort id = " + sortId);

        switch (eventType){
            //从主界面点击显示类型时接收到的消息事件
            case Constants.EVENT_SELECT_DISPLAY_SORT_LIST:
                //如果分类id为0， 则用户点击了全部,获取所有数据
                if (sortId == 0){
                    mPresenter.getDayMatterData(getActivity());
                }else {
                    mPresenter.getDayMatterData(getActivity(), sortId);
                }
                break;

            //新增倒计时事件接收到的消息事件
            case Constants.EVENT_ADD_DAY_MATTER:
                mPresenter.getDayMatterData(getActivity(), sortId);
                break;

            //修改
            case Constants.EVENT_MODIFY_DAY_MATTER:
                mPresenter.getDayMatterData(getActivity(), sortId);
                break;

            //删除
            case Constants.EVENT_DELETE_DAY_MATTER:
                mPresenter.getDayMatterData(getActivity(), sortId);
                break;

            default:
                break;
        }
    }
}
