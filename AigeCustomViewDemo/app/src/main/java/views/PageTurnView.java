package views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaobozheng on 11/4/2016.
 */
public class PageTurnView extends View {
    private static final float TEXT_SIZE_NORMAL = 1/40F,TEXT_SIZE_LARGER = 1 / 20F;
    private List<Bitmap> mBitmaps;
    private TextPaint mTextPaint;
    private Context mContext;
    private float mTextSizeNormal, mTextSizeLarger;
    private int mViewWidth, mViewHeight;  //控件的宽高
    private float mClipX;  //裁剪右端点坐标
    private float mCurPointX;  //指尖触碰屏幕的X坐标值
    private float mMoveValid;  //移动事件的有效距离

    private float autoAreaLeft, autoAreaRight;
    private boolean islastPage;
    private boolean isNextPage;
    //当前显示mBitmaps数据的下标
    private int pageIndex;

    public PageTurnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        //让字体绘制居中
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 初始化位图数据
     * 跟屏幕匹配
     */
    private void initBitmaps(){
        List<Bitmap> temp = new ArrayList<Bitmap>();
        for (int i = mBitmaps.size() - 1; i >= 0; i--){
            //按照比例来缩放图片
            Bitmap bitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), mViewWidth, mViewHeight, true);
            temp.add(bitmap);
        }
        mBitmaps = temp;
    }

    /**
     * 绘制位图,
     * 根据不同图层来绘制,将绘制独立开
     * 设置成裁剪上一张和下一张
     * @param canvas
     */
    private void drawBitmaps(Canvas canvas){
        islastPage = false;

        pageIndex = pageIndex < 0 ? 0 : pageIndex;
        pageIndex = pageIndex > mBitmaps.size() ? mBitmaps.size() : pageIndex;

        //绘画的顺序是颠倒的，end为画的最顶端
        //当pageIndex为0时，end为5, start为3
        int start = mBitmaps.size() - 2 - pageIndex;
        int end = mBitmaps.size() - pageIndex;

        if (start < 0){
            islastPage = true;
            showToast("到达最后一张");
            start = 0;
            end = 1;
        }

        for (int i = start; i < end; i++){
            canvas.save();
            //仅裁剪最顶部区域
            if ( !islastPage && i == end - 1){
                canvas.clipRect(0, 0, mClipX, mViewHeight);
            }
            canvas.drawBitmap(mBitmaps.get(i), 0, 0, null);
            canvas.restore();
        }
    }

    private void deafaultDisplay(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        //绘制标题文本
        mTextPaint.setTextSize(mTextSizeLarger);
        mTextPaint.setColor(Color.RED);
        canvas.drawText("FBI WARNING", mViewWidth / 2, mViewHeight / 4, mTextPaint);

        mTextPaint.setTextSize(mTextSizeNormal);
        mTextPaint.setColor(Color.BLACK);
        canvas.drawText("Please set data user setBitmaps method", mViewWidth / 2, mViewHeight / 3,
                mTextPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        //初始化位图
        initBitmaps();
        //计算文字尺寸
        mTextSizeLarger = TEXT_SIZE_LARGER * mViewHeight;
        mTextSizeNormal = TEXT_SIZE_NORMAL * mViewHeight;
        //初始化裁剪右端点坐标
        mClipX = mViewWidth;

        //计算控件左侧和右侧自动吸附的区域
        autoAreaLeft = mViewWidth * 1/5F;
        autoAreaRight = mViewWidth * 4/5F;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        deafaultDisplay(canvas);
        drawBitmaps(canvas);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //每次触发TouchEvent重置isnextPage为true
        isNextPage = true;

        //位运算符
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mCurPointX = event.getX();

                if (mCurPointX < autoAreaLeft){
                    //第一次接触屏幕是在最左端的位置，翻上一页
                    isNextPage = false;
                    pageIndex --;
                    mClipX = mCurPointX;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float SlideDis = mCurPointX - event.getX();

                if (Math.abs(SlideDis) > mMoveValid){
                    mClipX = event.getX();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                judgeSlideAuto();

                //当前不是最后一页
                //需要翻下一页的时候
                //并且上一页已被Clip掉
                if (!islastPage && isNextPage && mClipX <= 0){
                    pageIndex++;
                    mClipX = mViewWidth;
                    invalidate();
                }
                break;
            default:
                //获取触摸的x坐标
                mClipX = event.getX();
                invalidate();
                break;
        }

        return true;
    }

    /**
     * 判断是否需要自动滑动
     * 根据参数的当前值判断绘制
     */
    private void judgeSlideAuto(){

        //滑动到离左端五分之一的区域内，则自动滑动到控件左端
        if (mClipX < autoAreaLeft){
            while (mClipX > 0){
                mClipX --;
                invalidate();
            }
        }
        if (mClipX > autoAreaRight){
            while (mClipX < mViewWidth){
                mClipX ++;
                invalidate();
            }
        }
    }

    /**
     * 设置位图数据
     * @param mBitmaps
     */
    public synchronized void setBitmaps(List<Bitmap> mBitmaps){
        if (null == mBitmaps || mBitmaps.size() == 0){
            throw new IllegalArgumentException("no Bitmap to display");
        }
        if (mBitmaps.size() < 2){
            throw new IllegalArgumentException("bitmap less");
        }

        this.mBitmaps = mBitmaps;
        invalidate();  //通知重新绘制
    }

    /**
     * Toast显示
     *
     * @param msg
     *            Toast显示文本
     */
    private void showToast(Object msg) {
        Toast.makeText(mContext, msg.toString(), Toast.LENGTH_SHORT).show();
    }
}
