package com.matrix.myapplication.mvp2;

/**
 * Created by zhoufazhan on 2017/7/26.
 */

public interface MVPListener<T> {
    void onSuccess(T login);

    void onError();
}
