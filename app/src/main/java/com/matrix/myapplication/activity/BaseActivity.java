package com.matrix.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class BaseActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;
    Intent mIntent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntent = getIntent();
//        setContentView(R.layout.activity_base);
        initSwipeBcck();

    }


    private void initSwipeBcck() {
        //是否允许滑动返回
        setSwipeBackEnable(true);
        //滑动并关闭activity
//        scrollToFinishActivity();
        //获得SwipeBackLayout对象
//        getSwipeBackLayout();
//        mSwipeBackLayout = getSwipeBackLayout();
//        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);//设定从哪个方向可以滑动
//        mSwipeBackLayout.setEdgeSize(200);//来设置滑动触发的范围等等
//        mSwipeBackLayout.setScrimColor(R.color.colorAccent);//来设置滑动返回的背景色


    }
}
