package com.example.xiaobozheng.bilibilisearchview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Riane on 2016/8/29.
 */
public class FragmentRevealExample extends Fragment implements View.OnClickListener{

    private View content;
    private EditText mEditText;
    private int centerX;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        rootView.setOnClickListener(this);
        content = rootView.findViewById(R.id.content);
        mEditText = (EditText) rootView.findViewById(R.id.edit_search);
        ImageView img_search = (ImageView) rootView.findViewById(R.id.img_search);
        final View edit_lay = rootView.findViewById(R.id.edit_lay);
        View items = rootView.findViewById(R.id.items);

        //在绘制的时候取得控件的高度
        edit_lay.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //只是删除与结束附近监听器
                edit_lay.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {

    }
}
