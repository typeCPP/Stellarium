package com.app.stellarium.database.tables;

public class TaroCardsTable {
    public final static String TABLE_NAME = "TABLE_TARO_CARDS";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_NAME = "NAME";
    public final static String COLUMN_PICTURE_NAME = "PICTURE_NAME";
    public final static String COLUMN_DESCRIPTION_ONE_CARD = "DESCRIPTION_ONE_CARD";
    public final static String COLUMN_DESCRIPTION_DAY_CARD = "DESCRIPTION_DAY_CARD";
    public final static String COLUMN_DESCRIPTION_FIRST_OF_THREE_CARDS = "DESCRIPTION_FIRST_OF_THREE_CARDS";
    public final static String COLUMN_DESCRIPTION_SECOND_OF_THREE_CARDS = "DESCRIPTION_SECOND_OF_THREE_CARDS";
    public final static String COLUMN_DESCRIPTION_THIRD_OF_THREE_CARDS = "DESCRIPTION_THIRD_OF_THREE_CARDS";
    public final static String COLUMN_DESCRIPTION_FIRST_OF_FOUR_CARDS = "DESCRIPTION_FIRST_OF_FOUR_CARDS";
    public final static String COLUMN_DESCRIPTION_SECOND_OF_FOUR_CARDS = "DESCRIPTION_SECOND_OF_FOUR_CARDS";
    public final static String COLUMN_DESCRIPTION_THIRD_OF_FOUR_CARDS = "DESCRIPTION_THIRD_OF_FOUR_CARDS";
    public final static String COLUMN_DESCRIPTION_FOURTH_OF_FOUR_CARDS = "DESCRIPTION_FOURTH_OF_FOUR_CARDS";
    public final static String COLUMN_DESCRIPTION_FIRST_OF_SEVEN_CARDS = "DESCRIPTION_FIRST_OF_SEVEN_CARDS";
    public final static String COLUMN_DESCRIPTION_SECOND_OF_SEVEN_CARDS = "DESCRIPTION_SECOND_OF_SEVEN_CARDS";
    public final static String COLUMN_DESCRIPTION_THIRD_OF_SEVEN_CARDS = "DESCRIPTION_THIRD_OF_SEVEN_CARDS";
    public final static String COLUMN_DESCRIPTION_FOURTH_OF_SEVEN_CARDS = "DESCRIPTION_FOURTH_OF_SEVEN_CARDS";
    public final static String COLUMN_DESCRIPTION_FIFTH_OF_SEVEN_CARDS = "DESCRIPTION_FIFTH_OF_SEVEN_CARDS";
    public final static String COLUMN_DESCRIPTION_SIXTH_OF_SEVEN_CARDS = "DESCRIPTION_SIXTH_OF_SEVEN_CARDS";
    public final static String COLUMN_DESCRIPTION_SEVENTH_OF_SEVEN_CARDS = "DESCRIPTION_SEVENTH_OF_SEVEN_CARDS";
    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT NOT NULL," +
            COLUMN_PICTURE_NAME + " TEXT NOT NULL," + COLUMN_DESCRIPTION_ONE_CARD + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_DAY_CARD + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_FIRST_OF_THREE_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_SECOND_OF_THREE_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_THIRD_OF_THREE_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_FIRST_OF_FOUR_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_SECOND_OF_FOUR_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_THIRD_OF_FOUR_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_FOURTH_OF_FOUR_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_FIRST_OF_SEVEN_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_SECOND_OF_SEVEN_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_THIRD_OF_SEVEN_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_FOURTH_OF_SEVEN_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_FIFTH_OF_SEVEN_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_SIXTH_OF_SEVEN_CARDS + " TEXT NOT NULL," +
            COLUMN_DESCRIPTION_SEVENTH_OF_SEVEN_CARDS + " TEXT NOT NULL);";
}