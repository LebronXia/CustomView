package com.example.xiaobozheng.bezierart;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by xiaobozheng on 10/26/2016.
 */
public class ShopCartAdapter extends BaseAdapter {
    //图片集合
    private List<ImageView> mBitmapList;
    public ShopCartAdapter(List<ImageView> bitmaps) {
        super();
        this.mBitmapList = bitmaps;
    }

    @Override
    public int getCount() {
        return mBitmapList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBitmapList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
