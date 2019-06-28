package com.matrix.myapplication.newactivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;

import com.matrix.myapplication.R;
import com.matrix.myapplication.activity.BaseActivity;
import com.matrix.myapplication.activity.Main7Activity;
import com.matrix.myapplication.utils.MessageEvent;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.utils.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RxBusActivity extends BaseActivity {

    @BindView(R.id.rxbus)
    AppCompatButton rxbus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rxbus)
    public void onViewClicked() {
//        RxBus.getInstance().post(new MessageEvent("11111"));
        startAct(Main7Activity.class);
    }
    // RXBus Events

    @Override
    protected Subscription subscribeEvents() {
        return RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof MessageEvent) {
                            ToastUtils.showShort(((MessageEvent) o).getMessage());
                        }
                    }
                })
                .subscribe(RxBus.defaultSubscriber());
    }
}
