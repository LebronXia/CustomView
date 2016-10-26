package com.example.xiaobozheng.bezierart.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiaobozheng on 10/26/2016.
 */
public class SecondOrderBezier extends View {

    //辅助线
    private Paint mPaintAuxiliary;
    //控制点名称
    private Paint mPaintAuxiliaryText;
    //贝塞尔曲线
    private Paint mPaintBezier;

    //控制点坐标
    private float mAuxiliaryX;
    private float mAuxiliaryY;

    //起始点坐标
    private float mStartPointX;
    private float mStartPointY;
    //终点坐标
    private float mEndPointX;
    private float mEndPiuntY;

    private Path mPath = new Path();

    public SecondOrderBezier(Context context) {
        super(context);
    }

    public SecondOrderBezier(Context context, AttributeSet attrs) {
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
    }

    public SecondOrderBezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //起始和终点的坐标
        mStartPointX = w / 4;
        mStartPointY = h /2 - 200;

        mEndPointX = w / 4 * 3;
        mEndPiuntY = h /2 - 200;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //重置绘制路线
        mPath.reset();
        //mpath绘制的绘制起点
        mPath.moveTo(mStartPointX, mStartPointY);
        //绘制辅助控制点
       canvas.drawPoint(mAuxiliaryX, mAuxiliaryY, mPaintAuxiliary);
        canvas.drawText("控制点", mAuxiliaryX, mAuxiliaryY, mPaintAuxiliaryText);
        canvas.drawText("起始点", mStartPointX, mStartPointY, mPaintAuxiliaryText);
        canvas.drawText("终止点", mEndPointX, mEndPiuntY, mPaintAuxiliaryText);

        //辅助线
        canvas.drawLine(mStartPointX, mStartPointY, mAuxiliaryX, mAuxiliaryY, mPaintAuxiliary);
        canvas.drawLine(mEndPointX, mEndPiuntY, mAuxiliaryX, mAuxiliaryY, mPaintAuxiliary);
        //二阶贝塞尔、线，实现绘制贝塞尔平滑曲线；previousX, previousY为操作点，cX, cY为终点
        mPath.quadTo(mAuxiliaryX, mAuxiliaryY, mEndPointX, mEndPiuntY);
        canvas.drawPath(mPath, mPaintBezier);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                mAuxiliaryX = event.getX();
                mAuxiliaryY = event.getY();
                //更新绘制
                invalidate();
        }
        return true;
    }
}
