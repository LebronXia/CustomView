package com.example.galleryview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by xiaobozheng on 9/5/2016.
 */
public abstract class SmoothViewGroup extends ViewGroup{

    /**
     * 滑动状态
     */
    protected static final int STATUS_SMOOTHING = 0;
    /**
     * 停止状态
     */
    protected static final int STATUS_STOP = 1;

    //ViewGroup宽高
    protected int mWidth, mHeight;
    //变化的marginTop值
    protected int mSmoothMarginTop;
    //默认状态
    protected int mStatus = STATUS_STOP;
    //滚动事件间隔
    protected int mDuration = 500;
    //重复次数
    protected int mRepeatTimes;

    public SmoothViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmoothViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mSmoothMarginTop = -h;
        initView();
    }

    protected abstract void initView();

    /**
     * 开启滑动
     */
    public void startSmooth(){
        if (mStatus != STATUS_STOP){
            return;
        }

        final ValueAnimator animator = ValueAnimator.ofFloat(-mHeight, 0);
        animator.setDuration(mDuration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float marginTop = (float) valueAnimator.getAnimatedValue();
                mSmoothMarginTop = (int) marginTop;

                if (marginTop == 0){
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          mRepeatTimes ++;
                            mSmoothMarginTop = -mHeight;
                            doAnimFinish();
                            mStatus = STATUS_STOP;
                        }
                    },50);
                } else {
                    doAnim();
                }
            }
        });
        animator.start();
        mStatus = STATUS_SMOOTHING;
    }

    /**
     * 动画结束
     */
    protected abstract void doAnimFinish();

    /**
     * 动画进行时
     */
    protected abstract void doAnim();

    protected boolean isOddCircle(){
        return mRepeatTimes % 2 == 1;
    }
}
