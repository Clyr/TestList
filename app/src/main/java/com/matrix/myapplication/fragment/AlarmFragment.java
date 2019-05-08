package com.matrix.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.adapter.MessageGroupFragmentAdapter;
import com.matrix.myapplication.fragment.alarmfragment.ChildAlarmFragment;
import com.matrix.myapplication.fragment.alarmfragment.ChildAllFragment;
import com.matrix.myapplication.fragment.alarmfragment.ChildOtherFragment;
import com.matrix.myapplication.fragment.alarmfragment.ChildWarmFragment;
import com.matrix.myapplication.model.AlarmInfo;
import com.matrix.myapplication.model.JsonModel;
import com.matrix.myapplication.utils.MessageEvent;
import com.matrix.myapplication.utils.MyLog;
import com.matrix.myapplication.utils.OkHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 报警
 */
public class AlarmFragment extends BaseFragment {
    @BindView(R.id.tv_all)
    TextView mTvAll;
    @BindView(R.id.tv_alarm)
    TextView mTvAlarm;
    @BindView(R.id.tv_warn)
    TextView mTvWarn;
    @BindView(R.id.tv_other)
    TextView mTvOther;
    @BindView(R.id.iv_shape_circle)
    ImageView mIvShapeCircle;
    @BindView(R.id.ll_title)
    LinearLayout mLlTitle;
    @BindView(R.id.viewpager_home)
    ViewPager mViewpagerHome;
    @BindView(R.id.jrxz)
    LinearLayout mJrxz;
    @BindView(R.id.zzcl)
    LinearLayout mZzcl;
    @BindView(R.id.zwcl)
    LinearLayout mZwcl;
    private List<Fragment> list = new ArrayList<Fragment>();
    private MessageGroupFragmentAdapter adapter;
    Intent mIntent = new Intent();

    private int offset = 0;//偏移量
    private int currentIndex = 0;

    @BindView(R.id.title_center)
    TextView mTitleCenter;
    @BindView(R.id.title_left)
    TextView mTitleLeft;
    @BindView(R.id.title_right)
    TextView mTitleRight;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        mTitleCenter.setText("报警");
        mTitleRight.setVisibility(View.GONE);
        mTitleLeft.setVisibility(View.GONE);
        ChildAllFragment childAllFragment = new ChildAllFragment();
        ChildAlarmFragment childAlarmFragment = new ChildAlarmFragment();
        ChildWarmFragment childWarmFragment = new ChildWarmFragment();
        ChildOtherFragment childOtherFragment = new ChildOtherFragment();
        list.add(childAllFragment);
        list.add(childAlarmFragment);
        list.add(childWarmFragment);
        list.add(childOtherFragment);

