package com.zero.locker.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Vibrator;

import java.util.List;

/**
 * 全局方法
 */
public class Global {


    
    /**
     * broadcast
     * @param context
     * @param action
     */
    public static void Broadcast(Context context,String action){
        Intent intent = new Intent("com.zero.locker.unlock");
        intent.putExtra("action",action);
        context.sendBroadcast(intent);
    }

    /**
     * 打开拨号界面
     * @param context
     */
    public static void OpenCall(Context context){
        Intent intent;
        PackageManager pManager = context.getPackageManager();
        intent = new Intent(Intent.ACTION_DIAL);
        List<ResolveInfo> info = pManager.queryIntentActivities(intent,
                PackageManager.GET_RESOLVED_FILTER
                        | PackageManager.GET_INTENT_FILTERS
                        | PackageManager.MATCH_DEFAULT_ONLY);
        if (null == info || info.size() == 0) {
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("tel:");
            intent.setData(uri);
        }
        if (null != info) {
            info.clear();
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开相机界面
     * @param context
     */
    public static void OpenCamera(Context context){
    }

    /**
     * 打开短信界面
     * @param context
     */
    public static void OpenSms(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setType("vnd.android.cursor.dir/mms");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            intent.setType("vnd.android-dir/mms-sms");
            try {
                context.startActivity(intent);
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * quake
     * @param context 
     * @param time time of the quake
     */
    public static void Quake(Context context,long time) {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        v.vibrate(time);
    }
    
    
}