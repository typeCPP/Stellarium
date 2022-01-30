package com.app.stellarium.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.stellarium.database.tables.HoroscopePredictionsByPeriodTable;
import com.app.stellarium.database.tables.HoroscopePredictionsTable;
import com.app.stellarium.database.tables.HoroscopeSignCharacteristicTable;
import com.app.stellarium.database.tables.InformationTable;
import com.app.stellarium.database.tables.PythagoreanSquareTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int version = 2;
    public static String databaseName = "Stellarium.db";


    public DatabaseHelper(Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(InformationTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(HoroscopePredictionsTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(HoroscopePredictionsByPeriodTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(HoroscopeSignCharacteristicTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(PythagoreanSquareTable.CREATE_TABLE);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + InformationTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HoroscopePredictionsTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HoroscopePredictionsByPeriodTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HoroscopeSignCharacteristicTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PythagoreanSquareTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
