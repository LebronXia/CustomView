package com.example.xiaobozheng.xiamupuzzle.util;

import com.example.xiaobozheng.xiamupuzzle.activity.PuzzleMain;
import com.example.xiaobozheng.xiamupuzzle.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼图工具类
 * Created by xiaobozheng on 9/7/2016.
 */
public class GameUtil {

    //游戏中的单元格Bean
    public static List<ItemBean> mItemBeans = new ArrayList<>();
    //空格单元格
    public static ItemBean mBlankItemBean = new ItemBean();

    /**
     * 生成随机的Item
     */
    public static void getPuzzleGenerator(){
        int  index = 0;
        for (int i =0; i < mItemBeans.size(); i++){
            //得到一个在一定范围内的随机数
            index = (int) (Math.random()* PuzzleMain.TYPE*PuzzleMain.TYPE);

        }
    }

    /**
     * 交换空格与电机Item的位置
     * @param from 交换图
     * @param blank 空白图
     */
    public static void swapItems(ItemBean from, ItemBean blank){
        //创建一个空格
        ItemBean tempItemBean = new ItemBean();
        //交换BItmapid
        tempItemBean.setBitmapId(from.getBitmapId());
        from.setBitmapId(blank.getBitmapId());
        blank.setBitmapId(tempItemBean.getBitmapId());

        //交换Bitmap
        tempItemBean.setBitmapp(from.getBitmapp());
        from.setBitmapp(blank.getBitmapp());
        blank.setBitmapp(tempItemBean.getBitmapp());

        //设置新的Blank
        GameUtil.mBlankItemBean = from;

    }
}
