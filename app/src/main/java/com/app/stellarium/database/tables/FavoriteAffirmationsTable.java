package com.app.stellarium.database.tables;

public class FavoriteAffirmationsTable {

    public final static String TABLE_NAME = "TABLE_TODAY_AFFIRMATION";

    public final static String COLUMN_AFFIRMATION_ID = "AFFIRMATION_ID";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_AFFIRMATION_ID + " INTEGER NOT NULL);";
}

