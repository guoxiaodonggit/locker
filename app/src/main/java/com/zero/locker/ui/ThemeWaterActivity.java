package com.zero.locker.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.zero.locker.theme.RootView;
import com.zero.locker.util.Constant;

/**
 * @author lin
 * @version 1.0
 * @date 16-2-2
 */
public class ThemeWaterActivity extends Activity {
    private BroadcastReceiver mReceiver = null;
    private RootView mRootView = null;
    private static final boolean sIsFull = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (sIsFull) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        mRootView = new RootView(this);
        setContentView(mRootView);
        
        createConfig();
        createReceiver();
    }

    private void createConfig() {

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_SHOW_TIME, true);
        bundle.putBoolean(Constant.IS_LOCK_SOUND, false);
        bundle.putBoolean(Constant.IS_UNLOCK_SOUND, false);
        bundle.putBoolean(Constant.IS_LOCK_QUAKE, true);

        mRootView.onStart(bundle);
        mRootView.onShow();
    }

    private void createReceiver() {
        // 创建广播接收器
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals("com.zero.locker.unlock")) {
                    String strTheme = intent.getStringExtra("action");
                    if (strTheme != null) {
                        finish();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.zero.locker.unlock");
        // 注册广播接收器
        this.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRootView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRootView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRootView.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mRootView != null) {
            mRootView.onDestroy();
            mRootView = null;
        }
        this.unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
