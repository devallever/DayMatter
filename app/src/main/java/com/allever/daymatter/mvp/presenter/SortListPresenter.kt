package com.allever.daymatter.mvp.presenter

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.allever.daymatter.data.DataListener
import com.allever.daymatter.data.Event
import com.allever.daymatter.ui.dialog.DialogHelper
import com.allever.daymatter.event.SortEvent
import com.allever.daymatter.mvp.BasePresenter
import com.allever.daymatter.mvp.view.ISortListView
import com.allever.daymatter.utils.ToastUtil
import com.allever.daymatter.R
import com.allever.lib.common.app.App
import org.greenrobot.eventbus.EventBus

class SortListPresenter : BasePresenter<ISortListView>() {

    fun getSlideMenuSortData(context: Context) {
        //List<ItemSlidMenuSort> list  = mDataSource.getSlidMenuSortData(context);
        mDataSource.getSortData(context, object : DataListener<List<Event.Sort>> {
            override fun onSuccess(data: List<Event.Sort>) {
                mViewRef.get()?.setSortData(data)
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

    fun modifySort(activity: Activity, sort: Event.Sort) {
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

    fun deleteSort(activity: Activity, sort: Event.Sort) {
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