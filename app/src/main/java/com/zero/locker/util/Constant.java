package com.zero.locker.util;

/**
 * 常量
 * @author lin    
 * @version 1.0
 * @date 16-2-1
 */
public class Constant {

    /**
     * 默认屏幕尺寸
     */
    public static final int S_DEFAULT_WIDTH = 720;
    public static final int S_DEFAULT_HEIGHT = 1280;
    /**
     * 真实屏幕尺寸
     */
    public static int sRealWidth = S_DEFAULT_WIDTH;
    public static int sRealHeight = S_DEFAULT_HEIGHT;
    
    public static float sScaleX = 1f;
    public static float sScaleY = 1f;
   
    public static float sScaledTouchSlop = 10;
    
    /**
     * 字符常量
     */
    public static String IS_SHOW_TIME = "IsShowTime";
    public static String IS_LOCK_SOUND = "IsLockSound";
    public static String IS_UNLOCK_SOUND = "IsUnlockSound";
    public static String IS_LOCK_QUAKE = "IsLockQuake";

    public static String NUM_CALL = "NumCall";
    public static String NUM_SMS = "NumSms";
    
}
