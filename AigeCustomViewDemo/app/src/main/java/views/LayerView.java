package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画的图层
 * Created by xiaobozheng on 11/3/2016.
 */
public class LayerView extends View{
    private Paint mPaint; //画笔对象

    private int mViewWidth, mViewHeight;  //控件宽高
    public LayerView(Context context) {
        super(context);
    }

    public LayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewHeight = h;
        mViewWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.RED);
        //left：矩形的左边位置。
        //top：矩形的上边位置。
       // right：矩形的右边位置。
       // bottom：矩形的下边位置。
        canvas.drawRect(mViewWidth / 2F - 200, mViewHeight / 2F - 200, mViewWidth / 2F + 200,
                mViewHeight / 2F + 200, mPaint);

        //保存并裁剪画布填充颜色
        int saveID1 = canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(mViewWidth / 2F - 200, mViewHeight / 2F - 200, mViewWidth / 2F + 200, mViewHeight / 2F+ 200);
        canvas.drawColor(Color.GREEN);

        //保存画布并旋转后绘制一个蓝色的矩形
        int saveID2 = canvas.save(Canvas.MATRIX_SAVE_FLAG);
        mPaint.setColor(Color.BLUE);
        canvas.rotate(5);
        canvas.drawRect( mViewWidth / 2F- 100, mViewHeight / 2F - 100, mViewWidth / 2F + 100,
                mViewHeight / 2F + 100, mPaint);

        canvas.restoreToCount(saveID2);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(mViewWidth / 2F - 400, mViewHeight / 2F - 400, mViewWidth / 2F + 400, mViewHeight / 2F + 400, mPaint);
    }
}
