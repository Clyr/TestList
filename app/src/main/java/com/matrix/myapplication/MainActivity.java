package com.matrix.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.douyu.mvp.view.activity.HomeActivity;
import com.guard.NotiUtils;
import com.guard.VmMainActivity;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.igexin.sdk.PushManager;
import com.matrix.myapplication.activity.BigImageActivity;
import com.matrix.myapplication.activity.FPSGetActivity;
import com.matrix.myapplication.activity.FragmentActivity;
import com.matrix.myapplication.activity.LineChartActivity;
import com.matrix.myapplication.activity.LoadingActivity;
import com.matrix.myapplication.activity.Main4Activity;
import com.matrix.myapplication.activity.Main5Activity;
import com.matrix.myapplication.activity.Main6Activity;
import com.matrix.myapplication.activity.Main7Activity;
import com.matrix.myapplication.activity.MainGetFPSActivity;
import com.matrix.myapplication.activity.MarqueeActivity;
import com.matrix.myapplication.activity.NewTableActivity;
import com.matrix.myapplication.activity.RecycleViewActivity;
import com.matrix.myapplication.activity.SampleActivity;
import com.matrix.myapplication.activity.SendActivity;
import com.matrix.myapplication.activity.SqliteActivity;
import com.matrix.myapplication.activity.TableActivity;
import com.matrix.myapplication.activity.TreeListActivity;
import com.matrix.myapplication.baidumap.demo.BMapApiDemoMain;
import com.matrix.myapplication.cache.MainActivity3;
import com.matrix.myapplication.getuidemo.DemoIntentService;
import com.matrix.myapplication.getuidemo.DemoPushService;
import com.matrix.myapplication.getuidemo.GetuiSdkDemoActivity;
import com.matrix.myapplication.interfaceModel.HandlerCall;
import com.matrix.myapplication.interfaceclass.Setting;
import com.matrix.myapplication.interfaceclass.setSomeThing;
import com.matrix.myapplication.kotlin.KotlinActivity;
import com.matrix.myapplication.model.UpDateModel;
import com.matrix.myapplication.mvp.MVPActivity;
import com.matrix.myapplication.newactivity.RxBusActivity;
import com.matrix.myapplication.newactivity.ShareActivity;
import com.matrix.myapplication.newactivity.WebViewForBaiduMap;
import com.matrix.myapplication.receiver.HuaweiPushRevicer;
import com.matrix.myapplication.retrofit.RetrofitActivity;
import com.matrix.myapplication.rxjava.RxjavaActivity;
import com.matrix.myapplication.utils.ACacheUtils;
import com.matrix.myapplication.utils.ClidBoardUtils;
import com.matrix.myapplication.utils.DownloadAPK;
import com.matrix.myapplication.utils.MainHelper;
import com.matrix.myapplication.utils.MyLog;
import com.matrix.myapplication.utils.SPUtils;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.LoadingDialog;
import com.matrix.myapplication.view.SlideUnlockView;
import com.matrix.myapplication.view.captcha.SwipeCaptchaView;
import com.matrix.myapplication.view.timershaft.TimerShaftActivity;
import com.mm131.MM131Activity;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.IOException;
import java.util.Arrays;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.matrix.myapplication.receiver.HuaweiPushRevicer.ACTION_TOKEN;
import static com.matrix.myapplication.utils.MainHelper.mPermission;

//TODO 当前设置分支测试需要注意以下几点
// 1.注意分支代码提交时需要考虑是否需要全部Push    //有问题无法push修改到所有分支
// 2.右下角 Git:XXX 当前分支
// 3.注意分支切换

public class MainActivity extends Activity {

