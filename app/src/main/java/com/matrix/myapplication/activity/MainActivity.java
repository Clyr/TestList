package com.matrix.myapplication.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.matrix.myapplication.R;
import com.matrix.rxjava.RxjavaActivity;
import com.matrix.myapplication.cache.MainActivity3;
import com.matrix.myapplication.interfaceclass.Setting;
import com.matrix.myapplication.interfaceclass.setSomeThing;
import com.matrix.myapplication.model.upDateModel;
import com.matrix.myapplication.mvp.MVPActivity;
import com.matrix.myapplication.retrofit.RetrofitActivity;
import com.matrix.myapplication.utils.ACacheUtils;
import com.matrix.myapplication.utils.APKVersionUtils;
import com.matrix.myapplication.utils.DownloadAPK;
import com.matrix.myapplication.utils.MyLog;
import com.matrix.myapplication.utils.OkHolder;
import com.matrix.myapplication.utils.SPUtils;
import com.matrix.myapplication.utils.SystemStatusManager;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.LoadingDialog;
import com.matrix.myapplication.view.TextDialog;
import com.matrix.myapplication.view.UpdataDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends Activity {

    private TextView mTv;
    private TextDialog mDialog;
    private upDateModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setTranslucentStatus();
//        fullScreen(this);
//        statusBar(); //TODO 会导致应用打开之后 点击事件需要第二次点击才能触发
        TextView tx = findViewById(R.id.textview);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("TextView");
            }
        });

        update(false);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDiy1();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//124.128.225.19:8081
                Map<String, String> mapData = new HashMap<>();
                mapData.put("act", "version");
                mapData.put("oldVersionCode", "2017111111");
                OkHttpUtils.get().url("http://124.128.225.19:8081/hims/mobile").params(mapData).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("response", e.toString());
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("response", response);
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity3.class));
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> mapData = new HashMap<>();
                mapData.put("act", "login");
                mapData.put("email", "jn01");
                mapData.put("pwd", "000000");

                OkHttpUtils.get().url("http://192.168.0.80:8080/hims/mobile").params(mapData).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("response", e.toString());
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("response", response);
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(3600, TimeUnit.SECONDS)
                        .readTimeout(3600, TimeUnit.SECONDS)
                        .writeTimeout(3600, TimeUnit.SECONDS).build();
                FormBody.Builder body = new FormBody.Builder();
//                body.add("act", "login").add("email", "jn01").add("pwd", "000000");
//                body.add("act","loadmyinfo").add("loginname","jn01");
                Map<String, String> mapData = new HashMap<>();
                mapData.put("act", "loadmyinfo");
                mapData.put("pwd1", "");
                mapData.put("loginname", "jn01");
                String murl = "";
                for (Map.Entry<String, String> en : mapData.entrySet()) {
                    if (en.getKey() == null || en.getValue() == null) {
                        continue;
                    }
                    body.add(en.getKey(), en.getValue());
                    murl += en.getKey() + "=" + en.getValue() + "&";
                }
                Log.d("okhttp3url", murl);
                Request.Builder builder = new Request.Builder().url("http://172.31.0.23:8080/hims/mobile").post(body.build());
                Call call = client.newCall(builder.build());
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (e instanceof SocketTimeoutException) {//判断超时异常
//                    hideLoadingDialog();

                        }
                        if (e instanceof ConnectException) {//判断连接异常，我这里是报Failed to connect to 10.7.5.144

                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().toString();
                        String string2 = response.body().string();
                        Log.d("okhttp3", string);
                        Log.d("okhttp3", string2);
                    }
                });


            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new TextDialog(MainActivity.this, R.layout.textdialog,
                        new int[]{R.id.cancel, R.id.query}, new Setback() {
                    @Override
                    public void backgroundAlpha(float bgAlpha) {
                        setBackground(bgAlpha);
                    }
                });
                mDialog.show();
                mDialog
                        .setOnCenterItemClickListener(new TextDialog.OnCenterItemClickListener() {
                            @Override
                            public void OnCenterItemClick(TextDialog dialog, View view) {
                                switch (view.getId()) {
                                    case R.id.cancel:

                                        break;
                                    case R.id.query:
                                        dialogShow();
                                        break;
                                }
                                mDialog.dismiss();
                            }
                        });

            }

        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               Toast.makeText(MainActivity.this,getUpdateLog(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Main4Activity.class));
            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main5Activity.class));
            }
        });
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BigImageActivity.class));
            }
        });
        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main6Activity.class));
            }
        });
        saveString();
