package com.matrix.myapplication.utils;

import android.widget.Toast;

import com.matrix.myapplication.App;


/**
 * Created by clyr on 2018/3/12 0012.
 * 弹出Toast提示的快捷方式 不需要输入context 和 时长
 */

public class ToastUtils {
    String mTag = "ToastUtils";
    public static void showLong(String string){
        Toast.makeText(App.getApplication(), string, Toast.LENGTH_LONG).show();
    }
    public static void showLong(int str){
        Toast.makeText(App.getApplication(), str, Toast.LENGTH_LONG).show();
    }
    public static void showShort(String string){
        Toast.makeText(App.getApplication(), string, Toast.LENGTH_SHORT).show();
    }
    public static void showShort(int str){
        Toast.makeText(App.getApplication(), str, Toast.LENGTH_SHORT).show();
    }
}
