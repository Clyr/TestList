package com.douyu.mvp.view.fragment.impl;


import com.douyu.bean.RoomInfo;

import java.util.List;

/**
 * Created by l on 17-1-3.
 */
public interface RecommendFragmentView {
    void showData(List<RoomInfo> datas);

    void initView();

    void setRefresh();

    void setLoadmore();

    void swipeToLoadComplete();

    void startPlay(String roomName, int roomId);
}
