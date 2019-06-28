package com.matrix.myapplication.mvp2.v;

import com.matrix.myapplication.model.TestData;
import com.matrix.myapplication.model.User;

/**
 * Created by M S I of clyr on 2019/6/27.
 */
public interface TLoginView extends MVPBaseView {

    void initView();

    void showToast(TestData<User> value);

    void showError();
}
