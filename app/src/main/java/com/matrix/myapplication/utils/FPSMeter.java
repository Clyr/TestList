package com.matrix.myapplication.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by clyr on 2018/4/12 0012.
 */

public class FPSMeter {

    //获得格式化类对象
    DecimalFormat mdt;
    private int mFrameCount;
    private long mLastTime;
    private int mFps;

    public FPSMeter() {
        mdt = (DecimalFormat) NumberFormat.getInstance();
        mdt.applyPattern("0.00");
    }

    /***
     * 因为外面调用这个时候会执行循环。
     */
    public void mesureFps() {
        ++mFrameCount;
        if (mFrameCount == 30) {
            //毫微秒
            long now = System.nanoTime();
            long diff = now - mLastTime;
            mFps = (int) (1e9 * 30 / diff);
            mFrameCount = 0;
            mLastTime = now;
        }
    }

    public String getFps() {
        return mdt.format(mFps);
    }
}
