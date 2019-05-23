package com.allever.daymatter.dialog

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.allever.daymatter.App
import com.allever.daymatter.adapter.DialogSortAdapter
import com.allever.daymatter.data.DataListener
import com.allever.daymatter.data.Event
import com.allever.daymatter.data.Repository
import com.allever.daymatter.R
import java.lang.Exception

object DialogHelper{

    fun createSelectSortDialog(activity: Activity, callback: SelectSortCallback?): AlertDialog {
        val dialog = AlertDialog.Builder(activity).create()
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_sort, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.select_sort_rv)
        val data = mutableListOf<Event.Sort>()
        val adapter = DialogSortAdapter(data)
        adapter.setOnItemClickListener { adapter, view, position ->
            val data = data[position]
            callback?.onItemClick(position, data.name, data.id, dialog)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)


        Repository.getIns().getSortData(activity, object : DataListener<List<Event.Sort>> {
            override fun onSuccess(dataList: List<Event.Sort>?) {
                data.clear()
                if (dataList != null) {
                    data.addAll(dataList)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFail(msg: String?) {}
        })

        val tvAddSort = view.findViewById<TextView>(R.id.select_sort_tv_add_sort)
        tvAddSort.setOnClickListener {
            callback?.onAddSortClick(dialog)
        }
        dialog.setView(view)
        return dialog
    }

    fun createMessageDialog(activity: Activity, builder: Builder?, callback: TextDialogCallback?): AlertDialog {
        var builder = builder
        val dialog = AlertDialog.Builder(activity).create()
        val view = LayoutInflater.from(activity).inflate(R.layout.layout_alert_dialog, null)
        dialog.setView(view)
        val titleView = view.findViewById(R.id.title) as TextView
        val detailView = view.findViewById(R.id.detail) as TextView
        val cancelView = view.findViewById(R.id.btn_cancel) as AppCompatButton
        val okView = view.findViewById(R.id.btn_ok) as AppCompatButton
        val editText = view.findViewById(R.id.id_edit_text) as EditText

        titleView.visibility = View.VISIBLE
        detailView.visibility = View.VISIBLE
        okView.visibility = View.VISIBLE
        cancelView.visibility = View.VISIBLE
        editText.visibility = View.GONE

        if (builder == null) {
            builder = Builder()
        }

        okView.text = builder.okText
        cancelView.text = builder.cancelText
        titleView.text = builder.title
        detailView.text = builder.message

        okView.setOnClickListener {
            callback?.onOkClick(dialog)
        }

        cancelView.setOnClickListener {
            callback?.onCancelClick(dialog)
        }

        return dialog
    }

    fun createEditTextDialog(activity: Context, builder: DialogHelper.Builder?, callback: DialogHelper.EditDialogCallback?): AlertDialog {
        var builder = builder
        val editAlertDialog = AlertDialog.Builder(activity).create()
        val view = LayoutInflater.from(activity).inflate(R.layout.layout_alert_dialog, null)
        editAlertDialog.setView(view)
//        val window = editAlertDialog.window
//        window?.setContentView(R.layout.layout_alert_dialog)
        val titleView = view.findViewById(R.id.title) as TextView
        val detailView = view.findViewById(R.id.detail) as TextView
        val cancelView = view.findViewById(R.id.btn_cancel) as AppCompatButton
        val okView = view.findViewById(R.id.btn_ok) as AppCompatButton
        val editText = view.findViewById(R.id.id_edit_text) as EditText

        if (builder == null) {
            builder = DialogHelper.Builder()
        }

        titleView.text = builder.title
        detailView.text = builder.message
        cancelView.text = builder.cancelText
        okView.text = builder.okText
        editText.setText(builder.etContent)

        if (builder.showEditText == true){
            editText.visibility = View.VISIBLE
            val content = builder.etContent
            editText.setText(content)
            editText.setSelection(content?.length?: 0)
        }else{
            editText.visibility = View.GONE
        }

        if (builder.showMessage == true){
            detailView.visibility = View.VISIBLE
        }else{
            detailView.visibility = View.GONE
        }

        cancelView.setOnClickListener {
            callback?.onCancelClick(editAlertDialog)
            editAlertDialog.dismiss()
        }

        okView.setOnClickListener {
            callback?.onOkClick(editAlertDialog, editText.text.toString())
        }
        editAlertDialog.setCancelable(true)
        editAlertDialog.setCanceledOnTouchOutside(false)
        editAlertDialog.setOnShowListener {
            if (builder.showEditText == true){
                try {
                    val imm = App.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
                } catch (e: Exception) {

                }

            }
        }

        return editAlertDialog
    }

    interface EditDialogCallback{
        fun onOkClick(dialog: AlertDialog, etContent: String){}
        fun onCancelClick(dialog: AlertDialog){}
    }

    interface TextDialogCallback{
        fun onOkClick(dialog: AlertDialog){}
        fun onCancelClick(dialog: AlertDialog){}
    }

    interface SelectSortCallback{
        fun onItemClick(position: Int, sortName: String, id: Int, dialog: AlertDialog) {}
        fun onAddSortClick(dialog: AlertDialog)
    }


    class Builder{
        var title: String?= App.context.getString(R.string.dialog_default_title)
        var message: String? = App.context.getString(R.string.dialog_default_message)
        var okText: String? = App.context.getString(R.string.dialog_default_positive_text)
        var cancelText: String? = App.context.getString(R.string.dialog_default_negative_text)
        var showEditText: Boolean? = false
        var showMessage: Boolean? = true
        var etContent: String? = ""
        var list: MutableList<Any>? = null

        fun setTitleContent(title: String): Builder{
            this.title = title
            return this
        }

        fun setMessageContent(message: String): Builder{
            this.message = message
            return this
        }

        fun setOkContent(okText: String): Builder{
            this.okText = okText
            return this
        }

        fun setCancelContent(cancelText: String): Builder{
            this.cancelText = cancelText
            return this
        }

        fun isShowEditText(show: Boolean): Builder{
            showEditText = show
            return this
        }

        fun isShowMessage(show: Boolean): Builder{
            showMessage = show
            return this
        }

        fun setEditTextContent(etContent: String): Builder{
            this.etContent = etContent
            return this
        }
    }


}