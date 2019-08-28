package com.allever.daymatter.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.umeng.analytics.MobclickAgent;
import com.allever.daymatter.R;
import com.allever.daymatter.adapter.ViewPagerAdapter;
import com.allever.daymatter.bean.ItemDayMatter;
import com.allever.daymatter.event.EventDayMatter;
import com.allever.daymatter.mvp.BaseActivity;
import com.allever.daymatter.mvp.presenter.DayMatterDetailPresenter;
import com.allever.daymatter.mvp.view.IDayMatterDetailView;
import com.allever.daymatter.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Allever on 18/5/22.
 */

public class DayMatterDetailActivity extends BaseActivity<IDayMatterDetailView, DayMatterDetailPresenter> implements IDayMatterDetailView {

    public static final String EXTRA_ITEM_DAY_MATTER_LIST = "EXTRA_ITEM_DAY_MATTER_LIST";
    public static final String EXTRA_PAGE_POSITION = "EXTRA_PAGE_POSITION";

    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.id_day_matter_detail_vp)
    ViewPager mVp;
    @BindView(R.id.id_day_matter_detail_btn_share)
    FloatingActionButton mBtnShare;

    private List<Fragment> mFragmentList;
    private ViewPagerAdapter mAdapter;

    private List<ItemDayMatter> mItemDayMatterList;
    private int mPagePosition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_matter_detail);

        EventBus.getDefault().register(this);

        mItemDayMatterList = getIntent().getParcelableArrayListExtra(EXTRA_ITEM_DAY_MATTER_LIST);
        if (mItemDayMatterList == null){
            mItemDayMatterList = new ArrayList<>();
        }

        mPagePosition = getIntent().getIntExtra(EXTRA_PAGE_POSITION, 0);

        ButterKnife.bind(this);

        initToolbar(mToolbar, R.string.app_name);

        initData();

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData(){
        mFragmentList = new ArrayList<>();
        for (ItemDayMatter itemDayMatter: mItemDayMatterList){
            mFragmentList.add(new DayMatterDetailFragment(itemDayMatter));
        }
    }

    private void initView(){
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this, mFragmentList);
        mVp.setAdapter(mAdapter);
        mVp.setCurrentItem(mPagePosition);

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    protected DayMatterDetailPresenter createPresenter() {
        return new DayMatterDetailPresenter();
    }

    @OnClick(R.id.id_day_matter_detail_btn_share)
    public void onViewClicked() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_day_matter_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.id_menu_day_matter_detail_edit:
                EditDayMatterActivity.startSelf(this, true,  mItemDayMatterList.get(mPagePosition).getId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void startSelf(Context context, ArrayList<ItemDayMatter> itemDayMatters, int pagePosition){
        Intent intent = new Intent(context, DayMatterDetailActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_ITEM_DAY_MATTER_LIST, itemDayMatters);
        intent.putExtra(EXTRA_PAGE_POSITION, pagePosition);
        context.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveDayMatterOption(EventDayMatter event){
        String eventType = event.getEvent();
        switch (eventType){
            case Constants.EVENT_DELETE_DAY_MATTER:
                finish();
                break;
            default:
                break;
        }
    }
}
