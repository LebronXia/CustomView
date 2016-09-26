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
 * Created by xiaobozheng on 9/26/2016.
 */
public class Gallery extends SmoothViewGroup{

    private List<String> mImgList = new ArrayList<>();
    private ImageView[] mImgs = new ImageView[2];
    private View mShadowView;

    public Gallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Gallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {

        if (mImgList.size() == 0){
            return;
        }

        removeAllViews();
        MarginLayoutParams params = new MarginLayoutParams(mWidth, mHeight);
        //两个ImageView加载前两张图
        for (int i = 0; i < mImgs.length; i++){
            mImgs[i] = new ImageView(getContext());
            //为了requestLayout的绝对控制
            addViewInLayout(mImgs[i], -1, params, true);
            Glide.with(getContext()).load(getImgPath(i)).centerCrop().into(mImgs[i]);
        }

        //创建阴影View
        mShadowView = new View(getContext());
        mShadowView.setBackgroundColor(Color.parseColor("#60000000"));
        mShadowView.setAlpha(0);
        addViewInLayout(mShadowView, -1, params, true);
    }

    /**
     * 使用一定的规律交替控制两个ImageView和它们的marginTop
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
        for (int i = 0; i < cCount; i++){
            //得到子View
            View childView = getChildAt(i);
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr, cb;

            if (isOddCircle()){
                if (i == 1){
                    cl = cParams.leftMargin;
                    ct = mSmoothMarginTop + mHeight;
                } else if (i == 0){
                    cl = cParams.leftMargin;
                    ct = mSmoothMarginTop;
                }
            } else {
                if (i == 0){
                    cl = cParams.leftMargin;
                    ct = mSmoothMarginTop + mHeight;
                } else {
                    cl = cParams.leftMargin;
                    ct = mSmoothMarginTop;
                }
            }

            //控制shadowView
            if( i == 2){
                cl = cParams.leftMargin;
                ct = mSmoothMarginTop + mHeight;
            }

            cr = cl + mWidth;
            cb = ct + mHeight;
            childView.layout(cl, ct, cr, cb);
        }
    }

    @Override
    protected void doAnim() {
        //动画进行时
        mShadowView.setAlpha(((1 - (-mSmoothMarginTop)/ (float)mHeight)));
        requestLayout();
    }

    @Override
    protected void doAnimFinish() {
        if (isOddCircle()) {
            Glide.with(getContext()).load(getImgPath(mRepeatTimes + 1)).centerCrop().into(mImgs[0]);
        } else {
            Glide.with(getContext()).load(getImgPath(mRepeatTimes + 1)).centerCrop().into(mImgs[1]);
        }
        mShadowView.setAlpha(0);
    }

    public void setImgList(List<String> imgList){
        mImgList = imgList;
        initView();
    }
    /**
     * 为了实现循环行动
     * @param position
     * @return
     */
    private String getImgPath(int position){
        position = position % mImgList.size();
        return mImgList.get(position);
    }
}
