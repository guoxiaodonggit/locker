package com.zero.locker.theme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.locker.util.Constant;
import com.zero.locker.util.DateFormat;

import java.util.Date;

/**
 * @author
 * @version 1.0
 * @date 16-3-11
 */
public class ThemeFluorescenceTime extends LinearLayout implements ILockView{
    private TextView mHourMinuteTime;
    private TextView mDateTime;

    private static final int TextColor = 0xFFFFFFFF;
    private static final int TextPadding = (int) Constant.sScaleX * 25;

    public ThemeFluorescenceTime(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.START);

        mHourMinuteTime = new TextView(getContext());
        mHourMinuteTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, 100 * Constant.sScaleX);
        mHourMinuteTime.setGravity(Gravity.CENTER);
        mHourMinuteTime.setSingleLine();
        mHourMinuteTime.setTextColor(TextColor);
        mHourMinuteTime.setPadding(TextPadding, TextPadding * 3, TextPadding, TextPadding);
        LayoutParams paramsHourMinute = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(mHourMinuteTime, paramsHourMinute);

        mDateTime = new TextView(getContext());
        mDateTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                40 * Constant.sScaleX);
        mDateTime.setGravity(Gravity.CENTER);
        mDateTime.setSingleLine();
        mDateTime.setTextColor(TextColor);
        mDateTime.setPadding(TextPadding, 0, TextPadding, TextPadding);
        LayoutParams paramsDate = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(mDateTime, paramsDate);
    }

    public ThemeFluorescenceTime(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void updateTime(){
        Date date = new Date();
        if(mHourMinuteTime != null){
            mHourMinuteTime.setText(DateFormat.getFormatDate4(date));
        }
        if(mDateTime != null){
            mDateTime.setText(DateFormat.getFriendlyDate(date));
        }
    }

    private BroadcastReceiver mBroadcastReceiver;
    private final Handler mHandler = new Handler();

    private void initReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            updateTime();
                        }
                    });
                }
            }
        };
    }

    private void registerTimeReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        getContext().registerReceiver(mBroadcastReceiver, filter);
    }

    private void unregisterTimeReceiver(){
        if(mBroadcastReceiver != null)
            getContext().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onStart(Bundle bundle) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ILockView) {
                ((ILockView) view).onStart(bundle);
            }
        }
        initReceiver();
    }

    @Override
    public void onResume() {
        updateTime();
    }

    @Override
    public void onShow() {
        updateTime();
        registerTimeReceiver();
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
        unregisterTimeReceiver();
    }

    @Override
    public void onMonitor(Bundle bundle) {
    }
}
