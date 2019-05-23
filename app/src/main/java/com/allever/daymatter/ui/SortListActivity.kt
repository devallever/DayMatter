package com.allever.daymatter.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import com.allever.daymatter.adapter.SortAdapter
import com.allever.daymatter.data.Event
import com.allever.daymatter.mvp.BaseActivity
import com.allever.daymatter.mvp.presenter.SortListPresenter
import com.allever.daymatter.mvp.view.ISortListView
import com.allever.daymatter.utils.DisplayUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.umeng.analytics.MobclickAgent
import com.yanzhenjie.recyclerview.*
import com.allever.daymatter.R

class SortListActivity : BaseActivity<ISortListView,
        SortListPresenter>(),
        ISortListView,
        OnItemMenuClickListener,
        BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener {

    private lateinit var mRvSort: SwipeRecyclerView
    private lateinit var mAdapter: SortAdapter
    private var mSortData = mutableListOf<Event.Sort>()
    private lateinit var mToolbar: Toolbar
    private lateinit var mBtnAddSort: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sort_list)

        initData()

        initView()

        initToolbar(mToolbar, R.string.sort_manage)

        mPresenter.getSlideMenuSortData(this)

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
        mRvSort = findViewById(R.id.rv_sort_list)
        mRvSort.layoutManager = LinearLayoutManager(this)
        mRvSort.setOnItemMenuClickListener(this)

        val menuCreator = SwipeMenuCreator { leftMenu, rightMenu, position ->
            val modifyItem = SwipeMenuItem(this)
            modifyItem.text = getString(R.string.sort_list_menu_modify)
            modifyItem.setTextColor(resources.getColor(R.color.white))
            modifyItem.setBackgroundColor(resources.getColor(R.color.sort_list_menu_modify_color))
            modifyItem.height = ViewGroup.LayoutParams.MATCH_PARENT
            modifyItem.width = DisplayUtil.dip2px(60)
            rightMenu.addMenuItem(modifyItem)

            val deleteItem = SwipeMenuItem(this)
            deleteItem.text = getString(R.string.sort_list_menu_delete)
            deleteItem.setTextColor(resources.getColor(R.color.white))
            deleteItem.setBackgroundColor(resources.getColor(R.color.sort_list_menu_delete_color))
            deleteItem.height = ViewGroup.LayoutParams.MATCH_PARENT
            deleteItem.width = DisplayUtil.dip2px(60)
            rightMenu.addMenuItem(deleteItem)
        }

        mRvSort.setSwipeMenuCreator(menuCreator)
        mRvSort.adapter = mAdapter

        mBtnAddSort = findViewById(R.id.id_btn_add_sort)
        mBtnAddSort.setOnClickListener(this)

    }

    private fun initData() {
        mAdapter = SortAdapter(mSortData)
        mAdapter.onItemChildClickListener = this
    }

    override fun createPresenter(): SortListPresenter = SortListPresenter()


    /***
     * MenuItem回调
     */
    override fun onItemClick(menuBridge: SwipeMenuBridge?, adapterPosition: Int) {
        menuBridge?.closeMenu()
        val menuIndex = menuBridge?.position
        if (adapterPosition in 0..2) {
            showToast(getString(R.string.can_not_modify_default_sort))
            return
        }

        if (menuBridge?.position == 0) {
            mHandler.postDelayed({
                mPresenter.modifySort(this, mSortData[adapterPosition])
            }, 200)
        } else if (menuBridge?.position == 1) {
            mHandler.postDelayed({
                mPresenter.deleteSort(this, mSortData[adapterPosition])
            }, 200)
        }

    }

    /***
     * Rv Adapter item child回调
     */
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.id_item_slid_sort_iv_type -> {
                mRvSort.smoothOpenRightMenu(position)
            }
        }
    }

    /***
     * 点击事件回调
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_btn_add_sort -> {
                mPresenter.addSort(this)
            }
        }
    }


    override fun setSortData(data: List<Event.Sort>) {
        mSortData.clear()
        mSortData.addAll(data)
        mAdapter.notifyDataSetChanged()
    }


    companion object {
        fun actionStart(context: Context?) {
            val intent = Intent(context, SortListActivity::class.java)
            context?.startActivity(intent)
        }

    }
}