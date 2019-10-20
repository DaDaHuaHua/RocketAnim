package com.example.rocket.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.rocket.R;

/**
 * Created by SongH on 2019/10/19.
 */
public class HexagonAnimLayout extends RelativeLayout {
    private HexagonAnimView mHexagonAnimView;
    private ImageView mIvRotate;
    private ObjectAnimator mRotateAnim;

    public HexagonAnimLayout(Context context) {
        this(context, null);
    }

    public HexagonAnimLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HexagonAnimLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addHexagonAnimView();
        addRotateView();
    }

    private void addRotateView() {
        mIvRotate = new ImageView(getContext());
        mIvRotate.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mIvRotate.setImageResource(R.mipmap.ic_fun);
        addView(mIvRotate);
    }

    private void addHexagonAnimView() {
        mHexagonAnimView = new HexagonAnimView(getContext());
        mHexagonAnimView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mHexagonAnimView);
    }

    private int mRepeatCount;

    public void setRotateStart() {
        if(mHexagonAnimView != null){
            mHexagonAnimView.clearView();
        }
        mIvRotate.setVisibility(VISIBLE);
        if (mRotateAnim != null && mRotateAnim.isStarted()) {
            mRotateAnim.cancel();
        }
        mRotateAnim = ObjectAnimator.ofFloat(mIvRotate, "rotation", 0, 360);
        mRotateAnim.setRepeatCount(-1);
        mRotateAnim.setInterpolator(new LinearInterpolator());
        mRotateAnim.setDuration(800);
        mRotateAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                mRepeatCount++;
                if (mListener != null) {
                    mListener.onRoundRepeat(mRepeatCount);
                }
            }
        });
        mRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(mListener!= null){
                    mListener.onRotationRunning(((float) animation.getAnimatedValue()));
                }
            }
        });
        mRotateAnim.setRepeatMode(ValueAnimator.RESTART);
        mRotateAnim.start();
    }

    private static final String TAG = "HexagonAnimLayout";

    /**
     *  设置转圈结束，显示六边形动画效果
     */
    public void setRotateEnd() {
        ValueAnimator alphaAnim = ValueAnimator.ofFloat(1.0f,0.0f);
        alphaAnim.setDuration(200);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "onAnimationUpdate: 进度：" + animation.getAnimatedValue());
                mIvRotate.setAlpha((float) animation.getAnimatedValue());
                if((float) animation.getAnimatedValue() == 0.0F){
                    mIvRotate.setAlpha(1.0f);
                    mIvRotate.setVisibility(GONE);
                    if (mRotateAnim != null && mRotateAnim.isStarted()) {
                        mRotateAnim.cancel();
                        if(mListener!= null){
                            mListener.onRoundRotationComplete(mRepeatCount);
                        }
                    }
                    mHexagonAnimView.startRadioAnim();
                }
            }
        });
        alphaAnim.start();
    }

    /**
     * 暂停全部动画
     */
    public void pause(){
        if(mRotateAnim != null && mRotateAnim.isRunning()){
            mRotateAnim.pause();
        }
        if(mHexagonAnimView!= null ){
            mHexagonAnimView.pauseScaleAnim();
        }
    }

    /**
     *  恢复全部动画效果，与{@link #pause()}对应
     */
    public void restore(){
        if(mRotateAnim != null && mRotateAnim.isPaused()){
            mRotateAnim.resume();
        }
        if(mHexagonAnimView != null){
            mHexagonAnimView.resumeScaleAnim();
        }
    }


    //-------------- listener ----------------
    public interface AnimListener extends HexagonAnimView.OnInnerHexagonAnimListener {

        /**
         *  旋转动画执行中的回调
         * @param aRoundFraction 会回调多次 0.0~1.0的进度
         */
        void onRotationRunning(float aRoundFraction);

        /**
         * 旋转中的回调
         * @param repeatCount 旋转总次数
         */
        void onRoundRepeat(int repeatCount);

        /**
         * 旋转结束的回调，当调用{@link #setRotateEnd()}时回调
         * @param repeatCount 旋转总次数
         */
        void onRoundRotationComplete(int repeatCount);
    }


    public static class AnimListenerSimple implements AnimListener{

        @Override
        public void onRotationRunning(float aRoundFraction) {

        }

        @Override
        public void onRoundRepeat(int repeatCount) {

        }

        @Override
        public void onRoundRotationComplete(int repeatCount) {

        }

        @Override
        public void onRadioAnimUpdate(float fraction) {

        }

        @Override
        public void onLoadAnimComplete() {

        }
    }

    private AnimListener mListener;

    public void setAnimListener(AnimListener listener) {
        this.mListener = listener;
    }



}
