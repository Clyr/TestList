package com.douyu.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.douyu.mvp.presenter.SplahActivityPresenter;
import com.douyu.mvp.presenter.impl.ISplahActivityPresenter;
import com.douyu.mvp.view.activity.impl.SplashActivityView;


/**
 * Created by l on 16-12-30.
 * 欢迎界面
 */

public class SplashActivity extends AppCompatActivity implements SplashActivityView {

    private ISplahActivityPresenter mSplahActivityPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplahActivityPresenter = new SplahActivityPresenter(this);
        mSplahActivityPresenter.setDelay();
    }


    /**
     * 进入app
     */
    @Override
    public void enterApp() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册
        mSplahActivityPresenter.unRegister();
    }
}