    private static final String TAG = Activity.class.getSimpleName();
    private TextView mTv;
    private boolean vpn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        skipTo();
//        setTranslucentStatus();
//        fullScreen(this);
//        statusBar(); //TODO 会导致应用打开之后 点击事件需要第二次点击才能触发
        TextView tx = findViewById(R.id.textview);
        // Lambda 表达式
        //标题按钮
        tx.setOnClickListener(v -> ToastUtils.showShort("TextView"));
        //更新
//        MainHelper.update(false);
        MainHelper.initialize(this);
        //AlertDialog
        findViewById(R.id.button).setOnClickListener(v -> MainHelper.alertDialogDiy1());
        //自定义提示窗体
        findViewById(R.id.button4).setOnClickListener(v -> {
            MainHelper.button4();
        });

        //加载1
        findViewById(R.id.button11).setOnClickListener(v ->
                startAct(SampleActivity.class)
        );
        //加载2
        findViewById(R.id.button12).setOnClickListener(v -> startAct(LoadingActivity.class));
        //加载3
        findViewById(R.id.button28).setOnClickListener(v -> {
            LoadingDialog.showLoading(MainActivity.this);
            mHandler.postDelayed(task, 3000);
        });
        //加载Ball
        findViewById(R.id.button29).setOnClickListener(v -> {
            LoadingDialog.showLoadingBall(MainActivity.this);
            mHandler.postDelayed(task, 5000);
        });
        //加载Ball
        findViewById(R.id.button292).setOnClickListener(v -> {
            LoadingDialog.showLoadingBall(MainActivity.this,R.style.ProgressDialogTransparent);
            mHandler.postDelayed(task, 3000);
        });

        //okhttputils
        findViewById(R.id.button1).setOnClickListener(v -> {//124.128.225.19:8081
            MainHelper.button1();
        });
        //OkhttpClient
        findViewById(R.id.button9).setOnClickListener(v -> {
            MainHelper.button9();
        });
        //Acheck存储
        findViewById(R.id.button2).setOnClickListener(v -> startAct(MainActivity3.class));
        //hwms登陆测试
        findViewById(R.id.button3).setOnClickListener(v -> {
            MainHelper.button3();
        });
        //GridView
        findViewById(R.id.button5).setOnClickListener(v -> startAct(Main4Activity.class));
        //Video
        findViewById(R.id.button8).setOnClickListener(v -> startAct(Main6Activity.class));
        //刮刮乐+图片进度条
        findViewById(R.id.button6).setOnClickListener(v -> startAct(Main5Activity.class));
        //ImageToBig
        findViewById(R.id.button7).setOnClickListener(v -> startAct(BigImageActivity.class));

