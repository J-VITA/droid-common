package kr.skyware.commons.device.utils.wake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

import kr.skyware.commons.util.Globals;

import static android.os.PowerManager.*;

public class ScreenWake {

    private static PowerManager.WakeLock mWakeLock;
    static PowerManager powerManager = null;

    /**
     * @function 화면 깨우기
     * @descript
     *  - ACQUIRE_CAUSES_WAKEUP : WakeLock에게 조명이 켜지도록함
     *  - ON_AFTER_RELEASE      : WakeLock이 Release되고 조명이 오래 유지되도록함
     * */
    @SuppressLint("InvalidWakeLockTag")
    public static void wakeLock(Context context) {
        if(mWakeLock != null){
            return;
        }
        powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        mWakeLock = powerManager.newWakeLock(ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, Globals.LOG_TAG + " SKY Alarm WareLock");
        mWakeLock.acquire(3000);
    }

    /**
     * @function 화면 재우기
     * */
    @SuppressLint("InvalidWakeLockTag")
    public static void releaseWakeLock(){
        if(mWakeLock != null){
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
                mWakeLock = null;
            }
        }
        mWakeLock = powerManager.newWakeLock(PARTIAL_WAKE_LOCK, Globals.LOG_TAG + " SKY Alarm WareLock");
    }
}
