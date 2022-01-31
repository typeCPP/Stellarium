package com.app.stellarium.database.tables;

public class ZodiacSignsTable {
    public final static String TABLE_NAME = "TABLE_ZODIAC_SIGNS";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_NAME = "NAME";
    public final static String COLUMN_SIGN_ID = "SIGN_ID";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_SIGN_ID + " INTEGER NOT NULL);";
}
