package com.example.xiaobozheng.bilibilisearchview;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       // 点击搜索按钮，弹出fragment并放入回退栈fragment:reveal中：
        if (id == R.id.action_search){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new FragmentRevealExample(), "fragment_my")
                    .addToBackStack("fragment:reveal")
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentRevealExample fragment = (FragmentRevealExample)getSupportFragmentManager().
                findFragmentByTag("fragment_my");
        if (fragment != null){
            fragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
