package com.matrix.myapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.matrix.myapplication.utils.DownloadAPK;


/**
 * 更新服务
 * 
 * @author wangruru
 */
public class AppUpgradeService extends Service {
	public static final String EXTRA_SERVICE_START_TYPE = "extra_service_start_type";

	private static final int DEFAULT_START_TYPE = 0;

	private int mStartType = DEFAULT_START_TYPE; // 0代表启动app时候程序自己检测，1代表用户手动检测版本

	public static final int mNotificationId = 100;

	private String mDownloadUrl = null;

	private NotificationManager mNotificationManager = null;

	private Notification mNotification = null;

	private PendingIntent mPendingIntent = null;

	private int mServerVersion = 0;

	private String mFileName = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

//		// mDownloadUrl = "http://192.168.32.135:8080/hims/mobile/hims.apk";
//		mDownloadUrl = "http://" + ApkConstant.SERVER_API_URL_MID
//				+ "/hims/AndroidUpdate/机电设备维护系统.apk";
//		initIntentData(intent);
//		testUpgrade();

		DownloadAPK dl = new DownloadAPK(getApplicationContext());
		dl.downloadAPK(intent.getStringExtra("url")+intent.getStringExtra("name"),
				intent.getStringExtra("name"));
		
		return super.onStartCommand(intent, flags, startId);
		
	}

}
