package com.matrix.myapplication.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.VideoView;

import com.matrix.myapplication.R;
import com.matrix.myapplication.utils.BitmapUtils;

public class BigImageActivity extends BaseActivity {

    private static final int IMAGE_OPEN = 1;
    private PopupWindow mPop;
    private ImageView mLl_popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
//        init();
        init2();
        init3();
//        init4();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
           if(mPop!=null){
               mPop.dismiss();
               mPop=null;
           }else{
               finish();
           }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void init4() {
        mPop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows2, null);
        mLl_popup = (ImageView) view.findViewById(R.id.imagepop);
        mPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPop.setBackgroundDrawable(new BitmapDrawable());
        mPop.setContentView(view);
        mLl_popup.setImageBitmap(BitmapUtils.rToBitmap(R.drawable.s_title,BigImageActivity.this));
        mPop.showAtLocation(view, Gravity.BOTTOM,0,0);
        mLl_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        /*RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        Button bt4 = (Button) view
                .findViewById(R.id.item_popupwindows_video);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPop.dismiss();
                mLl_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPop.dismiss();
                mLl_popup.clearAnimation();
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
                mLl_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_OPEN);
                mPop.dismiss();
                mLl_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPop.dismiss();
                mLl_popup.clearAnimation();
            }
        });*/
    }

    private void init3() {
        findViewById(R.id.videobutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
      initVideo(mUri);
    }

    private void initVideo(final Uri data) {
        if(data==null){
            return;
        }
        final VideoView video=(VideoView)findViewById(R.id.video);
//        Uri uri = Uri.parse("");
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.start();
            }
        });
        //设置视频路径
        video.setVideoURI(data);
        //开始播放视频
        video.start();
        findViewById(R.id.re).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(BigImageActivity.this,ImageActivity.class);
                intent.setData(data);
                startActivity(intent);
            }
        });
    }
    Uri mUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                mUri = data.getData();
                initVideo(mUri);
                break;
        }
    }

    private void init2() {
        final ImageView image=(ImageView)findViewById(R.id.image);
        image.setImageBitmap(BitmapUtils.rToBitmap(R.drawable.image,this));
        image.setOnClickListener(new View.OnClickListener() { // 点击放大
            public void onClick(View paramView) {
                init4();
//                LayoutInflater inflater = LayoutInflater.from(BigImageActivity.this);
//                final AlertDialog dialog = new AlertDialog.Builder(BigImageActivity.this).create();
//                ImageView img = new ImageView(BigImageActivity.this);
//                img.setImageBitmap(BitmapUtils.rToBitmap(R.drawable.image,BigImageActivity.this));
//                dialog.setView(img); // 自定义dialog
//                dialog.show();
//                // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
//                img.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View paramView) {
//                        dialog.cancel();
//                    }
//                });
//                WindowManager m = getWindowManager();
//                Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
//                android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
//                p.height = d.getHeight();
//                p.width = d.getWidth();
//                dialog.getWindow().setAttributes(p);     //设置生效
            }
        });
    }

    private void init() {
        final ImageView image=(ImageView)findViewById(R.id.image);
        image.setImageBitmap(BitmapUtils.rToBitmap(R.drawable.image,this));
        image.setOnClickListener(new View.OnClickListener() { // 点击放大
            public void onClick(View paramView) {
                LayoutInflater inflater = LayoutInflater.from(BigImageActivity.this);
                View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
                final AlertDialog dialog = new AlertDialog.Builder(BigImageActivity.this).create();
                ImageView img = (ImageView)imgEntryView.findViewById(R.id.large_image);
//                imageDownloader.download("图片地址",img); // 这个是加载网络图片的，可以是自己的图片设置方法
                img.setImageBitmap(BitmapUtils.rToBitmap(R.drawable.image,BigImageActivity.this));
                dialog.setView(imgEntryView); // 自定义dialog
                dialog.show();
                // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
                imgEntryView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramView) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

}
