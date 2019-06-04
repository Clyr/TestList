package com.mm131;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.matrix.myapplication.R;

import java.util.List;

/**
 * Created by M S I of clyr on 2019/5/29.
 */
class MyListAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<String> mlist;
    Context context;
    public MyListAdapter(Context context, List<String> list) {
        inflater = LayoutInflater.from(context);
        mlist = list;
        this.context = context;
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


