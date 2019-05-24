package com.matrix.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;

import com.matrix.myapplication.R;
import com.matrix.myapplication.service.FPSGetService;
import com.matrix.myapplication.utils.CheckPermission;
import com.matrix.myapplication.utils.FPSMeter;
import com.matrix.myapplication.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainGetFPSActivity extends BaseActivity {

    @BindView(R.id.text_fps)
    TextView mTextFps;
    @BindView(R.id.button_fps)
    Button mButtonFps;
    @BindView(R.id.permission)
    Button permission;
    @BindView(R.id.op)
    Button op;
    private FPSMeter mFpsMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_get_fps);
        ButterKnife.bind(this);
        mFpsMeter = new FPSMeter();
        mHandler.post(task2);
//        init();
        permission.setOnClickListener(v -> {
            //启动Activity让用户授权
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        });
        op.setOnClickListener(v->{
            ToastUtils.showShort(CheckPermission.checkAlertWindowsPermission(this)+"");
        });
    }

    int frames = 0;
    long startTime, prevTime; // Used to track elapsed time for animations and fps
    float fps = 0;
    String fpsString = "";

    private void init() {
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        frames = 0;

        while (true) {
            ++frames;
            long nowTime = System.currentTimeMillis();
            long deltaTime = nowTime - startTime;
            if (deltaTime > 1000) {
                float secs = (float) deltaTime / 1000f;
                fps = (float) frames / secs;
                fpsString = "fps: " + fps;
                startTime = nowTime;
                frames = 0;
                mTextFps.setText("FPS:" + mFpsMeter.getFps());
            }
        }
    }

    Runnable task2 = new Runnable() {
        @Override
        public void run() {
            mFpsMeter.mesureFps();
            mHandler.postDelayed(this, 16);
        }
    };
    Handler mHandler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            mTextFps.setText("FPS:" + mFpsMeter.getFps());
            mHandler.postDelayed(this, 1000);
        }
    };

    @OnClick(R.id.button_fps)
    public void onViewClicked() {
        mHandler.postDelayed(task, 1000);
        startService(new Intent(MainGetFPSActivity.this, FPSGetService.class));
        finish();
    }


}
