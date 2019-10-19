package com.example.rocket.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SongH on 2019/10/19.
 */
public class HexagonAnimView extends View {
    private static final String TAG = "HexagonAnimView";
    public static final int INNER_PADDING = 200;
    private static double S_R = Math.sqrt(3);//square_root 3

    private int mViewWidth;
    private int mViewHeight;
    private int mInnerViewLength;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintClearDraw = new Paint(Paint.ANTI_ALIAS_FLAG);


    private AnimatorSet mScaleAnim ;
    public HexagonAnimView(Context context) {
        this(context, null);
    }

    public HexagonAnimView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HexagonAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaintClearDraw.setStrokeWidth(7);
        mPaintClearDraw.setStyle(Paint.Style.STROKE);
        mPaintClearDraw.setColor(Color.TRANSPARENT);
        mPaintClearDraw.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        createDrawPath();


        mHexagonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHexagonPaint.setColor(Color.parseColor("#ffffff"));
        mHexagonPaint.setStrokeWidth(10);
        mHexagonPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mInnerViewLength = mViewHeight - INNER_PADDING;
    }


    private PorterDuffXfermode mClearXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private PorterDuffXfermode mSrcXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth / 2f, mViewHeight / 2f);


        if (mClearFlag) {
            clearContent(canvas);
            return;
        }

        for (int i = 0; i < mPathDraw.size(); i++) {
            if (mRadioDrawFlag) {//画放射效果
                canvas.drawPath(mPathDraw.get(i), mPaint);
            }
            if (mReverseDrawFlag) {//画清空效果
                canvas.drawPath(mPathReverse.get(i), mPaintClearDraw);
            }
            if (mBorderAnimFlag) {//画边框效果
                canvas.rotate(60);
                float curLength = mPathLength * mPercent;
                mPathDestLeft.reset();
                boolean succLeft = mPathMeasureLeft.getSegment(0, curLength, mPathDestLeft, true);
                if (succLeft) {
                    canvas.drawPath(mPathDestLeft, mPaint);
                }
                mPathDestRight.reset();
                boolean succRight = mPathMeasureRight.getSegment(0, curLength, mPathDestRight, true);
                if (succRight) {
                    canvas.drawPath(mPathDestRight, mPaint);
                }
            }
        }


        if(mHexagonDrawFlag){
            Log.i(TAG, "画六边形" );
            canvas.drawPath(mHexagonPath,mHexagonPaint);
        }
    }

//------------------------  放射动画Start    -----------------------

    private boolean mReverseDrawFlag;
    private boolean mRadioDrawFlag;
    private ArrayList<Path> mPathDraw = new ArrayList<Path>(6);
    private ArrayList<Path> mPathReverse = new ArrayList<Path>(6);
    private boolean mOnceFlag;

    private void createDrawPath() {
        for (int i = 0; i < 6; i++) {
            mPathDraw.add(new Path());
            mPathReverse.add(new Path());
        }
    }

    public void startRadioAnim() {
        startAnim(false);
    }


    private void startAnim(final boolean isReverse) {
        if (!isReverse) {
            mRadioDrawFlag = true;
        }
        mReverseDrawFlag = isReverse;
        ValueAnimator animator = ValueAnimator.ofInt(0, mInnerViewLength / 2);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                if (fraction == 1.0F) {
                    if (!isReverse) {
                        startAnim(true);
                    } else {
                        mReverseDrawFlag = false;
                        mRadioDrawFlag = false;
                    }
                    return;
                }
                int percent = (int) (fraction * 100);
                Log.i(TAG, "onAnimationUpdate: 进度：" + percent);
                if (percent >= 50) {
                    if (isReverse && !mOnceFlag) {
                        Log.i(TAG, "onAnimationUpdate:  开始边框动画");
                        mOnceFlag = true;
                        startBorderAnim(mInnerViewLength / 2);
                    }
                }
                refreshDraw((int) animation.getAnimatedValue(), isReverse ? mPathReverse : mPathDraw);
            }
        });
        animator.start();
    }


    private void refreshDraw(int value, List<Path> pathDraw) {
        int bottomRightX = (int) (S_R / 2 * value);
        int bottomRightY = value / 2;
        //上
        pathDraw.get(0).lineTo(0, -value);
        //上右
        pathDraw.get(1).lineTo(bottomRightX, -bottomRightY);
        //下右
        pathDraw.get(2).lineTo(bottomRightX, bottomRightY);
        //下
        pathDraw.get(3).lineTo(0, value);
        //下左
        pathDraw.get(4).lineTo(-bottomRightX, bottomRightY);
        //上左
        pathDraw.get(5).lineTo(-bottomRightX, -bottomRightY);
        invalidate();
    }

