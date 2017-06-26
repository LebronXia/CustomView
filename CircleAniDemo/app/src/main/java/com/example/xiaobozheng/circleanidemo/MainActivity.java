package com.example.xiaobozheng.circleanidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private AnimationButton animationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animationButton = (AnimationButton) findViewById(R.id.btn_animation);

        animationButton.setAnimationButtonListener(new AnimationButton.AnimationButtonListener(){
            @Override
            public void onClickListener() {
                animationButton.start();
            }

            @Override
            public void animationFinish() {

            }
        });
    }
}
