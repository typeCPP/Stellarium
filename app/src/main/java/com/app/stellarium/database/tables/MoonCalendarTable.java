package com.app.stellarium.database.tables;

public class MoonCalendarTable {
    public final static String TABLE_NAME = "TABLE_MOON_CALENDAR";

    public final static String COLUMN_DATE = "DATE";
    public final static String COLUMN_PHASE = "PHASE";
    public final static String COLUMN_CHARACTERISTIC = "CHARACTERISTIC";
    public final static String COLUMN_HEALTH = "HEALTH";
    public final static String COLUMN_RELATIONS = "RELATIONS";
    public final static String COLUMN_BUSINESS = "BUSINESS";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_DATE + " TEXT NOT NULL PRIMARY KEY," +
            COLUMN_PHASE + " TEXT NOT NULL," + COLUMN_CHARACTERISTIC + " TEXT NOT NULL,"
            + COLUMN_HEALTH + " TEXT NOT NULL," + COLUMN_RELATIONS + " TEXT NOT NULL,"
            + COLUMN_BUSINESS + " TEXT NOT NULL);";
}
