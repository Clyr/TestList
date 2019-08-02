package com.douyu.mvp.view.fragment.impl;


import com.douyu.bean.RoomInfo;
import com.douyu.db.bean.Person;

import java.util.List;

/**
 * Created by l on 17-1-15.
 */
public interface AttentionFragmentView {

    void showData(List<Person> datas);

    void initView();

    void setRefresh();

    void setLoadmore();

    void swipeToLoadComplete();

    void startPlay(String roomName, int roomId);

    void getAllRoomInfo(List<RoomInfo> roomInfos);
}
