package com.mm131;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.matrix.myapplication.R;
import com.matrix.myapplication.activity.BaseActivity;
import com.matrix.myapplication.fragment.BaseFragment;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshBase;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BlankFragment1 extends BaseFragment {
    @BindView(R.id.list)
    PullToRefreshListView listView;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_blank, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        getData();
        return inflate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        listView = getActivity().findViewById(R.id.list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getData() {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                ToastUtils.showLong("onPullDownToRefresh");
                handler.postDelayed(task, 2000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                ToastUtils.showLong("onPullUpToRefresh");
                handler.postDelayed(task, 2000);
            }
        });
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showLong(position + "");
            }
        });
        listView.setOnLongClickListener(v->{
            ToastUtils.showLong( " OnLongClickListener ");
            return true;
        });*/
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(Math.random() * 100 + "");
        }
        MyListAdapter adapter = new MyListAdapter(getContext(), list);
        listView.setAdapter(adapter);

    }

    Handler handler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            listView.onRefreshComplete();
        }
    };


}
