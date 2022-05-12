package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.AffirmationsTable;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Affirmation;
import com.google.gson.Gson;

import java.util.Calendar;

public class ActivityAfterClickWidget extends AppCompatActivity {

    private boolean readyToGoNextAffirmation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        if (checkSignedUser(databaseHelper.getWritableDatabase())) {
            setContentView(R.layout.activity_after_click_widget);
            Bundle widgetBundle = new Bundle();
            widgetBundle.putInt("widget", 1);
            try {
                workWithBackgroundAndTextForAffirmation(widgetBundle);
                FragmentAffirmation fragment = new FragmentAffirmation();
                fragment.setArguments(widgetBundle);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
            } catch (Exception e) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new FragmentHome()).commit();
            }
        } else {
            Intent intent = new Intent(ActivityAfterClickWidget.this, MainRegistrationActivity.class);
            ActivityAfterClickWidget.this.startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityAfterClickWidget.this, MainActivity.class);
        ActivityAfterClickWidget.this.startActivity(intent);
        finish();
    }

    private static boolean checkSignedUser(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        return userCursor.getCount() > 0;
    }

    private void workWithBackgroundAndTextForAffirmation(Bundle bundle) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        String todayDate = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH)) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        readyToGoNextAffirmation = true;

        if (!checkDatabaseForTodayAffirmation(database, todayDate)) {
            try {
                getAffirmationFromServerToDatabase(database, todayDate);
            } catch (Exception e) {
                database.close();
                databaseHelper.close();
                readyToGoNextAffirmation = false;
                throw new NullPointerException("Error while getting information from server");
            }
        }
        if (readyToGoNextAffirmation) {
            Pair<String, String> pair = getTodayAffirmationTextAndBackground(database, todayDate);
            if (pair != null && pair.first != null && pair.second != null && !pair.first.isEmpty() && !pair.second.isEmpty()) {
                bundle.putString("text", pair.first);
                bundle.putString("backgroundName", pair.second);
                database.close();
                databaseHelper.close();
            } else {
                throw new NullPointerException("Error while getting information from server");
            }
        }
    }

    private boolean checkDatabaseForTodayAffirmation(SQLiteDatabase database, String todayDate) {
        @SuppressLint("Recycle") Cursor affirmationsCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        return affirmationsCursor.getCount() > 0;
    }

    @SuppressLint("Range")
    private Pair<String, String> getTodayAffirmationTextAndBackground(SQLiteDatabase database, String todayDate) {
        Cursor affirmationsCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        if (affirmationsCursor.getCount() < 1) {
            return null;
        }
        affirmationsCursor.moveToLast();
        String text = affirmationsCursor.getString(affirmationsCursor.getColumnIndex(AffirmationsTable.COLUMN_TEXT));
        String backgroundName = affirmationsCursor.getString(affirmationsCursor.getColumnIndex(AffirmationsTable.COLUMN_PICTURE));
        return new Pair<>(text, backgroundName);
    }

    @SuppressLint("Range")
    private void getAffirmationFromServerToDatabase(SQLiteDatabase database, String todayDate) {
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
        Affirmation affirmation = new Gson().fromJson(response, Affirmation.class);
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
}