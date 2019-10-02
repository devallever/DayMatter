package com.allever.daymatter.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.allever.daymatter.R;
import com.allever.daymatter.bean.ItemDayMatter;

import java.util.List;

/**
 * Created by Allever on 18/5/21.
 */

public class DayMatterListAdapter extends BaseQuickAdapter<ItemDayMatter, BaseViewHolder> {
    private Context mContext;
    public DayMatterListAdapter(Context context, List<ItemDayMatter> data) {
        super(R.layout.item_list_day_matter, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ItemDayMatter item) {
        //如果剩余天数大于等于0 ，表示还没到
        if (item.getLeftDay() >= 0){
            holder.setText(R.id.id_item_list_tv_title, mContext.getResources().getString(R.string.distance)
                    + item.getTitle()
                    + mContext.getResources().getString(R.string.has));
            holder.setText(R.id.id_item_list_tv_left_day, item.getLeftDay() + "");
            //设置背景默认
            holder.setBackgroundColor(R.id.id_item_list_ll_left_day_container, mContext.getResources().getColor(R.color.colorDefault));
        }else {
            //如果剩余天数小于0，表示已经过了
            holder.setText(R.id.id_item_list_tv_title, item.getTitle() + mContext.getString(R.string.already));
            int leftDay = -1 * item.getLeftDay();
            holder.setText(R.id.id_item_list_tv_left_day, leftDay+"");

            //设置背景黄色
            holder.setBackgroundColor(R.id.id_item_list_ll_left_day_container, mContext.getResources().getColor(R.color.orange_500));
        }

    }
}
