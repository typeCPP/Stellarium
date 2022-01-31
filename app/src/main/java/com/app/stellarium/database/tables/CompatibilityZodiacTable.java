package com.app.stellarium.database.tables;

public class CompatibilityZodiacTable {
    public final static String TABLE_NAME = "TABLE_COMPATIBILITY_ZODIAC";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_FIRST_SIGN_ID = "FIRST_SIGN_ID";
    public final static String COLUMN_SECOND_SIGN_ID = "SECOND_SIGN_ID";

    public final static String COLUMN_COMP_LOVE_TEXT = "COMP_LOVE_TEXT";
    public final static String COLUMN_COMP_SEX_TEXT = "COMP_SEX_TEXT";
    public final static String COLUMN_COMP_MARRIAGE_TEXT = "COMP_MARRIAGE_TEXT";
    public final static String COLUMN_COMP_FRIENDSHIP_TEXT = "COMP_FRIENDSHIP_TEXT";

    public final static String COLUMN_COMP_LOVE_VALUE = "COMP_LOVE_VALUE";
    public final static String COLUMN_COMP_SEX_VALUE = "COMP_SEX_VALUE";
    public final static String COLUMN_COMP_MARRIAGE_VALUE = "COMP_MARRIAGE_VALUE";
    public final static String COLUMN_COMP_FRIENDSHIP_VALUE = "COMP_FRIENDSHIP_VALUE";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_FIRST_SIGN_ID + " INTEGER NOT NULL,"
            + COLUMN_SECOND_SIGN_ID + " INTEGER NOT NULL,"

            + COLUMN_COMP_LOVE_TEXT + " TEXT NOT NULL,"
            + COLUMN_COMP_SEX_TEXT + " TEXT NOT NULL,"
            + COLUMN_COMP_MARRIAGE_TEXT + " TEXT NOT NULL,"
            + COLUMN_COMP_FRIENDSHIP_TEXT + " TEXT NOT NULL,"

            + COLUMN_COMP_LOVE_VALUE + " INTEGER NOT NULL,"
            + COLUMN_COMP_SEX_VALUE + " INTEGER NOT NULL,"
            + COLUMN_COMP_MARRIAGE_VALUE + " INTEGER NOT NULL,"
            + COLUMN_COMP_FRIENDSHIP_VALUE + " INTEGER NOT NULL);";
}
