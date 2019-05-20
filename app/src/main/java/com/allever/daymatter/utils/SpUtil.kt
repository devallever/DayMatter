package com.allever.daymatter.utils

import android.content.Context
import com.allever.daymatter.App

/**
 * SharedPreferences 工具类
 */
object SpUtil {

    /** SharedPreferences 文件名 */
    private const val SP_FILE_NAME = "ALSKF_ALSKDJFO"
    /** SharedPreferences 对象 */
    private val mSharePreference =
        App.context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)

    /**
     * 保存字符串
     *
     * @param key   键名
     * @param value 值
     */
    fun putString(key: String, value: String) {
        mSharePreference.edit().putString(key, value).apply()
    }

    /**
     * 获取字符串
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return 字符串
     */
    fun getString(key: String, defValue: String) = mSharePreference.getString(key, defValue)!!

    /**
     * 保存整形
     *
     * @param key   键名
     * @param value 值
     */
    fun putInteger(key: String, value: Int) {
        mSharePreference.edit().putInt(key, value).apply()
    }

    /**
     * 获取整形
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return 整形
     */
    fun getInteger(key: String, defValue: Int) = mSharePreference.getInt(key, defValue)

    /**
     * 保存布尔类型
     *
     * @param key   键名
     * @param value 值
     */
    fun putBoolean(key: String, value: Boolean) {
        mSharePreference.edit().putBoolean(key, value).apply()
    }

    /**
     * 获取布尔值
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return 布尔值
     */
    fun getBoolean(key: String, defValue: Boolean) = mSharePreference.getBoolean(key, defValue)

    /**
     * 保存Int
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun putInt(key: String, value: Int) {
        mSharePreference.edit().putInt(key, value).apply()
    }

    /**
     * 获取Int
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun getInt(key: String, value: Int) = mSharePreference.getInt(key, value)

    /**
     * 保存Long
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun putLong(key: String, value: Long) {
        mSharePreference.edit().putLong(key, value).apply()
    }

    /**
     * 获取Long
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun getLong(key: String, value: Long) = mSharePreference.getLong(key, value)

    /**
     * 根据 key，从 SharedPref 中移除数据
     *
     * @param key 键名
     */
    fun remove(key: String) {
        mSharePreference.edit().remove(key).apply()
    }

    /**
     * 获取 SharedPref 中所有数据集合
     *
     * @return 保存的所有数据集合
     */
    fun getAll(): Map<String, *> = mSharePreference.all

}