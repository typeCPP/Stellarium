package com.app.stellarium.database.tables;

public class UserTable {
    public final static String TABLE_NAME = "TABLE_USER";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_NAME = "NAME";
    public final static String COLUMN_DATE_OF_BIRTH = "DATE_OF_BIRTH";
    public final static String COLUMN_SEX = "SEX";
    public final static String COLUMN_HOROSCOPE_SIGN_ID = "HOROSCOPE_SIGN_ID";
    public final static String COLUMN_EMAIL = "EMAIL";
    public final static String COLUMN_PASSWORD = "PASSWORD";
    public final static String COLUMN_GOOGLE_ID = "GOOGLE_ID";
    public final static String COLUMN_FACEBOOK_ID = "FACEBOOK_ID";
    public final static String COLUMN_MAIL_CONFIRMED = "MAIL_CONFIRMED";
    public final static String COLUMN_SERVER_ID = "SERVER_ID";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_DATE_OF_BIRTH + " TEXT,"
            + COLUMN_SEX + " INTEGER,"
            + COLUMN_HOROSCOPE_SIGN_ID + " INTEGER,"

            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_MAIL_CONFIRMED + " INTEGER,"
            + COLUMN_SERVER_ID + " INTEGER,"

            + COLUMN_GOOGLE_ID + " TEXT,"
            + COLUMN_FACEBOOK_ID + " TEXT);";
}
