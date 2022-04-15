package com.app.stellarium;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.utils.AffirmationWidgetUtils;

public class AffirmationWidgetMono extends AppWidgetProvider {
    public static final String ACTION_WIDGET_CLICK_RECEIVER = "ActionReceiverWidget";
    private static String text;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent active = new Intent(context, AffirmationWidgetMono.class);
        active.setAction(ACTION_WIDGET_CLICK_RECEIVER);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.affirmation_widget_mono);
        Intent launchActivity = new Intent(context, ActivityAfterClickWidget.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchActivity, 0);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (AffirmationWidgetUtils.checkSignedUser(database) && FragmentAffirmation.text != null) {
            remoteViews.setTextViewText(R.id.text_affirmation_widget_mono, FragmentAffirmation.text);
            remoteViews.setOnClickPendingIntent(R.id.mono_widget_layout, pendingIntent);
        } else {
            if (AffirmationWidgetUtils.checkSignedUser(database)) {
                if (text == null) {
                    AffirmationWidgetUtils.workWithText(context);
                    remoteViews.setTextViewText(R.id.text_affirmation_widget_mono, "Перемены - мои друзья.");
                } else {
                    remoteViews.setTextViewText(R.id.text_affirmation_widget_mono, text);
                }
                remoteViews.setOnClickPendingIntent(R.id.mono_widget_layout, pendingIntent);
            } else {
                remoteViews.setTextViewText(R.id.text_affirmation_widget_mono, "Перемены - мои друзья.");
            }
        }
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

    public static void setNewTextForAffirmation(String str)
    {
        text = str;
    }
}