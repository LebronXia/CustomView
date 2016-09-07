package com.example.xiaobozheng.xiamupuzzle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.xiaobozheng.xiamupuzzle.R;
import com.example.xiaobozheng.xiamupuzzle.adapter.GridPicListAdapter;
import com.example.xiaobozheng.xiamupuzzle.util.ScreenUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // 返回码：系统图库
    private static final int RESULT_IMAGE = 100;
    // 返回码：相机
    private static final int RESULT_CAMERA = 200;
    // IMAGE TYPE
    private static final String IMAGE_TYPE = "image/*";
    // Temp照片路径
    public static String TEMP_IMAGE_PATH;
    private PopupWindow mPopupWindow;
    private View mPopupView;

    private GridView mGvPicList;
    private List<Bitmap> mPicList;
    //资源图片
    private int[] mResPicId;
    //显示Type
    private TextView mTvPuzzleMainTypeSelected;
    private TextView mTvType2;
    private TextView mTvType3;
    private TextView mTvType4;

    private LayoutInflater mLayoutInflater;
    //游戏类型
    private int mType = 2;
    private String[] mCustomItems = new String[]{"本地相册", "相机拍照"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/temp.png";
        mPicList = new ArrayList<Bitmap>();
        initViews();
        mGvPicList.setAdapter(new GridPicListAdapter(MainActivity.this, mPicList));

        //Item点击监听
        mGvPicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mResPicId.length - 1){
                    //选择本地图库
                    showDialogCustom();
                } else {
                    // 选择默认图片
                    Intent intent = new Intent(
                            MainActivity.this,
                            PuzzleMain.class);
                    intent.putExtra("picSelectedID", mResPicId[i]);
                    intent.putExtra("mType", mType);
                    startActivity(intent);
                }
            }
        });

        mTvPuzzleMainTypeSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupShow(view);
            }
        });
    }

    private void initViews() {
        mGvPicList = (GridView) findViewById(R.id.gv_puzzle_main_pic_list);
        // 显示type
        mTvPuzzleMainTypeSelected = (TextView) findViewById(
                R.id.tv_puzzle_main_type_selected);
        mResPicId = new int[]{
                R.mipmap.pic1, R.mipmap.pic2, R.mipmap.pic3,
                R.mipmap.pic4, R.mipmap.pic5, R.mipmap.pic6,
                R.mipmap.pic7, R.mipmap.pic8, R.mipmap.pic9,
                R.mipmap.pic10, R.mipmap.pic11, R.mipmap.pic12,
                R.mipmap.pic13, R.mipmap.pic14, R.mipmap.pic15,
                R.mipmap.ic_launcher
        };
        Bitmap[] bitmaps = new Bitmap[mResPicId.length];
        for (int i = 0; i < bitmaps.length; i++){
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), mResPicId[i]);
            mPicList.add(bitmaps[i]);
        }

        mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mPopupView = mLayoutInflater.inflate(R.layout.puzzle_main_type_selected, null);
        mTvType2 = (TextView) mPopupView.findViewById(R.id.tv_main_type_2);
        mTvType3 = (TextView) mPopupView.findViewById(R.id.tv_main_type_3);
        mTvType4 = (TextView) mPopupView.findViewById(R.id.tv_main_type_4);
        // 监听事件
        mTvType2.setOnClickListener(this);
        mTvType3.setOnClickListener(this);
        mTvType4.setOnClickListener(this);
    }

    /**
     * 显示popup window
     * @param view
     */
    private void popupShow(View view){
        int density = (int) ScreenUtil.getDeviceDensity(this);
        //显示popup window
        mPopupWindow = new PopupWindow(mPopupView, 200*density, 50*density);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        //设置popuWindow背景
        Drawable transpent = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(transpent);

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(view,
                Gravity.NO_GRAVITY,
                location[0] - 40*density,
                location[1] + 30*density);
    }

    /**
     * 显示选择系统图库，相机对话框
     */
   private void showDialogCustom(){
       AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
       builder.setTitle("选择：");
       builder.setItems(mCustomItems,
               new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if (0 == which) {
                           // 本地图册
                           Intent intent = new Intent(
                                   Intent.ACTION_PICK, null);
                           intent.setDataAndType(
                                   MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                   IMAGE_TYPE);
                           startActivityForResult(intent, RESULT_IMAGE);
                       } else if (1 == which) {
                           // 系统相机
                           Intent intent = new Intent(
                                   MediaStore.ACTION_IMAGE_CAPTURE);
                           Uri photoUri = Uri.fromFile(
                                   new File(TEMP_IMAGE_PATH));
                           intent.putExtra(
                                   MediaStore.EXTRA_OUTPUT,
                                   photoUri);
                           startActivityForResult(intent, RESULT_CAMERA);
                       }
                   }
               });
       builder.create().show();
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == RESULT_IMAGE && data != null){
                // 相册
                Cursor cursor = this.getContentResolver().query(
                        data.getData(), null, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(
                        cursor.getColumnIndex("_data"));
                Intent intent = new Intent(
                        MainActivity.this,
                        PuzzleMain.class);
                intent.putExtra("picPath", imagePath);
                intent.putExtra("mType", mType);
                cursor.close();
                startActivity(intent);
            } else if (requestCode == RESULT_CAMERA){
                Intent intent = new Intent(
                        MainActivity.this,
                        PuzzleMain.class);
                intent.putExtra("mPicPath", TEMP_IMAGE_PATH);
                intent.putExtra("mType", mType);
                startActivity(intent);
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Type
            case R.id.tv_main_type_2:
                mType = 2;
                mTvPuzzleMainTypeSelected.setText("2 X 2");
                break;
            case R.id.tv_main_type_3:
                mType = 3;
                mTvPuzzleMainTypeSelected.setText("3 X 3");
                break;
            case R.id.tv_main_type_4:
                mType = 4;
                mTvPuzzleMainTypeSelected.setText("4 X 4");
                break;
            default:
                break;
        }
        mPopupWindow.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
