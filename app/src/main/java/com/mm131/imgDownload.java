package com.mm131;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.matrix.myapplication.R;
import com.matrix.myapplication.retrofit.ORHelper;
import com.matrix.myapplication.utils.MyLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

/**
 * Created by M S I of clyr on 2019/5/24.
 */
public class imgDownload {


    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false); //设置不使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            SavaImage(bitmap, Environment.getExternalStorageDirectory().getPath() + "/Test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content:
                //加入网络图片地址
//                new Task().execute("https://pic.4j4j.cn/upload/pic/20130617/55695c3c95.jpg");
                break;

            case R.id.image:
                //点击图片后将图片保存到SD卡跟目录下的Test文件夹内
//                SavaImage(bitmap, Environment.getExternalStorageDirectory().getPath()+"/Test");
//                Toast.makeText(getBaseContext(), "图片保存", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x123) {
//                image.setImageBitmap(bitmap);
            }
        }

        ;
    };


    /**
     * 异步线程下载图片
     *
     */
    /*class Task extends AsyncTask<String, void="">{

        protected Void doInBackground(String... params) {
            bitmap=GetImageInputStream((String)params[0]);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Message message=new Message();
            message.what=0x123;
            handler.sendMessage(message);
        }

    }*/

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     * 需要获取存储权限
     */
    public void SavaImage(Bitmap bitmap, String path) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            fileOutputStream = new FileOutputStream(path + "/" + System.currentTimeMillis() + ".png");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String BASE_URl = "";

    public <T> T getHttpApi(Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .client(ORHelper.getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(service);
    }

    /**
     * 指定线程下载文件(异步)，非阻塞式下载
     *
     * @param url       图片url
     * @param savePatch 下载文件保存目录
     * @param fileName  文件名称(不带后缀)
     *                  Environment.getExternalStorageDirectory() + File.separator + "test"
     */
    public void downloadFile(String url, final String savePatch, final String fileName) {
        NetApi netApi = getHttpApi(NetApi.class);
        Call<ResponseBody> call = netApi.downloadImg(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Bitmap bitmap = null;
                byte[] bys;
                try {
                    bys = response.body().bytes();
                    bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);

                    try {
                        saveImg(bitmap, savePatch, fileName);
                        String savePath = savePatch + File.separator + fileName + ".jpg";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    bitmap.recycle();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        /*HttpManager.getInstance().getHttpApi(NetApi.class)
                .downloadImg(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Bitmap bitmap = null;
                        byte[] bys;
                        try {
                            bys = responseBody.bytes();
                            bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);

                            try {
                                FileUtils.saveImg(bitmap, savePatch, fileName);
                                String savePath = savePatch + File.separator + fileName + ".jpg";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //你的处理
                    }

                    @Override
                    public void onComplete() {
                        //你的处理
                    }
                });*/
    }

    /**
     * 保存图片到SD卡
     *
     * @param bm         图片bitmap对象
     * @param floderPath 下载文件保存目录
     * @param fileName   文件名称(不带后缀)
     */
    public void saveImg(Bitmap bm, String floderPath, String fileName) throws IOException {
        //如果不保存在sd下面下面这几行可以不加
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            MyLog.e("SD卡异常");
            return;
        }

        File folder = new File(floderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String savePath = folder.getPath() + File.separator + fileName + ".jpg";
        File file = new File(savePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        MyLog.d(savePath + " 保存成功");
        bos.flush();
        bos.close();
    }


    Bitmap bitmap = null;
    private static final String BASE_URL4 = " ";

    private void downLoadImg() {


        /*new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }).start();*/

//OK设置请求超时时间，读取超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL4)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        NetApi apiService = retrofit.create(NetApi.class);
        Observable<ResponseBody> observable = apiService.downLoadImg("34264.jpg");


        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<ResponseBody>() {

                    @Override
                    public void onNext(ResponseBody value) {
                        byte[] bys = new byte[0];
                        try {
                            bys = value.bytes(); //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程
                            bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);

                            try {//调用saveFile方法
                                saveFile(bitmap, Environment.getExternalStorageDirectory() + "/imgpic/");
                                Log.e("TAG", Environment.getExternalStorageDirectory() + "/imgpic/");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


    }


    //保存图片到SD卡
    public void saveFile(Bitmap bm, String fileName) throws IOException {

        String imgName = UUID.randomUUID().toString() + ".jpg"; //随机生成不同的名字
        File jia = new File(fileName);              //新创的文件夹的名字
        if (!jia.exists()) {   //判断文件夹是否存在，不存在则创建
            jia.mkdirs();
        }
        File file = new File(jia + "/" + imgName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);

        bos.flush();
        bos.close();

    }
}
