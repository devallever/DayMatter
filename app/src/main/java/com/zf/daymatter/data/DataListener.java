package com.zf.daymatter.data;

/**
 * Created by Allever on 18/6/4.
 */

public interface DataListener<T>{
    void onSuccess(T data);
    void onFail(String msg);
}
