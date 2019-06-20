package com.douyu.mvp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.douyu.adapter.SubChannelAdapter;
import com.douyu.bean.SubChannelInfo;
import com.douyu.mvp.presenter.ChannelPresenter;
import com.douyu.mvp.presenter.impl.IChannelPresenter;
import com.douyu.mvp.view.activity.ChannelInfoListActivity;
import com.douyu.mvp.view.fragment.impl.ChannelsFragmentView;
import com.matrix.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by l on 16-12-31.
 */

public class ChannelsFragment extends Fragment implements ChannelsFragmentView {
    private static ChannelsFragment fragment = null;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_target)
    RecyclerView rlChannels;
    private IChannelPresenter mChannelPresenter;
    private View mView;
    private List<SubChannelInfo> subChannelInfos;
    private SubChannelAdapter mAdapter;

    public static synchronized ChannelsFragment getInstance(int type) {
        if (fragment == null) {
            Bundle args = new Bundle();
            fragment = new ChannelsFragment();
            args.putInt("type", type);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live_list, container, false);
            ButterKnife.bind(this, mView);
        }
        subChannelInfos = new ArrayList<>();

        mChannelPresenter = new ChannelPresenter(this);
        mChannelPresenter.getAllChannels();
        initView();
        setRefresh();
        setLoadmore();
        return mView;
    }

    @Override
    public void showData(List<SubChannelInfo> datas) {
        subChannelInfos.clear();
        subChannelInfos.addAll(datas);
        mAdapter.addData(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView() {
        mAdapter = new SubChannelAdapter(getContext(), subChannelInfos);
        rlChannels.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getContext(), ChannelInfoListActivity.class);
                intent.putExtra("tag", subChannelInfos.get(i).tagId);
                intent.putExtra("name", subChannelInfos.get(i).tagName);
                startActivity(intent);
            }
        });
        rlChannels.setAdapter(mAdapter);
    }

    /**
     * 设置刷新
     */
    @Override
    public void setRefresh() {
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mChannelPresenter.getAllChannels();
            }
        });
    }

    /**
     * 设置加载更多
     */
    @Override
    public void setLoadmore() {
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeToLoadComplete();
            }
        });
    }

    @Override
    public void swipeToLoadComplete() {
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }


}
