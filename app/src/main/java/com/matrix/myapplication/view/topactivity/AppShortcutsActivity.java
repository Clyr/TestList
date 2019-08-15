package com.matrix.myapplication.view.topactivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;

/**
 * Created by Wen on 16/02/2017.
 */
@TargetApi(Build.VERSION_CODES.N)
public class AppShortcutsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (WatchingAccessibilityService.getInstance() == null || !Settings.canDrawOverlays(this)) {
            SPHelper.setIsShowWindow(this, true);
            Intent intent = new Intent(this, TopMainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        boolean isShow = !SPHelper.isShowWindow(this);
        SPHelper.setIsShowWindow(this, isShow);
        if (!isShow) {
            TasksWindow.dismiss(this);
            NotificationActionReceiver.showNotification(this, true);
        } else {
            TasksWindow.show(this, getPackageName() + "\n" + getClass().getName());
            NotificationActionReceiver.showNotification(this, false);
        }
        sendBroadcast(new Intent(TopMainActivity.ACTION_STATE_CHANGED));
        finish();
    }
}
