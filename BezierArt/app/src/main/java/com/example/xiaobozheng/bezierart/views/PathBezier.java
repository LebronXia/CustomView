package com.example.xiaobozheng.bezierart.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.xiaobozheng.bezierart.BezierEvaluator;

/**
 * 路径动画
 * Created by xiaobozheng on 10/26/2016.
 */
public class PathBezier extends View implements View.OnClickListener{

    private Paint mPathPaint;
    private Paint mCirclePaint;

    //起始值和终止值
    private int mStartPointX;
    private int mStartPointY;
    private int mEndPointX;
    private int mEndPointY;

    //移动的点
    private int mMovePointX;
    private int mMovePointY;
    //所控制的点
    private int mContorlPointX;
    private int mContorlPointY;

    private Path mPath;

    public PathBezier(Context context) {
        super(context);
    }

    public PathBezier(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(5);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mStartPointX = 100;
        mStartPointY = 100;
        mEndPointX = 600;
        mEndPointY = 600;

        mMovePointX = mStartPointX;
        mMovePointY = mStartPointY;
        mContorlPointX = 500;
        mContorlPointY = 0;
        mPath = new Path();
        setOnClickListener(this);
    }

    public PathBezier(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        //画出起始点和终止点的圆
        canvas.drawCircle(mStartPointX, mStartPointY, 30, mCirclePaint);
        canvas.drawCircle(mEndPointX, mEndPointY, 30, mCirclePaint);

        mPath.moveTo(mStartPointX, mStartPointY);
        mPath.quadTo(mContorlPointX, mContorlPointY, mEndPointX, mEndPointY);

        canvas.drawPath(mPath, mPathPaint);
        canvas.drawCircle(mMovePointX, mMovePointY, 30, mCirclePaint);
    }

    @Override
    public void onClick(View view) {
        BezierEvaluator bezierEvaluator = new BezierEvaluator(new PointF(mContorlPointX, mContorlPointY));
        ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator,new PointF(mStartPointX, mStartPointY),
                new PointF(mEndPointX, mEndPointY)
                );
        anim.setDuration(600);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF point = (PointF) valueAnimator.getAnimatedValue();
                //根据动画每帧小点移动所在的坐标
                mMovePointX = (int) point.x;
                mMovePointY = (int) point.y;
                invalidate();
            }
        });
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();

    }
}
