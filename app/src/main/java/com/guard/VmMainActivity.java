package com.guard;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.guard.services.VMBackgroundService;
import com.guard.services.VMCoreService;
import com.guard.services.VMDaemonJobService;
import com.guard.services.VMDaemonService;
import com.guard.services.VMForegroundService;
import com.matrix.myapplication.R;

public class VmMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainvm);

        findViewById(R.id.btn_foreground).setOnClickListener(viewListener);
        findViewById(R.id.btn_daemon).setOnClickListener(viewListener);
        findViewById(R.id.btn_background).setOnClickListener(viewListener);
        findViewById(R.id.btn_job_service).setOnClickListener(viewListener);
        findViewById(R.id.stop_service).setOnClickListener(viewListener);

        // 启动核心进程
        startCoreProcess();
    }

    /**
     * 点击事件监听
     */
    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_foreground:
                    Intent foregroundIntent =
                            new Intent(getApplicationContext(), VMForegroundService.class);
                    startService(foregroundIntent);
                    break;
                case R.id.btn_daemon:
                    Intent daemonIntent =
                            new Intent(getApplicationContext(), VMDaemonService.class);
                    startService(daemonIntent);
                    break;
                case R.id.btn_background:
                    Intent backgroundIntent =
                            new Intent(getApplicationContext(), VMBackgroundService.class);
                    startService(backgroundIntent);
                    break;
                case R.id.btn_job_service:
                    startJobScheduler();
                    break;
                case R.id.stop_service:
                    stopServices();
                    break;
            }
        }
    };

    private void stopServices() {
        stopService(new Intent(getApplicationContext(), VMCoreService.class));
        stopService(new Intent(getApplicationContext(), VMBackgroundService.class));
        stopService(new Intent(getApplicationContext(), VMDaemonJobService.class));
        stopService(new Intent(getApplicationContext(), VMDaemonService.class));
        stopService(new Intent(getApplicationContext(), VMForegroundService.class));
    }

    /**
     * 5.x以上系统启用 JobScheduler API 进行实现守护进程的唤醒操作
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP) private void startJobScheduler() {
        int jobId = 1;
        JobInfo.Builder jobInfo =
                new JobInfo.Builder(jobId, new ComponentName(this, VMDaemonJobService.class));
        jobInfo.setPeriodic(10000);
        jobInfo.setPersisted(true);
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo.build());
    }

    /**
     * 启动核心进程
     */
    private void startCoreProcess() {
        startService(new Intent(getApplicationContext(), VMCoreService.class));
    }
}
