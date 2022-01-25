package com.app.stellarium.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.stellarium.database.tables.InformationTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int version = 1;
    public static String databaseName = "Stellarium.db";

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(InformationTable.CREATE_TABLE);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + InformationTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
