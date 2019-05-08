package com.matrix.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.matrix.myapplication.view.TableShowView;

public class FPSGetService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        new TableShowView(getApplicationContext()).fun();
        Log.d("===service===","===service===");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }
}
