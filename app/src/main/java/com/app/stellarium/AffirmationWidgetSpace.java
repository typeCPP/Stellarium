package com.app.stellarium;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;

import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintSet;

import com.app.stellarium.utils.AffirmationWidgetUtils;

/**
 * Implementation of App Widget functionality.
 */
public class AffirmationWidgetSpace extends AppWidgetProvider {
    public static final String ACTION_WIDGET_CLICK_RECEIVER = "ActionReceiverWidget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent active = new Intent(context, AffirmationWidgetSpace.class);
        active.setAction(ACTION_WIDGET_CLICK_RECEIVER);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.affirmation_widget_space);
        Intent launchActivity = new Intent(context, ActivityAfterClickWidget.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchActivity, 0);
        remoteViews.setOnClickPendingIntent(R.id.space_widget_layout, pendingIntent);
        remoteViews.setTextViewText(R.id.text_affirmation_widget_space, AffirmationWidgetUtils.workWithText(context));
        ComponentName thisWidget = new ComponentName(context, AffirmationWidgetSpace.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, remoteViews);
    }


    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}