package com.riane.practicecustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义水平进度条
 * Created by Riane on 2017/6/26.
 */

public class HorizontalProgressBar extends View{

    private Paint bgPaint;
    private Paint progressPaint;

    private int mWidth;
    private int mHeight;
    private int mViewHeight;

    //进度条画笔的宽度
    private int progressPaintWidth;
    private int bgColor;
    private int progressColor;



    public HorizontalProgressBar(Context context) {
        super(context);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        bgPaint = getPaint(progressPaintWidth, bgColor, Paint.Style.STROKE);
        progressPaint = getPaint(progressPaintWidth, progressColor, Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(getPaddingLeft(), 0, getWidth(), 0, bgPaint);
        canvas.drawLine(getPaddingLeft(), 0, getWidth(), 0, progressPaint);

    }

    /**
     * 统一处理Paint
     * @param strokeWidth
     * @param color
     * @param style
     * @return
     */
    private Paint getPaint(int strokeWidth, int color, Paint.Style style){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(style);
        return paint;
    }

}
