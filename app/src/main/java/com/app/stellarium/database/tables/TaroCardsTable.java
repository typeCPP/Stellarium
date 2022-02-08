package com.app.stellarium.database.tables;

public class TaroCardsTable {
    public final static String TABLE_NAME = "TABLE_TARO_CARDS";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_NAME = "NAME";
    public final static String COLUMN_PICTURE_NAME = "PICTURE_NAME";
    public final static String COLUMN_DESCRIPTION = "DESCRIPTION";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_PICTURE_NAME + " TEXT NOT NULL," + COLUMN_DESCRIPTION + " TEXT NOT NULL);";
}
