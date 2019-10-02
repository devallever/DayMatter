package com.allever.daymatter;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.allever.daymatter.event.Event;
import com.allever.daymatter.event.SortEvent;
import com.allever.daymatter.ui.DateCalcFragment;
import com.allever.daymatter.ui.DayMatterListFragment;
import com.allever.daymatter.ui.SettingFragment;
import com.allever.daymatter.ui.SortFragment;
import com.allever.daymatter.ui.TabModel;
import com.allever.daymatter.ui.adapter.ViewPagerAdapter;
import com.allever.daymatter.bean.ItemSlidMenuSort;
import com.allever.daymatter.event.EventDayMatter;
import com.allever.daymatter.mvp.BaseActivity;
import com.allever.daymatter.mvp.presenter.MainActivityPresenter;
import com.allever.daymatter.mvp.view.IMainActivityView;
import com.allever.daymatter.ui.widget.tab.TabLayout;
import com.allever.daymatter.utils.Constants;
import com.allever.daymatter.utils.DisplayUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends
        BaseActivity<IMainActivityView,
                MainActivityPresenter>
        implements IMainActivityView, TabLayout.OnTabSelectedListener {

    @BindView(R.id.id_main_vp)
    ViewPager mVp;
    @BindView(R.id.tab_layout)
    TabLayout mTab;
    @BindView(R.id.tv_label)
    TextView mTvTitle;


    private ViewPagerAdapter mViewPagerAdapter;

    private List<Fragment> mFragmentList;

    private int mainTabHighlight = 0;
    private int mainTabUnselectColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        ButterKnife.bind(this);

        findViewById(R.id.iv_back).setVisibility(View.GONE);
        mPresenter.updateTitle();

        //如果是第一次启动，则向数据库添加默认的数据
        mPresenter.initDefaultSortData(this);

        //初始化ViewPager数据
        initViewPagerData();

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected MainActivityPresenter createPresenter() {
        return new MainActivityPresenter();
    }

    private void initViewPagerData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new DayMatterListFragment());
        mFragmentList.add(new SortFragment());
        mFragmentList.add(new DateCalcFragment());
        mFragmentList.add(new SettingFragment());
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, mFragmentList);
    }

    private void initView() {
        mVp.setOffscreenPageLimit(3);
        mVp.setAdapter(mViewPagerAdapter);

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //页面切换时，更改Toolbar标题
                switch (position) {
                    case 0:
                        mPresenter.updateTitle();
                        break;
                    case 1:
                        mTvTitle.setText(getString(R.string.sort));
                        break;
                    case 2:
                        mTvTitle.setText(getString(R.string.calc));
                        break;
                    case 3:
                        mTvTitle.setText(getString(R.string.setting));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mainTabHighlight = getResources().getColor(R.color.main_tab_highlight);
        mainTabUnselectColor = getResources().getColor(R.color.main_tab_unselect_color);


        //tab
        mVp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTab));
        mTab.setOnTabSelectedListener(this);

        int tabCount = TabModel.INSTANCE.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            TabModel.Tab tabModel = TabModel.INSTANCE.getTab(i);
            int labelId = tabModel.getLabelResId();
            TabLayout.Tab tab = mTab.newTab()
                    .setTag(tabModel)
                    .setCustomView(getTabView(i))
                    .setContentDescription(labelId);
            Drawable drawable = tabModel.getDrawable();
            if (drawable != null) {
                tab.setIcon(drawable);
            } else {
                tab.setIcon(tabModel.getIconResId());
            }

            tab.setText(labelId);
            ImageView imageView = tab.getCustomView().findViewById(R.id.icon);
            imageView.setColorFilter(null);
            TextView textView = tab.getCustomView().findViewById(R.id.text1);
            textView.setTextColor(mTab.getTabTextColors());
            mTab.addTab(tab);
        }

        mTab.setSelectedTabIndicatorWidth(DisplayUtil.INSTANCE.dip2px(0));
        mTab.setSelectedTabIndicatorHeight(DisplayUtil.INSTANCE.dip2px(0));
        mTab.setSelectedTabIndicatorColor(mainTabHighlight);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mVp.setCurrentItem(tab.getPosition());

        TabModel.INSTANCE.setSelectedTab((TabModel.Tab) tab.getTag());
        for (int i = 0; i < mTab.getTabCount(); i++) {
            TabLayout.Tab aTab = mTab.getTabAt(i);
            if (aTab != null) {
                ImageView imageView = aTab.getCustomView().findViewById(R.id.icon);
                TextView textView = aTab.getCustomView().findViewById(R.id.text1);
                if (aTab == tab) {
                    imageView.setColorFilter(mainTabHighlight, PorterDuff.Mode.SRC_IN);
                    textView.setTextColor(mainTabHighlight);
                } else {
                    imageView.setColorFilter(null);
                    textView.setTextColor(mainTabUnselectColor);
                }
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void updateTitle(String title) {
        mTvTitle.setText(title);
    }

    private long mPrevClickBackTime = -1;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (mPrevClickBackTime == -1 || currentTime - mPrevClickBackTime > 3000) {
            mPrevClickBackTime = currentTime;
            Toast.makeText(this, "Press again to exit",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Process.killProcess(Process.myPid());
    }

    /**
     * 响应倒计时事件操作
     * 添加、删除、修改事件都需要刷新列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDayMatterOptionReceive(EventDayMatter event) {
        String eventType = event.getEvent();
        switch (eventType) {
            case Constants.EVENT_MODIFY_DAY_MATTER:
            case Constants.EVENT_DELETE_DAY_MATTER:
            case Constants.EVENT_ADD_DAY_MATTER:
//                mPresenter.getSlideMenuSortData(this);
                mPresenter.updateTitle(event.getSortId());
                mVp.setCurrentItem(0, true);
                break;
            case Constants.EVENT_SELECT_DISPLAY_SORT_LIST:
                mVp.setCurrentItem(0, true);
                mPresenter.updateTitle(event.getSortId());
                break;
            default:
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceive(Event event) {
        if (event instanceof SortEvent) {
//            mPresenter.getSlideMenuSortData(this);
        }

    }

    private View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_tab, null);
        ImageView imageView = view.findViewById(R.id.icon);
        TextView textView = view.findViewById(R.id.text1);
        TabModel.Tab tab = TabModel.INSTANCE.getTab(position);
        textView.setText(tab.getLabelResId());
        imageView.setImageResource(tab.getIconResId());
        return view;
    }
}
