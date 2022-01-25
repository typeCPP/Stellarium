package com.app.stellarium.transitionGenerator;

import android.graphics.RectF;

public class StellariumMathUtils {
    protected static float truncate1(float f, int decimalPlaces) {
        float decimalShift = (float) Math.pow(10, decimalPlaces);
        return Math.round(f * decimalShift) / decimalShift;
    }


    protected static boolean haveSameAspectRatio1(RectF r1, RectF r2) {
        float srcRectRatio = StellariumMathUtils.truncate1(StellariumMathUtils.getRectRatio1(r1), 3);
        float dstRectRatio = StellariumMathUtils.truncate1(StellariumMathUtils.getRectRatio1(r2), 3);

        return (Math.abs(srcRectRatio-dstRectRatio) <= 0.01f);
    }

    protected static float getRectRatio1(RectF rect) {
        return rect.width() / rect.height();
    }
}
