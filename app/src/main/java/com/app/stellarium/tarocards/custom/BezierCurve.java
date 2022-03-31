package com.app.stellarium.tarocards.custom;

import android.graphics.PointF;

public class BezierCurve {

    public static PointF bezier(float t, PointF point0, PointF point1) {
        float oneMinusT = 1.0f - t;
        PointF point = new PointF();
        point.x = oneMinusT * point0.x + t * point1.x;
        point.y = oneMinusT * point0.y + t * point1.y;
        return point;
    }

    public static PointF bezier(float t, PointF point0, PointF point1, PointF point2) {
        float oneMinusT = 1.0f - t;
        PointF point = new PointF();
        point.x = oneMinusT * oneMinusT * point0.x
                + 2 * t * oneMinusT * point1.x
                + t * t * point2.x;
        point.y = oneMinusT * oneMinusT * point0.y
                + 2 * t * oneMinusT * point1.y
                + t * t * point2.y;
        return point;
    }

    public static PointF bezier(float t, PointF point0, PointF point1, PointF point2, PointF point3) {
        float oneMinusT = 1.0f - t;
        PointF point = new PointF();
        point.x = oneMinusT * oneMinusT * oneMinusT * (point0.x)
                + 3 * oneMinusT * oneMinusT * t * (point1.x)
                + 3 * oneMinusT * t * t * (point2.x)
                + t * t * t * (point3.x);

        point.y = oneMinusT * oneMinusT * oneMinusT * (point0.y)
                + 3 * oneMinusT * oneMinusT * t * (point1.y)
                + 3 * oneMinusT * t * t * (point2.y)
                + t * t * t * (point3.y);
        return point;
    }
}
