package com.allever.daymatter

import android.app.Application
import android.content.Context
import com.umeng.commonsdk.UMConfigure

import org.litepal.LitePal

/**
 * Created by Allever on 18/5/21.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        LitePal.initialize(this)

        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.init(this, null, null, UMConfigure.DEVICE_TYPE_PHONE, null)

    }

    companion object {
        lateinit var context: Context
    }
}
