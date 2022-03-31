package com.app.stellarium.tarocards.custom;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

public class BezierEvaluator implements TypeEvaluator<PointF> {

    @Override
    public PointF evaluate(float fraction, PointF startValue,
                           PointF endValue) {
        return BezierCurve.bezier(fraction, startValue, endValue);
    }


}
