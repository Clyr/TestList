package com.matrix.myapplication.fragment.monitorgragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.activity.ListingActivity;
import com.matrix.myapplication.fragment.BaseFragment;
import com.matrix.myapplication.model.ArrayListBT;
import com.matrix.myapplication.model.JsonModel;
import com.matrix.myapplication.utils.MyLog;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshBase;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by clyr on 2018/3/15 0015.
 * 监测的两个分类方式  设备类型
 */

public class ChildMonitorFragmentTypeOne extends BaseFragment {
    @BindView(R.id.text_null)
    TextView mTextNull;
    @BindView(R.id.monitoe_list)
    PullToRefreshListView mMonitoeList;
    private List<ArrayListBT> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child_type_one, null);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        initListView();
    }

    private void initListView() {
        refreshData();
        mMonitoeList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
            }
        });
        mMonitoeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//此listview排序从1开始因为有头部
//                startActivity(new Intent(getContext(), ListProgressActivity.class));
                MyLog.d("" + position);
                Intent intent = new Intent(getContext(), ListingActivity.class);
                intent.putExtra("code", mList.get(position - 1).getCode());
                intent.putExtra(Config.mTag, Config.mTagOne);
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
                mMonitoeList.onRefreshComplete();
            } else {
                requestdatas(true);
            }
        } else {
            refreshData();
        }
    }

    private void requestdatas(final boolean isLoadMore) {//刷新需要将之前的全部清除或者只加载后面的

        final String url = Config.mUrl + "/mobileinspect!InspectIndex";
//        showLoadingDialog("请稍后...");
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
               // hideLoadingDialog();
                ToastUtils.showShort("获取数据失败");
                mMonitoeList.onRefreshComplete();
                //CheckViewNull.deailViewNull(mList, mMonitoeList, mTextNull);
                MyLog.d("okhttp", url);
            }

            @Override
            public void onResponse(String response, int id) {
                MyLog.d("okhttp", url);
                mMonitoeList.onRefreshComplete();
                Gson gson = new Gson();
                JsonModel jsonModel = gson.fromJson(response, JsonModel.class);
                if (jsonModel != null) {
                    mList = jsonModel.getInstancestatusbusiness();
                   // mMonitoeList.setAdapter(new ListAdapterBT(getContext(), mList));
                }
               // CheckViewNull.deailViewNull(mList, mMonitoeList, mTextNull);
                //hideLoadingDialog();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.text_null)
    public void onViewClicked() {
    }

    @Override//TODO 被动刷新
    public void reShowExecute() {
        super.reShowExecute();

    }
}
