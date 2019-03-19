package com.zf.daymatter.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Allever on 18/5/21.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private Context mContext;
    public ViewPagerAdapter(FragmentManager fragmentManager, Context context, List<Fragment> fragmentList){
        super(fragmentManager);
        mFragmentList = fragmentList;
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
