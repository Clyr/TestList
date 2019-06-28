package com.matrix.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.matrix.myapplication.R;
import com.matrix.myapplication.utils.MessageEvent;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshBase;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshListView;
import com.matrix.myapplication.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main7Activity extends BaseActivity {

    @BindView(R.id.list)
    PullToRefreshListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        ButterKnife.bind(this);
        init();

    }

    private void init() {


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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.showLong(position + "");
                RxBus.getInstance().post(new MessageEvent(position + " - RxBus"));
            }
        });
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(Math.random() * 100 + "");
        }
        MyAdapter adapter = new MyAdapter(this, list);
        listView.setAdapter(adapter);
    }

    Handler handler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            listView.onRefreshComplete();
        }
    };

    class MyAdapter extends BaseAdapter {

        LayoutInflater inflater;
        List<String> mlist;

        public MyAdapter(Context context, List<String> list) {
            inflater = LayoutInflater.from(context);
            mlist = list;
        }

        @Override
        public int getCount() {
            if (mlist != null)
                return mlist.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mlist != null)
                return mlist.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listitemtextview, null);
                holder.textView = convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(mlist.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView;
    }
}
