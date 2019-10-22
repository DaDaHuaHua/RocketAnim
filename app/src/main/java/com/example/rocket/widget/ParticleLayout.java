package com.example.rocket.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.rocket.R;

/**
 * Created by SongH on 2019/10/22.
 */
public class ParticleLayout extends RelativeLayout {

    private View mDot1;
    private View mDot2;
    private View mDot3;
    private View mDot4;

    public ParticleLayout(Context context) {
        this(context,null);
    }

    public ParticleLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ParticleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDot1 = createDot(R.drawable.ic_dot_trans,40);
        mDot2 = createDot(R.drawable.ic_dot,30);
        mDot3 = createDot(R.drawable.ic_dot,50);
        mDot4 = createDot(R.drawable.ic_dot_trans,20);
    }

    private View createDot(int drawableRes,int radius){
        View v = new View(getContext());
        v.setBackgroundResource(drawableRes);
        addView(v,radius,radius);
        return v;
    }

    private int mCenterX ;
    private int mCenterY;
    private int mViewWidth;
    private int mViewHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        mCenterX  = mViewWidth/2;
        mCenterY = mViewHeight/2;
        prepareLocation(mDot1,mDot2,mDot3,mDot4);

    }

    private void prepareLocation(View... dots){
        int distance = 0;

        for (View dot : dots) {
            dot.setX(50+(distance+=50));
            dot.setY(mCenterY+(distance+=30));
        }
        invalidate();
    }

    public void startViewAnim(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,mCenterX,0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });
    }


}