        MainHelper.saveString();
        //ACacheUtils.saveString(MainActivity.this,"JSON",1000000000+"");
        mTv = findViewById(R.id.text);
        //Json
        findViewById(R.id.button10).setOnClickListener(v -> {
            int count = 0;
            //String string = SPUtils.getString(MainActivity.this, "JSON", "Null");
            String string = ACacheUtils.getString(MainActivity.this, "JSON");
            if (string != null) {
                count = Integer.parseInt(ACacheUtils.getString(MainActivity.this, "JSON"));
            }
            count++;
            ACacheUtils.saveString(MainActivity.this, "JSON", count + "");
            mTv.setText("Acheck: " + string);
            ToastUtils.showShort(string);
        });
        //Sqlite
        findViewById(R.id.button13).setOnClickListener(v -> startAct(SqliteActivity.class));
        //Tree_list
        findViewById(R.id.button14).setOnClickListener(v -> startAct(TreeListActivity.class));
        //雷达图
        findViewById(R.id.button15).setOnClickListener(v -> startAct(SendActivity.class));
        //滚动新闻
        findViewById(R.id.button16).setOnClickListener(v -> startAct(MarqueeActivity.class));
        //表格
        findViewById(R.id.button17).setOnClickListener(v -> startAct(TableActivity.class));
        //FPS
        findViewById(R.id.button18).setOnClickListener(v -> startAct(FPSGetActivity.class));
        //FPSGet
        findViewById(R.id.button19).setOnClickListener(v -> startAct(MainGetFPSActivity.class));
        //NewTable
        findViewById(R.id.button20).setOnClickListener(v -> startAct(NewTableActivity.class));
        //折线图
        findViewById(R.id.button21).setOnClickListener(v -> startAct(LineChartActivity.class));
        //测试PullToRefresh
        findViewById(R.id.button22).setOnClickListener(v -> startAct(Main7Activity.class));
        //测试调用三方软件
        findViewById(R.id.button23).setOnClickListener(v -> {
            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName("com.matrix.hims", "com.matrix.hims.activity.InitActivity");
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showLong("╮(╯▽╰)╭");
            }

                /*Intent intent = new Intent("android.intent.action.VIEW");
                ComponentName cn = new ComponentName("com.matrix.hims", "com.matrix.hims.activity.MainActivity");
                intent.putExtra("action_params"," id=123 name=测试一下");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
        });
        //测试调用Hbuilder-掌上运维
        findViewById(R.id.button24).setOnClickListener(v -> {
            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName("com.matrix.tramsh5", "io.dcloud.PandoraEntry");
                intent.putExtra("test", "测试");
                startActivity(intent);
                LoadingDialog.showLoading(MainActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showLong("╮(╯▽╰)╭");
            }
//                getIntent().getStringExtra("test");
        });
        String string = getIntent().getStringExtra("key");
        if (string != null && string != "") {
            ToastUtils.showLong(string);
        }
        //测试获取网络地址
        findViewById(R.id.button25).setOnClickListener(v -> {
            MainHelper.getNetIp();
//            ToastUtils.showLong(MainHelper.getLocalIpAddress());
        });
        //RecycleView
        findViewById(R.id.button31).setOnClickListener(v -> startAct(RecycleViewActivity.class));

        //todo 结束

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //短振动
        findViewById(R.id.button30).setOnClickListener(v -> {

//    long[] patter = {1000, 1000, 2000, 50};
//    vibrator.vibrate(patter,0);
//    vibrator.cancel();
//    vibrator.vibrate(10);
//    long[] patter = {1000, 1000, 2000, 50};
            long[] patter = {10, 10, 10, 10};
            vibrator.vibrate(patter, -1);
        });
        findViewById(R.id.button30).setOnLongClickListener(v -> {
            vibrator.cancel();
            return true;
        });
        //长振动
        findViewById(R.id.button32).setOnClickListener(v ->
                vibrator.vibrate(3000));

        findViewById(R.id.button32).setOnLongClickListener(v -> {
            vibrator.cancel();
            return true;
        });
        //Fragment 框架
        findViewById(R.id.button33).setOnClickListener(v -> startAct(FragmentActivity.class));
        //UpDate
        findViewById(R.id.button34).setOnClickListener(v ->
                MainHelper.update(new HandlerCall() {
                    @Override
                    public void handler() {
                        mHandler.postDelayed(task, 500);
                    }

                    @Override
                    public void setModle(UpDateModel model) {
                        mModel = model;
                    }
                })
        );
        //接口回调
        findViewById(R.id.button35).setOnClickListener(v -> {
//                setSomeThing someThing = new setSomeThing();
//                someThing.startSet(new Setting() {
//                    @Override
//                    public void setToast(String content) {
//                        ToastUtils.showShort(content);
//                    }
//                },"11111111111");
            new setSomeThing(new MySet("11111"));
        });
        //通知
        findViewById(R.id.button36).setOnClickListener(v ->
                MainHelper.sendNotification());
        findViewById(R.id.button36).setOnLongClickListener(v -> {
            MainHelper.clearNotification();
            //nm.cancel(0);
//                if (isNotificationEnabled(MainActivity.this)) {
//                    ToastUtils.showShort("开启");
//                } else {
//                    ToastUtils.showShort("关闭");
//                }
            return true;
        });
        //通知栏进度
        findViewById(R.id.button37).setOnClickListener(v -> MainHelper.sendProgressNotification());
        //MVPActivity
        findViewById(R.id.button38).setOnClickListener(v -> startAct(MVPActivity.class));
        //Retrofit
        findViewById(R.id.button39).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RetrofitActivity.class)));
        //Rxjava
        findViewById(R.id.button40).setOnClickListener(v ->
                startActivity(new Intent(this, RxjavaActivity.class)));

        //快捷方式通知数量提示 ShortcutBadger
        findViewById(R.id.button41).setOnClickListener(v -> {
            int badgeCount = 15;
            boolean b = ShortcutBadger.applyCount(this, badgeCount);//for 1.1.4+
            //ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3
            ToastUtils.showShort(b + "");
        });

        findViewById(R.id.button41).setOnLongClickListener(v ->
                        ShortcutBadger.removeCount(this) //for 1.1.4+
                //ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3
                // ShortcutBadger.applyCount(context, 0); //for 1.1.4+
                //ShortcutBadger.with(getApplicationContext()).count(0); //for 1.1.3
        );
        //VPN监测开关
        findViewById(R.id.button42).setOnClickListener(v -> {
            vpn = !vpn;
            ToastUtils.showShort(vpn ? "正在监测VPN" : "关闭监测VPN");
        });
        //Kotlin
        findViewById(R.id.button43).setOnClickListener(v -> startAct(KotlinActivity.class));
        //MM131
        findViewById(R.id.button44).setOnClickListener(v -> startAct(MM131Activity.class));
        //JPush
        findViewById(R.id.button45).setOnClickListener(v -> startAct(com.matrix.myapplication.jpushdemo.MainActivity.class));
        //GTPush
        findViewById(R.id.button46).setOnClickListener(v -> startAct(GetuiSdkDemoActivity.class));
        //BaiduMap
        findViewById(R.id.button47).setOnClickListener(v -> startAct(BMapApiDemoMain.class));
        //分享
        findViewById(R.id.button48).setOnClickListener(v -> startAct(ShareActivity.class));
        //finger 指纹验证
        findViewById(R.id.button49).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                ToastUtils.showShort("当前设备不支持该功能");
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                MainHelper.BiometricPrint();
            } else {
                MainHelper.Fingerprint();
            }

        });
        mHandler.postDelayed(task, 500);
        huaweiPushInit();
        //华为Push getToken
        findViewById(R.id.button50).setOnClickListener(v -> {
            getToken();
        });
        //分享华为 Token
        findViewById(R.id.button51).setOnClickListener(v -> {
            try {
                Intent qqIntent = new Intent(Intent.ACTION_SEND);
                qqIntent.setPackage("com.tencent.mobileqq");
                qqIntent.setType("text/plain");
                qqIntent.putExtra(Intent.EXTRA_TEXT, token);
                startActivity(qqIntent);
            } catch (Exception e) {

                if (e instanceof ActivityNotFoundException)
                    ToastUtils.showShort("请先安装QQ再进行尝试");
                else
                    ToastUtils.showShort("分享失败");
            }
        });
        //华为推送
        findViewById(R.id.button53).setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    MainHelper.sendHuaweiMsg(token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        });
        /* TODO 小米推送 */
        // 设置别名 //com.xiaomi.mipush.sdk.MiPushClient
        MiPushClient.setAlias(MainActivity.this, "001", null);
//        MiPushClient.setUserAccount(MainActivity.this, account, null);
        // 设置标签
//        MiPushClient.subscribe(MainActivity.this, topic, null);
//        MiPushClient.unsubscribe(MainActivity.this, topic, null);
        // 暂停推送
//        MiPushClient.pausePush(MainActivity.this, null);
//        MiPushClient.resumePush(MainActivity.this, null);
        //小米推送
        findViewById(R.id.button52).setOnClickListener(v -> {
            try {
                MainHelper.sendMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        //Music
        findViewById(R.id.button54).setOnClickListener(v -> {
            //permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, mPermission[2]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission[1], mPermission[2]}, 3);

            } else {
                startAct(com.music.ui.main.MainActivity.class);
            }

        });
        //守护进程
        findViewById(R.id.button55).setOnClickListener(v -> {
            startAct(VmMainActivity.class);
        });
        //守护进程Noti
        findViewById(R.id.button56).setOnClickListener(v -> {
            NotiUtils.show(this, "100000", "MainActivity", "MainActivityChannel");

        });
        //douyu直播 似乎api过时不管用了
        findViewById(R.id.button57).setOnClickListener(v -> {
            startAct(HomeActivity.class);
        });

        //TODO Lambda 表达式 进一步简化 ::
        //备注：v-> ()-> (a,b)-> 方法需要对应 v () (a,b)
        //非静态使用new Object 静态使用 Object
        findViewById(R.id.lambda).setOnClickListener(new MainHelper()::getMsg);
        findViewById(R.id.lambda).setOnClickListener(MainHelper::getView);
        //RxBus
        findViewById(R.id.button58).setOnClickListener(v ->
            startAct(RxBusActivity.class)
        );
        PushManager.getInstance().initialize(this, DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this, DemoIntentService.class);
        findViewById(R.id.button59).setOnClickListener(v -> {
            PushManager.getInstance().bindAlias(this, "b3b65f5a85226c11100");
        });
        //获取电量
        findViewById(R.id.button60).setOnClickListener(v -> {
//            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
//            int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
//            ToastUtils.showShort("当前电量， "+battery+" %");
//
//            registerReceiver(mbatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//            unregisterReceiver(mbatteryReceiver);
            getBatteryState();
        });
        findViewById(R.id.button61).setOnClickListener(v -> {
            startAct(WebViewForBaiduMap.class);
        });

        //startService(new Intent(this, BatteryService.class));
        /*findViewById(R.id.button62).setOnClickListener(v -> {
            startAct(TopMainActivity.class);
        });*/
        findViewById(R.id.button62).setVisibility(View.GONE);


        //This Branch Test
        findViewById(R.id.button62).setOnClickListener(v -> {
            ToastUtils.showShort("Branch 分支测试");
        });

        findViewById(R.id.button63).setOnClickListener(v -> {
            ClidBoardUtils.setCopy(this, "Hello");
        });

        findViewById(R.id.button64).setOnClickListener(v -> {
            ToastUtils.showShort(ClidBoardUtils.getCopy(this));
        });

        findViewById(R.id.button65).setOnClickListener(v -> {
            startAct(TimerShaftActivity.class);
        });
        findViewById(R.id.button66).setOnClickListener(v -> {
            showCaptchaDialog();
        });

        findViewById(R.id.button67).setOnClickListener(v -> {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("Web site")
                    .setLongLabel("Open the web site")
                    .setIcon(Icon.createWithResource(this, R.drawable.test))
                    .setIntent(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/Clyr/TestList.git")))
                    .build();

            shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut));
            /*if (Build.VERSION.SDK_INT >= 25) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "shortcut_id_search")
                        .setShortLabel(getString(R.string.lable_shortcut_static_search_disable))

                        .setLongLabel(getString(R.string.lable_shortcut_static_search_disable))
                        .setIcon(Icon.createWithResource(this, R.drawable.test))
                        .setIntent(intent)
                        .build();

                ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
                //这样就可以通过长按图标显示出快捷方式了
                shortcutManager.setDynamicShortcuts(Arrays.asList(shortcutInfo));

            }*/
        });


        SlideUnlockView slideUnlockView = (SlideUnlockView) findViewById(R.id.slideUnlockView);

        // 设置滑动解锁-解锁的监听
        slideUnlockView.setOnUnLockListener(unLock -> {
            // 如果是true，证明解锁
            if (unLock) {
                // 启动震动器 100ms
                vibrator.vibrate(100);
                // 当解锁的时候，执行逻辑操作，在这里仅仅是将图片进行展示
                // 重置一下滑动解锁的控件
                slideUnlockView.reset();
                // 让滑动解锁控件消失
//                slideUnlockView.setVisibility(View.GONE);
                ToastUtils.showLong("Open");
            }
        });
    }

    private void showCaptchaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dlg_activity_main, null);
        final SwipeCaptchaView mSwipeCaptchaView = (SwipeCaptchaView) view.findViewById(R.id.swipeCaptchaView);

