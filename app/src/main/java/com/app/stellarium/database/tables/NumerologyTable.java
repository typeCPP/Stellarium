package com.app.stellarium.database.tables;

public class NumerologyTable {
    public final static String TABLE_NAME = "TABLE_NUMEROLOGY";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_NUMBER = "NUMBER";
    public final static String COLUMN_GENERAL = "GENERAL";
    public final static String COLUMN_DIGNITIES = "NO_NUMBER";
    public final static String COLUMN_DISADVANTAGES = "DISADVANTAGES";
    public final static String COLUMN_FATE = "FATE";


    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NUMBER + " TEXT NOT NULL,"
            + COLUMN_GENERAL + " TEXT NOT NULL,"
            + COLUMN_DIGNITIES + " TEXT NOT NULL,"
            + COLUMN_DISADVANTAGES + " TEXT NOT NULL,"
            + COLUMN_FATE + " TEXT NOT NULL);";
}
