package com.zf.daymatter.mvp.view;

import com.zf.daymatter.data.Config;

/**
 * Created by Allever on 18/6/1.
 */

public interface IRemindView {
    void setCurrentRemindSwitch(boolean value);
    void setCurrentRemindTime(String time);

    void setBeforeRemindSwitch(boolean value);
    void setBeforeRemindTime(String time);

    void returnRemindConfig(Config config);
}
