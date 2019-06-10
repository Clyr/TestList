package com.matrix.myapplication.interfaceclass;

import com.matrix.myapplication.interfaceModel.HandlerCall;
import com.matrix.myapplication.model.UpDateModel;

/**
 * Created by M S I of clyr on 2019/6/10.
 */
public class HandlerCallHelper {
    HandlerCall handlerCall;
    ;
    public HandlerCallHelper(HandlerCall handlerCall) {
        this.handlerCall = handlerCall;
    }
    public void sethandler(){
        handlerCall.handler();
    }
    public void setModel(UpDateModel model){
        handlerCall.setModle(model);
    }
}
