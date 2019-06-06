package com.matrix.myapplication;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Created by clyr on 2018/4/2 0002.
 */

public class App extends Application {
    private static App mApp = null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mApp = this;
        /*
            设置具体的证书
            new InputStream[]{getAssets().open("srca.cer")
            new InputStream[]{new Buffer().writeUtf8(CER_12306).inputStream()
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(证书的inputstream, null, null);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager))
            //其他配置
            .build();
            OkHttpUtils.initClient(okHttpClient);
        * */
        //设置 1.可以访问所有https  请求时间延长为15s 这个过程会显示dialog 已经设置可以取消--但是网络访问不会取消（某些单页的网络请求过多）
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        SDKInitializer.initialize(getApplicationContext());
    }

    public static App getApplication() {
        return mApp;
    }
    public static App getContext() {
        return mApp;
    }
}
