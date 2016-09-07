package com.example.xiaobozheng.xiamupuzzle.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.xiaobozheng.xiamupuzzle.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 图像工具类
 * Created by xiaobozheng on 9/7/2016.
 */
public class ImagesUtil {

    public ItemBean itemBean;

    /**
     * 切图
     * @param type
     * @param picSelected
     * @param context
     */
    public void createInitBitmaps(int type, Bitmap picSelected,
                                  Context context){
        Bitmap bitmap = null;
        List<Bitmap> bitmapItems = new ArrayList<>();
        //每个item的宽高
        int itemWidth = picSelected.getWidth() / type;
        int itemHeight = picSelected.getHeight() / type;
        for (int i = 1; i <= type; i++){
            for (int j = 1; j <= type; j++){
                //切割得到的bitmap
                bitmap = Bitmap.createBitmap(
                        picSelected,
                        (j - 1)*itemWidth,
                        (i - 1)*itemHeight,
                        itemWidth,
                        itemHeight
                );
                bitmapItems.add(bitmap);
                itemBean = new ItemBean(
                        (i - 1) * type + j,
                        (i - 1) * type + j,
                        bitmap
                );
            }
        }
    }

    /**
     * 处理图片 放大缩小的合适位置
     * @param newWidth
     * @param newHeight
     * @param bitmap
     * @return
     */
    public Bitmap resizeBitmap(float newWidth, float newHeight, Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postScale(
                newWidth / bitmap.getWidth(),
                newHeight / bitmap.getHeight()
        );
        Bitmap newBitmap = Bitmap.createBitmap(
                bitmap, 0, 0,
                bitmap.getWidth(),
                bitmap.getHeight(),
                matrix, true
        );
        return newBitmap;
    }
}
