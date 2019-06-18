package com.guard;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.matrix.myapplication.MainActivity;
import com.matrix.myapplication.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by M S I of clyr on 2019/6/17.
 */
public class NotiUtils {
    public static Notification show(Context context, String channelId,String title) {
        String channelOneId = "com.matrix";
        if (channelId != "" && channelId != null) {
            channelOneId = channelId;
        }
        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Uri mUri = Settings.System.DEFAULT_NOTIFICATION_URI;
            NotificationChannel mChannel = new NotificationChannel(channelOneId, "driver", NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription("description");
            mChannel.setSound(mUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            manager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context, channelOneId)
                    .setChannelId(channelOneId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(title+" setContentText")
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();

        } else {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(title+" setContentText")
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();
        }
        manager.notify(10001, notification);
        return notification;
    }
}
