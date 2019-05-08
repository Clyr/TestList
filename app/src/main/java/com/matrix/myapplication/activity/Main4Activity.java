package com.matrix.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.matrix.myapplication.R;
import com.matrix.myapplication.utils.OkHolder;
import com.matrix.myapplication.view.MyVideoView;
import com.matrix.myapplication.view.ProcessImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class Main4Activity extends BaseActivity {
    private GridView mGridView;
    private GridViewAdapter mAdapter;
    private List<ListBean> mList = new ArrayList<>();
    private final int IMAGE_OPEN = 0;//打开图片标记
    private final int VIDEO_OPEN = 1;//打开视频标记
    private String mVideoUrl;
    private String pathImage;
    private final int SUCCESS = 0;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        initview();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.size() > 2) {
                    upDateFile();
                }
            }
        });
    }

    private void upDateFile() {
        PostFormBuilder url = OkHttpUtils.post().url("http://192.168.32.193:8080/hims/TestAndroidFile.do");
        for (int i = 1; i < mList.size(); i++) {
            File file = new File(mList.get(i).getInfo());
            url.addFile("files[" + (i - 1) + "]", file.getName(), file);
        }
        url.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(Main4Activity.this, "onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(Main4Activity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upDateFile(String urlStr) {
        PostFormBuilder url = OkHttpUtils.post().url("http://192.168.32.193:8080/hims/TestAndroidFile.do");
        File file = new File(urlStr);
        url.addFile("file", file.getName(), file);
        url.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(Main4Activity.this, "onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(Main4Activity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initview() {
        ListBean listBean = new ListBean();
        listBean.setTag("out");
        listBean.setInfo("");
        mList.add(listBean);
        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new GridViewAdapter(this);
        mGridView.setAdapter(mAdapter);
        findViewById(R.id.btn_image).setOnClickListener(new View.OnClickListener() {//Image
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_OPEN);
            }
        });
        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main4Activity.this, CameraActivity.class);
                startActivityForResult(intent, VIDEO_OPEN);
            }
        });
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//
//            }
//        });
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case IMAGE_OPEN:
                if (resultCode == RESULT_OK) {
                    ListBean listBean = new ListBean();
                    listBean.setTag("image");
                    listBean.setInfo(getPath(data));
                    mList.add(listBean);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(Main4Activity.this, getPath(data), Toast.LENGTH_SHORT).show();
//                    upDateFile(getPath(data));
                }
                break;
            case VIDEO_OPEN:
                if (resultCode == 1000) {
                    mVideoUrl = data.getStringExtra("video");
                    ListBean listBean = new ListBean();
                    listBean.setTag("video");
                    listBean.setInfo(mVideoUrl);
                    mList.add(listBean);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
        }
    }

    public String toAllString(List<ListBean> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str = str + list.get(i).getTag() + "+" + list.get(i).getInfo() + ";\n";
        }
        return str;
    }

    /*** start bitmap **/
    public Bitmap getImg(Intent data) throws IOException {
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        //获得图片的uri
        Uri originalUri = data.getData();
        Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
        return bm;
    }

    public String getPath(Intent data) {
        String[] imgPath = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data.getData(), imgPath, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        return path;
    }

    public Bitmap getBitmap(Uri imageUri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            return BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return null;
        }

    }


    /*** end bitmap **/
   /* @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }*/


    class ViewHolder {
        public ProcessImageView image;
        public MyVideoView video;
        public RelativeLayout videoRe;
        Button mButton;

    }

    class GridViewAdapter extends BaseAdapter {
        private LayoutInflater mInflater;


        public GridViewAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (mList != null) {
                return mList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mList != null) {
                return mList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder = new ViewHolder();
            if (mList != null) {
                if ("image".equals(mList.get(position).getTag())) {
                    convertView = mInflater.inflate(R.layout.layout_image,
                            null);
                    holder.image = (ProcessImageView) convertView
                            .findViewById(R.id.image);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 10;
                    holder.image.setImageBitmap(BitmapFactory.decodeFile(mList.get(position).getInfo(), options));
                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(Main4Activity.this);
                            alert.setTitle("温馨提示").setMessage("是否要删除该图片？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mList != null || mList.size() >= position) {
                                        mList.remove(position);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                        }
                    });
                } else if ("video".equals(mList.get(position).getTag())) {
                    convertView = mInflater.inflate(R.layout.layout_video,
                            null);
                    holder.video = (MyVideoView) convertView
                            .findViewById(R.id.video);
                    Uri uri = Uri.parse(mList.get(position).getInfo());
                    final MyVideoView videoView = holder.video;
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            videoView.start();
                        }
                    });
                    //设置视频路径
                    holder.video.setVideoURI(uri);
                    //开始播放视频
                    holder.video.start();
                    holder.videoRe = (RelativeLayout) convertView.findViewById(R.id.videolin);
                    holder.videoRe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(Main4Activity.this);
                            alert.setTitle("温馨提示").setMessage("是否要删除该视频？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mList != null || mList.size() >= position) {
                                        mList.remove(position);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                        }
                    });
//                    updateVideo(holder.video,mList.get(position).getInfo());
//                    convertView.setTag(holder);
                } else if ("out".equals(mList.get(position).getTag())) {
                    convertView = mInflater.inflate(R.layout.layout_image,
                            null);
                    holder.image = (ProcessImageView) convertView
                            .findViewById(R.id.image);
                    holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(Main4Activity.this,"点击",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, IMAGE_OPEN);

                        }
                    });
                    holder.image.setProgress(100);
//                    holder.mButton.setVisibility(View.GONE);
                }
            } /*else {
                holder = (ViewHolder) convertView.getTag();
            }*/
            return convertView;
        }
    }

    private void updatwImage(final ProcessImageView image, String info) {
        String url = "";
        File file = new File(info);
        Map<String, String> map = new HashMap<>();
        OkHolder.post(url, map).addFile("file", file.getName(), file).build().execute(new StringCallback() {
            @Override
            public void inProgress(float progress, long total, int id) {
                image.setProgress((int) (progress * 100));
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });
    }


    class ListBean {
        public String tag;
        public String info;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
