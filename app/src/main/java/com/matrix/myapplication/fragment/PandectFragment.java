package com.matrix.myapplication.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.model.JsonModel;
import com.matrix.myapplication.utils.MyLog;
import com.matrix.myapplication.utils.OkHolder;
import com.matrix.myapplication.view.CirclePercentBar;
import com.matrix.myapplication.view.RadarImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 总览
 */
public class PandectFragment extends BaseFragment {
    int[] colors = {Color.GREEN, Color.RED, Color.CYAN, Color.BLUE};
    @BindView(R.id.title_center)
    TextView mTitleCenter;
    @BindView(R.id.title_left)
    TextView mTitleLeft;
    @BindView(R.id.title_right)
    TextView mTitleRight;
    @BindView(R.id.circle_bar)
    CirclePercentBar mCircleview;
    Unbinder unbinder;
    @BindView(R.id.pandect_list)
    ListView mPandectList;
    @BindView(R.id.pandect_list_cpu)
    ListView mPandectListCpu;
    @BindView(R.id.pandect_list_ds)
    ListView mPandectListDs;
    @BindView(R.id.image_alarm)
    ImageView mImageAlarm;
    @BindView(R.id.image_more1)
    ImageView mImageMore1;
    @BindView(R.id.image_more2)
    ImageView mImageMore2;
    @BindView(R.id.load_more1)
    LinearLayout mLoadMore1;
    @BindView(R.id.load_more2)
    LinearLayout mLoadMore2;
    @BindView(R.id.radar)
    RelativeLayout mRadar;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.lin_alarm)
    LinearLayout mLinAlarm;
    @BindView(R.id.lin_deail)
    LinearLayout mLinDeail;
    @BindView(R.id.health1)
    LinearLayout mHealth1;
    @BindView(R.id.health2)
    LinearLayout mHealth2;
    @BindView(R.id.health3)
    LinearLayout mHealth3;
    @BindView(R.id.health4)
    LinearLayout mHealth4;
    @BindView(R.id.text1)
    TextView mText1;
    @BindView(R.id.text2)
    TextView mText2;
    @BindView(R.id.text3)
    TextView mText3;
    @BindView(R.id.text4)
    TextView mText4;
    @BindView(R.id.nulltext1)
    TextView mNulltext1;
    @BindView(R.id.nulltext2)
    TextView mNulltext2;
    private RadarImageView mImageView;
    private String[] titles = {"预警", "网络中断", "报警", "数据获取异常"};
    private float[] values = {8, 6, 3, 10}; // 各维度分值

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pandect, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        mScrollView.smoothScrollTo(0, 0);
        mImageView = RadarImageView.newInstance(getContext(), titles, values, 16);
        mRadar.addView(mImageView);
        mImageView.changeValues(new float[]{8, 6, 3, 10});

        mTitleCenter.setText("首页");
        mTitleRight.setVisibility(View.GONE);
        mTitleLeft.setVisibility(View.GONE);
        initGetProgressBar();
        initList();
    }

    private void initGetProgressBar() {
        mCircleview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(getContext(), HealthyActivity.class));
            }
        });
        String url = Config.mUrl + "/mobileindex!Init";
        MyLog.d(url);
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mCircleview.setPercentData(90.00f, new DecelerateInterpolator());
            }

            @Override
            public void onResponse(String response, int id) {
//                JsonModel jsonModel = new Gson().fromJson(response, JsonModel.class);
//                if (jsonModel != null) {
//                    mCircleview.setPercentData(StringUtils.toFloat(jsonModel.getTotalhealth()), new DecelerateInterpolator());
//                    initOtherFunction(jsonModel);
//                } else {
//                    mCircleview.setPercentData(90.00f, new DecelerateInterpolator());
//                }
            }
        });
    }

    private void initOtherFunction(JsonModel jsonModel) {
        List<Object[]> statusnumlist = jsonModel.getStatusnumlist();
        double all = 0;
        double alarm = 0;
        double warm = 0;
        double other = 0;
        if (statusnumlist != null) {
            for (int i = 0; i < statusnumlist.size(); i++) {
                switch ((String) statusnumlist.get(i)[0]) {
                    case "2":
                        warm = (double) statusnumlist.get(i)[1];
                        MyLog.d((double) statusnumlist.get(i)[1] + "");
                        break;
                    case "3":
                        alarm = (double) statusnumlist.get(i)[1];
                        MyLog.d((double) statusnumlist.get(i)[1] + "");
                        break;
                    case "4":
                        other = (double) statusnumlist.get(i)[1];
                        MyLog.d((double) statusnumlist.get(i)[1] + "");
                    case "5":
                        all = (double) statusnumlist.get(i)[1];
                        MyLog.d((double) statusnumlist.get(i)[1] + "");
                }
            }
        }
        mText1.setText((int) alarm + "");
        mText2.setText((int) warm + "");
        mText3.setText((int) other + "");
        mText4.setText((int) all + "");
    }

    private void initGetTypeCPU() {
        String url = Config.mUrl + "/mobileindex!CMSRank";// ?type=M_PER&rank=10 参数rank代表获取条目数量
        Map<String, String> map = new HashMap<>();
        map.put("type", "C_PER");
        map.put("rank", "5");
        OkHolder.post(url, map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });
    }

    private void initGetTypeMOM() {
        String url = Config.mUrl + "/mobileindex!CMSRank";// ?type=M_PER&rank=10 参数rank代表获取条目数量
        Map<String, String> map = new HashMap<>();
        map.put("type", "M_PER");
        map.put("rank", "5");
        OkHolder.post(url, map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //CheckViewNull.deailViewNull(mListMom, mPandectListDs, mNulltext2);
            }

            @Override
            public void onResponse(String response, int id) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    public void initList() {
        String[] string = {"服务器", "路由器", "网络设备", "数据库", "中间件", "应用和其他"};
//        String[] string = {"操作系统", "数据库", "中间件", "网络设备", "应用监控", "动力环境"};


        initGetTypeCPU();


    }

    @OnClick({R.id.image_more1, R.id.load_more1, R.id.image_more2,
            R.id.load_more2,
            R.id.lin_alarm, R.id.lin_deail, R.id.health1, R.id.health2,
            R.id.health3, R.id.health4, R.id.nulltext1, R.id.nulltext2})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.image_more1:
            case R.id.load_more1:

                intent.putExtra(Config.mPandectListTitle, "CPU排行（当日）");
                startActivity(intent);
                break;

            case R.id.nulltext1:
                initGetTypeCPU();
                break;
            case R.id.nulltext2:
                initGetTypeMOM();
                break;
        }
    }

    @Override//TODO 被动刷新
    public void reShowExecute() {
        super.reShowExecute();
        initGetProgressBar();
        initGetTypeCPU();
        initGetTypeMOM();
    }

}
