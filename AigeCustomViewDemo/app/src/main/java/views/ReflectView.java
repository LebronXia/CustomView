package views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.xiaobozheng.aigecustomviewdemo.R;

import utils.MeasureUtil;

/**
 * 倒影效果
 * Created by xiaobozheng on 10/31/2016.
 */
public class ReflectView extends View {
    //资源位图
    private Bitmap mSrcBitmap, mRefBitmap;
    private Paint mPaint;
    //混合模式
    private PorterDuffXfermode mXfermode;
    private int x, y;  //位图起点坐标


    public ReflectView(Context context) {
        super(context);
    }

    public ReflectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRes(context);
    }

    public ReflectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化资源
     */
    private void initRes(Context context) {
        //源图
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_xiamu);

        //实例化一个矩形对象
        Matrix matrix = new Matrix();
        //设置图形缩放，将原图像素点x坐标去反
        matrix.setScale(1F, -1F);
        //生成倒影图
        mRefBitmap = Bitmap.createBitmap(mSrcBitmap, 0, 0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), matrix, true);

        int screenW = MeasureUtil.getScreenSize((Activity)context)[0];
        int screenH = MeasureUtil.getScreenSize((Activity) context)[1];

        //位图所在位置
        x = screenW / 2 - mSrcBitmap.getWidth() / 2;
        y = screenH / 2 - mSrcBitmap.getHeight() / 2;

        mPaint = new Paint();
        //LinearGradient线性渐变
        //mPaint.setShader(new LinearGradient(left, top, right, bottom, Color.RED, Color.YELLOW, Shader.TileMode.REPEAT));
        //参数虽多其实很好理解x0和y0表示渐变的起点坐标而x1和y1则表示渐变的终点坐标，这两点都是相对于屏幕坐标系而言的,
        // 而color0和color1则表示起点的颜色和终点的颜色
        mPaint.setShader(new LinearGradient(x, y + mSrcBitmap.getHeight(), x, y + mSrcBitmap.getHeight() + mSrcBitmap.getHeight()/ 4,
                0xAA000000, Color.TRANSPARENT, Shader.TileMode.CLAMP));

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        //画源图（目标图）
        canvas.drawBitmap(mSrcBitmap, x, y, null);
        //屏幕缓存
        int sc = canvas.saveLayer(x, y + mSrcBitmap.getHeight(), x + mRefBitmap.getWidth(), y + mSrcBitmap.getHeight()*2, null,Canvas.ALL_SAVE_FLAG);
        //画倒影图
        canvas.drawBitmap(mRefBitmap, x , y + mSrcBitmap.getHeight(), null);
        mPaint.setXfermode(mXfermode);
        //将倒影化在源图底下的交界处的坐标，倒影为源图的一半
        canvas.drawRect(x, y + mSrcBitmap.getHeight(), x + mRefBitmap.getWidth(), y + mSrcBitmap.getHeight() * 2, mPaint);

        mPaint.setXfermode(null);
        //将源图还原为位置
        canvas.restoreToCount(sc);
    }
}
