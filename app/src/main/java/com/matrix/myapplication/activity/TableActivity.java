package com.matrix.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matrix.myapplication.R;
import com.matrix.myapplication.adapter.ScrollablePanelAdapter;
import com.matrix.myapplication.model.DateInfo;
import com.matrix.myapplication.model.JsonModle;
import com.matrix.myapplication.model.OrderInfo;
import com.matrix.myapplication.model.RoomInfo;
import com.matrix.myapplication.view.scrollablepanel.ScrollablePanel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class TableActivity extends BaseActivity {

    @BindView(R.id.scrollablepanel)
    ScrollablePanel mScrollablepanel;
    @BindView(R.id.url1)
    Button mUrl1;
    @BindView(R.id.url2)
    Button mUrl2;
    @BindView(R.id.url3)
    Button mUrl3;
    @BindView(R.id.url4)
    Button mUrl4;
    @BindView(R.id.url5)
    Button mUrl5;
    @BindView(R.id.linearLayout)
    LinearLayout mLinearLayout;
    private ScrollablePanelAdapter mScrollablePanelAdapter;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mScrollablePanelAdapter = new ScrollablePanelAdapter();
        mScrollablepanel.setPanelAdapter(mScrollablePanelAdapter);
        mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000586";
//        mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000708";
        initGetData();
    }

    private void initGetData() {
        OkHttpUtils.post().url(mUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(TableActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                JsonModle jsonModle = new Gson().fromJson(response, JsonModle.class);
                if (jsonModle != null && jsonModle.getDatalist() != null)
                    setData(jsonModle);
            }
        });
    }

    private void setData(JsonModle jsonModle) {
        int size = jsonModle.getDatalist().size();
        List<DateInfo> dateInfoList = new ArrayList<>();//horizontal
        List<RoomInfo> roomInfoList = new ArrayList<>();// vertical
        //设置颜色
        List<List<OrderInfo>> ordersList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomName("" + i);
            roomInfoList.add(roomInfo);
            TreeMap<String, String[]> stringMap = jsonModle.getDatalist().get(i);
            List<OrderInfo> orderInfoList = new ArrayList<>();
            for (Map.Entry<String, String[]> entry : stringMap.entrySet()) {
                if (i <= 0) {
                    /*RoomInfo roomInfo = new RoomInfo();
                    roomInfo.setRoomName(entry.getKey());
                    roomInfoList.add(roomInfo);*/
                    DateInfo dateInfo = new DateInfo();
                    dateInfo.setDate(entry.getKey());
                    dateInfoList.add(dateInfo);
                }
                String[] value = entry.getValue();
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setGuestName(value[0]);
                orderInfo.setStatus(getStatus(value[1]));
                orderInfoList.add(orderInfo);

            }
            ordersList.add(orderInfoList);
        }
        //horizontal
        mScrollablePanelAdapter.setHoriInfoList(dateInfoList);
        // vertical
        mScrollablePanelAdapter.setVertInfoList(roomInfoList);

        mScrollablePanelAdapter.setOrdersList(ordersList);
        mScrollablepanel.notifyDataSetChanged();
        for (int i = 0; i < ordersList.size(); i++) {
            List<OrderInfo> orderInfos = ordersList.get(i);
            for (int j = 0; j < orderInfos.size(); j++) {
                Log.d("数据打印-------------", orderInfos.get(j).getGuestName());
            }
        }
    }

    private OrderInfo.Status getStatus(int i) {
        switch (i % 3) {
            case 0:
                return OrderInfo.Status.ALARM;
            case 1:
                return OrderInfo.Status.NORMAL;
            case 2:
                return OrderInfo.Status.WARN;
            default:
                return OrderInfo.Status.ALARM;

        }

    }

    private OrderInfo.Status getStatus(String i) {
        switch (i) {
            case "0":
                return OrderInfo.Status.ALARM;
            case "1":
                return OrderInfo.Status.NORMAL;
            case "2":
                return OrderInfo.Status.WARN;
            default:
                return OrderInfo.Status.DANGER;
        }
    }


    @OnClick({R.id.url1, R.id.url2, R.id.url3, R.id.url4, R.id.url5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.url1:
                mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000625";
                break;
            case R.id.url2:
                mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000626";
                break;
            case R.id.url3:
                mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000640";
                break;
            case R.id.url4:
                mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000655";
                break;
            case R.id.url5:
//                mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000586";
                mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000708";
                break;

        }
        initGetData();
    }
}
