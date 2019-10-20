package com.example.rocket.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.example.rocket.R;

/**
 * Created by SongH on 2019/10/20.
 */
public class RocketAnimLayout extends RelativeLayout {

    private View mLayoutAnim;
    private RocketTailView mRocketTailView;

    public RocketAnimLayout(Context context) {
        this(context, null);
    }

    public RocketAnimLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RocketAnimLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.layout_rocket_anim, this);
        mLayoutAnim = findViewById(R.id.layout_anim);
        mRocketTailView = findViewById(R.id.rocket_tail_view);
        mLayoutAnim.setVisibility(INVISIBLE);

    }




    //-------------- 外部调用方法start

    /**
     * 开始动画，默认loading时间为2600
     */
    public void startAnim() {
        startAnim(DEFAULT_LOADING_DURATION);
    }
    /**
     * 开始动画
     * @param loadingDuration  设置loading时间
     */
    public void startAnim(int loadingDuration) {
        this.mLoadingDuration = loadingDuration;
        mLayoutAnim.setVisibility(VISIBLE);
        startTransAnim();
        if (mRocketTailView != null) {
            mRocketTailView.start();
        }
    }

    /**
     * 设置监听
     */
    public void setListener(AnimListener listener) {
        this.mAnimListener = listener;
    }

    /**
     * 取消动画
     */
    public void cancelAll(){
        if(mAnimatorSet != null && mAnimatorSet.isRunning()){
            mAnimatorSet.cancel();
        }
        if(mRocketTailView!=null){
            mRocketTailView.purge();
        }
    }

    //-------------- 外部调用方法end





    private int mViewHeight;
    private int mRocketLayoutHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mRocketLayoutHeight = mLayoutAnim.getMeasuredHeight();
    }


    private static final int DEFAULT_LOADING_DURATION = 2600;
    private int mLoadingDuration;



    private AnimListener mAnimListener;



    public interface AnimListener {
        void onLoading(float fraction);
    }



    private AnimatorSet mAnimatorSet;

    public void startTransAnim() {
        setTranslationY(0);
        mAnimatorSet = new AnimatorSet();

        float middleTargetTransition = ((mViewHeight / 2f + mRocketLayoutHeight / 4f));
        ObjectAnimator mToMiddleTrans = ObjectAnimator.ofFloat(mLayoutAnim, "translationY", mRocketLayoutHeight, -middleTargetTransition);
        mToMiddleTrans.setInterpolator(new AccelerateDecelerateInterpolator());
        mToMiddleTrans.setDuration(800);

        float keepTargetTransition = middleTargetTransition + mRocketLayoutHeight / 2f;
        ObjectAnimator mKeepAnimTrans = ObjectAnimator.ofFloat(mLayoutAnim, "translationY", -middleTargetTransition, -keepTargetTransition);
        mKeepAnimTrans.setInterpolator(new LinearInterpolator());
        mKeepAnimTrans.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(mAnimListener != null){
                    mAnimListener.onLoading(animation.getAnimatedFraction());
                }
            }
        });
        mKeepAnimTrans.setDuration(mLoadingDuration);
        float removeTargetTransition = mViewHeight + mRocketLayoutHeight;
        ObjectAnimator mRemoveAnimTrans = ObjectAnimator.ofFloat(mLayoutAnim, "translationY", -keepTargetTransition, -removeTargetTransition);
        mRemoveAnimTrans.setInterpolator(new AccelerateDecelerateInterpolator());
        mRemoveAnimTrans.setDuration(600);
        mRemoveAnimTrans.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if((float)animation.getAnimatedValue() == 1.0F){
                    mRocketTailView.purge();
                }
            }
        });

        mAnimatorSet.playSequentially(mToMiddleTrans, mKeepAnimTrans, mRemoveAnimTrans);
        mAnimatorSet.start();


    }


}
