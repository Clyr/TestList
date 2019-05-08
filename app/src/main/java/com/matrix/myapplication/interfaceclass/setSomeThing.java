package com.matrix.myapplication.interfaceclass;

/**
 * Created by M S I of clyr on 2019/3/28.
 */

public class setSomeThing {
    Setting setting;

    public setSomeThing(Setting setting) {
        this.setting = setting;
        setting.setToast("");
    }

    public void startSet(Setting sets, String str){
        setting = sets;
        setting.setToast(str);
    }
}
