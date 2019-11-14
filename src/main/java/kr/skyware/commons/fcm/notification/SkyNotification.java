package kr.skyware.commons.fcm.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import java.util.HashMap;

import kr.skyware.commons.util.Utils;

public class SkyNotification {
    public SkyNotification(){

    }

    public void sendNotification(HashMap<String, String> info, Context context, int icon, Uri sound){
        Intent intent = new Intent(context, ((Activity)context).getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent
                = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(info.get("title"))
                .setContentText(info.get("body"))
                .setAutoCancel(true)
                .setSound(Utils.isEmpty(sound) ? defaultSoundUri : sound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.build());
    }
}
