package com.matrix.myapplication.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

import com.matrix.myapplication.R;

public class ImageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        init();
    }

    private void init() {
        final VideoView videoView = (VideoView) findViewById(R.id.videobig);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
        //设置视频路径
        videoView.setVideoURI(getIntent().getData());
        //开始播放视频
        videoView.start();
    }
}
