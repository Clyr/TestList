package com.douyu.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.douyu.bean.RoomInfo;
import com.douyu.utils.LoadImage;
import com.matrix.myapplication.R;

import java.util.List;

/**
 * Created by l on 17-1-3.
 */

public class RoomInfoAdapter extends BaseQuickAdapter<RoomInfo> {
    private Context mCtx;
    public List<RoomInfo> mData;

    public RoomInfoAdapter(Context context, List<RoomInfo> datas) {
        super(context, R.layout.room_info_item, datas);
        this.mCtx = context;
        this.mData = datas;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, RoomInfo roomInfo) {
        baseViewHolder.setText(R.id.tv_nickname, roomInfo.nickname.trim())
                .setText(R.id.tv_online, String.valueOf(roomInfo.online))
                .setText(R.id.tv_room_name, roomInfo.roomName);
        ImageView imageView = baseViewHolder.getView(R.id.iv_room);
        LoadImage.loadNormalImage(mCtx, roomInfo.roomSrc, imageView);
    }


}
