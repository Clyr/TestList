package com.matrix.myapplication.utils;

import android.os.Environment;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by clyr on 2018/1/10 0010.
 * 便于打印Log
 */

public class OkHolder {
    public static GetBuilder get(String url, Map<String, String> map) {
        sprintLog(url, map);
        return OkHttpUtils.get().url(url).params(map);
    }

    public static PostFormBuilder post(String url, Map<String, String> map) {
        sprintLog(url, map);
        return OkHttpUtils.post().url(url).params(map);
    }

    /**
     * 打印网络请求Log
     */
    public static void sprintLog(String url, Map<String, String> params) {
        if (url != null && params != null) {
            url += "?";
            for (Map.Entry<String, String> en : params.entrySet()) {
                url += en.getKey() + "=" + en.getValue() + "&";
            }
            url = url.substring(0, url.length() - 1);
            Log.d("OkHttpUtils", url);
        }
    }

    public static void getFile(String url, String path, String name) {
        if (path == null) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        if (name == null) {
            name = String.valueOf(System.currentTimeMillis());//没有格式
        }
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(path, name) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(File response, int id) {

            }
        });
    }

//    public static void getBitmapNet(String url, final ImageView imageView) {
//        OkHttpUtils.get().url(url).build().execute(new BitmapCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                SDToast.showToast("图片加载失败");
//            }
//            @Override
//            public void onResponse(Bitmap response, int id) {
//                imageView.setImageBitmap(response);
//            }
//        });
//
//    }
//    public static void setBackgroundNet(String url, final View view) {
//        OkHttpUtils.get().url(url).build().execute(new BitmapCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                SDToast.showToast("图片加载失败");
//            }
//            @Override
//            public void onResponse(Bitmap response, int id) {
//                view.setBackground(BitmapUtils.bitmapToDrawable(response));
//            }
//        });
//    }
}
