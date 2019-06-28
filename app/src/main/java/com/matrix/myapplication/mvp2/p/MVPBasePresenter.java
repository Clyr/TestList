package com.matrix.myapplication.mvp2.p;

import android.content.Context;

import com.matrix.myapplication.mvp2.MVPBasePM;
import com.matrix.myapplication.mvp2.v.MVPBaseView;

/**
 * Created by M S I of clyr on 2019/6/27.
 */
public abstract class MVPBasePresenter<V extends MVPBaseView> extends MVPBasePM {
    V mView;
    Context mContent;

    public MVPBasePresenter(V mView, Context mContent) {
        this.mView = mView;
        this.mContent = mContent;
        init();
    }

    public void init() {
        mView.initView();
    }

}
