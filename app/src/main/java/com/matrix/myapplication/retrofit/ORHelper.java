package com.matrix.myapplication.retrofit;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by M S I of clyr on 2019/5/20.
 */
public class ORHelper {
    public static Retrofit.Builder retrofit(){
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit.Builder retrofit = new Retrofit.Builder()
                .client(client);
        return retrofit;
    }
    /*
     **打印retrofit信息部分
     */
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
            //打印retrofit日志
            Log.e("RetrofitLog", "retrofitBack = " + message)
    );
}
