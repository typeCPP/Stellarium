package com.app.stellarium.database.tables;

public class TodayAffirmationTable {

    public final static String TABLE_NAME = "TABLE_TODAY_AFFIRMATION";

    public final static String COLUMN_DATE = "DATE";
    public final static String COLUMN_LIKE = "LIKE";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_DATE + " TEXT NOT NULL,"
            + COLUMN_LIKE + " TEXT NOT NULL);";
}

