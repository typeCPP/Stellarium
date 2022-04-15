package com.app.stellarium.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import com.app.stellarium.AffirmationWidget;
import com.app.stellarium.AffirmationWidgetMono;
import com.app.stellarium.AffirmationWidgetSpace;
import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.AffirmationsTable;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.utils.jsonmodels.Affirmation;
import com.google.gson.Gson;

import java.util.Calendar;


public class AffirmationWidgetUtils {
    static Affirmation affirmation;
    public static String str;

    public static boolean checkDatabaseForTodayAffirmation(SQLiteDatabase database, String todayDate) {
        @SuppressLint("Recycle") Cursor affirmationsCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        return affirmationsCursor.getCount() > 0;
    }

    @SuppressLint("Range")
    public static String getTodayText(SQLiteDatabase database, String todayDate) {
        Cursor affirmationsCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        if (affirmationsCursor.getCount() < 1) {
            return null;
        }
        affirmationsCursor.moveToLast();
        return affirmationsCursor.getString(affirmationsCursor.getColumnIndex(AffirmationsTable.COLUMN_TEXT));
    }

    @SuppressLint("Range")
    public static void getDataFromServerToDatabase(SQLiteDatabase database, String todayDate) {
        ServerConnection serverConnection = new ServerConnection();
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        String params;
        userCursor.moveToLast();
        int userServerId = userCursor.getInt(userCursor.getColumnIndex(UserTable.COLUMN_SERVER_ID));
        if (userServerId == -1) {
            params = "affirmationNotReg/";
        } else {
            params = "affirmation/?id=" + userServerId;
        }
        String response = serverConnection.getStringResponseByParameters(params);
        affirmation = new Gson().fromJson(response, Affirmation.class);
        if (affirmation == null) {
            throw new NullPointerException();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(AffirmationsTable.COLUMN_DATE, todayDate);
        contentValues.put(AffirmationsTable.COLUMN_TEXT, affirmation.text);
        contentValues.put(AffirmationsTable.COLUMN_ID, affirmation.id);
        contentValues.put(AffirmationsTable.COLUMN_PICTURE, affirmation.picture);

        database.insert(AffirmationsTable.TABLE_NAME, null, contentValues);
    }

    public static void workWithText(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        String todayDate = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH)) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkDatabaseForTodayAffirmation(database, todayDate)) {
                    try {
                        getDataFromServerToDatabase(database, todayDate);
                    } catch (Exception e) {
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       str = getTodayText(database, todayDate);
                        AffirmationWidget.setNewTextForAffirmation(str);
                        AffirmationWidgetMono.setNewTextForAffirmation(str);
                        AffirmationWidgetSpace.setNewTextForAffirmation(str);
                    }
                });
            }
        }).start();
    }

    public static boolean checkSignedUser(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        return userCursor.getCount() > 0;
    }
}
