package com.matrix.myapplication.utils;

/**
 * Created by clyr on 2018/3/13 0013.
 * EventBus信息传递类
 */

public class MessageEvent {
    private String message;
    private String messageTag;
    public MessageEvent(String message){
        this.message=message;
    }
    public MessageEvent(String message, String messageTag){
        this.message=message;
        this.messageTag=messageTag;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
