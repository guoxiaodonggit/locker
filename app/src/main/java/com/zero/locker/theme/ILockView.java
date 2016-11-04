package com.zero.locker.theme;

import android.os.Bundle;

/**
 * 生命周期接口
 * @author lin
 * @version 1.0
 * @date 16-2-1
 */
public interface ILockView {
    
    void onStart(Bundle bundle);
    
    void onResume();
    
    void onShow();
    
    void onPause();
    
    void onStop();
    
    void onDestroy();
    
    void onMonitor(Bundle bundle);
}
