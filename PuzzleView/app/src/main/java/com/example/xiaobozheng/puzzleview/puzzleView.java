package com.example.xiaobozheng.puzzleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaobozheng on 11/11/2016.
 */
public class puzzleView extends View {

    private int mViewWidth, mViewHeight;
    private List<Bitmap> mBitmaps = new ArrayList<>();

    public puzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;
        initBitmaps();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipRect(0, 0, getWidth()/2, getHeight());
        canvas.drawBitmap(mBitmaps.get(0), 0, 0, null);
        canvas.restore();

        canvas.save();
        canvas.clipRect(getWidth() / 2, 0, getWidth(), getHeight());
        canvas.drawBitmap(mBitmaps.get(1), 0, 0, null);
        canvas.restore();
    }

    /**
     * 初始化位图数据
     * 跟屏幕匹配
     */
    private void initBitmaps(){
        List<Bitmap> temp = new ArrayList<Bitmap>();
        for (int i = mBitmaps.size() - 1; i >= 0; i--){
            //按照比例来缩放图片
            Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), mViewWidth, mViewHeight, true);
            temp.add(bitmap);
        }
        mBitmaps = temp;
    }

    /**
     * 设置位图数据
     * @param mBitmaps
     */
    public synchronized void setBitmaps(List<Bitmap> mBitmaps){
        if (null == mBitmaps || mBitmaps.size() == 0){
            throw new IllegalArgumentException("no Bitmap to display");
        }
        if (mBitmaps.size() < 2){
            throw new IllegalArgumentException("bitmap less");
        }

        this.mBitmaps = mBitmaps;
        invalidate();  //通知重新绘制
    }
}
