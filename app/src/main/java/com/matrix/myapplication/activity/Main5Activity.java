package com.matrix.myapplication.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.matrix.myapplication.R;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.GuaGuaKa;
import com.matrix.myapplication.view.MyVideoView;
import com.matrix.myapplication.view.ProcessImageView;

import butterknife.ButterKnife;

public class Main5Activity extends BaseActivity {
    private static final int IMAGE_OPEN = 100;
    ProcessImageView processImageView =null;
    ProcessImageView processImageView2 =null;
    private final int SUCCESS=0;
    int progress=0;
    MyVideoView mMyVideoView;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(Main5Activity.this, "上传完成", Toast.LENGTH_SHORT).show();
//                    processImageView.setVisibility(View.GONE);
                    processImageView.setImageResource(R.drawable.error);
                    mButton.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private Button mButton;
    private Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        ButterKnife.bind(this);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GuaGuaKa guaGuaKa = new GuaGuaKa(Main5Activity.this);
                finish();
            }
        });
        GuaGuaKa.setText("星期三");
        mButton2 = (Button)findViewById(R.id.buttonimage2);
        mButton2.setVisibility(View.GONE);
        initView();
        initVideo();
    }

    private void initVideo() {
        mMyVideoView = (MyVideoView) findViewById(R.id.video);
        processImageView2=(ProcessImageView) findViewById(R.id.image2);
        findViewById(R.id.rel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_OPEN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IMAGE_OPEN:
                if(data==null){
                    ToastUtils.showShort("data==null");
                    return;
                }
                Uri uri = data.getData();
                videoPlayer(uri);
                break;
        }
    }

    private void videoPlayer(Uri uri) {
        mMyVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMyVideoView.start();
            }
        });
        //设置视频路径
        mMyVideoView.setVideoURI(uri);
        //开始播放视频
        mMyVideoView.start();
        progress = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(progress==100){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Main5Activity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
                                processImageView2.setImageResource(R.drawable.error);
                                mButton2.setVisibility(View.VISIBLE);

                            }
                        });
                        return;
                    }
                    progress++;
                    processImageView2.setProgress(progress);
                    try{
                        Thread.sleep(50);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initView() {
        processImageView=(ProcessImageView) findViewById(R.id.image);
        //模拟图片上传进度
        mButton = (Button)findViewById(R.id.buttonimage);
        final Animation animation = AnimationUtils.loadAnimation(Main5Activity.this, R.anim.revolve_refresh);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.startAnimation(animation);
            }
        });
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateImage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        updateImage();
    }

    private void updateImage() {
        mButton.setVisibility(View.GONE);
        processImageView.setImageResource(R.drawable.image);
        progress = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(progress==100){//图片上传完成
                        handler.sendEmptyMessage(SUCCESS);
                        return;
                    }
                    progress++;
                    processImageView.setProgress(progress);
                    try{
                        Thread.sleep(50);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {//跳转到桌面
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
