package com.allever.daymatter.utils

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.ArrayList

object ActivityController {

    private val TAG = ActivityController::class.java.simpleName

    private val mActivityList = ArrayList<WeakReference<Activity>?>()

    fun size(): Int {
        return mActivityList.size
    }

    fun add(weakRefActivity: WeakReference<Activity>?) {
        mActivityList.add(weakRefActivity)
    }

    fun remove(weakRefActivity: WeakReference<Activity>?) {
        val result = mActivityList.remove(weakRefActivity)
    }

    fun finishAll() {
        if (mActivityList.isNotEmpty()) {
            for (activityWeakReference in mActivityList) {
                val activity = activityWeakReference?.get()
                if (activity != null && !activity.isFinishing) {
                    activity.finish()
                }
            }
            mActivityList.clear()
        }
    }
}