package com.app.stellarium;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.widget.RemoteViews;

import com.app.stellarium.database.tables.AffirmationsTable;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.utils.AffirmationWidgetUtils;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Affirmation;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 */
public class AffirmationWidgetMono extends AppWidgetProvider{
    public static final String ACTION_WIDGET_CLICK_RECEIVER = "ActionReceiverWidget";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent active = new Intent(context, AffirmationWidgetMono.class);
        active.setAction(ACTION_WIDGET_CLICK_RECEIVER);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.affirmation_widget_mono);
        Intent launchActivity = new Intent(context, ActivityAfterClickWidget.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchActivity, 0);
        remoteViews.setOnClickPendingIntent(R.id.mono_widget_layout, pendingIntent);
        remoteViews.setTextViewText(R.id.text_affirmation_widget_mono, AffirmationWidgetUtils.workWithText(context));
        ComponentName thisWidget = new ComponentName(context, AffirmationWidgetMono.class);
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