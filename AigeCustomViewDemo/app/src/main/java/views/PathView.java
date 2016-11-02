package views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiaobozheng on 11/2/2016.
 */
public class PathView extends View{
    private Path mPath; //路径对象
    private Paint mPaint;   //路径画笔对象
    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



}
