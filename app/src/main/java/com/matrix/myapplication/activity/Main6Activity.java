package com.matrix.myapplication.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

import com.matrix.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main6Activity extends BaseActivity {

    @BindView(R.id.video)
    VideoView mVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        String path = "http://gslb.miaopai.com/stream/oxX3t3Vm5XPHKUeTS-zbXA__.mp4";
        mVideo.setVideoPath(path);
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideo.start();
            }
        });
        mVideo.start();
    }
}
