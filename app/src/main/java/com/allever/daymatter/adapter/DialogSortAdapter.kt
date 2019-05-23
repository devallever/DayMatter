package com.allever.daymatter.adapter

import com.allever.daymatter.data.Event
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.allever.daymatter.R

class DialogSortAdapter(data: List<Event.Sort>)
    : BaseQuickAdapter<Event.Sort, BaseViewHolder>(R.layout.item_dialog_sort, data) {

    override fun convert(holder: BaseViewHolder?, item: Event.Sort?) {
        holder?.setText(R.id.item_dialog_sort_title, item?.name)
    }
}