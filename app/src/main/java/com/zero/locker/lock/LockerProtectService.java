package com.zero.locker.lock;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;


/**
 * 锁屏守护服务
 * 
 * @author lin
 * @version 1.0
 * @date 16-3-2
 */
public class LockerProtectService extends Service {

    private static final String TAG = "LockerProtectService";
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStart");
        restoreLockerService();
        return START_STICKY;
    }

  
    //恢复锁屏服务
    private void restoreLockerService(){
        /*
        if(!isServiceRunning()){
            Log.e(TAG,"restore LockerService");
            startService(new Intent(this,LockerService.class));
        }
        */
        startService(new Intent(this,LockerService.class));
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //判断服务是否运行
    private boolean isServiceRunning()
    {
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (service.service.getClassName().equals("com.zero.locker.lock.LockerService"))
            {
                return true;
            }
        }
        return false;
    }
    
    //开启轮询服务  
    public static void startPollingService(Context context, int seconds, Class<?> cls) {
        Log.e(TAG, "keep service alive");
        //获取AlarmManager系统服务  
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent  
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //触发服务的起始时间  
        long triggerAtTime = SystemClock.elapsedRealtime();

        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service  
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,
                seconds * 1000, pendingIntent);
    }

    //停止轮询服务  
    public static void stopPollingService(Context context, Class<?> cls) {
        Log.e(TAG, "cancel keep service alive");
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务  
        manager.cancel(pendingIntent);
    }
}
