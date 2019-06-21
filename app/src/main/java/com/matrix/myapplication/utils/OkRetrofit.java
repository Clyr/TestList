package com.matrix.myapplication.utils;

import android.util.Log;

import com.matrix.myapplication.retrofit.HttpLog;
import com.matrix.myapplication.retrofit.ORService;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by M S I of clyr on 2019/6/21.
 */
public class OkRetrofit {
    public static Call<ResponseBody> getCall(@NotNull String mUrl, String url) {
        ORService orService = getOrService(mUrl);
        return orService.get(url);
    }

    public static Call<ResponseBody> getCall(@NotNull String mUrl, String url, @NotNull Map<String, String> map) {
        ORService orService = getOrService(mUrl);
        return orService.get(url, map);
    }

    public static Call<ResponseBody> postCall(@NotNull String mUrl, String url) {
        ORService orService = getOrService(mUrl);
        return orService.post(url);
    }

    public static Call<ResponseBody> postCall(@NotNull String mUrl, String url, @NotNull Map<String, String> map) {
        ORService orService = getOrService(mUrl);
        return orService.post(url, map);
    }

    private static ORService getOrService(@NotNull String mUrl) {
        //打印retrofit信息部分 //打印retrofit日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                Log.e("RetrofitLog", "retrofitBack = " + message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .client(HttpLog.getLogClient())//此client是为了打印信息
                .build();
        return retrofit.create(ORService.class);
    }

    //TODO 仅作为例子
    public static Call<ResponseBody> intactCall(@NotNull String mUrl, String url) {
        //打印retrofit信息部分 //打印retrofit日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                Log.e("RetrofitLog", "retrofitBack = " + message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .client(HttpLog.getLogClient())//此client是为了打印信息
                .build();
        ORService orService = retrofit.create(ORService.class);
        return orService.get(url);
    }
}
