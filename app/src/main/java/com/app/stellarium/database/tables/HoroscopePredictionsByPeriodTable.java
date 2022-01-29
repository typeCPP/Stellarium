package com.app.stellarium.database.tables;

public class HoroscopePredictionsByPeriodTable {
    public final static String TABLE_NAME = "TABLE_HOROSCOPE_PREDICTIONS_BY_PERIOD";

    public final static String COLUMN_ID = "_ID";
    public final static String COLUMN_SIGN_NAME = "SIGN_NAME";
    public final static String COLUMN_TODAY_PREDICTION_ID = "TODAY_PREDICTION_ID";
    public final static String COLUMN_TOMORROW_PREDICTION_ID = "TOMORROW_PREDICTION_ID";
    public final static String COLUMN_WEEK_PREDICTION_ID = "WEEK_PREDICTION_ID";
    public final static String COLUMN_MONTH_PREDICTION_ID = "MONTH_PREDICTION_ID";
    public final static String COLUMN_YEAR_PREDICTION_ID = "YEAR_PREDICTION_ID";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SIGN_NAME + " TEXT NOT NULL,"
            + COLUMN_TODAY_PREDICTION_ID + " INTEGER NOT NULL," + COLUMN_TOMORROW_PREDICTION_ID
            + " INTEGER NOT NULL," + COLUMN_WEEK_PREDICTION_ID + " INTEGER NOT NULL,"
            + COLUMN_MONTH_PREDICTION_ID + " INTEGER NOT NULL," + COLUMN_YEAR_PREDICTION_ID
            + " INTEGER NOT NULL);";
}
