package com.matrix.myapplication.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.matrix.myapplication.BuildConfig;

import java.io.File;

public class DownloadAPK {
    private static String downloadPath;
    private long mTaskId;
    Context mContext;
    String versionName1;

    public DownloadAPK(Context context) {
        mContext = context;
    }

    // 使用系统下载器下载
    public void downloadAPK(String versionUrl, String versionName) {
        // 创建下载任务
        versionName1 = versionName;
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);// 漫游网络是否可以下载

        // 设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
                .getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);

        // 在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        // sdcard的目录下的download文件夹，必须设置
        request.setDestinationInExternalPublicDir("/download/", versionName);
        // request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

        downloadManager = (DownloadManager) mContext
                .getSystemService(Context.DOWNLOAD_SERVICE);
        // 加入下载队列后会给该任务返回一个long型的id，
        // 通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
        mTaskId = downloadManager.enqueue(request);
        // 注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    // 接下来是广播接收器

    // 广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();// 检查下载状态
        }
    };
    private DownloadManager downloadManager;

    // 检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);// 筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c
                    .getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.d("1111", ">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    Log.d("1111", ">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    Log.d("1111", ">>>正在下载");

                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.d("1111", ">>>下载完成");
                    // 下载完成安装APK
                    downloadPath = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                            + File.separator + versionName1;
                    installAPK(downloadPath);
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.d("1111", ">>>下载失败");
                    break;
            }
        }

    }

    // 下载到本地后执行安装
    protected void installAPK(String apkFilePath) {

        try {
            File apkfile = new File(apkFilePath);
        /*try {
			if (!file.exists())
                return;
			Intent intent = new Intent(Intent.ACTION_VIEW);
//			Uri uri = Uri.parse("file://" + file.toString());
			Uri uri = getUriForFile(mContext,file);
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
			// 在服务中开启activity必须设置flag
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			SDToast.showToast("手机兼容性问题您可以去以下位置安装应用"+file.getAbsolutePath(), Toast.LENGTH_LONG);
		}*/

            if (!apkfile.exists()) {
                return;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                String authority = "com.matrix.myapplication.fileProvider";
                Uri contentUri = FileProvider.getUriForFile(mContext, authority, new File(apkFilePath));
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(new File(apkFilePath)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
			e.printStackTrace();
			ToastUtils.showLong("打开APK失败::>_<::，请手动安装,安装包位于下载器中"+apkFilePath);
        }


    }

    public static Uri getUriForFile(Context context, File file) {
//		return FileProvider.getUriForFile(context, GB.getCallBack().getApplicationId(), file);
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, file);
    }
}

