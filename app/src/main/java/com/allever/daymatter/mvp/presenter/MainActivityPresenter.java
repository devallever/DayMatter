package com.allever.daymatter.mvp.presenter;

import android.content.Context;

import com.allever.daymatter.R;
import com.allever.daymatter.utils.SPUtils;
import com.allever.daymatter.mvp.BasePresenter;
import com.allever.daymatter.mvp.view.IMainActivityView;
import com.allever.lib.common.app.App;

/**
 * Created by Allever on 18/5/21.
 */

public class MainActivityPresenter extends BasePresenter<IMainActivityView> {

    private String mAppName = App.context.getString(R.string.matter);
    private String mCurrentSortName = App.context.getString(R.string.all);

    public void initDefaultSortData(Context context) {
        //如果是第一次启动，则向数据库添加初始化数据
        if (SPUtils.getIsFirstLaunch(context)){
            SPUtils.setFirstLaunch(context,false);
            mDataSource.addDefaultSortData(context);
            mDataSource.addDefaultConfig();
        }
    }

    public void updateTitle() {
        mViewRef.get().updateTitle(mAppName + "." + mCurrentSortName);
    }

    public void updateTitle(int id) {
        if (id == 0) {
            mCurrentSortName = App.context.getString(R.string.all);
        } else {
            mCurrentSortName = mDataSource.getSortName(id);
        }
        updateTitle();
    }
}
