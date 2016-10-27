package com.example.xiaobozheng.bezierart;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.xiaobozheng.bezierart.model.GoodsModel;
import java.util.List;

/**
 * Created by xiaobozheng on 10/26/2016.
 */
public class ShopCartAdapter extends BaseAdapter {
    //图片集合
    private List<GoodsModel> mData;
    private LayoutInflater mLayoutInflater;
    private CallBackListener mCallBackListener;

    public ShopCartAdapter(Context context, List<GoodsModel> data) {
        super();
        this.mData = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mData != null ? mData.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.item_shopcart, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            //复用viewHolder
            viewHolder = (ViewHolder) view.getTag();
        }
        Log.d("ShopCartAdapter", mData.size() + "" + mData.get(i).getGoodsBitmap());
        if (mData != null && mData.get(i).getGoodsBitmap() != null ){
            Log.d("ShopCartAdapter", mData.get(i).getGoodsBitmap() + "");
            viewHolder.mShoppingCartItemIv.setImageBitmap(mData.get(i).getGoodsBitmap());
        }

        return view;
    }

    class ViewHolder{
        private ImageView mShoppingCartItemIv;

        public ViewHolder(View view){
            mShoppingCartItemIv = (ImageView) view.findViewById(R.id.iv_shopping_cart_item);

            view.findViewById(R.id.tv_shopping_cart_item).setOnClickListener(
                    new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (mShoppingCartItemIv != null && mCallBackListener != null){

                        mCallBackListener.callBackImg(mShoppingCartItemIv);
                    }
                }
            });
        }
    }

    public void setCallBackListener(CallBackListener callBackListener){
        this.mCallBackListener = callBackListener;
    }


    public interface CallBackListener{
        void callBackImg(ImageView imageView);
    }
}
