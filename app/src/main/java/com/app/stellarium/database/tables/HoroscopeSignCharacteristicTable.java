package com.app.stellarium.database.tables;

public class HoroscopeSignCharacteristicTable {
    public final static String TABLE_NAME = "TABLE_HOROSCOPE_SIGN_CHARACTERISTIC";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_DESCRIPTION = "DESCRIPTION";
    public final static String COLUMN_CHARACTER = "CHARACTER";
    public final static String COLUMN_LOVE = "LOVE";
    public final static String COLUMN_CAREER = "CAREER";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_DESCRIPTION + " TEXT NOT NULL,"
            + COLUMN_CHARACTER + " TEXT NOT NULL," + COLUMN_LOVE + " TEXT NOT NULL," + COLUMN_CAREER
            + " TEXT NOT NULL);";
}
