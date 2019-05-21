package com.allever.daymatter;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.allever.daymatter.event.Event;
import com.allever.daymatter.event.SortEvent;
import com.allever.daymatter.ui.EditDayMatterActivity;
import com.allever.daymatter.ui.DateCalcFragment;
import com.allever.daymatter.ui.DayMatterListFragment;
import com.allever.daymatter.ui.RemindFragment;
import com.allever.daymatter.ui.SortListActivity;
import com.allever.daymatter.ui.TabModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zf.daymatter.R;
import com.allever.daymatter.adapter.SlidMenuSortAdapter;
import com.allever.daymatter.adapter.ViewPagerAdapter;
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
import butterknife.OnClick;

public class MainActivity extends
        BaseActivity<IMainActivityView,
                MainActivityPresenter>
        implements IMainActivityView, TabLayout.OnTabSelectedListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.id_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.id_main_vp)
    ViewPager mVp;
    @BindView(R.id.id_main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.id_header_main_tv_day_matter)
    TextView mTvDayMatter;
    //    @BindView(R.id.id_header_main_tv_more)
//    TextView mTvMore;
//    @BindView(R.id.id_header_main_tv_date_calc)
//    TextView mTvDateCalc;
    @BindView(R.id.id_main_rv_sort)
    RecyclerView mRvSort;
    @BindView(R.id.tab_layout)
    TabLayout mTab;


    private ViewPagerAdapter mViewPagerAdapter;

    private List<Fragment> mFragmentList;

    private SlidMenuSortAdapter mSlidMenuSortAdapter;

    private List<ItemSlidMenuSort> mItemSlidMenuSortList;

    private int mPageIndex = 0;

    private int mSortId = 0;

    private int mainTabHighlight = 0;
    private int mainTabUnselectColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        ButterKnife.bind(this);

        //如果是第一次启动，则向数据库添加默认的数据
        mPresenter.initDefaultSortData(this);

        //初始化ViewPager数据
        initViewPagerData();

        //初始化侧滑菜单分类列表数据
        initSliceMenuSort();

        //获取侧滑菜单分类列表数据
        mPresenter.getSlideMenuSortData(this);

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
        mFragmentList.add(new DateCalcFragment());
        mFragmentList.add(new RemindFragment());
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, mFragmentList);
    }

    private void initSliceMenuSort() {
        mItemSlidMenuSortList = new ArrayList<>();
        mSlidMenuSortAdapter = new SlidMenuSortAdapter(mItemSlidMenuSortList);
        mRvSort.setLayoutManager(new LinearLayoutManager(this));
        mRvSort.setAdapter(mSlidMenuSortAdapter);

        //侧滑菜单中的RecyclerView列表设置监听
        mSlidMenuSortAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mSortId = mItemSlidMenuSortList.get(position).getId();

                //viewpager切换到首页
                mPageIndex = 0;
                mVp.setCurrentItem(mPageIndex);
                mDrawerLayout.closeDrawers();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //发送消息事件通知更新列表，带sortId
                        EventDayMatter eventDayMatter = new EventDayMatter();
                        eventDayMatter.setEvent(Constants.EVENT_SELECT_DISPLAY_SORT_LIST);
                        eventDayMatter.setSortId(mSortId);
                        EventBus.getDefault().post(eventDayMatter);
                        mVp.setCurrentItem(mPageIndex);
                    }
                }, 500);

            }
        });
    }

    private void initView() {
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.app_name,
                R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                Log.d(TAG, "onDrawerClosed: position = " + mPageIndex);

                //如果页面下标小于页面数，则允许切换页面，避免索引号异常
                if (mPageIndex < mFragmentList.size()) {
                    mVp.setCurrentItem(mPageIndex);
                }
            }
        };
//        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        //要不要这个侧滑菜单栏的动画效果都无所谓了。
        actionBarDrawerToggle.syncState();

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
                        mToolbar.setTitle(getString(R.string.matter));
                        break;
                    case 1:
                        mToolbar.setTitle(getString(R.string.calc));
                        break;
                    case 2:
                        mToolbar.setTitle(getString(R.string.setting));
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


    @OnClick({R.id.id_header_main_tv_day_matter,
//            R.id.id_header_main_tv_more,
//            R.id.id_header_main_tv_date_calc,
//            R.id.id_header_main_tv_remind,
            R.id.id_header_main_tv_sort})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_header_main_tv_day_matter:
                mPageIndex = 0;
                mDrawerLayout.closeDrawers();
                break;
//            case R.id.id_header_main_tv_date_calc:
//                mPageIndex = 1;
//                mDrawerLayout.closeDrawers();
//                break;
//            case R.id.id_header_main_tv_remind:
//                mPageIndex = 2;
//                mDrawerLayout.closeDrawers();
//                break;
//            case R.id.id_header_main_tv_more:
//                mPageIndex = 3;
//                mDrawerLayout.closeDrawers();
//                break;
            case R.id.id_header_main_tv_sort:
                SortListActivity.Companion.actionStart(MainActivity.this);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawerLayout.closeDrawers();
                    }
                }, 500);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.id_menu_main_add:
                EditDayMatterActivity.startSelf(this);
                break;
//            case R.id.id_menu_main_style:
//                showToast("Style");
//                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void setSlidMenuSort(List<ItemSlidMenuSort> itemSlidMenuSortList) {
        if (itemSlidMenuSortList == null) {
            return;
        }

        mItemSlidMenuSortList.clear();
        mItemSlidMenuSortList.addAll(itemSlidMenuSortList);
        mSlidMenuSortAdapter.notifyDataSetChanged();
    }

    private long mPrevClickBackTime = -1;

    @Override
    public void onBackPressed() {
//        ExitDialog exitDialog = new ExitDialog(this);
//        exitDialog.setExitListener(this);
//        exitDialog.show();

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
                mPresenter.getSlideMenuSortData(this);
                break;
            default:
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceive(Event event) {
        if (event instanceof SortEvent) {
            mPresenter.getSlideMenuSortData(this);
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
