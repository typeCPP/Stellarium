package com.app.stellarium.utils.custom;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

public class PointEvaluator implements TypeEvaluator<PointF> {

    private PointF point1;

    public PointEvaluator(PointF targetPointF) {
        point1 = targetPointF;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue,
                           PointF endValue) {
        final float t = fraction;
        float oneMinusT = 1.0f - t;
        PointF point = new PointF();
        PointF point0 = (PointF) startValue;
        PointF point2 = endValue;
        PointF point3 = (PointF) endValue;

        point.x = oneMinusT * oneMinusT * (point0.x)
                + 2 * (point1.x) * t * oneMinusT
                + t * t * (point2.x);

        point.y = oneMinusT * oneMinusT * (point0.y)
                + 2 * (point1.y) * t * oneMinusT
                + t * t * (point2.y);

        return point;
    }


}
