package kr.skyware.commons.fcm.notification;

import android.annotation.TargetApi;
import android.app.Activity;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.reflect.Field;
import java.util.HashMap;

import kr.skyware.commons.R;
import kr.skyware.commons.fcm.param.PushParam;
import kr.skyware.commons.util.Globals;
import kr.skyware.commons.util.Logging;

public class SkyFirebaseService extends FirebaseMessagingService {

    private static final String TAG = Globals.LOG_TAG + "[SkyFirebaseService]";
    public PushParam pushParam;
    public SkyFirebaseService(){}


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Logging.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Logging.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.

            } else {
                // Handle message within 10 seconds
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            HashMap map = new HashMap<>();
            map.put("title", remoteMessage.getNotification().getTitle());
            map.put("body", remoteMessage.getNotification().getBody());
            SkyNotification.sendNotification(map, getRunningActivity(), R.mipmap.ic_launcher, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        pushParam = PushParam.builder().token(token).build();
    }

    /**
     * 뉴 토큰 앱에 전달
     * @param token The new token.
     */
    public String sendRegistrationToServer(String token) {
        return token;
    }

    /**
     * 현재 foreground 최상위 액티비티 가져오기
     * @param
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Activity getRunningActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread")
                    .invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            ArrayMap activities = (ArrayMap) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Didn't find the running activity");
    }
}
