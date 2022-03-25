package com.app.stellarium.database.tables;

public class AffirmationsTable {
    public final static String TABLE_NAME = "TABLE_AFFIRMATIONS";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_TEXT = "TEXT";
    public final static String COLUMN_PICTURE = "PICTURE";
    public final static String COLUMN_DATE = "DATE";


    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TEXT + " TEXT NOT NULL,"
            + COLUMN_DATE + " TEXT NOT NULL,"
            + COLUMN_PICTURE + " NUMBER);";
}
