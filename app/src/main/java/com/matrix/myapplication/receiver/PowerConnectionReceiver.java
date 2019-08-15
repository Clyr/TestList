package com.matrix.myapplication.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.BatteryManager;

import com.matrix.myapplication.MainActivity;
import com.matrix.myapplication.R;
import com.matrix.myapplication.utils.ToastUtils;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.matrix.myapplication.utils.MainHelper.NOTIFICATION_ID;

/**
 * Created by M S I of clyr on 2019/8/12.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int batteryPct = level * 100 / scale;

        if (batteryPct>=100){
            sendNotification(context);
        }else{
            ToastUtils.showShort("当前电量："+batteryPct);
        }
    }


    public static void sendNotification(Context context) {
        //1、NotificationManager
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        /** 2、Builder->Notification
         *  必要属性有三项
         *  小图标，通过 setSmallIcon() 方法设置
         *  标题，通过 setContentTitle() 方法设置
         *  内容，通过 setContentText() 方法设置*/
        Intent inte = new Intent(context, MainActivity.class);
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, inte, 0);
        long[] vibrate = new long[]{0, 500, 1000, 1500};

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentInfo("TestList")
                .setContentTitle("TestList")//设置通知标题
                .setContentText("电源已充满")//设置通知内容
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.test))
                .setSmallIcon(R.drawable.test)//不能缺少的一个属性 通知栏小图标 默认圆头安卓很丑
                .setSubText("Subtext")
                .setTicker("滚动消息......")
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + R.raw.fadeout))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001", "TestList电源通知", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }

        Notification noti = builder.build();
       // noti.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        noti.flags = Notification.FLAG_AUTO_CANCEL;//不能删除通知
        //3、manager.notify()
        manager.notify(NOTIFICATION_ID, noti);
    }
}
