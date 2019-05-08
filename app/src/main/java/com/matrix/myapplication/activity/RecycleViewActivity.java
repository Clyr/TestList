package com.matrix.myapplication.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matrix.myapplication.R;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.RecycleViewDivider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleViewActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private ArrayList<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
//        设置布局管理器
//        recycler.setLayoutManager(layoutManager);
//        设置为垂直布局，这也是默认的
//        layoutManager.setOrientation(OrientationHelper. VERTICAL);
//        //设置分隔线
//        recycler.addItemDecoration( new DividerGridItemDecoration(this ));
//        设置Adapter
//        recycler.setAdapter(recycleAdapter);
//        设置增加或删除条目的动画
//        recycler.setItemAnimator( new DefaultItemAnimator());
        initData();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        HomeAdapter mAdapter = new HomeAdapter();
        recycler.setAdapter(mAdapter);
        recycler.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
//        添加默认分割线：高度为2px，颜色为灰色
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
//        添加自定义分割线：可自定义分割线drawable
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(
//                mContext, LinearLayoutManager.VERTICAL, R.drawable.divider_mileage));
//        添加自定义分割线：可自定义分割线高度和颜色
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(
//                mContext, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.divide_gray_color)));

    }

    protected void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(RecycleViewActivity.this).inflate(R.layout.recycleitem, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv.setText(mDatas.get(position));
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(mDatas.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
            }
        }
    }
}