        adapter = new MessageGroupFragmentAdapter(getChildFragmentManager(), list);
        mViewpagerHome.setAdapter(adapter);
        mViewpagerHome.setOffscreenPageLimit(3);
        mViewpagerHome.setCurrentItem(0);
//        mViewpagerHome.setOnPageChangeListener(pageChangeListener);
        mViewpagerHome.addOnPageChangeListener(pageChangeListener);
        mTvAll.setSelected(true);//默认选中
        initCursorPosition();
        //添加导航栏数量 http to request * 4
        initNavigationTextSet();
//        initNavigationTextSet2();
    }

    private void initNavigationTextSet2() {
        String url = Config.mUrl + "/mobileindex!Init";
        MyLog.d(url);
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.d("all", "数据获取失败");
            }

            @Override
            public void onResponse(String response, int id) {
                JsonModel alarmInfo = new Gson().fromJson(response, JsonModel.class);
                if (alarmInfo != null) {
                    if (alarmInfo != null) {
//                        mTvAll.setText("全部(" + alarmInfo.getAlarmlist().size() + ")");
                        initOtherFunction(alarmInfo);
                    } else {
                        MyLog.d("all", "alarmInfo==null");
                    }
                }
            }
        });
    }

    private void initNavigationTextSet() {
        String url = Config.mUrl + "/mobilealarm!AlarmIndex";
        Map<String, String> map = new HashMap<>();
        map.put("status", "-1");
        /** ALL **/
        OkHolder.post(url, map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.d("all", "数据获取失败");
            }

            @Override
            public void onResponse(String response, int id) {
                AlarmInfo alarmInfo = new Gson().fromJson(response, AlarmInfo.class);
                if (alarmInfo != null) {
                    if (alarmInfo.getAlarmlist() != null) {
                        mTvAll.setText("全部(" + alarmInfo.getAlarmlist().size() + ")");
                        MyLog.d("AlarmFragment.class", alarmInfo.getAlarmlist().size() + "");
                    } else {
                        MyLog.d("all", "alarmInfo.getAlarmlist()==null");
                    }
//                    initOtherFunction(alarmInfo);
                } else {
                    MyLog.d("all", "alarmInfo==null");
                }
            }
        });

        Map<String, String> map2 = new HashMap<>();
        map2.put("status", "2");
        /** 预警 **/
        OkHolder.post(url, map2).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.d("all", "数据获取失败");
            }

            @Override
            public void onResponse(String response, int id) {
                AlarmInfo alarmInfo = new Gson().fromJson(response, AlarmInfo.class);
                if (alarmInfo != null) {
                    if (alarmInfo.getAlarmlist() != null) {
                        mTvWarn.setText("预警(" + alarmInfo.getAlarmlist().size() + ")");
                        MyLog.d("AlarmFragment.class", alarmInfo.getAlarmlist().size() + "");
                    } else {
                        MyLog.d("all", "alarmInfo.getAlarmlist()==null");
                    }
                } else {
                    MyLog.d("all", "alarmInfo==null");
                }
            }
        });
        Map<String, String> map3 = new HashMap<>();
        map3.put("status", "3");
        /** 报警 **/
        OkHolder.post(url, map3).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.d("all", "数据获取失败");
            }

            @Override
            public void onResponse(String response, int id) {
                AlarmInfo alarmInfo = new Gson().fromJson(response, AlarmInfo.class);
                if (alarmInfo != null) {
                    if (alarmInfo.getAlarmlist() != null) {
                        mTvAlarm.setText("报警(" + alarmInfo.getAlarmlist().size() + ")");
                        MyLog.d("AlarmFragment.class", alarmInfo.getAlarmlist().size() + "");
                    } else {
                        MyLog.d("all", "alarmInfo.getAlarmlist()==null");
                    }
                } else {
                    MyLog.d("all", "alarmInfo==null");
                }
            }
        });
        Map<String, String> map4 = new HashMap<>();
        map4.put("status", "4,5");
        /** 其他 **/
        OkHolder.post(url, map4).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.d("all", "数据获取失败");
            }

            @Override
            public void onResponse(String response, int id) {
                AlarmInfo alarmInfo = new Gson().fromJson(response, AlarmInfo.class);
                if (alarmInfo != null) {
                    if (alarmInfo.getAlarmlist() != null) {
                        mTvOther.setText("其他(" + alarmInfo.getAlarmlist().size() + ")");
                        MyLog.d("AlarmFragment.class", alarmInfo.getAlarmlist().size() + "");
                    } else {
                        MyLog.d("all", "alarmInfo.getAlarmlist()==null");
                    }
                } else {
                    MyLog.d("all", "alarmInfo==null");
                }
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
        mTvAll.setText("全部(" + (int) (warm + alarm) + ")" );
        mTvWarn.setText("预警(" +(int) warm + ")");
        mTvAlarm.setText("报警(" +(int) alarm + ")");
        mTvOther.setText("其他(" +(int) (other + all) + ")");
        MyLog.d("-------" + all + "-----------" + warm + "-----------" + alarm + "---------------" + other + "--------------");

    }

    private void initCursorPosition() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
