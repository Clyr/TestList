package com.matrix.myapplication.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/11/8 0008.
 */

public class AndroidJavaScript {
    Context mContext;


    public AndroidJavaScript(Context content) {
        this.mContext = content;
    }
    public void startFunction() {
        Toast.makeText(mContext, "js调用了java函数", Toast.LENGTH_SHORT).show();

    }

    public void startFunction(final String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();

    }
}
