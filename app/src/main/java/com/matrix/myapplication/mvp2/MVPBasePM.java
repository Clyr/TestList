package com.matrix.myapplication.mvp2;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by M S I of clyr on 2019/6/27.
 */
public abstract class MVPBasePM {
    CompositeDisposable mDisposable = new CompositeDisposable();

    public void unSubscribe() {
        mDisposable.clear();
    }


    public void register(Disposable disposable) {
        mDisposable.add(disposable);
    }
}
