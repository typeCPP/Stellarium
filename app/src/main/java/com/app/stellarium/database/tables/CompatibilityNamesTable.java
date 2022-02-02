package com.app.stellarium.database.tables;

public class CompatibilityNamesTable {
    public final static String TABLE_NAME = "TABLE_COMPATIBILITY_NAMES";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_HASHED_ID = "HASHED_ID";

    public final static String COLUMN_COMP_LOVE_TEXT = "COMP_LOVE_TEXT";
    public final static String COLUMN_COMP_FRIENDSHIP_TEXT = "COMP_FRIENDSHIP_TEXT";
    public final static String COLUMN_COMP_JOB_TEXT = "COMP_JOB_TEXT";

    public final static String COLUMN_COMP_LOVE_VALUE = "COMP_LOVE_VALUE";
    public final static String COLUMN_COMP_FRIENDSHIP_VALUE = "COMP_FRIENDSHIP_VALUE";
    public final static String COLUMN_COMP_JOB_VALUE = "COMP_JOB_VALUE";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_HASHED_ID + " INTEGER NOT NULL,"

            + COLUMN_COMP_LOVE_TEXT + " TEXT NOT NULL,"
            + COLUMN_COMP_FRIENDSHIP_TEXT + " TEXT NOT NULL,"
            + COLUMN_COMP_JOB_TEXT + " TEXT NOT NULL,"

            + COLUMN_COMP_LOVE_VALUE + " INTEGER NOT NULL,"
            + COLUMN_COMP_FRIENDSHIP_VALUE + " INTEGER NOT NULL,"
            + COLUMN_COMP_JOB_VALUE + " INTEGER NOT NULL);";
}
