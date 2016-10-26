package com.example.xiaobozheng.bezierart.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by xiaobozheng on 10/26/2016.
 */
public class ThirdOrderBezier extends View implements View.OnClickListener{
    //辅助线
    private Paint mPaintAuxiliary;
    //控制点名称
    private Paint mPaintAuxiliaryText;
    //贝塞尔曲线
    private Paint mPaintBezier;

    //控制点one坐标
    private float mAuxiliaryOneX;
    private float mAuxiliaryOneY;

    //控制点Two坐标
    private float mAuxiliaryTwoX;
    private float mAuxiliaryTwoY;

    //起始点坐标
    private float mStartPointX;
    private float mStartPointY;
    //终点坐标
    private float mEndPointX;
    private float mEndPointY;

    private boolean isSecondPoint = false;
    private Path mPath = new Path();
    private ValueAnimator mValueAnimator;

    public ThirdOrderBezier(Context context) {
        super(context);
    }

    public ThirdOrderBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintBezier = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setStyle(Paint.Style.STROKE); //设置画笔为空心
        mPaintBezier.setStrokeWidth(8);

        mPaintAuxiliary = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliary.setStyle(Paint.Style.STROKE);
        mPaintAuxiliary.setStrokeWidth(2);

        mPaintAuxiliaryText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintAuxiliaryText.setStyle(Paint.Style.STROKE);
        mPaintAuxiliaryText.setTextSize(20);
        setOnClickListener(this);

    }

    public ThirdOrderBezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //起始和终点的坐标
        mStartPointX = w / 4;
        mStartPointY = h /2 - 200;

        mEndPointX = w / 4 * 3;
        mEndPointY = h /2 - 200;

        mAuxiliaryOneX = mStartPointX;
        mAuxiliaryOneY = mStartPointY;
        mAuxiliaryTwoX = mEndPointX;
        mAuxiliaryTwoY = mEndPointY;

        mValueAnimator = ValueAnimator.ofFloat(mStartPointX, (float) h);
        //设置插值器,会弹起
        mValueAnimator.setInterpolator(new BounceInterpolator());
        mValueAnimator.setDuration(1000);
        //增加动画监听
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAuxiliaryOneY = (float) valueAnimator.getAnimatedValue();
                mAuxiliaryTwoY = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        mPath.moveTo(mStartPointX, mStartPointY);

        // 辅助点
        canvas.drawPoint(mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliary);
        canvas.drawPoint(mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliary);
        canvas.drawText("控制点1", mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliaryText);
        canvas.drawText("控制点2", mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliaryText);
        canvas.drawText("起始点", mStartPointX, mStartPointY, mPaintAuxiliaryText);
        canvas.drawText("终止点", mEndPointX, mEndPointY, mPaintAuxiliaryText);
        mPath.cubicTo(mAuxiliaryOneX, mAuxiliaryOneY, mAuxiliaryTwoX, mAuxiliaryTwoY, mEndPointX, mEndPointY);
        // 辅助线
        canvas.drawLine(mStartPointX, mStartPointY, mAuxiliaryOneX, mAuxiliaryOneY, mPaintAuxiliary);
        canvas.drawLine(mEndPointX, mEndPointY, mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliary);
        //三阶贝塞尔曲线
        canvas.drawLine(mAuxiliaryOneX, mAuxiliaryOneY, mAuxiliaryTwoX, mAuxiliaryTwoY, mPaintAuxiliary);
        canvas.drawPath(mPath, mPaintBezier);
    }

    @Override
    public void onClick(View view) {
        mValueAnimator.start();
    }

    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //ACTION_MASK处理多点触控
//        switch (event.getAction() & MotionEvent.ACTION_MASK){
//            case MotionEvent.ACTION_POINTER_DOWN:
//                isSecondPoint = true;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                mAuxiliaryOneX = event.getX(0);
//                mAuxiliaryOneY = event.getY(0);
//
//                if (isSecondPoint){
//                    mAuxiliaryTwoX = event.getY(1);
//                    mAuxiliaryTwoY = event.getY(1);
//                }
//                invalidate();
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                isSecondPoint =false;
//                break;
//        }
//        return true;
//
//    }
}
