package com.example.xiaobozheng.aigecustomviewdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import views.PageTurnView;

public class MainActivity extends AppCompatActivity {

    private PageTurnView mPageTurnView; //翻页控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPageTurnView = (PageTurnView) findViewById(R.id.view_custom);
        Bitmap bitmap = null;
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page_img_a);
        bitmaps.add(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page_img_b);
        bitmaps.add(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page_img_c);
        bitmaps.add(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page_img_d);
        bitmaps.add(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page_img_e);

        mPageTurnView.setBitmaps(bitmaps);
    }
}
