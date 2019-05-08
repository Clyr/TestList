package com.matrix.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.matrix.myapplication.R;
import com.matrix.myapplication.view.RadarImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendActivity extends BaseActivity {
    @BindView(R.id.lin)
    LinearLayout mLin;
    private String[] titles = {"预警", "网络中断", "报警", "数据获取异常"};
    private float[] values = {8, 6, 3, 10}; // 各维度分值
//    private LifeWheelRadarGraph mLf;
private RadarImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        /*mLf = LifeWheelRadarGraph.newInstance(this, titles,
                values,15);*/
        /*addContentView(lf, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/
        mImageView = RadarImageView.newInstance(this,titles,values,16);
        mLin.addView(mImageView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.changeValues(new float[]{(float) (Math.random()*10),(float) (Math.random()*10),(float) (Math.random()*10),(float) (Math.random()*10)});
            }
        });
    }
}
