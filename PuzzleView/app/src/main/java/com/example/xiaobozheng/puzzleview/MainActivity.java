package com.example.xiaobozheng.puzzleview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private puzzleView mPuzzleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPuzzleView = (puzzleView) findViewById(R.id.puzzleview);

        Bitmap bitmap = null;
        List<Bitmap> bitmaps = new ArrayList<>();

        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.demo1);
        bitmaps.add(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.demo2);
        bitmaps.add(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.demo3);
        bitmaps.add(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.demo4);
        bitmaps.add(bitmap);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.demo5);
        mPuzzleView.setBitmaps(bitmaps);
    }


}
