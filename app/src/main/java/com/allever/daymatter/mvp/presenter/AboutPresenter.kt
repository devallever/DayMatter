package com.allever.daymatter.mvp.presenter

import com.allever.daymatter.App
import com.allever.daymatter.mvp.BasePresenter
import com.allever.daymatter.mvp.view.IAboutView
import com.allever.daymatter.utils.SystemUtils

class AboutPresenter: BasePresenter<IAboutView>() {

    private val PRIVACY_URL = "https://plus.google.com/116794250597377070773/posts/SYoEZWDm77x"

    fun goToPrivacy() {
        SystemUtils.startWebView(App.context, PRIVACY_URL)
    }
}