package com.allever.daymatter.mvp.presenter

import android.content.Context
import com.allever.daymatter.bean.ItemSlidMenuSort
import com.allever.daymatter.data.DataListener
import com.allever.daymatter.mvp.BasePresenter
import com.allever.daymatter.mvp.view.ISortListView

class SortListPresenter: BasePresenter<ISortListView>() {

    fun getSlideMenuSortData(context: Context) {
        //List<ItemSlidMenuSort> list  = mDataSource.getSlidMenuSortData(context);
        mDataSource.getSlidMenuSortData(context, object : DataListener<List<ItemSlidMenuSort>> {
            override fun onSuccess(data: List<ItemSlidMenuSort>) {
                mViewRef.get()?.setSlidMenuSort(data)
            }

            override fun onFail(msg: String) {

            }
        })
    }
}