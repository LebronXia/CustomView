package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 水位下降
 * Created by xiaobozheng on 11/2/2016.
 */
public class WaveView extends View{
    private Path mPath;  //路径对象
    private Paint mPaint;  //画笔对象

    private int vWidth, vHeight;  //控件宽高
    private float ctrX, ctrY;   //控制点的xy坐标
    private float waveY;   //整个Wave顶部两端点的Y坐标

    private boolean isInc; //判断控制点是该右移还是左移

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //实例化画笔并设置参数
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(0xFFA2D6AE);

        mPath = new Path();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 在里面设置参数
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        vWidth = w;
        vHeight = h;

        //计算端点的Y坐标
        waveY = 1/8 * vHeight;
        //计算控制点的Y坐标
        ctrY = -1/16F * vHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPath.moveTo(-1/4 * vWidth, waveY);
        mPath.quadTo(ctrX, ctrY, vWidth + 1/ 4F * vWidth, waveY);

        mPath.lineTo(vWidth + 1/4F * vWidth, vHeight);
        mPath.lineTo(-1/4F * vWidth, vHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
        //当控制点的x坐标大于或等于终点x坐标时更改标识符
        if (ctrX >= vWidth + 1 / 4F * vWidth){
            isInc = false;
        }
        else if (ctrX <= -1/4F * vWidth){
            isInc = true;
        }

        //看水波纹，大于最大宽度时开始减20
        ctrX = isInc ? ctrX + 20: ctrX - 20;

        if (ctrY <= vHeight){
            ctrY += 2;
            waveY += 2;
        }

        mPath.reset();
        // 重绘
        invalidate();
    }
}
