package com.riane.practicecustomview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.text.DecimalFormat;

/**
 * 自定义水平进度条
 * Created by Riane on 2017/6/26.
 */

public class HorizontalProgressBar extends View{

    //背景画笔
    private Paint bgPaint;
    //进度条画笔
    private Paint progressPaint;
    //提示框画笔
    private Paint tipPaint;
    //提示框字里面的画笔
    private Paint textPaint;

    //宽度
    private int mWidth;
    //高度
    private int mHeight;
    //视图高度
    private int mViewHeight;

    //进度条画笔的宽度
    private int progressPaintWidth;
    //百分比提示框画笔的宽度
    private int tipPaintWidth;
    //百分比提示框的宽度
    private int tipWidth;
    //百分比提示框的高度
    private int tipHeight;

    private int bgColor = 0xFFe1e5e8;
    private int progressColor = 0xFFf66b12;

    //进度动画
    private ValueAnimator progressAnimator;
    //持续时间
    private int duration = 1000;
    private int startDelay = 500;
    //当前进度
    private float currentProgress;
    //进度条比例
    private float mProgress;
    //提示框移动的距离
    private float moveDis;

    //矩形的圆角半径
    private int roundRectRadius;
    //绘制三角形路径
    private Path path = new Path();
    //三角形的高
    private int triangleHeight;

    //进度条距离提示框的高度
    private int progressMarginTop;
    //绘制字体的大小
    private int textPaintSize;

    private Rect textRect = new Rect();
    private String textString = "0";

    private RectF rectF = new RectF();

    public HorizontalProgressBar(Context context) {
        super(context);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initPaint();
    }

    private void init() {
        progressPaintWidth = dp2px(4);
        tipHeight = dp2px(15);
        tipWidth = dp2px(30);
        tipPaintWidth = dp2px(1);
        triangleHeight = dp2px(3);
        roundRectRadius = dp2px(2);
        textPaintSize = sp2px(10);
        progressMarginTop = dp2px(8);

        //View的真实高度
        mViewHeight = tipPaintWidth + tipHeight + triangleHeight + progressPaintWidth + progressMarginTop;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //决定View当前大小
        setMeasuredDimension(measureWidth(widthMode, width), measureHeight(heightMode, height));
    }

    //测量宽度
    private int measureWidth(int mode, int width){
        switch (mode){
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                 break;
            case MeasureSpec.EXACTLY:
                mWidth = width;
                break;
        }
        return mWidth;
    }

     private int measureHeight(int mode, int height){
         switch (mode){  //在没有限制和最大的时候去View的高度
             case MeasureSpec.UNSPECIFIED:
             case MeasureSpec.AT_MOST:
                 mHeight = mViewHeight;
                 break;
             case MeasureSpec.EXACTLY:
                 mHeight = height;
                 break;
         }
         return mHeight;
     }

     //进度移动动画  通过插值的方式改变移动的距离
    private void initAnimation() {
        progressAnimator = new ValueAnimator().ofFloat(0, mProgress);
        progressAnimator.setDuration(duration);
        progressAnimator.setStartDelay(startDelay);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();

                textString = formatNum(format2Int((value)));
                //当前进度值转为相当前宽度的对应的比例
                currentProgress = value * mWidth / 100;

                //最左边的时候 进度条条位于提示框位置之后才开始移动
                if (currentProgress >= (tipWidth / 2) && currentProgress <= (mWidth - tipWidth / 2)){
                    moveDis = currentProgress - tipWidth/2;
                }
                invalidate();
            }
        });

        progressAnimator.start();
    }

    public HorizontalProgressBar setProgress(float progress){
        mProgress = progress;
        initAnimation();
        return this;
    }

    private void initPaint() {
        bgPaint = getPaint(progressPaintWidth, bgColor, Paint.Style.STROKE);
        progressPaint = getPaint(progressPaintWidth, progressColor, Paint.Style.STROKE);
        tipPaint = getPaint(tipPaintWidth, progressColor, Paint.Style.FILL);

        initTextPaint();
    }

    private void initTextPaint() {
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textPaintSize);
    }

    //绘制进度条上边完整的View
    private void drawTipView(Canvas canvas){
        drawRoundRect(canvas);
        drawRoundTriangle(canvas);
    }

    //绘制矩形
    public void drawRoundRect(Canvas canvas){
        //设置矩形的左右坐标
        rectF.set(moveDis, 0 , tipWidth + moveDis, tipHeight);
        canvas.drawRoundRect(rectF, roundRectRadius, roundRectRadius, tipPaint);
    }

    //绘制三角形
    private void drawRoundTriangle(Canvas canvas){
        path.moveTo(tipWidth / 2 - triangleHeight + moveDis, tipHeight);
        path.lineTo(tipWidth / 2 + moveDis, tipHeight + triangleHeight);
        path.lineTo(tipWidth / 2 + triangleHeight + moveDis, tipHeight);
        canvas.drawPath(path, tipPaint);
        path.reset();
    }

    private void drawText(Canvas canvas, String textString){
        textRect.left = (int) moveDis;
        textRect.top = 0;
        textRect.right = (int) (tipWidth + moveDis);
        textRect.bottom = tipHeight;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //文字绘制到整个布局的中心位置
        canvas.drawText(textString + "%", textRect.centerX(), baseline, textPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(getPaddingLeft(), tipHeight + triangleHeight + progressMarginTop , tipHeight + triangleHeight + progressMarginTop, 0, bgPaint);
        canvas.drawLine(getPaddingLeft(), tipHeight + triangleHeight + progressMarginTop, currentProgress, tipHeight + triangleHeight + progressMarginTop, progressPaint);

        drawTipView(canvas);
        drawText(canvas, textString);
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

    //开启动画
    public void startProgressAnimation(){
        if (progressAnimator != null &&
                !progressAnimator.isRunning() && !progressAnimator.isStarted())
            progressAnimator.start();
    }

    /**
     * 格式化数字(保留一位小数)
     *
     * @param money
     * @return
     */
    public static String formatNum(int money) {
        DecimalFormat format = new DecimalFormat("0");
        return format.format(money);
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

    public static int format2Int(double i) {
        return (int) i;
    }

}
