package com.allever.daymatter.utils


import android.util.Log

object LogUtil {


    private val TAG = LogUtil::class.java.simpleName

    private var mShow: Boolean = true

    fun init(show: Boolean = true) {
        mShow = show
    }


    fun d(tag: String, msg: String) {
        if (mShow) {
            Log.d(tag, msg)
        }
    }

    fun d(msg: String) {
        if (mShow) {
            d(TAG, msg)
        }
    }

    fun e(msg: String) {
        if (mShow) {
            e(TAG, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (mShow) {
            Log.e(tag, msg)
        }
    }
}