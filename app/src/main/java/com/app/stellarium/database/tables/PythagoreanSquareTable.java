package com.app.stellarium.database.tables;

public class PythagoreanSquareTable {
    public final static String TABLE_NAME = "TABLE_PYTHAGOREAN_SQUARE";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_NUMBER = "NUMBER";
    public final static String COLUMN_NO_NUMBER = "NO_NUMBER";
    public final static String COLUMN_ONE_NUMBER = "ONE_NUMBER";
    public final static String COLUMN_TWO_NUMBERS = "TWO_NUMBERS";
    public final static String COLUMN_THREE_NUMBERS = "THREE_NUMBERS";
    public final static String COLUMN_FOUR_NUMBERS = "FOUR_NUMBERS";
    public final static String COLUMN_FIVE_NUMBERS = "FIVE_NUMBERS";
    public final static String COLUMN_SIX_NUMBERS = "SIX_NUMBERS";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NUMBER + " TEXT NOT NULL,"
            + COLUMN_NO_NUMBER + " TEXT NOT NULL,"
            + COLUMN_ONE_NUMBER + " TEXT NOT NULL,"
            + COLUMN_TWO_NUMBERS + " TEXT NOT NULL,"
            + COLUMN_THREE_NUMBERS + " TEXT NOT NULL,"
            + COLUMN_FOUR_NUMBERS + " TEXT NOT NULL,"
            + COLUMN_FIVE_NUMBERS + " TEXT NOT NULL,"
            + COLUMN_SIX_NUMBERS + " TEXT NOT NULL);";
}
