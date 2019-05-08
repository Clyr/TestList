package com.matrix.myapplication.utils;

import android.util.Log;

import com.matrix.myapplication.BuildConfig;

/**
 * Created by M S I of clyr on 2018/9/20.
 */

public final class MyLog {
    private static final String TAG = "------MyLog------";
    //开关
    private final static boolean flag1 = true;//true 测试   false  上线
    private final static boolean flag = BuildConfig.DEBUG;//true 测试   false  上线

    public static void v(String tag,String msg){
        if(flag){
            Log.v(tag, msg);
        }
    }
    public static void d(String tag,String msg){
        if(flag){
            Log.d(tag, msg);
        }
    }
    public static void i(String tag,String msg){
        if(flag){
            Log.i(tag, msg);
        }
    }
    public static void w(String tag,String msg){
        if(flag){
            Log.w(tag, msg);
        }
    }
    public static void e(String tag,String msg){
        if(flag){
            Log.e(tag, msg);
        }
    }
    //------下面的是单参数(当你迷茫的时候懒得写的时候)------//
    public static void v(String msg){
        v(TAG,msg);
    }
    public static void d(String msg){
        d(TAG,msg);
    }
    public static void i(String msg){
        i(TAG,msg);
    }
    public static void w(String msg){
        w(TAG,msg);
    }
    public static void e(String msg){
        e(TAG,msg);
    }
}
