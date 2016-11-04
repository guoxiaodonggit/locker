package com.zero.locker.lock;


import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import com.zero.locker.R;
import com.zero.locker.theme.RootView;
import com.zero.locker.ui.MainActivity;
import com.zero.locker.util.Config;
import com.zero.locker.util.Constant;
import com.zero.locker.util.Global;



/**
 * 通过悬浮窗实现锁屏
 * @author lin
 * @version 1.0
 * @date 16-2-25
 */
public class LockerService extends Service{
    
    private static final String TAG = "LockerService";
    private ScreenListener screenListener;
    
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardLock;
    private WindowManager mWindowManager;
    private RootView mRootView;
    private BroadcastReceiver mReceiver = null;
    
    private static final int sNotificationIdentify = 0x111;
    private static final String sNotificationTickerText = "Locker";
    private static final String sNotificationContentTitle = "Locker Service";
    private static final String sNotificationContentText = "Make this service run in the foreground.";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        init();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stopForeground(true);
        screenListener.finish();
        screenListener = null;
        mWindowManager = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStart");        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            /**
             * 设置前台运行，使Service常驻内存
             */
            Notification.Builder builder = new Notification.Builder(this);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(LockerService.this, MainActivity.class), 0);
            builder.setContentIntent(contentIntent);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setTicker(sNotificationTickerText);
            builder.setContentTitle(sNotificationContentTitle);
            builder.setContentText(sNotificationContentText);
            Notification notification = builder.build();
            startForeground(sNotificationIdentify, notification);
        }
        return START_STICKY;
    }

    
    //初始化数据
    private void init(){
        if(mWindowManager == null){
            mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        if(screenListener == null) {
            screenListener = new ScreenListener(LockerService.this);
            screenListener.begin(new ScreenListener.ScreenStateListener() {
                @Override
                public void onScreenOn() {
                    disableDefaultLock();
                }

                @Override
                public void onScreenOff() {
                    enableDefaultLock();
                    if (mRootView == null)
                        mRootView = new RootView(getApplicationContext());
                    addLockView();
                    createConfig();
                    createReceiver();
                }

                @Override
                public void onUnlock() {
                }
            });
        }
    }
    
  
    //添加锁屏悬浮窗
    private void addLockView(){
        if(mWindowManager == null) return ;
            
        mRootView = new RootView(LockerService.this);
        WindowManager.LayoutParams param = new WindowManager.LayoutParams();
        param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        if(Config.sIsFull) param.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        param.format = PixelFormat.RGBA_8888;
        param.width = WindowManager.LayoutParams.MATCH_PARENT;
        param.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowManager.addView(mRootView, param);
    }

  
    //移除锁屏悬浮窗
    private void removeLockView(){
        if(mWindowManager == null) return ;
        mWindowManager.removeView(mRootView);
    }

    //创建锁屏的参数
    private void createConfig() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_SHOW_TIME, true);
        bundle.putBoolean(Constant.IS_LOCK_SOUND, false);
        bundle.putBoolean(Constant.IS_UNLOCK_SOUND, false);
        bundle.putBoolean(Constant.IS_LOCK_QUAKE, true);

        mRootView.onStart(bundle);
        mRootView.onShow();
    }

    
    //创建锁屏的广播接收器
    private void createReceiver() {
        // 创建广播接收器
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals("com.zero.locker.unlock")) {
                    String strTheme = intent.getStringExtra("action");
                    if (strTheme != null) {
                        switch (strTheme) {
                            case "sms":
                                Global.OpenSms(LockerService.this);
                                break;
                            case "phone":
                                Global.OpenCall(LockerService.this);
                                break;
                            default:
                                break;
                        }
                        removeLockView();
                        destroyReceiver();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.zero.locker.unlock");
        // 注册广播接收器
        this.registerReceiver(mReceiver, filter);
    }
    
    
    //屏蔽系统锁屏
    private void disableDefaultLock(){
        if (mKeyguardManager == null) {
            mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            mKeyguardLock = mKeyguardManager.newKeyguardLock("IN");
        }
        mKeyguardLock.disableKeyguard();
    }
    
    //恢复系统锁屏
    private void enableDefaultLock(){
        if(mKeyguardLock != null)
            mKeyguardLock.reenableKeyguard();
    }
    
    //销毁广播接收器说
    private void destroyReceiver(){
        if(mReceiver != null)
            unregisterReceiver(mReceiver);
    }
    

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