//------------------------  放射动画End    -----------------------
//------------------------ 边框动画start   -----------------------

    private float mPercent;
    private boolean mBorderAnimFlag;
    private PathMeasure mPathMeasureLeft;
    private PathMeasure mPathMeasureRight;
    private Path mPathDestLeft = new Path();
    private Path mPathDestRight = new Path();
    private float mPathLength;

    private void startBorderAnim(int h) {
        Path borderPathRight = new Path();
        borderPathRight.moveTo(0, -h);
        borderPathRight.lineTo((float) (S_R * h / 4.0), -3f / 4 * h);
        Path borderPathLeft = new Path();
        borderPathLeft.moveTo(0, -h);
        borderPathLeft.lineTo((float) (-S_R * h / 4.0), -3f / 4 * h);
        mBorderAnimFlag = true;
        mPathMeasureLeft = new PathMeasure(borderPathLeft, false);
        mPathMeasureRight = new PathMeasure(borderPathRight, false);
        mPathLength = mPathMeasureLeft.getLength();


        ValueAnimator borderAnim = ValueAnimator.ofFloat(0, 1.0f);
        borderAnim.setDuration(1000);
        borderAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (float) animation.getAnimatedValue();
                if (animation.getAnimatedFraction() == 1.0F) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startHexagonScaleAnim();
                        }
                    },300);
                }
                invalidate();
            }
        });
        borderAnim.start();
    }
//------------------------ 边框动画End   -----------------------
//------------------------  缩放动画Start  -----------------------

    private Path mHexagonPath = new Path();
    private Paint mHexagonPaint;
    private boolean mHexagonDrawFlag;

    private void refreshHexagonPath(float length) {
        mHexagonPath.reset();
        mHexagonPath.moveTo(0, length);//下
        mHexagonPath.lineTo((float) (S_R / 2f * length), length / 2f);//右下
        mHexagonPath.lineTo((float) (S_R / 2f * length), -length / 2f);//右上
        mHexagonPath.lineTo(0, -length);//上
        mHexagonPath.lineTo((float) (-S_R / 2f * length), -length / 2f);//左上
        mHexagonPath.lineTo((float) (-S_R / 2f * length), length / 2f);//左下
        mHexagonPath.close();//回到下
    }

    public void startHexagonScaleAnim() {
        mHexagonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHexagonPaint.setColor(Color.parseColor("#88ffffff"));
        mHexagonPaint.setStrokeWidth(3);
        mHexagonPaint.setStyle(Paint.Style.STROKE);
        int maxLength = mViewHeight - 4;
        int minLength = mInnerViewLength - 4;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(minLength,maxLength);
        valueAnimator.setDuration(1000);
        mHexagonDrawFlag = true;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                Log.i(TAG, "onAnimationUpdate: 六边形动画：进度" + fraction +" ,value="+animation.getAnimatedValue());
                if(fraction == 1.0F){
                    startAllScale();
                }
                refreshHexagonPath((float) animation.getAnimatedValue()/2f);
                invalidate();
            }
        });
        valueAnimator.start();
    }
    //------------------------  缩放动画End   -----------------------

    //------------------------  整体缩放动画Start   -----------------------

    private void startAllScale(){
        ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(this,"scaleX",1.0f,0.9f);
        ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(this,"scaleY",1.0f,0.9f);
        mScaleAnim = new AnimatorSet();
        mScaleAnim.playTogether(scaleXAnim,scaleYAnim);
        mScaleAnim.setDuration(800);
        scaleXAnim.setRepeatCount(-1);
        scaleYAnim.setRepeatCount(-1);
        scaleXAnim.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnim.setRepeatMode(ValueAnimator.REVERSE);
        mScaleAnim.start();
        if(mListener!= null){
            mListener.onLoadAnimComplete();
        }
    }
//------------------------  整体缩放动画End   -----------------------
//------------------------  暂停缩放动画效果Start   -----------------------

    public void pauseScaleAnim(){
        if(mScaleAnim != null && mScaleAnim.isRunning()){
            mScaleAnim.pause();
        }
    }

    public void resumeScaleAnim(){
        if(mScaleAnim != null && mScaleAnim.isPaused()){
            mScaleAnim.resume();
        }
    }
//------------------------  暂停缩放动画效果End   -----------------------
//------------------------  清除动画效果Start   -----------------------
    private boolean mClearFlag;
    public void clearView() {
        mClearFlag = true;
        invalidate();
    }

    private void clearContent(Canvas canvas) {
        mPaint.setXfermode(mClearXfermode);
        canvas.drawPaint(mPaint);
        mPaint.setXfermode(mSrcXfermode);
        mClearFlag = false;
        mHexagonDrawFlag =false;
        mOnceFlag = false;
        mBorderAnimFlag = false;
        mRadioDrawFlag = false;
        for (Path path : mPathReverse) {
            path.reset();
        }
        for (Path path : mPathDraw) {
            path.reset();
        }
        mPathDestRight.reset();
    }

 //------------------------  清除动画效果End   -----------------------
//------------------------  设置监听Start   -----------------------

    public interface OnInnerHexagonAnimListener{
        void onLoadAnimComplete();
    }

    private OnInnerHexagonAnimListener mListener;
    public void setOnInnerHexagonAnimListener(OnInnerHexagonAnimListener listener){
        this.mListener = listener;
    }
//------------------------  设置监听End   -----------------------




}
