package com.zero.locker.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zero.locker.util.Config;

/**
 * Locker 自启动接收器
 * @author lin
 * @version 1.0
 * @date 16-2-29
 */
public class LockerBootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Config.sIsLock){
            context.startService(new Intent(context, LockerService.class));
        }
    }
}
