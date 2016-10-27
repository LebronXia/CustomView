package com.example.xiaobozheng.bezierart;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.xiaobozheng.bezierart.utils.BezierUtil;

/**
 * Created by xiaobozheng on 10/26/2016.
 */
public class BezierEvaluator implements TypeEvaluator<PointF>{

    //PointF中包含了x,y的当前位置，单位是Float
    private PointF mControlPoint;

    public BezierEvaluator(PointF controlPoint){
        this.mControlPoint = controlPoint;
    }

    @Override
    public PointF evaluate(float t, PointF startValue, PointF endValue) {
        return BezierUtil.CalculateBezierPointForQuadratic(t, startValue, mControlPoint, endValue);
    }
}
