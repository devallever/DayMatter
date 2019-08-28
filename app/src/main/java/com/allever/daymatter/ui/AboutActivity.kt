package com.allever.daymatter.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.allever.daymatter.mvp.BaseActivity
import com.allever.daymatter.mvp.presenter.AboutPresenter
import com.allever.daymatter.mvp.view.IAboutView
import com.umeng.analytics.MobclickAgent
import com.allever.daymatter.R

class AboutActivity : BaseActivity<IAboutView, AboutPresenter>(), View.OnClickListener {


    private lateinit var mToolbar: Toolbar
    private lateinit var mTvPrivacy: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        initView()

        initToolbar(mToolbar, R.string.about)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }

    private fun initView() {
        mToolbar = findViewById(R.id.id_toolbar)
        mTvPrivacy = findViewById(R.id.about_privacy)
        mTvPrivacy.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun createPresenter(): AboutPresenter = AboutPresenter()

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.about_privacy -> {
                mPresenter.goToPrivacy()
            }
        }
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, AboutActivity::class.java)
            context.startActivity(intent)
        }
    }
}