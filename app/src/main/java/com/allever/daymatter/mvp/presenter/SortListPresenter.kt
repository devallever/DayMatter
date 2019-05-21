package com.allever.daymatter.mvp.presenter

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import com.allever.daymatter.App
import com.allever.daymatter.bean.ItemSlidMenuSort
import com.allever.daymatter.data.DataListener
import com.allever.daymatter.dialog.DialogHelper
import com.allever.daymatter.event.EventDayMatter
import com.allever.daymatter.event.SortEvent
import com.allever.daymatter.mvp.BasePresenter
import com.allever.daymatter.mvp.view.ISortListView
import com.allever.daymatter.utils.Constants
import com.allever.demoapp.util.ToastUtil
import com.zf.daymatter.R
import org.greenrobot.eventbus.EventBus

class SortListPresenter : BasePresenter<ISortListView>() {

    fun getSlideMenuSortData(context: Context) {
        //List<ItemSlidMenuSort> list  = mDataSource.getSlidMenuSortData(context);
        mDataSource.getSlidMenuSortData(context, object : DataListener<List<ItemSlidMenuSort>> {
            override fun onSuccess(data: List<ItemSlidMenuSort>) {
                mViewRef.get()?.setSlidMenuSort(data)
                val sortEvent = SortEvent()
                EventBus.getDefault().post(sortEvent)
            }

            override fun onFail(msg: String) {

            }
        })
    }

    fun addSort(activity: Activity) {
        val dialogBuilder = DialogHelper.Builder()
        dialogBuilder.title = App.context.resources.getString(R.string.add_sort)
        dialogBuilder.isShowMessage(false)
        dialogBuilder.isShowEditText(true)
        val dialog = DialogHelper.createEditTextDialog(activity, dialogBuilder, object : DialogHelper.EditDialogCallback {
            override fun onCancelClick(dialog: AlertDialog) {
                dialog.dismiss()
            }

            override fun onOkClick(dialog: AlertDialog, etContent: String) {
                if (etContent.isEmpty()) {
                    ToastUtil.show("请输入内容")
                    return
                } else {
                    mDataSource.saveSort(etContent)
                    getSlideMenuSortData(App.context)
                    dialog.dismiss()
                }
            }
        })

        dialog.show()
    }

    fun modifySort(activity: Activity, sort: ItemSlidMenuSort) {
        val dialogBuilder = DialogHelper.Builder()
        dialogBuilder.title = App.context.resources.getString(R.string.modify_sort)
        dialogBuilder.etContent = sort.name
        dialogBuilder.isShowMessage(false)
        dialogBuilder.isShowEditText(true)
        val dialog = DialogHelper.createEditTextDialog(activity, dialogBuilder, object : DialogHelper.EditDialogCallback {
            override fun onCancelClick(dialog: AlertDialog) {
                dialog.dismiss()
            }

            override fun onOkClick(dialog: AlertDialog, etContent: String) {
                if (etContent.isEmpty()) {
                    ToastUtil.show("请输入内容")
                    return
                } else {
                    mDataSource.modifySort(sort.id, etContent)
                    getSlideMenuSortData(App.context)
                    dialog.dismiss()
                }
            }
        })

        dialog.show()
    }

    fun deleteSort(activity: Activity, sort: ItemSlidMenuSort) {
        val builder = DialogHelper.Builder()
        builder.title = App.context.resources.getString(R.string.delete_sort)
        builder.message = App.context.resources.getString(R.string.make_sure_delete_sort)
        val dialog = DialogHelper.createMessageDialog(activity, builder, object : DialogHelper.TextDialogCallback {
            override fun onOkClick(dialog: AlertDialog) {
                mDataSource.deleteSort(sort.id)
                getSlideMenuSortData(activity)
                dialog.dismiss()
            }

            override fun onCancelClick(dialog: AlertDialog) {
                dialog.dismiss()
            }
        })

        dialog.show()
    }
}