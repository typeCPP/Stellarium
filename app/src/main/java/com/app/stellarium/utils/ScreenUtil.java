package com.app.stellarium.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtil {

    public static int dip2px(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5F * (float) (dip >= 0.0F ? 1 : -1));
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

}
