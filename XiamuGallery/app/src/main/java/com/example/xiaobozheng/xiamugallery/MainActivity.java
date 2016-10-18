package com.example.xiaobozheng.xiamugallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.galleryview.Gallery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Gallery> mGalleries = new ArrayList<>();
    private List<String> mImgList = new ArrayList<>();

    private String[] mImgs = new String[]{"http://awb.img1.xmtbang.com/wechatmsg2015/article201505/20150525/thumb/9b65bb01da504a12807f50324fe01e3b.jpg",
//            "http://img.gaonengfun.com/attach/img/2015/12/11/1449820178464698.gif",
            "http://ww1.sinaimg.cn/large/610dc034jw1f867mvc6qjj20u00u0wh7.jpg",
            "http://ww3.sinaimg.cn/large/610dc034jw1f837uocox8j20f00mggoo.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity" ,  "哈哈哈哈哈");
        mGalleries.add((Gallery) findViewById(R.id.gallery0));
        mGalleries.add((Gallery) findViewById(R.id.gallery1));
        mGalleries.add((Gallery) findViewById(R.id.gallery2));

        for (int i = 0; i < mGalleries.size(); i++){
            for (int j = 0; j < mImgs.length; j++){
                mImgList.add(mImgs[j]);
            }
            Log.d("MainActivity" , mImgList.size() + "mImgList的数量");
            mGalleries.get(i).setImgList(mImgList);
        }
    }

    private void startSmooth(){
        for (int i= 0;i < mGalleries.size(); i++ ){
            final int index = i;
            mGalleries.get(i).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGalleries.get(index).startSmooth();
                }
            }, 100 * i);
        }
    }

    public void onRefresh(View view){
        startSmooth();
    }
}
