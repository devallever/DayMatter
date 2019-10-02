package com.allever.daymatter

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.allever.lib.common.app.App

import org.litepal.LitePal

/**
 * Created by Allever on 18/5/21.
 */

class MyApp : App(){
    override fun onCreate() {
        super.onCreate()
        context = this
        LitePal.initialize(this)
    }
}
