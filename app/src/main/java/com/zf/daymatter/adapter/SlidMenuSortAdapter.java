package com.zf.daymatter.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zf.daymatter.R;
import com.zf.daymatter.bean.ItemSlidMenuSort;

import java.util.List;

/**
 *
 * @author Allever
 * @date 18/5/26
 */

public class SlidMenuSortAdapter extends BaseQuickAdapter<ItemSlidMenuSort, BaseViewHolder> {
    public SlidMenuSortAdapter(List<ItemSlidMenuSort> data) {
        super(R.layout.item_slid_menu_sort, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ItemSlidMenuSort item) {
        holder.setText(R.id.id_item_slid_sort_tv_name, item.getName());
        holder.setText(R.id.id_item_slid_sort_tv_count, item.getCount() + "");

    }
}
