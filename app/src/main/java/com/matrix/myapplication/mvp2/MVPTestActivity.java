package com.matrix.myapplication.mvp2;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.widget.AppCompatButton;

import com.matrix.myapplication.R;
import com.matrix.myapplication.model.TestData;
import com.matrix.myapplication.model.User;
import com.matrix.myapplication.mvp2.m.TLoginModel;
import com.matrix.myapplication.mvp2.p.TLoginPresenter;
import com.matrix.myapplication.mvp2.v.TLoginView;
import com.matrix.myapplication.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MVPTestActivity extends MVPBaseActivty<TLoginPresenter, TLoginModel> implements TLoginView {

    @BindView(R.id.user)
    AutoCompleteTextView user;
    @BindView(R.id.pwd)
    AutoCompleteTextView pwd;
    @BindView(R.id.login)
    AppCompatButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_mvptest;
    }

    @Override
    public void init() {
        mModel = new TLoginModel();
        mPresenter = new TLoginPresenter(this,this);

    }

    @OnClick(R.id.login)
    public void onViewClicked() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void showToast(TestData<User> value) {
        ToastUtils.showShort(value.getState());
    }

    @Override
    public void showError() {
        ToastUtils.showShort("Error");
    }
}
