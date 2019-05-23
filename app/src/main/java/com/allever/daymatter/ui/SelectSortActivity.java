package com.allever.daymatter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.umeng.analytics.MobclickAgent;
import com.allever.daymatter.R;
import com.allever.daymatter.mvp.BaseActivity;
import com.allever.daymatter.mvp.presenter.SelectSortPresenter;
import com.allever.daymatter.mvp.view.ISelectSortView;

/**
 * Created by Allever on 18/5/22.
 */

public class SelectSortActivity extends BaseActivity<ISelectSortView, SelectSortPresenter> implements ISelectSortView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sort);
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
    protected SelectSortPresenter createPresenter() {
        return new SelectSortPresenter();
    }
}
