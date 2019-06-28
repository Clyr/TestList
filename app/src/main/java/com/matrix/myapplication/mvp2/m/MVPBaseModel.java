package com.matrix.myapplication.mvp2.m;

import com.google.gson.GsonBuilder;
import com.matrix.myapplication.model.TestData;
import com.matrix.myapplication.model.User;
import com.matrix.myapplication.mvp2.MVPBasePM;
import com.matrix.myapplication.mvp2.MVPListener;
import com.matrix.myapplication.retrofit.ORService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by M S I of clyr on 2019/6/27.
 */
public abstract class MVPBaseModel extends MVPBasePM {
    public MVPBaseModel() {
    }

    Retrofit retrofit;

    public void getUser(String name, String password, final MVPListener mvpListener) {
        //做一些网络请求
        initHttp();
        DisposableObserver<TestData<User>> observer = new DisposableObserver<TestData<User>>() {
            @Override
            public void onNext(TestData<User> value) {
                //回调给presenter
                mvpListener.onSuccess(value);
            }

            @Override
            public void onError(Throwable e) {
                mvpListener.onError();
            }

            @Override
            public void onComplete() {

            }
        };
        retrofit.create(ORService.class)
                .reLogin(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        register(observer);


    }

    public void initHttp() {
        retrofit = new Retrofit.Builder().baseUrl("http://wnd.agri114.cn/wndms/")
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        // 有日志打印的 retrofit
        //retrofit = ORHelper.getHttpApi();
    }
}
