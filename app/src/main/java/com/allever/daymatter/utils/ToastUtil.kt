package com.allever.daymatter.utils

import android.widget.Toast
import com.allever.lib.common.app.App

object ToastUtil {
    fun show(msg: String) {
        Toast.makeText(App.context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLong(msg: String) {
        Toast.makeText(App.context, msg, Toast.LENGTH_LONG).show()
    }
}