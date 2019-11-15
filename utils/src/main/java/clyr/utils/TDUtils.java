package clyr.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * Created by clyr on 2018/3/12 0012.
 * 弹出Toast提示的快捷方式 不需要输入context 和 时长
 */

public class TDUtils {
    String mTag = "TDUtils";

    /**
     * @param context 上下文
     * @param string  提示文
     */
    public static void showLong(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context  上下文
     * @param title    标题
     * @param content  内容
     * @param cla      点击跳转的Activity
     * @param drawable 图标
     * @return
     */
    public static Notification showNotfi(Context context, String title, String content, Class<? extends Activity> cla, int drawable) {
        String channelOneId = "getui_" + context.getString(R.string.app_name);
        CharSequence channelName = "个推";

        Intent intent1 = new Intent(context, cla);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Uri mUri = Settings.System.DEFAULT_NOTIFICATION_URI;

            NotificationChannel mChannel = new NotificationChannel(channelOneId, channelName, NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription("description");
            mChannel.setSound(mUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            manager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context, channelOneId)
                    .setChannelId(channelOneId)
                    .setSmallIcon(drawable)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();

        } else {
            notification = new Notification.Builder(context)
                    .setSmallIcon(drawable)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();
        }
        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(10001, notification);
        return notification;
    }

    public static Notification showNotfi(Context context, String title, String content, int drawable) {
        String channelOneId = context.getString(R.string.app_name) + "_Test";
        CharSequence channelName = context.getString(R.string.app_name);

        Notification notification = null;
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Uri mUri = Settings.System.DEFAULT_NOTIFICATION_URI;

            NotificationChannel mChannel = new NotificationChannel(channelOneId, channelName, NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription("description");
            mChannel.setSound(mUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            manager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context, channelOneId)
                    .setChannelId(channelOneId)
                    .setSmallIcon(drawable)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .build();

        } else {
            notification = new Notification.Builder(context)
                    .setSmallIcon(drawable)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .build();
        }
        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(10001, notification);
        return notification;
    }
}
