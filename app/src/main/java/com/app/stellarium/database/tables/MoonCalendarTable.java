package com.app.stellarium.database.tables;

public class MoonCalendarTable {
    public final static String TABLE_NAME = "TABLE_MOON_CALENDAR";

    public final static String COLUMN_DATE = "DATE";
    public final static String COLUMN_DESCRIPTION = "DESCRIPTION";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_DATE + " TEXT NOT NULL PRIMARY KEY," +
            COLUMN_DESCRIPTION + " TEXT NOT NULL);";
}
