package views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.xiaobozheng.aigecustomviewdemo.R;

import utils.MeasureUtil;

/**
 * Created by xiaobozheng on 10/28/2016.
 */
public class EraserView extends View {

    private static final int MIN_MOVE_DIS = 5;

    private Bitmap fgBitmap, bgBitmap; // 前景橡皮差， 和背景底图的Bitmap
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;

    private int screenWidth, screenHeight;  //屏幕宽高
    private float preX, preY;
    public EraserView(Context context) {
        super(context);
    }

    public EraserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //计算屏幕尺寸
        cal(context);
        //初始化对象
        init(context);
    }

    public EraserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        mPath = new Path();
        //实例化画笔            抗锯齿和抗抖动
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        //设置画布透明度
        mPaint.setARGB(188, 255, 0, 0);
        //混合模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //描边风格
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //设置宽度
        mPaint.setStrokeWidth(50);
        //生成前景图
        fgBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(fgBitmap);
        mCanvas.drawColor(0xFF808080);

        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.a4);
        bgBitmap = Bitmap.createScaledBitmap(bgBitmap, screenWidth, screenHeight, true);
    }

    private void cal(Context context) {
        int screenSize[] = MeasureUtil.getScreenSize((Activity)context);

        //获取屏幕宽高
        screenWidth = screenSize[0];
        screenHeight = screenSize[1];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景视图
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        //前景视图
        canvas.drawBitmap(fgBitmap, 0, 0, null);

        mCanvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手指第一次接触屏幕
                mPath.reset();
                mPath.moveTo(x, y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
              if (dx >= MIN_MOVE_DIS || dy >= MIN_MOVE_DIS){
                  mPath.quadTo((x + preX)/2, (y+preY) /2,preX, preY);
                  preX = x;
                  preY = y;
              }
                break;
        }
        invalidate();
        return true;
    }
}
