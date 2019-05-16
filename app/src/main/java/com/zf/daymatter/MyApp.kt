package com.zf.daymatter

import android.app.Application
import android.content.Context

import org.litepal.LitePal

/**
 * Created by Allever on 18/5/21.
 */

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        LitePal.initialize(this)
    }

    companion object {
        lateinit var context: Context
    }
}
