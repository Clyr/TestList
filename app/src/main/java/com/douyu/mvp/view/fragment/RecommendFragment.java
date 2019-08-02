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
import com.douyu.adapter.RoomInfoAdapter;
import com.douyu.bean.RoomInfo;
import com.douyu.mvp.presenter.RecommendPresenter;
import com.douyu.mvp.presenter.impl.IRecommendPresenter;
import com.douyu.mvp.view.activity.PalyActivity;
import com.douyu.mvp.view.fragment.impl.RecommendFragmentView;
import com.matrix.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by l on 16-12-31.
 */

public class RecommendFragment extends Fragment implements RecommendFragmentView {

    private static RecommendFragment fragment = null;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_target)
    RecyclerView rlChannels;

    private View mView;
    private List<RoomInfo> roomInfos;
    private IRecommendPresenter mRecommendPresenter;
    private RoomInfoAdapter mAdapter;
    private int offset = 0;

    public static synchronized RecommendFragment getInstance(int type) {
        if (fragment == null) {
            Bundle args = new Bundle();
            fragment = new RecommendFragment();
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
            ButterKnife.bind(this, this.mView);
        }
        roomInfos = new ArrayList<>();
        mRecommendPresenter = new RecommendPresenter(this);
        mRecommendPresenter.getRecommend(offset);
        initView();
        setRefresh();
        setLoadmore();
        return mView;
    }

    @Override
    public void showData(List<RoomInfo> datas) {
        if (offset == 0) {
            roomInfos.clear();
            roomInfos.addAll(datas);
            mAdapter.notifyDataSetChanged();
            mAdapter.setNewData(datas);
        } else {
            roomInfos.addAll(datas);
            roomInfos.addAll(datas);
            mAdapter.notifyDataSetChanged();
            mAdapter.addData(datas);
        }
    }

    @Override
    public void initView() {
        mAdapter = new RoomInfoAdapter(getContext(), roomInfos);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        //第一列单独占一行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        rlChannels.setLayoutManager(gridLayoutManager);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                //TODO：推荐待完成
                int roomId = roomInfos.get(i).roomId;
                String roomName = roomInfos.get(i).roomName;
                startPlay(roomName, roomId);
            }
        });
        rlChannels.setAdapter(mAdapter);
    }

    @Override
    public void setRefresh() {
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;//刷新页数置0
                mRecommendPresenter.getRecommend(offset);
            }
        });
    }

    @Override
    public void setLoadmore() {
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                offset += 20;
                mRecommendPresenter.getRecommend(offset);
            }
        });
    }

    @Override
    public void swipeToLoadComplete() {
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void startPlay(String roomName, int roomId) {
        Intent intent = new Intent(getContext(), PalyActivity.class);
        intent.putExtra("name", roomName);
        intent.putExtra("id", roomId);
        startActivity(intent);
    }

}
