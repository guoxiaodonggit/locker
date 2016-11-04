package com.zero.locker.theme;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import com.zero.locker.util.Config;
import com.zero.locker.util.Constant;
import com.zero.locker.util.DensityUtil;

/**
 * @author lin
 * @version 1.0
 * @date 16-2-1
 */
public class RootView extends LinearLayout implements ILockView {

    public RootView(Context context) {
        super(context);
        init();
    }

    public RootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        this.setOrientation(LinearLayout.VERTICAL);
        DensityUtil.resetDensity(getContext());
    }

    @Override
    public void onStart(Bundle bundle) {
        Config.init(bundle);
        
        Constant.sRealWidth = DensityUtil.sWidthPixels;
        int statusBarHeight = 0;
        Constant.sRealHeight = DensityUtil.sHeightPixels - statusBarHeight;
        Constant.sScaleX = (float) Constant.sRealWidth
                / Constant.S_DEFAULT_WIDTH;
        Constant.sScaleY = (float) Constant.sRealHeight
                / Constant.S_DEFAULT_HEIGHT;
        Constant.sScaledTouchSlop = ViewConfiguration.get(getContext())
                .getScaledTouchSlop();

        LayoutParams lp = new LayoutParams(Constant.sRealWidth,
                Constant.sRealHeight);

        //addView(new ThemeWater(getContext()), lp);
        //addView(new ThemeGraph(getContext()), lp);
        addView(new ThemeFluorescence(getContext()), lp);
        
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ILockView) {
                ((ILockView) view).onStart(bundle);
            }
        }
    }

    @Override
    public void onResume() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ILockView) {
                ((ILockView) view).onResume();
            }
        }
    }

    @Override
    public void onShow() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ILockView) {
                ((ILockView) view).onShow();
            }
        }
    }

    @Override
    public void onPause() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ILockView) {
                ((ILockView) view).onPause();
            }
        }
    }

    @Override
    public void onStop() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ILockView) {
                ((ILockView) view).onStop();
            }
        }
    }

    @Override
    public void onDestroy() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ILockView) {
                ((ILockView) view).onDestroy();
            }
        }
    }

    @Override
    public void onMonitor(Bundle bundle) {
        if(bundle == null) return ;
        
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ILockView) {
                ((ILockView) view).onMonitor(bundle);
            }
        }
    }
}
