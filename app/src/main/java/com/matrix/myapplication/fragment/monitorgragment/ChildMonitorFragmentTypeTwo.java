package com.matrix.myapplication.fragment.monitorgragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.activity.ListingActivity;
import com.matrix.myapplication.fragment.BaseFragment;
import com.matrix.myapplication.model.ArrayListBT;
import com.matrix.myapplication.utils.MyLog;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by clyr on 2018/3/15 0015.
 * 监测的两个分类方式  业务系统
 */

public class ChildMonitorFragmentTypeTwo extends BaseFragment {

    @BindView(R.id.text_null)
    TextView textNull;
    @BindView(R.id.monitoe_grid)
    PullToRefreshListView monitoeGrid;
    Unbinder unbinder;
    private List<ArrayListBT> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child_type_two, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        initGridView();
    }

    private void initGridView() {
        refreshData();

        monitoeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLog.d("" + position);
                Intent intent = new Intent(getContext(), ListingActivity.class);
                intent.putExtra("code", mList.get(position).getCode());
                intent.putExtra(Config.mTag, Config.mTagTwo);
                startActivity(intent);
            }
        });
    }

    private int mCurrentPage = 1;

    private int mTotalPage = 0;

    private void refreshData() {
        mCurrentPage = 1;
        requestdatas(false);
    }

    private void loadMoreData() {
        if (mList != null && mList.size() > 0) {
            if (++mCurrentPage > mTotalPage && mTotalPage != 0) {
                ToastUtils.showShort("没有更多数据了!");
                monitoeGrid.onRefreshComplete();
            } else {
                requestdatas(true);
            }
        } else {
            refreshData();
        }
    }

    private void requestdatas(final boolean isLoadMore) {//刷新需要将之前的全部清除或者只加载后面的
        final String url = Config.mUrl + "/mobileinspect!InspectIndex";
        showLoadingDialog("请稍后...");
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                monitoeGrid.onRefreshComplete();
                hideLoadingDialog();
                //CheckViewNull.deailViewNull(mList, mMonitoeGrid, mTextNull);
                ToastUtils.showShort("获取数据失败");
                MyLog.d("okhttp", url);
            }

            @Override
            public void onResponse(String response, int id) {
                monitoeGrid.onRefreshComplete();
                MyLog.d("okhttp", url);
                hideLoadingDialog();
                Gson gson = new Gson();
//                JsonModel jsonModel = gson.fromJson(response, JsonModel.class);
//                if (jsonModel != null) {
//                    mList = jsonModel.getInstancestatustype();
//                    mMonitoeGrid.setAdapter(new GridViewAdapter(getContext(), mList));
//                }
//                CheckViewNull.deailViewNull(mList, mMonitoeGrid, mTextNull);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.text_null)
    public void onViewClicked() {
    }

    @Override//TODO 被动刷新
    public void reShowExecute() {
        super.reShowExecute();

    }
}
