package com.matrix.myapplication.mvp2.p;

import android.content.Context;

import com.matrix.myapplication.model.TestData;
import com.matrix.myapplication.model.User;
import com.matrix.myapplication.mvp2.MVPListener;
import com.matrix.myapplication.mvp2.m.TLoginModel;
import com.matrix.myapplication.mvp2.v.TLoginView;

/**
 * Created by M S I of clyr on 2019/6/27.
 */
public class TLoginPresenter extends MVPBasePresenter {
    TLoginView mView;
    Context mContent;
    TLoginModel model;

    public TLoginPresenter(TLoginView mView, Context mContent) {
        super(mView, mContent);
        this.mContent = mContent;
        this.mView = mView;
    }

    public void login(String name, String pwd, MVPListener mvpListener) {
        TLoginModel model = new TLoginModel();
        model.getUser(name, pwd, new MVPListener() {
            @Override
            public void onSuccess(Object login) {
                if (login instanceof TestData)
                    mView.showToast((TestData<User>) login);
            }

            @Override
            public void onError() {
                mView.showError();
            }
        });
    }
}
