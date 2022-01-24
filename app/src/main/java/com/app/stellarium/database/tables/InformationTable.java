package com.app.stellarium.database.tables;

public class InformationTable {
    public final static String TABLE_NAME = "TABLE_INFORMATION";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_NAME = "NAME";
    public final static String COLUMN_DESCRIPTION = "DESCRIPTION";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_DESCRIPTION + " TEXT);";
}
