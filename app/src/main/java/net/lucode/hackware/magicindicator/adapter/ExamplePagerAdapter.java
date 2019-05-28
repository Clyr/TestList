package net.lucode.hackware.magicindicator.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.google.gson.Gson;
import com.matrix.myapplication.R;
import com.matrix.myapplication.model.JsonModel;
import com.matrix.myapplication.retrofit.ORHelper;
import com.matrix.myapplication.retrofit.ORService;
import com.matrix.myapplication.view.LoadingDialog;
import com.matrix.myapplication.view.pulltorefresh.PullToRefreshListView;
import com.mm131.ImgActivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hackware on 2016/9/10.
 */

public class ExamplePagerAdapter extends PagerAdapter {
    private List<String> mDataList;
    LayoutInflater inflater;
    Activity context;
    PullToRefreshListView listview;

    public ExamplePagerAdapter(List<String> dataList, Activity context) {
//        inflater = LayoutInflater.from(context);
        inflater = context.getLayoutInflater();
        mDataList = dataList;
        this.context = context;
        View view = inflater.inflate(R.layout.activity_main7, null);
        listview = view.findViewById(R.id.list);
        getData();
    }

    private void getData() {
        LoadingDialog.showLoading(context);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(Math.random() * 100 + "");
        }
        listview.setAdapter(new MyAdapter(context, list));
        String urll = "http://192.168.32.22:8080/mobileindex!Init";
        Call<ResponseBody> call = ORHelper.getService(ORService.class).get(urll);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.isSuccessful()) {
                    Gson gson = new Gson();
                    JsonModel model = gson.fromJson(response.body().toString(), JsonModel.class);

                }

                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LoadingDialog.cancelLoading();
            }
        });
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        /*TextView textView = new TextView(container.getContext());
        textView.setText(mDataList.get(position));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(24);
        container.addView(textView);*/
        return listview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        TextView textView = (TextView) object;
        String text = textView.getText().toString();
        int index = mDataList.indexOf(text);
        if (index >= 0) {
            return index;
        }
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position);
    }

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
                holder.img = convertView.findViewById(R.id.img);
                holder.lin = convertView.findViewById(R.id.lin);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.lin.setOnClickListener(v -> {
                Intent intent = new Intent(context, ImgActivity.class);
                intent.putExtra("url", mlist.get(position));
                context.startActivity(intent);
            });

            holder.textView.setText(mlist.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView;
        ImageView img;
        LinearLayout lin;
    }
}
