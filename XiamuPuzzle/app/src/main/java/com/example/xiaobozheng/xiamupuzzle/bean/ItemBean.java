package com.example.xiaobozheng.xiamupuzzle.bean;

import android.graphics.Bitmap;

/**
 * 拼图Item逻辑实体类：封装逻辑
 * Created by xiaobozheng on 9/7/2016.
 */
public class ItemBean {
    private int mItemId;
    private int mBitmapId;
    private Bitmap mBitmapp;

    public ItemBean(){

    }

    public ItemBean(int itemId, int bitmapId, Bitmap bitmapp) {
        mItemId = itemId;
        mBitmapId = bitmapId;
        mBitmapp = bitmapp;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public int getBitmapId() {
        return mBitmapId;
    }

    public void setBitmapId(int bitmapId) {
        mBitmapId = bitmapId;
    }

    public Bitmap getBitmapp() {
        return mBitmapp;
    }

    public void setBitmapp(Bitmap bitmapp) {
        mBitmapp = bitmapp;
    }
}
