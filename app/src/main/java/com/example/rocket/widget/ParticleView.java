package com.example.rocket.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 *  让这个View的中心以旋转动画的中心重合即可
 */
public class ParticleView extends View {
    private Paint mPaint;
    private Paint mPaintTrans;


    /***
     *  开始动画
     *  对外方法，只有这一个
     * @param in 入场动画， out出场动画
     */
    public void startBackCenterAnim(boolean in) {
        setVisibility(VISIBLE);
        setAlpha(1.0f);
        mAnimator = in ? ValueAnimator.ofFloat(1, 0):ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(1500);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value  = (float) animation.getAnimatedValue();
                float fraction = animation.getAnimatedFraction();
                if(animation.getAnimatedFraction() == 1.0F){
                    setVisibility(GONE);
                    mAnimator.cancel();
                    return;
                }
                if(fraction >= 0.8f){
                    setAlpha(1.0f-fraction);
                }

                mDegree = (int) (value*360);
                mRadius1 = (int) (mCenterX*value);
                mRadius2 = (int) (mCenterX*1.5f*value);
                mRadius3 = (int) (mCenterX*0.8f*value);
                mRadius4 = (int) (mCenterX*1.2f*value);


                mPoint1 = calcPoint(mDegree, mRadius1,false);
                mPoint2 = calcPoint(mDegree*0.6f, mRadius2,true);
                mPoint3 = calcPoint(mDegree*1.2f, mRadius3,true);
                mPoint4 = calcPoint(mDegree*1.5f, mRadius4,true);
                Log.i(TAG, "onAnimationUpdate: mDegree=" + mDegree);
                invalidate();
            }
        });
        mAnimator.start();
    }


    public ParticleView(Context context) {
        this(context, null);
    }

    public ParticleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTrans = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaintTrans.setColor(Color.parseColor("#88FFFFFF"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mCenterX, mCenterY);
        canvas.save();
        if (mPoint1 != null) {
            canvas.drawCircle(mPoint1.x, mPoint1.y, 10, mPaintTrans);
        }
        if(mPoint2!=null){
            canvas.drawCircle(mPoint2.x, mPoint2.y, 6, mPaint);
        }
        if(mPoint3!=null){
            canvas.drawCircle(mPoint3.x, mPoint3.y, 8, mPaintTrans);
        }
        if(mPoint4!=null){
            canvas.drawCircle(mPoint4.x, mPoint4.y, 12, mPaint);
        }
        canvas.restore();
    }

    private int mCenterX;
    private int mCenterY;
    private int mViewWidth;
    private int mViewHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        mCenterX = mViewWidth / 2;
        mCenterY = mViewHeight / 2;
    }


    private Point mPoint1;
    private Point mPoint2;
    private Point mPoint3;
    private Point mPoint4;
    private int mRadius1;
    private int mRadius2;
    private int mRadius3;
    private int mRadius4;

    private int mDegree;
    private static final String TAG = "ParticleView";

    private Point calcPoint(float deg, int radius,boolean reverse) {
        Point point = new Point();
        point.y = (int) (Math.sin(deg * Math.PI / 180.0) * radius);
        point.x = reverse ? (int) (Math.cos(deg * Math.PI / 180.0) * radius) : -(int) (Math.cos(deg * Math.PI / 180.0) * radius);
        return point;
    }

    private ValueAnimator mAnimator;




}
