package com.app.stellarium.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.stellarium.database.tables.CompatibilityNamesTable;
import com.app.stellarium.database.tables.CompatibilityZodiacTable;
import com.app.stellarium.database.tables.HoroscopePredictionsByPeriodTable;
import com.app.stellarium.database.tables.HoroscopePredictionsTable;
import com.app.stellarium.database.tables.HoroscopeSignCharacteristicTable;
import com.app.stellarium.database.tables.InformationTable;
import com.app.stellarium.database.tables.MoonCalendarTable;
import com.app.stellarium.database.tables.NumerologyTable;
import com.app.stellarium.database.tables.PythagoreanSquareTable;
import com.app.stellarium.database.tables.TaroCardsTable;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.database.tables.ZodiacSignsTable;
import com.app.stellarium.utils.jsonmodels.User;

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
            sqLiteDatabase.execSQL(CompatibilityZodiacTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(ZodiacSignsTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(CompatibilityNamesTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(TaroCardsTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(MoonCalendarTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(NumerologyTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(UserTable.CREATE_TABLE);
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompatibilityZodiacTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ZodiacSignsTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompatibilityNamesTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaroCardsTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoonCalendarTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NumerologyTable.TABLE_NAME);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertUser(SQLiteDatabase database, User user) {
        ContentValues cv = new ContentValues();
        cv.put(UserTable.COLUMN_NAME, user.name);
        cv.put(UserTable.COLUMN_MAIL_CONFIRMED, user.mail_confirm);
        cv.put(UserTable.COLUMN_DATE_OF_BIRTH, user.date);
        cv.put(UserTable.COLUMN_HOROSCOPE_SIGN_ID, user.sign);
        cv.put(UserTable.COLUMN_SEX, user.sex);
        cv.put(UserTable.COLUMN_SERVER_ID, user.id);
        database.insert(UserTable.TABLE_NAME, null, cv);
    }

    public void dropUserTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);

        sqLiteDatabase.execSQL(UserTable.CREATE_TABLE);
    }

    public void dropHoroscopeTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HoroscopePredictionsTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HoroscopePredictionsByPeriodTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HoroscopeSignCharacteristicTable.TABLE_NAME);

        sqLiteDatabase.execSQL(HoroscopePredictionsTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(HoroscopePredictionsByPeriodTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(HoroscopeSignCharacteristicTable.CREATE_TABLE);
    }
}
