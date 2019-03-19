package com.zf.daymatter;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by Allever on 18/5/21.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
