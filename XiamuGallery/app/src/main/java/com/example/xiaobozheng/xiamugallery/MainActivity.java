package com.example.xiaobozheng.xiamugallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.galleryview.Gallery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Gallery> mGalleries = new ArrayList<>();
    private List<String> mImgList = new ArrayList<>();

    private String[] mImgs = new String[]{"http://awb.img1.xmtbang.com/wechatmsg2015/article201505/20150525/thumb/9b65bb01da504a12807f50324fe01e3b.jpg",
//            "http://img.gaonengfun.com/attach/img/2015/12/11/1449820178464698.gif",
            "http://p3.gexing.com/G1/M00/B0/E2/rBACE1IaEE2iXDJcAAAY2UyOZcc821_200x200_3.jpg",
            "http://img4.imgtn.bdimg.com/it/u=665141257,1340555319&fm=21&gp=0.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGalleries.add((Gallery) findViewById(R.id.gallery0));
        mGalleries.add((Gallery) findViewById(R.id.gallery1));
        mGalleries.add((Gallery) findViewById(R.id.gallery2));

        for (int i = 0; i < mGalleries.size(); i++){
            for (int j = 0; j < mImgs.length; j++){
                mImgList.add(mImgs[j]);
            }
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
