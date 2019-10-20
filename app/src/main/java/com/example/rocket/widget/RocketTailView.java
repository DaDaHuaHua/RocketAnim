package com.example.rocket.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by SongH on 2019/10/20.
 */
public class RocketTailView extends View {
    private int mViewWidth;
    private int mViewHeight;
    public static final int RADIUS = 20;
    private LinkedList<Point> mAlphaPoints = new LinkedList<>();
    private LinkedList<Point> mPoints = new LinkedList<>();
    private Paint mPaint;
    private Paint mPaintTrans;
    private static final String TAG = "RocketTailView";

    public RocketTailView(Context context) {
        this(context, null);
    }

    public RocketTailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RocketTailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTrans = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaintTrans.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaintTrans.setColor(Color.parseColor("#66ffffff"));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
    }


    public void start() {
        mAlphaPoints.clear();
       mHandler.sendEmptyMessage(MSG_REFRESH_TAIL_ANIM);
    }

    public void purge(){
        mHandler.removeCallbacksAndMessages(null);
    }


    public static final int MSG_REFRESH_TAIL_ANIM = 0X01;
    public static final int TAIL_REFRESH_INTERVAL = 100;



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_REFRESH_TAIL_ANIM:
                    createAndDraw();
                    sendEmptyMessageDelayed(MSG_REFRESH_TAIL_ANIM,TAIL_REFRESH_INTERVAL);
                    break;
            }
        }
    };


    public void createAndDraw(){
        createAlphaPoint();
        createPoint();
        invalidate();
    }


    private void createAlphaPoint() {
        mAlphaPoints.clear();
        int startX = mViewWidth / 2;
        int startY = mViewHeight / 8;
        int count = 40;
        int xRange = mViewWidth / 4;
        for (int i = 0; i < 6; i++) {
            Part partTopCenter = new Part(startX, startY, count, xRange / 2, RADIUS * 2);
            partTopCenter.addRandomPoint(mAlphaPoints);
            startY += mViewHeight / 15;
            xRange += mViewWidth / 10;
            if(i>=4){
                count+=20;
            }
            count+=5;
        }

    }


    private void createPoint(){
        mPoints.clear();
        int startX = mViewWidth / 2;
        int startY = mViewHeight / 10;
        int count = 20;
        int xRange = mViewWidth / 4;
        for (int i = 0; i < 8; i++) {
            Part partTopCenter = new Part(startX, startY, count, xRange / 2, RADIUS );
            partTopCenter.addRandomPoint(mPoints);
            startY += mViewHeight / 30;
            xRange += mViewWidth / 30;
           if(i>3){
               count-=3;
           }else{
               count+= 3;
           }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point point : mPoints) {
            canvas.drawCircle(point.x - RADIUS, point.y - RADIUS, RADIUS, mPaint);
        }
        for (Point point : mAlphaPoints) {
            canvas.drawCircle(point.x - RADIUS, point.y - RADIUS, RADIUS, mPaintTrans);
        }



    }


    private static class Part {
        private Random mRandom;
        private Point mCenterPoint;
        private int mCount;
        private int mRangeX;
        private int mRangeY;

        public Part(int x, int y, int count, int xRange, int yRange) {
            this.mCenterPoint = new Point(x, y);
            this.mCount = count;
            this.mRangeX = xRange;
            this.mRangeY = yRange;
            mRandom = new Random();
        }

        public void addRandomPoint(List<Point> list) {
            for (int i = 0; i < mCount; i++) {
                int x = mRandom.nextInt(mRangeX * 2) + mCenterPoint.x - mRangeX;
                int y = mRandom.nextInt(mRangeY * 2) + mCenterPoint.y - mRangeY;
                Point p = new Point(x, y);
                list.add(p);
            }
        }
    }
}
