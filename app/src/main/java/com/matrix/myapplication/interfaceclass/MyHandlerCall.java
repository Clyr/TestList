package com.matrix.myapplication.interfaceclass;

import com.matrix.myapplication.interfaceModel.HandlerCall;
import com.matrix.myapplication.model.UpDateModel;

/**
 * Created by M S I of clyr on 2019/6/10.
 */
public class MyHandlerCall implements HandlerCall {

    public MyHandlerCall(UpDateModel model) {
        setModle(model);
        handler();
    }

    @Override
    public void handler() {

    }

    @Override
    public void setModle(UpDateModel model) {

    }
}
