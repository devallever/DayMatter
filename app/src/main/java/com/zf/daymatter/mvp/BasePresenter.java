package com.zf.daymatter.mvp;

import com.zf.daymatter.data.DataSource;
import com.zf.daymatter.data.Repository;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by allever on 18-2-28.
 */

public abstract class BasePresenter<V> {
    //View类(Activity Fragment)接口弱引用
    protected Reference<V> mViewRef;

    protected DataSource mDataSource = Repository.getIns();

    public void attachView(V view){
        mViewRef = new WeakReference<V>(view);
    }

    protected V getView(){
        return mViewRef.get();
    }

    public boolean isAttachedView(){
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView(){
        if (mViewRef != null){
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
