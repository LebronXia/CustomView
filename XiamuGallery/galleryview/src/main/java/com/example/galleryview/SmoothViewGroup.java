package com.example.galleryview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 自定义控件：
 * 1.自定义ViewGroup
 * 2.装载里两个ImageView和一个阴影View
 * 3.通过一定规律交替控制两个ImageView和它们的marginTop
 * 4.marginTop的具体值由属性动画控制，不断调用requestLayout()
 * Created by xiaobozheng on 9/26/2016.
 */
public abstract class SmoothViewGroup extends ViewGroup{

    //滑动状态
    protected static final int STATUS_SMOOTHING = 0;
    //停止状态
    protected static final int STATUS_STOP = 1;

    //ViewGroup宽高
    protected int mWidth, mHeight;
    //变化的marginTop值
    protected int mSmoothMarginTop;
    //默认状态
    protected int mStatus = STATUS_STOP;
    //滚动时间间隔
    protected int mDuration = 500;
    //重复次数
    protected int mRepeatTimes = 0;

    public SmoothViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmoothViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 只有在View大小发生变化的时候才会调用
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
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
     * 是否是奇数圈
     * @return
     */
    protected boolean isOddCircle(){
        return mRepeatTimes % 2 == 1;
    }

    /**
     * 开启滑动
     */
    public void startSmooth(){
        if (mStatus != STATUS_STOP){
            return;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(-mHeight , 0);
        animator.setDuration(mDuration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //得到距离的高度
                float marginTop = (float) valueAnimator.getAnimatedValue();
                mSmoothMarginTop = (int) marginTop;

                //动画没动
                if (marginTop == 0){

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //发生动画一次，重复加1
                            mRepeatTimes++;
                            mSmoothMarginTop = -mHeight;
                            doAnimFinish();

                            mStatus = STATUS_STOP;
                        }
                    }, 50);
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
    protected abstract void doAnim();

    /**
     * 动画进行时
     */
    protected abstract void doAnimFinish();


}