//        ACacheUtils.saveString(MainActivity.this,"JSON",1000000000+"");
        mTv = (TextView) findViewById(R.id.text);
        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
//                String string = SPUtils.getString(MainActivity.this, "JSON", "Null");
                String string = ACacheUtils.getString(MainActivity.this, "JSON");
                if (string != null) {
                    count = Integer.parseInt(ACacheUtils.getString(MainActivity.this, "JSON"));
                }
                count++;
                ACacheUtils.saveString(MainActivity.this, "JSON", count + "");
                mTv.setText("Acheck: " + string);
            }
        });
        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SampleActivity.class));
            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoadingActivity.class));
            }
        });
        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SqliteActivity.class));
            }
        });
        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TreeListActivity.class));
            }
        });
        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SendActivity.class));
            }
        });
        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MarqueeActivity.class));
            }
        });
        findViewById(R.id.button17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TableActivity.class));
            }
        });
        findViewById(R.id.button18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FPSGetActivity.class));
            }
        });
        findViewById(R.id.button19).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainGetFPSActivity.class));
            }
        });
        findViewById(R.id.button20).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewTableActivity.class));
            }
        });
        findViewById(R.id.button21).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LineChartActivity.class));
            }
        });
        findViewById(R.id.button22).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main7Activity.class));
            }
        });
        findViewById(R.id.button23).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName("com.matrix.hims", "com.matrix.hims.activity.InitActivity");
                startActivity(intent);

                /*Intent intent = new Intent("android.intent.action.VIEW");
                ComponentName cn = new ComponentName("com.matrix.hims", "com.matrix.hims.activity.MainActivity");
                intent.putExtra("action_params"," id=123 name=测试一下");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/

            }
        });
        findViewById(R.id.button24).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName("com.matrix.tramsh5", "io.dcloud.PandoraEntry");
                intent.putExtra("test", "测试");
                startActivity(intent);
                LoadingDialog.showLoading(MainActivity.this);
//                getIntent().getStringExtra("test");
            }
        });
        String string = getIntent().getStringExtra("key");
        if (string != null && string != "") {
            ToastUtils.showLong(string);
        }
        findViewById(R.id.button25).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLong(getLocalIpAddress());
                getNetIp();
            }
        });
        findViewById(R.id.button26).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnyOfficeActivity.class));
            }
        });
        findViewById(R.id.button27).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnyOfficeActivity2.class));
            }
        });

        Intent intent = new Intent();
        intent.setClass(this, AnyOfficeActivity2.class);
        //todo 结束

        findViewById(R.id.button28).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.showLoading(MainActivity.this);
                mHandler.postDelayed(task, 3000);
            }
        });
        findViewById(R.id.button29).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialog.showLoadingBall(MainActivity.this);
                mHandler.postDelayed(task, 5000);
            }
        });

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        findViewById(R.id.button30).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//    long[] patter = {1000, 1000, 2000, 50};
//    vibrator.vibrate(patter,0);
//    vibrator.cancel();
//    vibrator.vibrate(10);
//    long[] patter = {1000, 1000, 2000, 50};
                long[] patter = {10, 10, 10, 10};
                vibrator.vibrate(patter, -1);
            }
        });
        findViewById(R.id.button30).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.cancel();
                return true;
            }
        });
        findViewById(R.id.button32).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(3000);
            }
        });
        findViewById(R.id.button32).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.cancel();
                return true;
            }
        });
        findViewById(R.id.button31).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecycleViewActivity.class));
            }
        });
        findViewById(R.id.button33).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FragmentActivity.class));
            }
        });
        findViewById(R.id.button34).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(true);
            }
        });
        findViewById(R.id.button35).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSomeThing someThing = new setSomeThing();
//                someThing.startSet(new Setting() {
//                    @Override
//                    public void setToast(String content) {
//                        ToastUtils.showShort(content);
//                    }
//                },"11111111111");
                new setSomeThing(new MySet("11111"));

            }
        });


        findViewById(R.id.button36).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        findViewById(R.id.button36).setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onLongClick(View v) {
                clearNotification();
                //nm.cancel(0);
//                if (isNotificationEnabled(MainActivity.this)) {
//                    ToastUtils.showShort("开启");
//                } else {
//                    ToastUtils.showShort("关闭");
//                }
                return true;
            }
        });
        findViewById(R.id.button37).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                sendProgressNotification();
            }
        });
        findViewById(R.id.button38).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MVPActivity.class));
            }
        });
        findViewById(R.id.button38).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MVPActivity.class));
            }
        });

        // Lambda 表达式
        findViewById(R.id.button39).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RetrofitActivity.class))
        );

        findViewById(R.id.button40).setOnClickListener(v-> startActivity(new Intent(this, RxjavaActivity.class)));

    }

    private void alertDialogDiy1() {
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
        ab.setMessage("message-第一个弹窗样式")
                .setTitle("title")
                .setPositiveButton("第二个", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //使用message可以显示全部字符串
                        AlertDialog.Builder ab1 = new AlertDialog.Builder(MainActivity.this);
                        ab1.setMessage("message-这是第二个弹窗样式加长（Please install the Android Support Repository from the Android SDK Manager.\n" +
                                "<a href=\"openAndroidSdkManager\">Open Android SDK Manager</a>）\n")
                                .setPositiveButton("确定", null)
                                .setNegativeButton("取消", null)
                                .show();
                    }
                })
                .setNegativeButton("第三个", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //使用title字体略大但是只能显示部分信息大约char=31
                        AlertDialog.Builder ab1 = new AlertDialog.Builder(MainActivity.this);
                        ab1.setTitle("message-这是第三个弹窗样式加长（Please install the Android Support Repository from the Android SDK Manager.\n" +
                                "<a href=\"openAndroidSdkManager\">Open Android SDK Manager</a>）\n")
                                .setPositiveButton("确定", null)
                                .setNegativeButton("取消", null)
                                .show();
                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    class MySet implements Setting {

        public MySet(String string) {
            setToast(string);
        }

        @Override
        public void setToast(String content) {
            ToastUtils.showShort(content);
        }
    }

    private UpdataDialog updataDialog;
    private TextView tvmsg;
    private TextView tvcode;

    private void update(final boolean b) {
        //TODO 后需添加打包之后自动配置 update.json
        if (b)
            LoadingDialog.showLoadingBall(MainActivity.this);
        String url = "https://TomcatIp/UpDate/update.json";
        Map<String, String> map = new HashMap<>();
        map.put("null", "null");
        OkHolder.get(url, map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (b) {
                    mHandler.postDelayed(task, 500);
                    ToastUtils.showShort("获取更新失败！");
                }
            }

            @Override
            public void onResponse(String response, int id) {
                if (b)
                    mHandler.postDelayed(task, 500);
                MyLog.d(response);
                upDateModel model = new Gson().fromJson(response, upDateModel.class);
                String versionName = APKVersionUtils.getVersionName(MainActivity.this);
                String versionCode = APKVersionUtils.getVersionCode(MainActivity.this) + "";
                MyLog.d(versionCode + "--------" + versionName);
                try {
                    String[] split = versionName.split("\\.");

                    if (Integer.parseInt(model.getVersion_main()) > Integer.parseInt(split[0])) {
                        loadNewApk(model);
                    } else {
                        if (Integer.parseInt(model.getVersion_minor()) > Integer.parseInt(split[1])) {
                            loadNewApk(model);
                        } else {
                            if (Integer.parseInt(model.getVersion_revise()) > Integer.parseInt(split[2])) {
                                loadNewApk(model);
                            } else {
                                if (b) {
                                    ToastUtils.showShort("当前已是最新版本！");
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MyLog.d(e.toString());
                    if (b)
                        ToastUtils.showShort("获取更新失败！");
                }

            }
        });

    }

    private void loadNewApk(final upDateModel model) {
        updataDialog = new UpdataDialog(this,
                R.layout.dialog_updataversion,
                new int[]{R.id.dialog_sure});

        updataDialog.show();
        tvmsg = (TextView) updataDialog
                .findViewById(R.id.updataversion_msg);
        tvcode = (TextView) updataDialog
                .findViewById(R.id.updataversioncode);
        tvcode.setText(model.getVersion_main() + "." + model.getVersion_minor() + "." + model.getVersion_revise());
        tvmsg.setText(strFormat(model.getLog()));
        updataDialog
                .setOnCenterItemClickListener(new UpdataDialog.OnCenterItemClickListener() {
                    @Override
                    public void OnCenterItemClick(
                            UpdataDialog dialog, View view) {
                        switch (view.getId()) {
                            case R.id.dialog_sure:
                                /*Intent updateIntent = new Intent(
                                        MainActivity.this,
                                        AppUpgradeService.class);
                                updateIntent.putExtra("name",
                                        model.getName());
                                updateIntent.putExtra("url",
                                        "http://192.168.32.111:8080/MyApplication/UpDate/");
                                startService(updateIntent);*/
                                if (ContextCompat.checkSelfPermission(MainActivity.this, mPermission[1]) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{mPermission[1], mPermission[2]}, 5);
                                    mModel = model;
                                } else {
                                    DownloadAPK dl = new DownloadAPK(getApplicationContext());
                                    dl.downloadAPK("http://192.168.32.111:8080/MyApplication/UpDate/" + model.getName(),
                                            model.getName());

                                    ToastUtils.showShort("正在后台下载...");
                                    SPUtils.saveBoolean(MainActivity.this,
                                            "ISSHOWALERT", false);
                                }


                                break;
                        }
                        updataDialog.dismiss();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 5:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted 授予权限
                    DownloadAPK dl = new DownloadAPK(getApplicationContext());
                    dl.downloadAPK("http://192.168.32.111:8080/MyApplication/UpDate/" + mModel.getName(),
                            mModel.getName());

                    ToastUtils.showShort("正在后台下载...");
                    SPUtils.saveBoolean(MainActivity.this,
                            "ISSHOWALERT", false);
                } else {
                    // Permission Denied 权限被拒绝
                    Toast.makeText(MainActivity.this, "Permission Denied",
                            Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }


    }

    private static String strFormat(List<upDateModel.Log> str) {
        String result = "";
        if (str == null) {
            return result;
        }
        for (int i = 0; i < str.size(); i++) {
            if (!TextUtils.isEmpty(str.get(i).getLogContent())) {
                result += (i + 1) + "." + str.get(i).getLogContent() + "\n";
            }
        }

        return result;
    }

    Handler mHandler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            LoadingDialog.cancelLoading();
        }
    };

    @SuppressLint("ResourceAsColor")
    private void statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(R.color.blue9);
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
            }
        }*/


    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if(hasFocus&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            decorView.setSystemUiVisibility(option);
//        }
//    }

    public static boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    MyLog.d("isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isVpnUsed()) {
            ToastUtils.showShort("使用VPN中...\n" + getIPAddress(this));
        } else {
            ToastUtils.showShort("未使用使用VPN\n" + getIPAddress(this));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoadingDialog.cancelLoading();
    }

    private void saveString() {
        List<String> list = new ArrayList<>();
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        String string = JSONObject.toJSONString(list);
        SPUtils.saveString(MainActivity.this, "Json", string);
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        // 透明状态栏
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.blue9);
        getWindow().getDecorView().setFitsSystemWindows(true);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void dialogShow() {
        final View view = (LinearLayout) getLayoutInflater().inflate(
                R.layout.dialog_edittext, null);
        EditText editText = (EditText) view.findViewById(R.id.edittext);
        editText.setText("1111111111111");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("这是第二个")
                .setMessage("这是第二个")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private static String mString = "";

    public static String getUpdateLog() {

        OkHttpUtils.get().url("http://124.128.225.19:8081/hims/AndroidUpdate/UpdateLog.txt")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("Exception", e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                mString = response;
                Log.e("response", response);
            }
        });
        return mString;
    }

    @SuppressLint("ResourceType")
    public void setBackground(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setWindowAnimations(R.anim.anim_cancel);
        getWindow().setAttributes(lp);
    }

    public interface Setback {//使用接口调用setBackground方法设置透明背景

        void backgroundAlpha(float bgAlpha);
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    /**
     * 获取外网的IP(要访问Url，要放到后台线程里处理)
     *
     * @param @return
     * @return String
     * @throws
     * @Title: GetNetIp
     * @Description:
     */
    public static String GetNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL("http://ip168.com/");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");

                Pattern pattern = Pattern
                        .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Matcher matcher = pattern.matcher(strber.toString());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (httpConnection != null)
                    httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ipLine;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                Enumeration<InetAddress> enIp = ni.getInetAddresses();
                while (enIp.hasMoreElements()) {
                    InetAddress inet = enIp.nextElement();
                    if (!inet.isLoopbackAddress()
                            && (inet instanceof Inet4Address)) {
                        return inet.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "0";
    }

    private void getNetIp() {
        OkHttpUtils.get().url("http://pv.sohu.com/cityjson?ie=utf-8").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.e(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                MyLog.d(response);
                if (response != null) {
                    int start = response.indexOf("{");
                    int end = response.indexOf("}") + 1;
                    String string = response.substring(start, end);
                    MyLog.d(string);
                    Gson gson = new Gson();
                    ipsohu ipsohu = gson.fromJson(string, ipsohu.class);
                    MyLog.d(ipsohu.cip);
                    ToastUtils.showLong("外网IP：" + ipsohu.cip);
                }
            }
        });
    }

    class ipsohu {
        private String cid;//370100
        private String cip;//211.137.204.197
        private String cname;//	山东省济南市
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static String[] mPermission = {
            android.Manifest.permission.CAMERA,//相机
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储写
            android.Manifest.permission.READ_EXTERNAL_STORAGE,//存储读
            android.Manifest.permission.RECORD_AUDIO,//录音
            android.Manifest.permission.ACCESS_FINE_LOCATION//GPS

    };

    public static void getPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, mPermission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{mPermission[0]}, 5);
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{mPermission[1], mPermission[2]}, 5);
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[3]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{mPermission[3]}, 5);
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[4]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{mPermission[4]}, 5);
        }
    }

    private static final int NOTIFICATION_ID = 1001;

    private void sendNotification() {
        //1、NotificationManager
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /** 2、Builder->Notification
         *  必要属性有三项
         *  小图标，通过 setSmallIcon() 方法设置
         *  标题，通过 setContentTitle() 方法设置
         *  内容，通过 setContentText() 方法设置*/
        Intent inte = new Intent(this, MainActivity.class);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, inte, 0);
        long[] vibrate = new long[]{0, 500, 1000, 1500};

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentInfo("Content info")
                .setContentTitle("TestListTitle")//设置通知标题
                .setContentText("Notification ContentText")//设置通知内容
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.test))
                .setSmallIcon(R.drawable.test)//不能缺少的一个属性
                .setSubText("Subtext")
                .setTicker("滚动消息......")
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + R.raw.fadeout))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001", "TestList通知", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }

        Notification noti = builder.build();
        noti.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        //3、manager.notify()
        manager.notify(NOTIFICATION_ID, noti);
    }


    private void clearNotification() {
        //单利的系统服务
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
    }

    private static final int NOTIFICATION_ID3 = 1005;

    private void sendProgressNotification() {
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.test)
                .setContentTitle("TestList")
                .setContentText("正在下载...")
                .setProgress(1000, 10, true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("005", "new_download_channel", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder.setChannelId("005");
        }

        Notification n = builder.build();
        n.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        manager.notify(NOTIFICATION_ID3, n);
        //每隔1秒更新进度条进度
        //启动工作线程
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Notification n = builder.build();
                //manager.notify(NOTIFICATION_ID3, n);
                for (int i = 1; i <= 1000; i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //发通知
                    builder.setProgress(1000, i * 1, false);
                    manager.notify(NOTIFICATION_ID3, n);
                }
                //更新通知内容
                manager.cancel(NOTIFICATION_ID3);
                builder.setProgress(0, 0, false);
                builder.setContentText("下载完毕");
//                Notification n = builder.build();
                n.flags = Notification.FLAG_AUTO_CANCEL;//不能删除通知
                manager.notify(NOTIFICATION_ID3, n);
            }
        }.start();
    }


    //----- 废品代码参考区域 ----------------------------------------------------------------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMsg() {
        Intent inte = new Intent(this, MainActivity.class);
        // intent.setAction(Intent.ACTION_CALL);
        // intent.setData(Uri.parse("tel://17353461529"));
        //即将要执行的意图对象
        final PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, inte, 0);

        //拿到系统的远程服务----AIDL
        final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //建造者模式  -  链式调用
        long[] vibrate = new long[]{0, 500, 1000, 1500};
        final Notification notification = new Notification.Builder(MainActivity.this)
                .setTicker("有新的消息到来。。。")//提示内容文本
                .setAutoCancel(true)//点击通知后自动清除
                .setContentTitle("TestList")
                .setContentText("Notification")
                .setSmallIcon(R.drawable.test)
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + R.raw.fadeout))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .build();
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                .setTicker("有新的消息到来。。。")//提示内容文本
                .setAutoCancel(true)//点击通知后自动清除
                .setContentTitle("TestList")
                .setContentText("Notification")
                .setSmallIcon(R.drawable.test)
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + R.raw.fadeout))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000);//闪光灯 呼吸灯
        //FLAG_AUTO_CANCEL-设置点击的时候取消当前通知
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        //显示一个通知
        //nm.notify(0, notification);
        //nm.notify(0,mBuilder.build());
//                NotificationManager manager=
//                        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                Notification notification =new NotificationCompat.Builder(MainActivity.this,"default")
//                        .setContentTitle("测试notification")
//                        .setContentText("This is content text")
//                        .setWhen(System.currentTimeMillis())
//                        .setSmallIcon(R.mipmap.ic_launcher_round)
//                        .build();
//                manager.notify(1,notification);

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //ChannelId为"001",ChannelName为"my_channel"
        NotificationChannel channel = new NotificationChannel("1",
                "my_channel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true); //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.GREEN); //小红点颜色
        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        manager.createNotificationChannel(channel);

        //同时，Notification.Builder需要多设置一个
        //new Notification.Builder(MainActivity.this).setChannelId("001");
        Notification notification2 = new Notification.Builder(MainActivity.this)
                .setTicker("有新的消息到来。。。")//提示内容文本
                .setAutoCancel(true)//点击通知后自动清除
                .setContentTitle("TestList")
                .setContentText("Notification")
                .setSmallIcon(R.drawable.test)
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + R.raw.fadeout))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .setChannelId("001")
                .build();
        manager.notify(1, notification2);
    }
}
