package com.allever.daymatter.mvp;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.allever.daymatter.R;

/**
 * Created by allever on 18-2-28.
 */

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    protected T mPresenter;
    protected Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        //view 与 Presenter 关联
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    protected abstract T createPresenter();

    protected void showToast(String msg){
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
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
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
