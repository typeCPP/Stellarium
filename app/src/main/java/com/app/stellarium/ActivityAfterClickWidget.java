package com.app.stellarium;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.UserTable;

public class ActivityAfterClickWidget extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        if (checkSignedUser(databaseHelper.getWritableDatabase())) {
            setContentView(R.layout.activity_after_click_widget);
            Bundle widgetBundle = new Bundle();
            widgetBundle.putInt("widget", 1);
            FragmentAffirmation fragment = new FragmentAffirmation();
            fragment.setArguments(widgetBundle);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        } else {
            Intent intent = new Intent(ActivityAfterClickWidget.this, MainRegistrationActivity.class);
            ActivityAfterClickWidget.this.startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityAfterClickWidget.this, MainActivity.class);
        ActivityAfterClickWidget.this.startActivity(intent);
    }

    private static boolean checkSignedUser(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        return userCursor.getCount() > 0;
    }
}