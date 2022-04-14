package com.app.stellarium;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.app.stellarium.utils.AffirmationWidgetUtils;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class AffirmationWidget extends AppWidgetProvider {
    public static final String ACTION_WIDGET_CLICK_RECEIVER = "ActionReceiverWidget";

    private static final String Sync_clicked = "WidgetImageClicker";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent active = new Intent(context, AffirmationWidget.class);
        active.setAction(ACTION_WIDGET_CLICK_RECEIVER);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.affirmation_widget);
        Intent launchActivity = new Intent(context, ActivityAfterClickWidget.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchActivity, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        remoteViews.setTextViewText(R.id.text_affirmation_widget, AffirmationWidgetUtils.workWithText(context));
        ComponentName thisWidget = new ComponentName(context, AffirmationWidget.class);
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