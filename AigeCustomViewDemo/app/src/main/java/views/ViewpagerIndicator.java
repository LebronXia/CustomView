package views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiaobozheng.aigecustomviewdemo.R;

import java.util.List;

/**
 * Created by xiaobozheng on 11/3/2016.
 */
public class ViewpagerIndicator extends LinearLayout{
    //画笔
    private Paint mPaint;
    //路径
    private Path mPath;
    //三角型的宽度
    private int mTriangleWidth;
    //三角形的高度
    private int mTriangleHeight;

    //三角形的宽度为的那个Tab的1/6
    private static final float RADIO_TRIANGEL = 1.0f / 6;

    //三角形的最大宽度
    private final int DIMENSSION_TRIANGEL_WIDTH = (int) (getScreenWidth() / 3 * RADIO_TRIANGEL);
    //初始时，三角形指示器的偏移量
    private int mInitTranslationX;
    //手指滑动时的偏移量
    private float mTranslationX;

    private static final int COUNT_DEGAULT_TAB = 4;
    //tab的数量
    private int mTabVisisbleCount = COUNT_DEGAULT_TAB;

    private List<String> mTabTitles;
    //与之绑定的ViewPager
    private ViewPager mViewPager;

    /**
     * 标题正常时的颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
    /**
     * 标题选中时的颜色
     */
    private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFFFFFFFF;
    public ViewpagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取自定义属性，tab的数量
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);

        mTabVisisbleCount = a.getInt(R.styleable.ViewPagerIndicator_item_count, COUNT_DEGAULT_TAB);

        if (mTabVisisbleCount < 0 )
            mTabVisisbleCount = COUNT_DEGAULT_TAB;
        a.recycle();

        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    /**
     * 当View中所有的子控件均被映射成xml后触发
     * 设置布局中View的一些必要属性
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int cCount = getChildCount();
        if (cCount == 0)
            return;

        //设置限定的数量,顶部标题导航栏里的字的宽度
        for (int i = 0; i < cCount; i++){
            View view  = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mTabVisisbleCount;
            view.setLayoutParams(lp);
        }
        setItemClickEvent();
    }

    /**
     * 当view的大小发生变化时触发
     * 设值，进行初始化，和初始化三角形的宽度
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //三角形宽度的设置,有几个Tab显示，后除以三角形在每个Tab里所占的控件
        mTriangleWidth = (int) (w / mTabVisisbleCount * RADIO_TRIANGEL);

        //其中三角形默认的Tab为3个
        mTriangleWidth = Math.min(DIMENSSION_TRIANGEL_WIDTH, mTriangleWidth);

        //画出三角形
        initTriangle();
        //初始化的偏移量 ，使三角形位于每个Tab中间的位置
        mInitTranslationX = getWidth() / mTabVisisbleCount / 2 - mTriangleWidth/2;

    }

    /**
     * 初始化三角形的指示器
     */
    private void initTriangle(){
        mPath = new Path();

        mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2));
        mPath.moveTo(0,0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        //闭合路径
        mPath.close();

    }

    /**
     * 分发给子组件的绘制
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();
        //这一步在绘制画笔,  初始化偏移量加上手指滑动的偏移量
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
    }

    public void setViewPager(ViewPager mViewPager, int pos){
        this.mViewPager = mViewPager;

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //滚动,让三角形也跟着滑动，标题栏滑动
                scroll(position, positionOffset);

                // 回调
                if (onPageChangeListener != null)
                {
                    onPageChangeListener.onPageScrolled(position,
                            positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                // 设置字体颜色高亮
                resetTextViewColor();
                highLightTextView(position);

                // 回调
                if (onPageChangeListener != null)
                {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 回调
                if (onPageChangeListener != null)
                {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        // 设置当前页
        mViewPager.setCurrentItem(pos);
        // 高亮
        highLightTextView(pos);
    }

    /**
     * 页面切换监听
     */
    public interface PageChangeListener{
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    // 对外的ViewPager的回调接口
    private PageChangeListener onPageChangeListener;

    // 对外的ViewPager的回调接口的设置
    public void setOnPageChangeListener(PageChangeListener pageChangeListener)
    {
        this.onPageChangeListener = pageChangeListener;
    }

    /**
     * 指示器跟随手指滑动， 以及容器滑动
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset){

        //不断改变偏移量，跟局offse在变化，  offset的值在0~1之间
        mTranslationX = getWidth() / mTabVisisbleCount * (position + offset);

        //一个tab的宽度
        int tabWidth = getScreenWidth() / mTabVisisbleCount;

        // 容器滚动，当移动到倒数最后一个的时候，开始滚动
        //滑动到最后一个的时候，留的空间多余，可以让他在滑动到倒数第二个的时候，禁止滑动，而让三角形滑到最后一个的效果
        if (offset > 0 && position >= (mTabVisisbleCount - 2)
                && getChildCount() > mTabVisisbleCount && position < (getChildCount() - 2))
        {
            if (mTabVisisbleCount != 1)
            {
                //控制标题栏滑到倒数第二个
                this.scrollTo((position - (mTabVisisbleCount - 2)) * tabWidth
                        + (int) (tabWidth * offset), 0);
            } else
            // 为count为1时 的特殊处理
            {
                this.scrollTo(
                        position * tabWidth + (int) (tabWidth * offset), 0);
            }
        }

        invalidate();
    }

    /**
     * 设置可见的tab的数量
     *
     * @param count
     */
    public void setVisibleTabCount(int count)
    {
        this.mTabVisisbleCount = count;
    }

    /**
     * 设置tab的标题内容 可选，可以自己在布局文件中写死
     *
     * @param datas
     */
    public void setTabItemTitles(List<String> datas)
    {
        // 如果传入的list有值，则移除布局文件中设置的view
        if (datas != null && datas.size() > 0)
        {
            this.removeAllViews();
            this.mTabTitles = datas;

            for (String title : mTabTitles)
            {
                // 添加view
                addView(generateTextView(title));
            }
            // 设置item的click事件
            setItemClickEvent();
        }

    }

    /**
     * 根据标题生成我们的TextView
     *
     * @param text
     * @return
     */
    private TextView generateTextView(String text)
    {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / mTabVisisbleCount;
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setLayoutParams(lp);
        return tv;
    }


    /**
     * 高亮文本
     *
     * @param position
     */
    protected void highLightTextView(int position)
    {
        View view = getChildAt(position);
        if (view instanceof TextView)
        {
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
        }

    }

    /**
     * 重置文本颜色
     */
    private void resetTextViewColor()
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            if (view instanceof TextView)
            {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 设置标题的点击事件
     */
    public void setItemClickEvent(){
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++)
        {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public int getScreenWidth()
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

}
