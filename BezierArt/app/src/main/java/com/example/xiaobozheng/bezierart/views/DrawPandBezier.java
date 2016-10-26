package com.example.xiaobozheng.bezierart.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Switch;

/**
 * 圆滑路径
 * Created by xiaobozheng on 10/26/2016.
 */
public class DrawPandBezier extends View{

    //起始点
    private float mX;
    private float mY;
    //得到滑动的最小距离
    private float offset = ViewConfiguration.get(getContext()).getScaledDoubleTapSlop();

    private Paint mPaint;
    private Path mPath;

    public DrawPandBezier(Context context) {
        super(context);
    }

    public DrawPandBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        //用于绘制时抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.RED);
    }

    public DrawPandBezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                float x = event.getX();
                float y = event.getY();

                mX = x;
                mY = y;
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                float x1 = event.getX();
                float y1 = event.getY();
                float preX = mX;
                float preY = mY;
                //它移动的距离
                float dx = Math.abs(x1 - preX);
                float dy = Math.abs(y1 - preY);

                if (dx >= offset || dy >= offset){
                    //起始位置+终止位置的中间点
                    float cX = (x1 + preX) / 2;
                    float cY = (y1 + preY) / 2;

                   mPath.quadTo(preX, preY, cX, cY);
                   // mPath.lineTo(x1, y1);
                    mX = x1;
                    mY = y1;
                }
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }
}
