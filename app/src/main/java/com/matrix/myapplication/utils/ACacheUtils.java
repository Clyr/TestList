package com.matrix.myapplication.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by clyr on 2018/2/8 0008.
 */

public class ACacheUtils {
    private static ACache mCache;
    public static void saveString(Context context,String name,String str){
        if(mCache==null){
            mCache = ACache.get(context);
        }
        mCache.put(name,str);
    }
    public static String getString(Context context,String name){
        if(mCache==null){
            mCache = ACache.get(context);
        }
        return  mCache.getAsString(name);
    }
    public static void clear(Context context,String name){
        if(mCache==null){
            mCache = ACache.get(context);
        }
        mCache.remove(name);
    }
    public static void saveJsonString(Context context,String name,List list){
        String string = JSONObject.toJSONString(list);
        saveString(context,name,string);
    }
}
