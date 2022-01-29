package com.app.stellarium.database.tables;

public class HoroscopePredictionsTable {
    public final static String TABLE_NAME = "TABLE_HOROSCOPE_PREDICTIONS";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_LOVE = "LOVE";
    public final static String COLUMN_COMMON = "COMMON";
    public final static String COLUMN_HEALTH = "HEALTH";
    public final static String COLUMN_BUSINESS = "BUSINESS";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_LOVE + " TEXT NOT NULL,"
            + COLUMN_COMMON + " TEXT NOT NULL," + COLUMN_HEALTH + " TEXT NOT NULL," + COLUMN_BUSINESS
            + " TEXT NOT NULL);";
}
