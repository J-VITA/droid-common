package kr.skyware.commons.application;

import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import kr.skyware.commons.R;
import kr.skyware.commons.util.Globals;
import kr.skyware.commons.util.Logging;

public class SkywareApplication extends Application {

    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;		//고려하지 않던 오류를 잡아주는 핸들러
    private UncaughtExceptionHandlerApplication unCatchExceptionHandlerApplication;

    private static Application singleton;
    private static final Object mutex = new Object();
    Context mContext;

    public static Application getInstance() {
        Application r = singleton;
        if (r == null) {
            synchronized (mutex) {  // While we were waiting for the sync, another
                r = singleton;       // thread may have instantiated the object.
                if (r == null) {
                    r = new Application();
                    singleton = r;
                }
            }
        }
        return r;
    }

    @Override
    public void onCreate() {
        mContext = this.getApplicationContext();
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        unCatchExceptionHandlerApplication = new UncaughtExceptionHandlerApplication();
        Thread.setDefaultUncaughtExceptionHandler(unCatchExceptionHandlerApplication);

//        disableKeyguard();	//잠금화면 해제

        super.onCreate();

        init();

        //
//        if (DeviceMng.isNetworkConnected(mContext)) {
////            getCallerId(mContext);
//        }else{
//            Toast.makeText(mContext, "Please Check the network setting..", Toast.LENGTH_LONG).show();
//            ((Activity)mContext.getApplicationContext()).finish();
//        }

    }

    private void init(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Logging.w(Globals.LOG_TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Logging.d(Globals.LOG_TAG, msg);
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return unCatchExceptionHandlerApplication;
    }

    private void disableKeyguard() {
        KeyguardManager manager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock(KEYGUARD_SERVICE);
        lock.disableKeyguard();	// 잠금화면 해제의 역할
        //lock.reenableKeyguard();	//해제된 잠금화면을 다시 원상태로 돌리는 기능
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        Log.d(Globals.LOG_TAG, "Application onTerminate()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        Log.d(Globals.LOG_TAG, "Application onLowMemory()");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        Log.d(Globals.LOG_TAG, "Application onConfigurationChanged()");
    }

    class UncaughtExceptionHandlerApplication implements  Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
            //TO-DO : Service & BroadCas Receiver Restart
            mUncaughtExceptionHandler.uncaughtException(thread, throwable);
        }
    }

    @RequiresApi(28)
    private static class OnUnhandledKeyEventListenerWrapper implements View.OnUnhandledKeyEventListener {
        private ViewCompat.OnUnhandledKeyEventListenerCompat mCompatListener;

        OnUnhandledKeyEventListenerWrapper(ViewCompat.OnUnhandledKeyEventListenerCompat listener) {
            this.mCompatListener = listener;
        }

        public boolean onUnhandledKeyEvent(View v, KeyEvent event) {
            return this.mCompatListener.onUnhandledKeyEvent(v, event);
        }
    }
}
