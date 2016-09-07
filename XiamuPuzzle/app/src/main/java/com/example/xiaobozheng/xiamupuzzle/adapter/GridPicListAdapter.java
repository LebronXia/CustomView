package com.example.xiaobozheng.xiamupuzzle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.xiaobozheng.xiamupuzzle.util.ScreenUtil;

import java.util.List;

/**
 * Created by xiaobozheng on 9/7/2016.
 */
public class GridPicListAdapter extends BaseAdapter {

    private List<Bitmap> picList;
    private Context context;

    public GridPicListAdapter(Context context, List<Bitmap> picList){
        this.context = context;
        this.picList = picList;
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public Object getItem(int i) {
        return picList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView iv_pic_item = null;
        //获取屏幕密度
        int density = (int) ScreenUtil.getDeviceDensity(context);
        if (view == null){
            iv_pic_item = new ImageView(context);
            //设置布局
            iv_pic_item.setLayoutParams(new GridView.LayoutParams(
                    80 * density,
                    100 * density
            ));
            //设置显示比例类型
            iv_pic_item.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            iv_pic_item = (ImageView) view;
        }
        iv_pic_item.setBackgroundColor(android.R.color.black);
        iv_pic_item.setImageBitmap(picList.get(i));
        return iv_pic_item;
    }
}
