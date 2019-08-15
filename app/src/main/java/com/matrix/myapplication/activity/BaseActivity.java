package com.matrix.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;

import com.matrix.myapplication.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends SwipeBackActivity {
    private CompositeSubscription mSubscriptions;
    private SwipeBackLayout mSwipeBackLayout;
    Intent mIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntent = getIntent();
//        setContentView(R.layout.activity_base);
        initSwipeBcck();
        EventBus.getDefault().register(this);
        addSubscription(subscribeEvents());
    }
    public void startAct(Class<?> tClass) {
        startActivity(new Intent(this, tClass));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {

//        EventBus.getDefault().post(new MessageEvent(""));
//        EventBus.getDefault().post(new MessageEvent(Config.mRefreash));
    }
    /*@Override
    public void Event(MessageEvent messageEvent) {
        super.Event(messageEvent);
        switch (messageEvent.getMessage()){
            case Config.mRefreash:
                break;
        }
    }*/

    protected void addSubscription(Subscription subscription) {
        if (subscription == null) return;
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
        mSubscriptions.add(subscription);
    }

    protected Subscription subscribeEvents() {
        return null;
    }
}
