package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiaobozheng on 10/31/2016.
 */
public class MultiCricleView extends View {
    //控件以控件的边长作为参考

    private static final float STOKE_WIDTH = 1F/256F,
             LINE_LENGTH = 3F / 32F,
            CRICLE_LARGER_RADIU = 3F / 32F;

    private Paint strokePaint; //描边画笔
    private int size; //控件边长
    private float stroWidth;  //描边的宽度
    private float ccX, ccY; //中心圆圆心坐标
    private float largeCitcleRadiu;  //大圆半径

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
    }
}
