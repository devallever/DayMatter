package com.zf.daymatter.mvp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zf.daymatter.R;


/**
 * Created by Mac on 18/3/1.
 */

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    protected T mPresenter;

    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);

        mActivity = getActivity();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

    protected void initToolbar(Toolbar toolbar, String title){
        toolbar.setTitle(title);
        initToolbar(toolbar);
    }

    protected void initToolbar(Toolbar toolbar, int strId){
        toolbar.setTitle(strId);
        initToolbar(toolbar);
    }

    protected void initToolbar(Toolbar toolbar){
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                _mActivity.onBackPressed();
//            }
//        });
    }

    protected void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    protected void showToast(int msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected abstract T createPresenter();
}
