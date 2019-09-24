package com.allever.daymatter.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.allever.daymatter.R
import com.allever.daymatter.data.Event
import com.allever.daymatter.event.EventDayMatter
import com.allever.daymatter.mvp.BaseFragment
import com.allever.daymatter.mvp.presenter.SortListPresenter
import com.allever.daymatter.mvp.view.ISortListView
import com.allever.daymatter.ui.adapter.SortAdapter
import com.allever.daymatter.utils.Constants
import com.allever.daymatter.utils.DisplayUtil
import com.allever.lib.common.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yanzhenjie.recyclerview.OnItemMenuClickListener
import com.yanzhenjie.recyclerview.SwipeMenuBridge
import com.yanzhenjie.recyclerview.SwipeMenuCreator
import com.yanzhenjie.recyclerview.SwipeMenuItem
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import org.greenrobot.eventbus.EventBus

class SortFragment: BaseFragment<ISortListView,
        SortListPresenter>(),
        ISortListView,
        OnItemMenuClickListener,
        BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private lateinit var mRvSort: SwipeRecyclerView
    private lateinit var mAdapter: SortAdapter
    private var mSortData = mutableListOf<Event.Sort>()
    private lateinit var mBtnAddSort: FloatingActionButton
    private lateinit var mView: View
    private val mHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mView = LayoutInflater.from(activity).inflate(R.layout.activity_sort_list, container, false)
        initData()
        initView()
        mPresenter.getSlideMenuSortData(context!!)
        return mView
    }

    private fun initView() {
        mRvSort = mView.findViewById(R.id.rv_sort_list)
        mRvSort.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
        mRvSort.setOnItemMenuClickListener(this)

        val menuCreator = SwipeMenuCreator { leftMenu, rightMenu, position ->
            val modifyItem = SwipeMenuItem(activity)
            modifyItem.text = getString(R.string.sort_list_menu_modify)
            modifyItem.setTextColor(resources.getColor(R.color.white))
            modifyItem.setBackgroundColor(resources.getColor(R.color.sort_list_menu_modify_color))
            modifyItem.height = ViewGroup.LayoutParams.MATCH_PARENT
            modifyItem.width = DisplayUtil.dip2px(60)
            rightMenu.addMenuItem(modifyItem)

            val deleteItem = SwipeMenuItem(activity)
            deleteItem.text = getString(R.string.sort_list_menu_delete)
            deleteItem.setTextColor(resources.getColor(R.color.white))
            deleteItem.setBackgroundColor(resources.getColor(R.color.sort_list_menu_delete_color))
            deleteItem.height = ViewGroup.LayoutParams.MATCH_PARENT
            deleteItem.width = DisplayUtil.dip2px(60)
            rightMenu.addMenuItem(deleteItem)
        }

        mRvSort.setSwipeMenuCreator(menuCreator)
        mRvSort.adapter = mAdapter

        mBtnAddSort = mView.findViewById(R.id.id_btn_add_sort)
        mBtnAddSort.setOnClickListener(this)

    }

    private fun initData() {
        mAdapter = SortAdapter(mSortData)
        mAdapter.onItemChildClickListener = this
        mAdapter.onItemClickListener = this
    }

    override fun createPresenter(): SortListPresenter = SortListPresenter()

    override fun setSortData(data: List<Event.Sort>) {
        mSortData.clear()
        mSortData.addAll(data)
        mAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        ToastUtils.show(mSortData[position].name)
        val event = EventDayMatter()
        event.event = Constants.EVENT_SELECT_DISPLAY_SORT_LIST
        event.sortId = mSortData[position].id
        event.name = mSortData[position].name
        EventBus.getDefault().post(event)
    }

    override fun onItemClick(menuBridge: SwipeMenuBridge?, adapterPosition: Int) {
        menuBridge?.closeMenu()
        val menuIndex = menuBridge?.position
        if (adapterPosition in 0..2) {
            showToast(getString(R.string.can_not_modify_default_sort))
            return
        }

        if (menuBridge?.position == 0) {
            mHandler.postDelayed({
                mPresenter.modifySort(activity!!, mSortData[adapterPosition])
            }, 200)
        } else if (menuBridge?.position == 1) {
            mHandler.postDelayed({
                mPresenter.deleteSort(activity!!, mSortData[adapterPosition])
            }, 200)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.id_item_slid_sort_iv_type -> {
                mRvSort.smoothOpenRightMenu(position)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.id_btn_add_sort -> {
                mPresenter.addSort(context!!)
            }
        }
    }
}