//        Matrix matrix = new Matrix();
//
//        //标题栏 weight设置权重  分成6份   180  对于1080p
//        //(width / 6) * 2  这里表示标题栏两个控件的宽度
//        //(width / 12)  标题栏一个控件的2分之一
//        //50/2 约等于原点宽度的一半
//        matrix.postTranslate((width / 6) * 1, 0);//图片平移
//        mIvShapeCircle.setImageMatrix(matrix);

        //一个控件的宽度  我的手机宽度是1080/5=216 不同的手机宽度会不一样哦
        offset = (width / 4);
        /*TranslateAnimation animation = new TranslateAnimation(0, offset, 0, 0);
        animation.setFillAfter(true);
        animation.setDuration(300);
        mIvShapeCircle.startAnimation(animation);*/
    }

    /**
     * ViewPager滑动监听,用位移动画实现指示器效果
     * <p>
     * TranslateAnimation 强调一个地方,无论你移动了多少次,现在停留在哪里,你的起始位置从未变化过.
     * 例如:我这个demo里面  报警移动到了预警,指示器也停留到了预警下面,但是指示器在屏幕上的位置还是报警下面.
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            EventBus.getDefault().post(new MessageEvent(""+index));
            changeTextColor(index);
            translateAnimation(index);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * 改变标题栏字体颜色
     *
     * @param index
     */
    private void changeTextColor(int index) {
        mTvAll.setSelected(false);
        mTvAlarm.setSelected(false);
        mTvWarn.setSelected(false);
        mTvOther.setSelected(false);

        switch (index) {
            case 0:
                mTvAll.setSelected(true);
                break;
            case 1:
                mTvAlarm.setSelected(true);
                break;
            case 2:
                mTvWarn.setSelected(true);
                break;
            case 3:
                mTvOther.setSelected(true);
                break;
        }
    }

    /**
     * 移动标题栏点点点...
     *
     * @param index
     */
    private void translateAnimation(int index) {
        TranslateAnimation animation = null;
        switch (index) {
            case 0:
                if (currentIndex == 1) {//从报警移动到全部   X坐标向左移动180
                    animation = new TranslateAnimation(1 * offset, 0, 0, 0);
                } else if (currentIndex == 2) {//从预警移动到全部   X坐标向左移动180*2  记住起始x坐标是预警那里
                    animation = new TranslateAnimation(2 * offset, 0, 0, 0);
                } else if (currentIndex == 3) {//从其他移动到全部   X坐标向左移动180*2  记住起始x坐标是其他那里
                    animation = new TranslateAnimation(3 * offset, 0, 0, 0);
                }
                break;
            case 1:
                if (currentIndex == 0) {//从全部移动到报警   X坐标向右移动180
                    animation = new TranslateAnimation(0, offset, 0, 0);
                } else if (currentIndex == 2) {//从预警移动到报警   X坐标向左移动180
                    animation = new TranslateAnimation(2 * offset, offset, 0, 0);
                } else if (currentIndex == 3) {//从预警移动到报警   X坐标向左移动180
                    animation = new TranslateAnimation(3 * offset, offset, 0, 0);
                }
                break;
            case 2:
                if (currentIndex == 0) {//从全部移动到预警   X坐标向右移动180*2  记住起始x坐标是全部那里
                    animation = new TranslateAnimation(0, 2 * offset, 0, 0);
                } else if (currentIndex == 1) {//从报警移动到预警   X坐标向右移动180
                    animation = new TranslateAnimation(offset, 2 * offset, 0, 0);
                } else if (currentIndex == 3) {//从报警移动到预警   X坐标向右移动180
                    animation = new TranslateAnimation(3 * offset, 2 * offset, 0, 0);
                }
                break;
            case 3:
                if (currentIndex == 0) {//从全部移动到预警   X坐标向右移动180*2  记住起始x坐标是全部那里
                    animation = new TranslateAnimation(0, 3 * offset, 0, 0);
                } else if (currentIndex == 1) {//从报警移动到预警   X坐标向右移动180
                    animation = new TranslateAnimation(offset, 3 * offset, 0, 0);
                } else if (currentIndex == 2) {//从报警移动到预警   X坐标向右移动180
                    animation = new TranslateAnimation(2 * offset, 3 * offset, 0, 0);
                }
                break;
            default:
//                    animation = new TranslateAnimation(0, offset, 0, 0);
        }
        animation.setFillAfter(true);
        animation.setDuration(300);
        mIvShapeCircle.startAnimation(animation);

        currentIndex = index;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_all, R.id.tv_alarm, R.id.tv_warn, R.id.tv_other, R.id.jrxz, R.id.zzcl, R.id.zwcl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                mViewpagerHome.setCurrentItem(0);
                break;
            case R.id.tv_alarm:
                mViewpagerHome.setCurrentItem(1);
                break;
            case R.id.tv_warn:
                mViewpagerHome.setCurrentItem(2);
                break;
            case R.id.tv_other:
                mViewpagerHome.setCurrentItem(3);
                break;
        }
    }

    @Override//TODO 被动刷新
    public void reShowExecute() {
        super.reShowExecute();

    }

    @Override
    public void Event(MessageEvent messageEvent) {
        super.Event(messageEvent);
        switch (messageEvent.getMessage()){
            case Config.mRefreash:
                initNavigationTextSet();
                break;
        }
    }
}
