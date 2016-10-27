package com.example.xiaobozheng.bezierart;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xiaobozheng.bezierart.model.GoodsModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //购物车父布局
    private RelativeLayout mRlyShoppingCartRly;
    private ImageView mIvShoppingCart;
    private TextView mTvShoppingCart;
    private ListView mLvShoopingCart;
    private ShopCartAdapter mShopCartAdapter;
    //购物车商品图片
    private ArrayList<GoodsModel> mData;
    private int goodsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        intiView();
        iniData();
        configView();
    }

    private void configView() {
        mLvShoopingCart.setAdapter(mShopCartAdapter);
    }

    private void iniData() {
        addData();
        mShopCartAdapter = new ShopCartAdapter(this, mData);
        mShopCartAdapter.setCallBackListener(new ShopCartAdapter.CallBackListener() {
            @Override
            public void callBackImg(ImageView imageView) {
                addGoodsToCart(imageView);
            }
        });
    }

    /**
     * 添加商品到购物车里
     * @param goodsImageView
     */
    private void addGoodsToCart(ImageView goodsImageView) {
        //创造出执行动画的主题goodsImg
        final ImageView goods = new ImageView(this);
        goods.setImageDrawable(goodsImageView.getDrawable());

        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(100, 100);
        mRlyShoppingCartRly.addView(goods, parms);

        //得到父布局的起始点坐标
        int[] parentLocation = new int[2];
        mRlyShoppingCartRly.getLocationInWindow(parentLocation);
        //得到商品图片坐标
        int startLoc[] = new int[2];
        goodsImageView.getLocationInWindow(startLoc);
        //得到购物车图片的坐标
        int endLoc[] = new int[2];
        mIvShoppingCart.getLocationInWindow(endLoc);

        //得到开始掉落商品的起始坐标
        float startX = startLoc[0] - parentLocation[0] + goodsImageView.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + goodsImageView.getHeight() / 2;
        //得到商品掉落的终点坐标
        float toX = endLoc[0] - parentLocation[0] + goodsImageView.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        float mContorlPointX = (startX + toX)/2;
        float mContorlPointY = startY;
        Path mPath = new Path();
        mPath.reset();

        //第一步先移动到起始位置
        mPath.moveTo(startX,startY);
        //开始绘制贝塞尔曲线
        mPath.quadTo( mContorlPointX, mContorlPointY, toX, toY);
        //贝塞尔曲线的插值器
        BezierEvaluator bezierEvaluator = new BezierEvaluator(new PointF(mContorlPointX, mContorlPointY));
        // 属性动画实现
        ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator,new PointF(startX, startY),
                new PointF(toX, toY)
        );
        anim.setDuration(600);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF point = (PointF) valueAnimator.getAnimatedValue();
                //根据动画每帧小点移动所在的坐标
                goods.setTranslationX((int) point.x);
                goods.setTranslationY((int) point.y);
                //重绘
                mRlyShoppingCartRly.invalidate();
            }
        });

        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();

        // 动画结束后的处理
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                goodsCount ++;
                isShowCartGoodsCount();

                mTvShoppingCart.setText(String.valueOf(goodsCount));
                mRlyShoppingCartRly.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    private void addData() {
        // 初始化数据源
        mData = new ArrayList<>();
        // 添加数据源
        GoodsModel goodsModel = new GoodsModel();
        goodsModel.setmGoodsBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.goods_one));
        mData.add(goodsModel);

        goodsModel = new GoodsModel();
        goodsModel.setmGoodsBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.goods_two));
        mData.add(goodsModel);

        goodsModel = new GoodsModel();
        goodsModel.setmGoodsBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.goods_three));
        mData.add(goodsModel);
    }

    private void intiView() {
        mTvShoppingCart = (TextView) findViewById(R.id.tv_bezier_cure_shopping_cart);
        mLvShoopingCart = (ListView) findViewById(R.id.lv_bezier_cure_shopping_cart);
        mRlyShoppingCartRly = (RelativeLayout) findViewById(R.id.rly_bezier_curve_shopping_cart);
        mIvShoppingCart = (ImageView) findViewById(R.id.iv_bezier_curve_shopping_cart);
        isShowCartGoodsCount();
    }

    public void isShowCartGoodsCount() {
        if (goodsCount == 0){
            mTvShoppingCart.setVisibility(View.GONE);
        } else {
            mTvShoppingCart.setVisibility(View.VISIBLE);
        }
      //  return mShowCartGoodsCount;
    }
}
