package com.zero.locker.util;

import android.os.Bundle;

/**
 * @author lin
 * @version 1.0
 * @date 16-2-1
 */
public class Config {

    

    public static void init(Bundle bundle){
        if(bundle == null)
            return ;

        /**
         * init the config data
         */
        sIsShowTime = bundle.getBoolean(Constant.IS_SHOW_TIME);
        sIsLockSound = bundle.getBoolean(Constant.IS_LOCK_SOUND);
        sIsUnlockSound = bundle.getBoolean(Constant.IS_UNLOCK_SOUND);
        sIsLockQuake = bundle.getBoolean(Constant.IS_LOCK_QUAKE);
        sCall = bundle.getInt(Constant.NUM_CALL);
        sSms = bundle.getInt(Constant.NUM_SMS);
    }

    /**
     * 是否开启锁屏
     */
    public static boolean sIsLock = true;

    /**
     * 是否全屏
     */
    public static boolean sIsFull = true;

    /**
     * 是否添加背景
     */
    public static boolean sIsShowBg = true;
    
    /**
     * 是否刷新锁屏界面
     */
    public static boolean sIsRefresh = false;

    /**
     * 是否显示时间
     */
    public static boolean sIsShowTime = true;

    /**
     * 锁屏声音
     */
    public static boolean sIsLockSound = false;

    /**
     * 解锁声音
     */
    public static boolean sIsUnlockSound = false;

    /**
     * 解锁震动
     */
    public static boolean sIsLockQuake = false;

    /**
     * 未接电话
     */
    public static int sCall = 0;

    /**
     * 未读短信
     */
    public static int sSms = 0;
    
}
