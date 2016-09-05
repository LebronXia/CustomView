package com.example.galleryview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaobozheng on 9/5/2016.
 */
public class Gallery extends SmoothViewGroup {

    private List<String> mImgList = new ArrayList<>();
    private ImageView[] mImageViews = new ImageView[2];
    private View mShadowView;

    public Gallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Gallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void initView(){
        if (mImgList.size() == 0){
            return;
        }
        removeAllViews();

        //去除margin值，两个ImageView加载前两张图
        MarginLayoutParams params = new MarginLayoutParams(mWidth, mHeight);
        for (int i = 0; i < mImageViews.length; i++){
            mImageViews[i] = new ImageView(getContext());
            addViewInLayout(mImageViews[i], -1, params, true);
        }

        //将阴影部分添加到里面
        mShadowView = new View(getContext());
        mShadowView.setBackgroundColor(Color.parseColor("#60000000"));
        mShadowView.setAlpha(0);
        addViewInLayout(mShadowView, -1, params, true);
    }

    /**
     * 通过控制两个ImageView和他们的marginTop，在onLayout中实现
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        MarginLayoutParams cParams;

        for(int i = 0; i < cCount; i++){
            View childView = getChildAt(i);
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0,ct = 0, cr, cb;
            //判断是否为奇数
            if (isOddCircle()){
                if (i == 1){
                    cl = cParams.leftMargin;
                    //显示mHeight的高度
                    ct = mSmoothMarginTop + mHeight;
                } else if (i == 0){
                    cl = cParams.leftMargin;
                    ct = mSmoothMarginTop;
                }
            } else {
                if (i == 0){
                    cl = cParams.leftMargin;
                    ct = mSmoothMarginTop + mHeight;
                } else if (i == 1){
                    cl = cParams.leftMargin;
                    ct = mSmoothMarginTop;
                }
            }

            //控制shadowView
            if (i == 2){
                cl = cParams.leftMargin;
                ct = mSmoothMarginTop + mHeight;
            }
            cr = cl + mWidth;
            cb = ct + mHeight;
            childView.layout(cl, ct, cr, cb);
        }
    }

    @Override
    protected void doAnimFinish() {
        if (isOddCircle()){
            Glide.with(getContext()).load(getImgPath(mRepeatTimes + 1)).centerCrop().into(mImageViews[0]);
        } else {
            Glide.with(getContext()).load(getImgPath(mRepeatTimes + 1)).centerCrop().into(mImageViews[1]);
        }
        mShadowView.setAlpha(0);
    }

    @Override
    protected void doAnim() {
        mShadowView.setAlpha((1 - (-mSmoothMarginTop) / (float)mHeight));
        //开启动画不断刷新
        requestLayout();
    }

    /**
     * 获取图片地址
     * @param position
     * @return
     */
    private String getImgPath(int position){
        position = position % mImgList.size();
        return mImgList.get(position);
    }
}
