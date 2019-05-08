package com.matrix.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.model.BusinessSystem;
import com.matrix.myapplication.utils.OkHolder;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshBase;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshListView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/*** 监控资源列表 **/
public class ListingActivity extends BaseActivity {

    @BindView(R.id.title_center)
    TextView mTitleCenter;
    @BindView(R.id.title_left)
    TextView mTitleLeft;
    @BindView(R.id.title_right)
    TextView mTitleRight;
    @BindView(R.id.text_null)
    TextView mTextNull;
    @BindView(R.id.listing_list)
    PullToRefreshListView mListingList;
    List<BusinessSystem> mList;
    private String mExtra;
    private String mTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mExtra = mIntent.getStringExtra("code");
        mTag = mIntent.getStringExtra(Config.mTag);
        mTitleCenter.setText("监控资源列表");
        mTitleRight.setVisibility(View.GONE);
        mTitleLeft.setText("  ");
        initList();
    }

    private void initList() {
        refreshData();
        mListingList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
            }
        });

        mListingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.showShort(position+"");
                // refreashlistview   head body foot  从1开始，list从0开始 故而-1
//                Intent intent = new Intent(ListingActivity.this, ListingSubActivity.class);
                mIntent.putExtra(Config.mId, mList.get(position - 1).getId());
                mIntent.putExtra(Config.mIp, mList.get(position - 1).getIp());
                startActivity(mIntent);
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
                mListingList.onRefreshComplete();
            } else {
                requestdatas(true);
            }
        } else {
            refreshData();
        }
    }

    private void requestdatas(final boolean isLoadMore) {//刷新需要将之前的全部清除或者只加载后面的
        mList = new ArrayList<>();//仅有刷新，后续如果添加更多 isLoadMore 标记为true
//        showLoadingDialog("请稍后...");
        Map<String, String> map = new HashMap<>();
        String url = Config.mUrl + "/mobileinspect!InstanceListBussiness";
        if (Config.mTagOne.equals(mTag)) {
            map.put("bussinessid", mExtra);
            url = Config.mUrl + "/mobileinspect!InstanceListBussiness";
        } else if (Config.mTagTwo.equals(mTag)) {
            url = Config.mUrl + "/mobileinspect!InstanceListDeviceType";
            map.put("devicetype", mExtra);
        }
        OkHolder.post(url, map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                hideLoadingDialog();
                mListingList.onRefreshComplete();
//                CheckViewNull.deailViewNull(mList, mListingList, mTextNull);
//                ToastUtils.showShort(R.string.net_error);
            }

            @Override
            public void onResponse(String response, int id) {
//                hideLoadingDialog();
//                mListingList.onRefreshComplete();
//                JsonModel jsonModel = new Gson().fromJson(response, JsonModel.class);
//                if (jsonModel != null) {
//                    mList = jsonModel.getInstancelist();
//                    mListingList.setAdapter(new RefreshListAdapterOTO(ListingActivity.this, mList));
//                }
//                CheckViewNull.deailViewNull(mList, mListingList, mTextNull);
            }
        });

    }

    @OnClick({R.id.title_left, R.id.text_null})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.text_null:
                refreshData();
                break;
        }
    }
}