        final SeekBar mSeekBar = (SeekBar) view.findViewById(R.id.dragBar);
        builder.setView(view);
        final AlertDialog alertDialog = builder.show();
        view.findViewById(R.id.btnChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeCaptchaView.createCaptcha();
                mSeekBar.setEnabled(true);
                mSeekBar.setProgress(0);
            }
        });
        mSwipeCaptchaView.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                Toast.makeText(MainActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(false);
                alertDialog.dismiss();
            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                Log.d(TAG, "matchFailed() called with: swipeCaptchaView = [" + swipeCaptchaView + "]");
                Toast.makeText(MainActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                swipeCaptchaView.resetCaptcha();
                mSeekBar.setProgress(0);
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
                mSwipeCaptchaView.matchCaptcha();
            }
        });

        //测试从网络加载图片是否ok
        Glide.with(this)
                .load(R.drawable.bg_slide_unblock)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                    }
                });

    }

    private void getBatteryState() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = registerReceiver(null, ifilter);
        //如果设备正在充电，可以提取当前的充电状态和充电方式（无论是通过 USB 还是交流充电器），如下所示：
        // Are we charging / charged?
        int status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        // How are we charging?
        int chargePlug = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int level = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int batteryPct = level * 100 / scale;
        if (batteryPct >= 100) {
            MainHelper.sendNotification(this);
        }
        String nowbattery = "当前电量 " + batteryPct + "%";

        if (isCharging) {
            if (usbCharge) {
                Toast.makeText(MainActivity.this, "USB连接！ " + nowbattery, Toast.LENGTH_SHORT).show();
            } else if (acCharge) {
                Toast.makeText(MainActivity.this, "通过电源充电中！ " + nowbattery, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "未连接USB线！ " + nowbattery, Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver mbatteryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    ToastUtils.showShort("充电中!");
                }
            } else {
                ToastUtils.showShort("未充电!");
            }
        }
    };

    private void skipTo() {
        Intent intent = getIntent();
        if (intent != null) {
            String tag = intent.getStringExtra("tag");
            if ("share".equals(tag)) {
                startAct(ShareActivity.class);
            }
        }
    }

    String token = "";

    private void huaweiPushInit() {
        /**
         * SDK连接HMS
         */

        HMSAgent.connect(this, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                MyLog.d("HMS connect end:" + rst);
            }
        });
        HuaweiPushRevicer.registerPushCallback(new HuaweiPushRevicer.IPushCallback() {
            @Override
            public void onReceive(Intent intent) {
                token = intent.getStringExtra(ACTION_TOKEN);
                MyLog.d(token);

            }
        });
    }

    /**
     * 获取token
     */

    private void getToken() {
        MyLog.d("get token: begin");
        HMSAgent.Push.getToken(new GetTokenHandler() {
            @Override
            public void onResult(int rst) {
                MyLog.d("get token: end = " + rst);
                ToastUtils.showShort("token: " + rst);
            }
        });
    }

    private void startAct(Class<?> tClass) {
        startActivity(new Intent(this, tClass));
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

    public UpDateModel mModel;

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
            case 3:
                startAct(com.music.ui.main.MainActivity.class);
                break;
            default:
                break;
        }


    }


    Handler mHandler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            LoadingDialog.cancelLoading();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (vpn)
            if (MainHelper.isVpnUsed()) {
                ToastUtils.showShort("使用VPN中...\n" + MainHelper.getIPAddress(this));
            } else {
                ToastUtils.showShort("未使用使用VPN\n" + MainHelper.getIPAddress(this));
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LoadingDialog.cancelLoading();
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


    private static final int REQUST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(getBaseContext(), "识别成功!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "识别失败!", Toast.LENGTH_SHORT).show();
        }
    }

// RXBus Events

   /* @Override
    protected Subscription subscribeEvents() {
        return RxBus.getInstance().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof PlaySongEvent) {
                            RxBus.getInstance().post(new PlayListCreatedEvent(playList));
                        }
                    }
                })
                .subscribe(RxBus.defaultSubscriber());
    }*/

}
