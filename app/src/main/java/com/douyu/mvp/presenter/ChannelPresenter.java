package com.douyu.mvp.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.douyu.bean.AllSubChannels;
import com.douyu.bean.SubChannelInfo;
import com.douyu.mvp.model.DouYuModel;
import com.douyu.mvp.model.impl.IDouYuModel;
import com.douyu.mvp.presenter.impl.IChannelPresenter;
import com.douyu.mvp.view.fragment.impl.ChannelsFragmentView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by l on 16-12-30.
 */

public class ChannelPresenter implements IChannelPresenter {
    private ChannelsFragmentView view;
    private IDouYuModel model;

    public ChannelPresenter(ChannelsFragmentView view) {
        this.view = view;
        model = new DouYuModel();
    }

    @Override
    public void getAllChannels() {
        model.ChannelsGet()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getAllChannelsSubscriber());
    }

    private Subscriber<String> getAllChannelsSubscriber() {
        Subscriber<String> allChannelsSubscriber;
        allChannelsSubscriber = new Subscriber<String>() {

            @Override
            public void onCompleted() {
                Log.i("RX", "onCompleted");
                //停止刷新
                view.swipeToLoadComplete();
            }

            @Override
            public void onError(Throwable e) {
                Log.i("RX", "error");
                //停止刷新
                view.swipeToLoadComplete();
            }

            @Override
            public void onNext(String s) {
                Log.i("RX-->result: ", s);
                List<SubChannelInfo> subChannelInfos = new ArrayList<>();
                Gson gson = new Gson();
                try {
                    AllSubChannels allSubChannels = gson.fromJson(s, AllSubChannels.class);
                    for (AllSubChannels.Data data : allSubChannels.data) {
                        SubChannelInfo subChannelInfo = new SubChannelInfo();
                        subChannelInfo.tagId = data.tag_id;
                        subChannelInfo.tagName = data.tag_name;
                        subChannelInfo.iconUrl = data.icon_url;
                        subChannelInfos.add(subChannelInfo);
                    }
                    view.showData(subChannelInfos);
                    view.swipeToLoadComplete();
                    unsubscribe();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return allChannelsSubscriber;
    }

}
