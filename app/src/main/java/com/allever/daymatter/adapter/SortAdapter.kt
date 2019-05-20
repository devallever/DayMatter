package com.allever.daymatter.adapter

import com.allever.daymatter.bean.ItemSlidMenuSort
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zf.daymatter.R

class SortAdapter(data: List<ItemSlidMenuSort>)
    : BaseQuickAdapter<ItemSlidMenuSort, BaseViewHolder>(R.layout.item_sort, data) {

    override fun convert(holder: BaseViewHolder?, item: ItemSlidMenuSort?) {
        holder?.setText(R.id.id_item_slid_sort_tv_name, item?.name)
        holder?.setText(R.id.id_item_slid_sort_tv_count, item?.count.toString() + "")
        holder?.addOnClickListener(R.id.id_item_slid_sort_iv_type)
    }
}