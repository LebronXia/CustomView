package com.example.xiaobozheng.bilibilisearchview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.xiaobozheng.bilibilisearchview.utils.PdUtil;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Riane on 2016/8/29.
 */
public class FragmentRevealExample extends Fragment implements View.OnClickListener{

    private View content;
    private EditText mEditText;
    private int centerX;
    private int centerY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        rootView.setOnClickListener(this);
        content = rootView.findViewById(R.id.content);
        mEditText = (EditText) rootView.findViewById(R.id.edit_search);
        final ImageView img_search = (ImageView) rootView.findViewById(R.id.img_search);
        //搜索框布局
        final View edit_lay = rootView.findViewById(R.id.edit_lay);
        //底部listview布局
        final View items = rootView.findViewById(R.id.items);

        //在绘制的时候取得控件的高度,绘制渲染之前执行动画
        edit_lay.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //只是删除与结束附近监听器
                edit_lay.getViewTreeObserver().removeOnPreDrawListener(this);
                items.setVisibility(View.INVISIBLE);
                items.setOnClickListener(FragmentRevealExample.this);
                edit_lay.setVisibility(View.INVISIBLE);

                //动画其实是模仿一个圆放大的过程，首先计算出圆心的坐标 centerX，centerY
                centerX = img_search.getLeft() + img_search.getWidth()/2;
                centerY = img_search.getTop() + img_search.getHeight()/2;

                /**
                 * 第一个参数view：是你要进行圆形缩放的 view；
                 第二和第三个参数：分别是开始缩放点的 x 和 y 坐标；
                 第四和第五：分别是开始的半径和结束的半径。
                 * */
                SupportAnimator mRevealAnimator = ViewAnimationUtils.createCircularReveal(
                        //endRadius则取控件的最大宽度(对角线长度)
                        edit_lay,centerX,centerY,20, PdUtil.hypo(edit_lay.getWidth(), edit_lay.getHeight())
                );
                mRevealAnimator.addListener(new SupportAnimator.AnimatorListener(){
                    @Override
                    public void onAnimationStart() {
                        edit_lay.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd() {
                        //在动画结束后延迟100毫秒，然后显示整个fragment，并弹出键盘：
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                items.setVisibility(View.VISIBLE);
                                //把输入焦点放在调用这个方法的控件上。
                                mEditText.requestFocus();
                                if (getActivity() != null){
                                    InputMethodManager imm = (InputMethodManager) getActivity().
                                            getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
                                }
                            }
                        }, 100);
                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });
                mRevealAnimator.setDuration(200);
                mRevealAnimator.setStartDelay(100);
                mRevealAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                mRevealAnimator.start();
                return true;
            }
        });
        return rootView;
    }

    public boolean onBackPressed(){
        SupportAnimator mRevealAnimator = ViewAnimationUtils.createCircularReveal(content, centerX, centerY, 20,
                PdUtil.hypo(content.getWidth(), content.getHeight()));
        //使动画方向反转
        mRevealAnimator = mRevealAnimator.reverse();
        if (mRevealAnimator==null) return false;
        mRevealAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                content.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd() {
                content.setVisibility(View.INVISIBLE);
                if (getActivity()!=null)
                    //注意在动画结束后把fragment实例从回退栈中移除（出栈）：
                    getActivity().getSupportFragmentManager().popBackStack();
            }
            @Override
            public void onAnimationCancel() {
            }
            @Override
            public void onAnimationRepeat() {
            }
        });
        mRevealAnimator.setDuration(200);
        mRevealAnimator.setStartDelay(100);
        mRevealAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mRevealAnimator.start();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.root:
                onBackPressed();
                break;
        }
    }
}
