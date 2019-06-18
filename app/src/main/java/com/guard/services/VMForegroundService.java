package com.guard.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import com.matrix.myapplication.MainActivity;
import com.matrix.myapplication.R;

import static android.app.NotificationManager.IMPORTANCE_HIGH;


/**
 * 正常的系统前台进程，会在系统通知栏显示一个Notification通知图标，因为这个通知的关系，应用的进程优先级是比较高的，
 * 一般系统的内存回收是无法杀死的
 *
 * Created by lzan13 on 2017/3/7.
 */
public class VMForegroundService extends Service {

    private final static String TAG = VMForegroundService.class.getSimpleName();

    // 通知栏发送的通知 id
    private final static int NOTIFY_ID = 1000;

    @Override
    public void onCreate() {
        Log.i(TAG, "VMForegroundService->onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "VMForegroundService->onStartCommand");

        /*// 创建个通知，这样可以提高服务的优先级
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.test);
        builder.setContentTitle("这是一条前台通知");
        builder.setContentText("这是一条前台通知，表示当前程序有一个前台服务在运行");
        builder.setContentInfo("前台服务");
        builder.setWhen(System.currentTimeMillis());
        Intent activityIntent = new Intent(this, VmMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("11002", "守护进程", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            channel.setSound(null,null);
            manager.createNotificationChannel(channel);
            builder.setChannelId("11002");
        }

        Notification notification = builder.build();
        manager.notify(NOTIFY_ID, notification);
        // 开启前台通知
        startForeground(NOTIFY_ID, notification);*/

        String CHANNEL_ONE_ID = "com.matrix.se";
        String CHANNEL_ONE_NAME = "Channel One";
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getApplicationContext(),CHANNEL_ONE_ID)
                    .setChannelId(CHANNEL_ONE_ID)
                    .setContentTitle("VMForegroundService")
                    .setContentText("VMForegroundService ContentText")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(icon)
                    .build();
        }

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        startForeground(NOTIFY_ID, notification);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("onBind 未实现");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "VMForegroundService->onDestroy");
        super.onDestroy();
    }
}