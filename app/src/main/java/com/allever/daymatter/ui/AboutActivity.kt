package com.allever.daymatter.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.allever.daymatter.mvp.BaseActivity
import com.allever.daymatter.mvp.presenter.AboutPresenter
import com.allever.daymatter.mvp.view.IAboutView
import com.zf.daymatter.R

class AboutActivity: BaseActivity<IAboutView, AboutPresenter>() {

    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        initView()

        initToolbar(mToolbar, R.string.about)
    }

    private fun initView() {
        mToolbar = findViewById(R.id.id_toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    override fun createPresenter(): AboutPresenter = AboutPresenter()

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, AboutActivity::class.java)
            context.startActivity(intent)
        }
    }
}