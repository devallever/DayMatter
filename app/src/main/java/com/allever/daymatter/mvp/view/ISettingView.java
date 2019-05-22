package com.allever.daymatter.mvp.view;

import com.allever.daymatter.data.Config;

/**
 * Created by Allever on 18/6/1.
 */

public interface ISettingView {
    void setCurrentRemindSwitch(boolean value);
    void setCurrentRemindTime(String time);

    void setBeforeRemindSwitch(boolean value);
    void setBeforeRemindTime(String time);

    void returnRemindConfig(Config config);
}
