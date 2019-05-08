package com.matrix.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.adapter.MessageGroupFragmentAdapter;
import com.matrix.myapplication.fragment.monitorgragment.ChildMonitorFragmentTypeOne;
import com.matrix.myapplication.fragment.monitorgragment.ChildMonitorFragmentTypeTwo;
import com.matrix.myapplication.model.JsonModel;
import com.matrix.myapplication.utils.StringUtils;
import com.matrix.myapplication.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;


/**
 * 监测
 */
public class MonitorFragment extends BaseFragment {

    @BindView(R.id.title_center)
    TextView mTitleCenter;
    @BindView(R.id.title_left)
    TextView mTitleLeft;
    @BindView(R.id.title_right)
    TextView mTitleRight;
    Unbinder unbinder;
    @BindView(R.id.image_type1)
    ImageView mImageType1;
    @BindView(R.id.image_type2)
    ImageView mImageType2;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.image_type3)
    ImageView mImageType3;
    @BindView(R.id.image_type4)
    ImageView mImageType4;
    @BindView(R.id.serach_modle_ll)
    LinearLayout mSerachModleLl;
    @BindView(R.id.name11)
    TextView mName11;
    @BindView(R.id.num11)
    TextView mNum11;
    @BindView(R.id.name12)
    TextView mName12;
    @BindView(R.id.num12)
    TextView mNum12;
    @BindView(R.id.name13)
    TextView mName13;
    @BindView(R.id.num13)
    TextView mNum13;
    @BindView(R.id.name21)
    TextView mName21;
    @BindView(R.id.num21)
    TextView mNum21;
    @BindView(R.id.name22)
    TextView mName22;
    @BindView(R.id.num22)
    TextView mNum22;
    @BindView(R.id.name23)
    TextView mName23;
    @BindView(R.id.num23)
    TextView mNum23;
    @BindView(R.id.type1)
    TextView mType1;
    @BindView(R.id.type2)
    TextView mType2;
    @BindView(R.id.iv_shape_circle)
    ImageView mIvShapeCircle;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    private List<JsonModel> mListSearch = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_monitor, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        initCursorPosition();
        mType1.setSelected(true);
        mType2.setSelected(false);
        mTitleCenter.setText("监测");
        mTitleRight.setVisibility(View.GONE);
        mTitleLeft.setVisibility(View.GONE);
        mImageType1.setSelected(true);
        mImageType3.setSelected(true);
        initViewPager();
        //initGetData();
       /* mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mListSearch.clear();
                if ("".equals(mSearchEdit.getText().toString().trim())) {
                    mMonitoeList.setAdapter(new RefreshListAdapter(getContext(), mList));
                    return;
                }
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getName().contains(mSearchEdit.getText().toString().trim())) {
                        mListSearch.add(mList.get(i));
                    }
                }
                RefreshListAdapter refreshListAdapter = new RefreshListAdapter(getContext(), mListSearch);
                mMonitoeList.setAdapter(refreshListAdapter);
//                refreshListAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

    }

    private void initGetData() {
        String url = Config.mUrl + "/mobileinspect!InspectIndex";
//        showLoadingDialog("请稍后...");
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                hideLoadingDialog();
                ToastUtils.showShort("获取数据失败");
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                JsonModel jsonModel = gson.fromJson(response, JsonModel.class);
                setTextViewData(jsonModel);
            }
        });
    }

    private void setTextViewData(JsonModel jsonModel) {
        if (jsonModel != null) {
            mNum11.setText(jsonModel.getInstancenum());
            mNum12.setText(jsonModel.getInstancealarmnum());
            mName13.setText("占比 "+strToIntAve(jsonModel.getInstancealarmnum(), jsonModel.getInstancenum())+"%");//转换成数字

            mNum21.setText(jsonModel.getItemnum());
            mNum22.setText(jsonModel.getItemalarmnum());
            mName23.setText("占比 "+strToIntAve(jsonModel.getItemalarmnum(), jsonModel.getItemnum())+"%");
        }
    }

    private String strToIntAve(String str1, String str2) {
        int anInt1 = StringUtils.toInt(str1, 0);
        int anInt2 = StringUtils.toInt(str2, 0);
        if (anInt2 != 0)
            return 100 * anInt1 / anInt2 + "";
        return 0 + "";
    }

    private List<Fragment> list = new ArrayList<Fragment>();
    private MessageGroupFragmentAdapter adapter;

    private void initViewPager() {
        ChildMonitorFragmentTypeOne childMonitorFragmentTypeOne = new ChildMonitorFragmentTypeOne();
        ChildMonitorFragmentTypeTwo childMonitorFragmentTypeTwo = new ChildMonitorFragmentTypeTwo();
        list.add(childMonitorFragmentTypeOne);
        list.add(childMonitorFragmentTypeTwo);
        adapter = new MessageGroupFragmentAdapter(getChildFragmentManager(), list);
        mViewpager.setAdapter(adapter);
        mViewpager.setOffscreenPageLimit(2);
        mViewpager.setCurrentItem(0);
        mViewpager.setOnPageChangeListener(pageChangeListener);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            setAnim(index + 1);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private int offset = 0;//偏移量

    private void initCursorPosition() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        offset = (width / 2);
    }

    int mInt = 0;

    public void setAnim(int index) {
        TranslateAnimation animation = null;
        switch (index) {
            case 1:
                if (mInt == 1) {
                    return;
                }
                animation = new TranslateAnimation(offset, 0, 0, 0);
                mInt = 1;
                break;
            case 2:
                if (mInt == 2) {
                    return;
                }
                animation = new TranslateAnimation(0, offset, 0, 0);
                mInt = 2;
                break;
        }
        animation.setFillAfter(true);
        animation.setDuration(300);
        mIvShapeCircle.startAnimation(animation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.image_type1, R.id.image_type2, R.id.image_type3, R.id.image_type4, R.id.type1, R.id.type2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_type1:
                mImageType1.setSelected(true);
                mImageType2.setSelected(false);
                break;
            case R.id.image_type2:
                mImageType2.setSelected(true);
                mImageType1.setSelected(false);
                break;
            case R.id.image_type3:
                mImageType3.setSelected(true);
                mImageType4.setSelected(false);
                break;
            case R.id.image_type4:
                mImageType4.setSelected(true);
                mImageType3.setSelected(false);
                break;
            case R.id.type1:
                mType1.setSelected(true);
                mType2.setSelected(false);
                mViewpager.setCurrentItem(0);
                setAnim(1);
                break;
            case R.id.type2:
                mViewpager.setCurrentItem(1);
                mType2.setSelected(true);
                mType1.setSelected(false);
                setAnim(2);
                break;
        }
    }

    @Override//TODO 被动刷新
    public void reShowExecute() {
        super.reShowExecute();

    }
}
