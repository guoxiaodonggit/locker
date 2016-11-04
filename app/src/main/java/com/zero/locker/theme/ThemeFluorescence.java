package com.zero.locker.theme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.zero.locker.util.Config;
import com.zero.locker.util.Constant;
import com.zero.locker.util.Global;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 荧光主题
 * @author lin
 * @version 1.0
 * @date 16-3-8
 */
public class ThemeFluorescence extends FrameLayout{
    private static final String TAG = "Fluorescence";
    
    private static final Float UNLOCK_DISTANCE = Constant.sScaleX * 600;
    /**
     * 当前触摸点
     */
    private PointF mTouchPoint = new PointF();
    /**
     * 开始触摸点
     */
    private PointF mStartPoint = new PointF();
    
    public ThemeFluorescence(Context context) {
        super(context);
        init();
        setBackground();
    }
    
    public ThemeFluorescence(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setBackground();
    }

    /**
     * 设置背景
     */
    private void setBackground(){
        setBackgroundColor(0xFF000000);
    }
    
    /**
     * 初始化视图
     */
    private void init(){
        mPaintShader = new Paint();
        mPaintShader.setStyle(Paint.Style.FILL);
        mPaintShader.setAntiAlias(true);

        if(Config.sIsShowTime) {
            LayoutParams lp = new LayoutParams((int)Constant.sScaleX * 800,
                    (int)Constant.sScaleX * 150);
            addView(new ThemeFluorescenceTime(getContext()),lp);
        }
    }

    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        mTouchPoint.x = event.getX();
        mTouchPoint.y = event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mStartPoint.x = mTouchPoint.x;
                mStartPoint.y = mTouchPoint.y;
                createRandomPoints();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                createRandomPoints();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(getDistance(mTouchPoint.x,mTouchPoint.y,mStartPoint.x,mStartPoint.y) >= 
                        UNLOCK_DISTANCE)
                    Global.Broadcast(getContext(),"");
                break;
            case MotionEvent.ACTION_CANCEL:
                if(getDistance(mTouchPoint.x,mTouchPoint.y,mStartPoint.x,mStartPoint.y) >=
                        UNLOCK_DISTANCE)
                    Global.Broadcast(getContext(),"");
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawFluorescence(canvas);
        invalidate();
    }

    /**
     * 荧光的渲染笔
     */
    private Paint mPaintShader;
    /**
     * 荧光的变化颜色
     */
    private static final int COLOR_WHITE = 0xFFFFFFFF;
    private static final int COLOR_WHITE_SHADER = 0x00FFFFFF;
    /**
     * 荧光点产生的随机区域
     */
    private static final float sRandomCircle = Constant.sScaleX * 55;
    /**
     * 荧光点产生的随机最大数目
     */
    private static final int sMaxRandomNumber = 5;
    /**
     * 荧光点产生的随机最小数目
     */
    private static final int sMinRandomNumber = 3;
    /**
     * 随机数
     */
    private static Random mRandom = new Random();
    /**
     * 荧光点集合
     */
    private List<FluorescencePointF> mPointFList = new LinkedList<>();

    /**
     * 荧光点
     */
    private class FluorescencePointF{
        public PointF mPointF;   //荧光点坐标
        public long mStartTime;   //开始时间
        private float mRadius;    //荧光点半径
        private final float MAX_RADIUS = Constant.sScaleX * 15;   //荧光点最大半径
        private final float MIN_RADIUS = Constant.sScaleX * 8;    //荧光点最小半径
        private final long SHOW_TIME = 600;                  //荧光维持时间
        private final Interpolator DEC_INTERPOLATOR = new DecelerateInterpolator(); //减速
        /**
         * 构造方法
         */
        public FluorescencePointF(PointF pointF) {
            this.mPointF = pointF;
            this.mStartTime = System.currentTimeMillis();
            this.mRadius = MIN_RADIUS + mRandom.nextInt((int) MAX_RADIUS);
            Log.e(TAG,"FluorescencePointF point:" + "x:" +pointF.x + " y:" + pointF.y);
            Log.e(TAG,"FluorescencePointF radius:" + this.mRadius);
        }
        /**
         * 获得半径
         */
        public float getRadius(){
            if(!isAlive()) return 0;
            return mRadius * DEC_INTERPOLATOR.getInterpolation(
                    ((float)SHOW_TIME - (System.currentTimeMillis() - mStartTime))/ SHOW_TIME);
        }
        /**
         * 荧光点是否生存
         * @return
         */
        public boolean isAlive(){
             return System.currentTimeMillis() - mStartTime <= SHOW_TIME;
        }
    }
    
    /**
     * 绘制荧光
     */
    private void drawFluorescence(Canvas canvas){
        if(mPointFList.size() == 0) return ;
        for(FluorescencePointF fluorescencePointF:mPointFList){
            float radius = fluorescencePointF.getRadius();
//            if(radius <= 0) continue;
            if(radius <= 0) continue;
            Shader shader = new RadialGradient(fluorescencePointF.mPointF.x,fluorescencePointF
                    .mPointF.y,radius,COLOR_WHITE,COLOR_WHITE_SHADER,
                    Shader.TileMode.CLAMP);
            mPaintShader.setShader(shader);
            canvas.drawCircle(fluorescencePointF.mPointF.x,fluorescencePointF.mPointF.y,
                    fluorescencePointF.getRadius(),mPaintShader);
        }
        //删除已经耗尽生命周期的荧光点
        int index = 0;
        while(index >= mPointFList.size()){
            if(!mPointFList.get(index).isAlive())
                mPointFList.remove(index);
            else break;   //测到一个没耗尽生命的点，之后的点都是活的
        }
    }


    /**
     * 根据触摸点产生随机荧光点
     */
    private void createRandomPoints(){
        int randomNum = sMinRandomNumber + mRandom.nextInt(sMaxRandomNumber);
        for(int i = 0; i < randomNum ; i++){
            int randomCircle = mRandom.nextInt((int) sRandomCircle);
            double randomAngle = mRandom.nextInt(360);
            PointF pointF1 = new PointF(
                    mTouchPoint.x + randomCircle * (float)Math.cos(Math.toRadians(randomAngle)), 
                    mTouchPoint.y + randomCircle * (float)Math.sin(Math.toRadians(randomAngle))
            );
            mPointFList.add(new FluorescencePointF(pointF1));
        }
    }


    //获取两点之间的距离
    private float getDistance(float x1,float y1,float x2,float y2){
        return (float) Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
    }
    
}
