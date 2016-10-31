package views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import bo.PorterDuffBO;
import utils.MeasureUtil;

/**
 * 不同PorterDuff之间的View
 * Created by xiaobozheng on 10/31/2016.
 */
public class PorterDuffView extends View{

    private static final PorterDuff.Mode MODE = PorterDuff.Mode.ADD;

    //左右上方正方形的大小
    private static final int RECT_SIZE_SMALL = 400;
    //中间正方形的大小
    private static final int RECT_SIZE_BIG = 800;

    private Paint mPaint;
    private PorterDuffBO porterDuffBO; //PorterDuffView类的业务对象
    private PorterDuffXfermode porterDuffxfermode;  //图形混合模式

    private int screenW, screenH;  //屏幕宽高
    private int s_l, s_t;  //左上方正方形原点的坐标
    private int d_l, d_t;  //右上方正方形原点的坐标
    private int rectX,  rectY; //中间正方形的坐标


    public PorterDuffView(Context context) {
        super(context);
    }

    public PorterDuffView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PorterDuffView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        porterDuffBO = new PorterDuffBO();

        //实例化混合模式
        porterDuffxfermode = new PorterDuffXfermode(MODE);

        calu(context);
    }

    /**
     * 计算坐标
     * @param context
     */
    private void calu(Context context) {
        int[] screenSize = MeasureUtil.getScreenSize((Activity) context);

        screenW = screenSize[0];
        screenH = screenSize[1];

        //左上角的坐标
        s_l = 0;
        s_t = 0;

        //右上角的坐标
        d_l = screenW - RECT_SIZE_SMALL;
        d_t = 0;

        //中间正方形的原点坐标
        rectX = screenW / 2 - RECT_SIZE_BIG / 2;
        //屏幕高度减去左右的小正方形
        rectY = RECT_SIZE_SMALL + (screenH - RECT_SIZE_SMALL)/2 - RECT_SIZE_BIG / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);

        porterDuffBO.setSize(RECT_SIZE_SMALL);
        //绘制小正方形
        //左边为src,右边为dis
        canvas.drawBitmap(porterDuffBO.initSrcBitmap(), s_l, s_t, mPaint);
        canvas.drawBitmap(porterDuffBO.initDisBitmap(), d_l, d_t, mPaint);

        //将绘制操作保存到新的图层
        int sc = canvas.saveLayer(0, 0, screenW, screenH, null, Canvas.ALL_SAVE_FLAG );
        porterDuffBO.setSize(RECT_SIZE_BIG);
        //先绘制dis目标图
        canvas.drawBitmap(porterDuffBO.initDisBitmap(), rectX, rectY, mPaint);
        //设置混合模式
        mPaint.setXfermode(porterDuffxfermode);
        //再绘制scr源图
        canvas.drawBitmap(porterDuffBO.initSrcBitmap(), rectX, rectY, mPaint);
        //设置混合模式
        mPaint.setXfermode(null);
        //当我们绘制完成后要通过restore将所有缓冲（层）中的绘制操作还原到画布以结束绘制
        canvas.restoreToCount(sc);
    }
}
