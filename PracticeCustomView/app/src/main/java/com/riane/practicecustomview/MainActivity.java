package com.riane.practicecustomview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private HorizontalProgressBar mHorizontalProgressBar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHorizontalProgressBar = (HorizontalProgressBar) findViewById(R.id.horizontal_progress_view);
        button = (Button) findViewById(R.id.btn_start);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHorizontalProgressBar.setProgress(100);
            }
        });
    }
}
