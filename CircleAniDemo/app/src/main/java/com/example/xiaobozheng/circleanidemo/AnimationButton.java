package com.example.xiaobozheng.circleanidemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by xiaobozheng on 6/26/2017.
 */

public class AnimationButton extends View{

    //View的宽度
    private int width;

    //View的高度
    private int height;

    //圆的半径
    private int circleAngle;

    private int default_two_circle_distance;
    private int two_circle_distance;
    private int bg_color = 0xffbc7d53;

    //圆角矩形画笔
    private Paint paint;

    //文字画笔
    private Paint textPaint;
    private Paint okPaint;
    //文字所在矩形
    private Rect textRect = new Rect();

    //初始化矩形
    private RectF rectF = new RectF();
    //路径
    private Path path = new Path();

    //路径长度
    private PathMeasure pathMeasure;
    //从矩形到圆角矩形的动画
    private ValueAnimator animator_rect_to_angle;
    //从圆形矩形到圆
    private ValueAnimator animator_rect_to_square;

    private ObjectAnimator animator_move_to_up;

    private boolean startDrawOk = false;
    //绘制对勾动画
    private ValueAnimator animator_draw_ok;
    //动画集
    private AnimatorSet animatorSet = new AnimatorSet();
    //延时时间
    private int duration = 1000;
    //View向上移动距离
    private int move_distance = 300;
    private String buttonString = "确认完成";

    /**
     * 对路径处理实现绘制动画效果
     */
    private PathEffect effect;

    //点击事件及动画
    private AnimationButtonListener animationButtonListener;

    public void setAnimationButtonListener(AnimationButtonListener listener){
        animationButtonListener = listener;
    }

    public AnimationButton(Context context) {
        this(context, null);
    }

    public AnimationButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animationButtonListener != null){
                    //动画启动监听
                    animationButtonListener.onClickListener();
                }
            }
        });
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(bg_color);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(40);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);

        okPaint = new Paint();
        okPaint.setStrokeWidth(10);
        okPaint.setStyle(Paint.Style.STROKE);
        okPaint.setAntiAlias(true);
        okPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        default_two_circle_distance = (w - h) / 2;
        initOk();
        initAnimation();
    }

    //初始化所有动画
    private void initAnimation() {
        set_rect_to_angle_animation();
        set_rect_to_circle_animation();
        set_move_to_up_animation();
        set_draw_ok_animation();

        animatorSet.play(animator_move_to_up)
                .before(animator_draw_ok)
                .after(animator_rect_to_square)
                .after(animator_rect_to_angle);
    }

    //画个圆角矩形
    private void draw_oval_to_circle(Canvas canvas){
        //对矩形
        rectF.left = two_circle_distance;
        rectF.top = 0;
        rectF.right = width - two_circle_distance;
        rectF.bottom = height;
        canvas.drawRoundRect(rectF, circleAngle, circleAngle, paint);

    }

    //设置矩形过渡圆角矩形动画
    private void set_rect_to_angle_animation(){
        animator_rect_to_angle = ValueAnimator.ofInt(0, height/2);
        animator_rect_to_angle.setDuration(duration);
        animator_rect_to_angle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleAngle = (int) animation.getAnimatedValue();
                //重新绘制
                invalidate();
            }
        });
    }

    //设置圆形矩形到圆的动画
    private void set_rect_to_circle_animation(){
        animator_rect_to_square = ValueAnimator.ofInt(0, default_two_circle_distance);
        animator_rect_to_square.setDuration(duration);
        animator_rect_to_square.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                two_circle_distance = (int) animation.getAnimatedValue();

                //透明值
                int alpha = 255 - (two_circle_distance * 255) / default_two_circle_distance;

                textPaint.setAlpha(alpha);

                invalidate();
            }
        });

    }

    //设置View上移的动画
    private void set_move_to_up_animation(){
        //移动的距离
        final float curTranslationY = this.getTranslationY();
        animator_move_to_up = ObjectAnimator.ofFloat(this, "translationY", curTranslationY, curTranslationY - move_distance);
        animator_move_to_up.setDuration(duration);
        animator_move_to_up.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private void set_draw_ok_animation(){
        animator_draw_ok = ValueAnimator.ofFloat(1, 0);
        animator_draw_ok.setDuration(duration);
        animator_draw_ok.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startDrawOk = true;
                float value = (Float) animation.getAnimatedValue();

                effect = new DashPathEffect(new float[]{pathMeasure.getLength(), pathMeasure.getLength()}, value * pathMeasure.getLength());
                okPaint.setPathEffect(effect);
                invalidate();
            }
        });
    }

    //绘制文字
    private void drawText(Canvas canvas){
        textRect.left = 0;
        textRect.top = 0;
        textRect.right = width;
        textRect.bottom = height;

        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetricsInt.bottom - fontMetricsInt.top) / 2;
        canvas.drawText(buttonString, textRect.centerX(), baseline, textPaint);

    }

    private void initOk(){
        path.moveTo(default_two_circle_distance + height / 8 * 3, height / 2);
        path.lineTo(default_two_circle_distance + height / 2, height / 5 * 3);
        path.lineTo(default_two_circle_distance + height / 3 * 2, height / 5 * 2);

        //勾画出路径
        pathMeasure = new PathMeasure(path, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        draw_oval_to_circle(canvas);
        drawText(canvas);

        if (startDrawOk){
            canvas.drawPath(path, okPaint);
        }
    }

    public void start(){
        animatorSet.start();
    }

    public interface AnimationButtonListener{
        void onClickListener();

        void animationFinish();
    }

}
