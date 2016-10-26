package com.example.xiaobozheng.bezierart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTvShoppingCart;
    private ListView mLvShoopingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        intiView();
    }

    private void intiView() {
        mTvShoppingCart = (TextView) findViewById(R.id.tv_bezier_cure_shopping_cart);
        mLvShoopingCart = (ListView) findViewById(R.id.lv_bezier_cure_shopping_cart);


    }
}
