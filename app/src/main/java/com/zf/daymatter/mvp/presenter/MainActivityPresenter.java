package com.zf.daymatter.mvp.presenter;

import android.content.Context;

import com.zf.daymatter.bean.ItemSlidMenuSort;
import com.zf.daymatter.data.DataListener;
import com.zf.daymatter.mvp.BasePresenter;
import com.zf.daymatter.mvp.view.IMainActivityView;
import com.zf.daymatter.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allever on 18/5/21.
 */

public class MainActivityPresenter extends BasePresenter<IMainActivityView> {

    public void getSlideMenuSortData(Context context) {
        //List<ItemSlidMenuSort> list  = mDataSource.getSlidMenuSortData(context);
        mDataSource.getSlidMenuSortData(context, new DataListener<List<ItemSlidMenuSort>>() {
            @Override
            public void onSuccess(List<ItemSlidMenuSort> data) {
                mViewRef.get().setSlidMenuSort(data);
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    public void initDefaultSortData(Context context) {
        //如果是第一次启动，则向数据库添加初始化数据
        if (SPUtils.getIsFirstLaunch(context)){
            SPUtils.setFirstLaunch(context,false);
            mDataSource.addDefaultSortData(context);
            mDataSource.addDefaultConfig();
        }
    }
}
