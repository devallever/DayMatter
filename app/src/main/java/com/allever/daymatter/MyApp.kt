package com.allever.daymatter

import com.allever.lib.common.app.App
import com.allever.lib.umeng.UMeng

import org.litepal.LitePal

/**
 * Created by Allever on 18/5/21.
 */

class MyApp : App(){
    override fun onCreate() {
        super.onCreate()
        context = this
        LitePal.initialize(this)
        UMeng.init(this)
    }
}
