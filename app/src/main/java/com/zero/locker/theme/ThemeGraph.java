package com.zero.locker.theme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.zero.locker.util.Config;
import com.zero.locker.util.Constant;
import com.zero.locker.util.Global;

import java.util.ArrayList;
import java.util.List;

/**
 * 图形锁屏
 * @author lin
 * @version 1.0
 * @date 16-2-26
 */
public class ThemeGraph extends FrameLayout implements ILockView{
    
    private final Paint mPaint = new Paint();
    private final Path mPath = new Path();

    private static final float sTimeViewHeight = 380 * Constant.sScaleX;
    private static final float sTimeViewWidth = Constant.sRealWidth;
    
    private static final float sSmallCircleRadius = 20 * Constant.sScaleX;
    private static final float sBigCircleRadius = 50 * Constant.sScaleX;
    private static final float sPathWidth = 0.5f * sSmallCircleRadius;
    
    private final PointF mCurrentPointF = new PointF();
    
    private boolean isTouch = false;
    
    private final PointF[] mPointFs = new PointF[]{
      new PointF(150 * Constant.sScaleX,450 * Constant.sScaleX),
            new PointF(360 * Constant.sScaleX, 450 * Constant.sScaleX),
            new PointF(570 * Constant.sScaleX, 450 * Constant.sScaleX),
            new PointF(150 * Constant.sScaleX, 680 * Constant.sScaleX),
            new PointF(360 * Constant.sScaleX, 680 * Constant.sScaleX),
            new PointF(570 * Constant.sScaleX, 680 * Constant.sScaleX),
            new PointF(150 * Constant.sScaleX, 910 * Constant.sScaleX),
            new PointF(360 * Constant.sScaleX, 910 * Constant.sScaleX),
            new PointF(570 * Constant.sScaleX, 910 * Constant.sScaleX)
    };
    
    private List<Integer> mAnswer = new ArrayList<>();
    private final List<Integer> mInput = new ArrayList<>();

    public ThemeGraph(Context context) {
        super(context);
        init();
    }
    
    public ThemeGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        mCurrentPointF.x = event.getX();
        mCurrentPointF.y = event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                for(int i = 0 ; i < mPointFs.length; i++){
                    if(getDistance(mCurrentPointF.x,mCurrentPointF.y,mPointFs[i].x,mPointFs[i].y)
                            < sBigCircleRadius){
                        mInput.add(i);
                        isTouch = true;
                    }
                }
                if(mInput.size() == 0)
                    return false;
                break;
            case MotionEvent.ACTION_MOVE:
                for(int i = 0 ; i < mPointFs.length; i++){
                    if(getDistance(mCurrentPointF.x,mCurrentPointF.y,mPointFs[i].x,mPointFs[i].y)
                            < sBigCircleRadius && !isPressed(i)){
                        mInput.add(i);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                unLock();
                mInput.clear();
                isTouch = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                unLock();
                mInput.clear();
                isTouch = false;
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawCircle(canvas);
        drawPath(canvas);
        
    }

    private void drawCircle(Canvas canvas){
        mPaint.setStyle(Paint.Style.FILL);
        for(int i = 0 ; i < mPointFs.length ;i++){
            int sColor_BigCircle_normal = 0xFF000000;
            int sColor_BigCircle_press = 0x9F21B2E7;
            if(isPressed(i))
                mPaint.setColor(sColor_BigCircle_press);
            else mPaint.setColor(sColor_BigCircle_normal);
            canvas.drawCircle(mPointFs[i].x, mPointFs[i].y,sBigCircleRadius,mPaint);
            int sColor_SmallCircle_normal = 0xFFFFFFFF;
            int sColor_SmallCircle_press = 0xFF21B2E7;
            if(isPressed(i))
                mPaint.setColor(sColor_SmallCircle_press);
            else mPaint.setColor(sColor_SmallCircle_normal);
            canvas.drawCircle(mPointFs[i].x,mPointFs[i].y, sSmallCircleRadius, mPaint);
        }
    }
    
    private void drawPath(Canvas canvas){
        if(!isTouch) return; 
        mPath.reset();
        if(mInput.size() > 0)
            mPath.moveTo(mPointFs[mInput.get(0)].x, mPointFs[mInput.get(0)].y);
        if(mInput.size() > 1)
            for(int i = 1; i < mInput.size(); i++)
                mPath.lineTo(mPointFs[mInput.get(i)].x,mPointFs[mInput.get(i)].y);
        mPath.lineTo(mCurrentPointF.x,mCurrentPointF.y);
        int sColor_Path = 0x5521B2E7;
        mPaint.setColor(sColor_Path);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(sPathWidth);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 解锁
     */
    private void unLock(){
        if(checkAnswer()){
            Global.Broadcast(getContext(),"");
        }
    }
    
    //检查路径是否正确
    private boolean checkAnswer(){
        if(!isTouch) return false;
        if(mInput.size() != mAnswer.size()) return false;
        for(int i = 0 ; i < mInput.size() ; i++){
            if(!mInput.get(i).equals(mAnswer.get(i)))
                return false;
        }
        return true;
    }
    
    //判断改点是否已被点击
    private boolean isPressed(int pos){
        for(int i = 0; i < mInput.size() ; i++){
            if(mInput.get(i).equals(pos))
                return true;
        }
        return false;
    }
    
    private void init(){
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        
        //the answer
        mAnswer.add(0);
        mAnswer.add(3);
        mAnswer.add(6);
        mAnswer.add(7);
        mAnswer.add(8);
        
        if(Config.sIsShowBg) {
            updateBG();
        }
        
        if(Config.sIsShowTime) {
            LayoutParams lp = new LayoutParams((int)sTimeViewWidth,
                    (int)sTimeViewHeight);
            addView(new ThemeGraphTime(getContext()),lp);
        }
    }

    private void updateBG(){
        setBackgroundColor(0xFFCCCCCC);
    }
    
    public void setAnswer(List<Integer> answer){
        this.mAnswer = answer;
    }


    //获取两点之间的距离
    private float getDistance(float x1,float y1,float x2,float y2){
        return (float) Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
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
    }

    @Override
    public void onResume() {
        invalidate();
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
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ILockView) {
                ((ILockView) view).onMonitor(bundle);
            }
        }
    }
}
