package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiaobozheng on 10/31/2016.
 */
public class MultiCricleView extends View {
    //控件以控件的边长作为参考

    private static final float STOKE_WIDTH = 1F/256F,  //描边宽度占比
             LINE_LENGTH = 3F / 32F,   //线段长度占比
            CRICLE_LARGER_RADIU = 3F / 32F,  //大圆半径
            CRICLE_SMALL_RADIU = 5F / 64F,   //小圆半径
            ARC_RADIU = 1F / 8F,  //弧半径
            ARC_TEXT_RADIU = 5F / 32F;  //弧圆绕文字半径


    private Paint strokePaint, textPaint; //描边画笔
    private int size; //控件边长
    private float stroWidth;  //描边的宽度
    private float ccX, ccY; //中心圆圆心坐标
    private float largeCitcleRadiu;  //大圆半径

    private float lineLength;  //线段长度
    private float textOffsetY;  //文本的Y轴编译量


    public MultiCricleView(Context context) {
        super(context);
    }

    public MultiCricleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public MultiCricleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint(Context context) {
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);

        /*
         * 初始化文字画笔
         */
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
        //Baseline往上至字符最高处的距离我们称之为ascent（上坡度），Baseline往下至字符最底处的距离我们称之为descent（下坡度），
        textOffsetY = (textPaint.descent() + textPaint.ascent()) / 2;
    }

    /**
     * 强制长宽一样
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        size = w;
        //参数计算
        calculation();
    }

    private void calculation() {
        //计算描边宽度，按比例划算
        stroWidth = STOKE_WIDTH * size;
        largeCitcleRadiu = size * CRICLE_LARGER_RADIU;

        lineLength = size * LINE_LENGTH;
        //中心圆的圆心
        ccX = size / 2;
        ccY = size / 2 + size * CRICLE_LARGER_RADIU;
        //设置参数
        setPara();

    }

    private void setPara() {
        strokePaint.setStrokeWidth(stroWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFF29B76);

        canvas.drawCircle(ccX, ccY, largeCitcleRadiu, strokePaint);
        canvas.drawText("Aigestudio", ccX, ccY - textOffsetY, textPaint);
        
        drawTopLeft(canvas);
    }

    private void drawTopLeft(Canvas canvas) {
        //锁定画布
        canvas.save();

        //等于是一个新的画布，移动到大圆的半径
        canvas.translate(ccX, ccY);
        //之后将画布转30度的角
        canvas.rotate(-30);

        //划线怎大圆的半径为长度确定开始点，
        canvas.drawLine(0, -largeCitcleRadiu, 0, -lineLength * 2, strokePaint);
        canvas.drawCircle(0, -lineLength * 2 - largeCitcleRadiu, largeCitcleRadiu, strokePaint);
        //canvas.drawText("Apple", 0, -lineLength * 3 - textOffsetY, textPaint);
        canvas.translate(0, -lineLength * 3 - textOffsetY);
        canvas.rotate(30);
        canvas.drawText("Apple", 0, 0, textPaint);
        // drawTextTopLeft(canvas);
        // 释放画布
        canvas.restore();


        canvas.drawLine(0, -largeCitcleRadiu * 4, 0, -lineLength * 5, strokePaint);
        canvas.drawCircle(0, -lineLength * 6, largeCitcleRadiu, strokePaint);
        canvas.drawText("Orange", 0, -lineLength * 6 - textOffsetY, textPaint);

       canvas.restore();

    }
}
