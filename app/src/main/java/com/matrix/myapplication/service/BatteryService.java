package com.matrix.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;

import com.matrix.myapplication.utils.MainHelper;
import com.matrix.myapplication.utils.MyLog;

public class BatteryService extends Service {
    public BatteryService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.postDelayed(task, 0);
        MyLog.d("startSevices");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    Handler mHandler = new Handler();

    Runnable task = () -> {
        getBatteryState();
    };

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
        if (isCharging && batteryPct >= 100) {
            MainHelper.sendNotification(this);
        } else if (isCharging && batteryPct < 100) {
            mHandler.postDelayed(task, 1000 * 60 * 5);
        }
        String nowbattery = "当前电量 " + batteryPct + "%";
        MyLog.d(nowbattery);
        /*if (isCharging) {
            if (usbCharge) {
                Toast.makeText(MainActivity.this, "USB连接！ " + nowbattery, Toast.LENGTH_SHORT).show();
            } else if (acCharge) {
                Toast.makeText(MainActivity.this, "通过电源充电中！ " + nowbattery, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "未连接USB线！ " + nowbattery, Toast.LENGTH_SHORT).show();
        }*/
    }
}
