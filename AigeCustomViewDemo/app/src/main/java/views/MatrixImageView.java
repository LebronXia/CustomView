package views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.xiaobozheng.aigecustomviewdemo.R;

import utils.MeasureUtil;

/**
 * 旋转图片
 * Created by xiaobozheng on 10/31/2016.
 */
public class MatrixImageView extends ImageView {

    private static final int MODE_NONE = 0x00123;  //默认的触摸模式
    private static final int MODE_DRAG = 0x00321; //拖曳
    private static final int MODE_ZOOM = 0x00132; //缩放

    private static final String TAG = "MatrixImageView";

    //当前触摸模式
    private int mode;

    private float preMove = 1F;  //上一次手指移动的距离
    private float saveRotate = 0F;  //保存了的角度
    private float rotate = 0F;   //旋转的角度

    private float[] preEventCoor; //上一次各触摸点的坐标集合

    private PointF start, mid; //开始和中点
    private Matrix currentMatrix, saveMatrix;  //当前和保存的matrix对象
    private Context mContext;
    public MatrixImageView(Context context) {
        super(context);
    }

    public MatrixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public MatrixImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    private void init() {
        currentMatrix = new Matrix();
        saveMatrix = new Matrix();
        start = new PointF();
        mid = new PointF();

        //模式初始化
        mode = MODE_NONE;

        //设置图片资源,与屏幕等高等宽
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mylove);
        bitmap = Bitmap.createScaledBitmap(bitmap, MeasureUtil.getScreenSize((Activity) mContext)[0],
                MeasureUtil.getScreenSize((Activity) mContext)[1], true);
        setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //多点触控触发
        switch (event.getAction() & MotionEvent.ACTION_MASK){

            //单点触碰屏幕时
            case  MotionEvent.ACTION_DOWN:
                saveMatrix.set(currentMatrix);
                //开始的点保存
                start.set(event.getX(), event.getY());
                //设置状态为拖曳
                mode = MODE_DRAG;
                preEventCoor = null;
                break;
            //此为第二点触摸屏幕 触发
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(TAG, "两手指点击");
                preMove = calSpacing(event);

                //距离相差10的时候，触发
                if (preMove > 10F){
                    saveMatrix.set(currentMatrix);
                    calMidPoint(mid, event);
                    mode = MODE_ZOOM;
                }
                preEventCoor = new float[4];
                preEventCoor[0] = event.getX(0);
                preEventCoor[1] = event.getX(1);

                preEventCoor[2] = event.getY(0);
                preEventCoor[3] = event.getY(1);
                //第一次保存的两点的角度
                saveRotate = calRotation(event);
                break;
            case MotionEvent.ACTION_UP: //单击离开屏幕
            //第二个点离开后
            case MotionEvent.ACTION_POINTER_UP:
                //将状态改为初始状态
                mode = MODE_NONE;
                //清楚保存的坐标
                preEventCoor = null;
                break;
            //触摸点移动
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "触摸移动了");
                if(mode  == MODE_DRAG){
                    currentMatrix.set(saveMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    //将当前视图平移
                    currentMatrix.postTranslate(dx, dy);
                    //接触屏幕的点数为2current，两点触控旋转
                } else if (mode == MODE_ZOOM && event.getPointerCount() == 2){
                    //两点的距离
                    float currentMove = calSpacing(event);
                    currentMatrix.set(saveMatrix);
                    if (currentMove > 10F){
                        //算出缩小放大的比例
                        float scale = currentMove / preMove;
                        currentMatrix.postScale(scale, scale, mid.x, mid.y);
                    }

                    //保持两点旋转
                    if(preEventCoor != null){
                        rotate = calRotation(event);
                        float r = rotate - saveRotate;
                        currentMatrix.postRotate(r, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                    }
                }
                break;
        }
        setImageMatrix(currentMatrix);
        return true;
    }

    /**
     * 计算两点的触摸距离
     * @param event
     * @return
     */
    private float calSpacing(MotionEvent event){
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算两个触摸点的中点坐标
     * @param point
     * @param event
     */
    private void calMidPoint(PointF point, MotionEvent event){
        float x = event.getX(0) + event.getY(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * 计算旋转角度
     * @param event
     * @return
     */
    private float calRotation(MotionEvent event){
        double deltaX = event.getX(0) - event.getX(1);
        double deltaY = event.getY(0) - event.getY(1);
        //根据math封装类求解
        double radius = Math.atan2(deltaY, deltaX);
        return (float) Math.toDegrees(radius);
    }
}
