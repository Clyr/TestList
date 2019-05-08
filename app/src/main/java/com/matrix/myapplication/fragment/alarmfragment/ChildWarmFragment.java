package com.matrix.myapplication.fragment.alarmfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.fragment.BaseFragment;
import com.matrix.myapplication.model.AlarmInfoSub;
import com.matrix.myapplication.utils.MessageEvent;
import com.matrix.myapplication.utils.MyLog;
import com.matrix.myapplication.utils.OkHolder;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshBase;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshListView;
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
 * Created by clyr on 2018/3/15 0015.
 * 报警--预警
 */

public class ChildWarmFragment extends BaseFragment {
    @BindView(R.id.text_null)
    TextView mTextNull;
    @BindView(R.id.refreshlist)
    PullToRefreshListView mRefreshlist;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child_all, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }
    private List<AlarmInfoSub> mList = new ArrayList<>();
    private void init() {
        mRefreshlist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
                EventBus.getDefault().post(new MessageEvent(Config.mRefreash));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMoreData();
            }
        });
        final Intent intent = new Intent();
        mRefreshlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra(Config.mId, mList.get(position-1).getId());
                intent.putExtra(Config.mName, mList.get(position-1).getValues());
                intent.putExtra(Config.mIp, mList.get(position-1).getIp());
                intent.putExtra(Config.mItemdisplay, mList.get(position-1).getItemdisplay());
                getActivity().startActivity(intent);
            }
        });
        refreshData();
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
                mRefreshlist.onRefreshComplete();
            } else {
                requestdatas(true);
            }
        } else {
            refreshData();
        }
    }

    private void requestdatas(final boolean isLoadMore) {//刷新需要将之前的全部清除或者只加载后面的
        showLoadingDialog("请稍后...");
        String url = Config.mUrl + "/mobilealarm!AlarmIndex";
        Map<String, String> map = new HashMap<>();
        map.put("status", "2");
        /** ALL **/
        OkHolder.post(url, map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                hideLoadingDialog();mRefreshlist.onRefreshComplete();
                //CheckViewNull.deailViewNull(mList, mRefreshlist, mTextNull);
                MyLog.d("all", "数据获取失败");
            }

            @Override
            public void onResponse(String response, int id) {
                mRefreshlist.onRefreshComplete();
                hideLoadingDialog();
//                AlarmInfo alarmInfo = new Gson().fromJson(response, AlarmInfo.class);
//                if (alarmInfo != null) {
//                    if (alarmInfo.getAlarmlist() != null) {
//                        mList = alarmInfo.getAlarmlist();
//                        mRefreshlist.setAdapter(new RefreshListAdapterAlarm(getContext(), mList));
//                        CheckViewNull.deailViewNull(mList, mRefreshlist, mTextNull);
//                    } else {
//                        MyLog.d("all", "alarmInfo.getAlarmlist()==null");
//                    }
//                } else {
//                    MyLog.d("all", "alarmInfo==null");
//                }
            }
        });
    }
    @OnClick(R.id.text_null)
    public void onViewClicked() {
        refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override//TODO 被动刷新
    public void reShowExecute() {
        super.reShowExecute();

    } @Override
    public void Event(MessageEvent messageEvent) {
        super.Event(messageEvent);
        switch (messageEvent.getMessage()){
            case "1":
                refreshData();

        }
    }

}
