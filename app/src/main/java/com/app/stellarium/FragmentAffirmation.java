package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.AffirmationsTable;
import com.app.stellarium.database.tables.FavoriteAffirmationsTable;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.dialog.LoadingDialog;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Affirmation;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.Calendar;
import java.util.function.UnaryOperator;


public class FragmentAffirmation extends Fragment {

    private FrameLayout frameLayout;
    private TextView affirmationText;
    private LoadingDialog loadingDialog;
    private LikeButton likeButton;
    private boolean isLiked = false;
    private Affirmation affirmation;
    private boolean isOpenByWidget = false;
    public FragmentAffirmation() {
    }

    public static FragmentAffirmation newInstance(String param1, String param2) {
        FragmentAffirmation fragment = new FragmentAffirmation();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        isOpenByWidget = true;
        if (bundle == null) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.hideBottomBar(true);
            }
            isOpenByWidget = false;
        }

        View view = inflater.inflate(R.layout.fragment_affirmation, container, false);

        frameLayout = view.findViewById(R.id.frameLayout);
        affirmationText = view.findViewById(R.id.affirmation_text);
        loadingDialog = new LoadingDialog(view.getContext());
        likeButton = view.findViewById(R.id.heart_button);

        Calendar calendar = Calendar.getInstance();
        String todayDate
                = String.valueOf(calendar.get(Calendar.YEAR))
                + String.valueOf(calendar.get(Calendar.MONTH))
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        likeButton.setOnLikeListener(new OnLikeListener() {

            @SuppressLint("Range")
            @Override
            public void liked(LikeButton likeButton) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                updateFavoriteAffirmationTable(database, todayDate, true);
                int userID = databaseHelper.getCurrentUserServerID(database);
                Log.d("AFFIRMATION USER ID", String.valueOf(userID));
                if (userID != 0) {
                    ServerConnection serverConnection = new ServerConnection();
                    serverConnection.getStringResponseByParameters("like_affirm/?user_id=" + userID + "&affirm_id=" + getCurrentAffirmationID(todayDate, database));
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                updateFavoriteAffirmationTable(database, todayDate, false);
                int userID = databaseHelper.getCurrentUserServerID(database);
                Log.d("AFFIRMATION USER ID", String.valueOf(userID));
                if (userID != 0) {
                    ServerConnection serverConnection = new ServerConnection();
                    serverConnection.getStringResponseByParameters("unlike_affirm/?user_id=" + userID + "&affirm_id=" + getCurrentAffirmationID(todayDate, database));
                }
            }
        });

        loadingDialog.setOnClick(new UnaryOperator<Void>() {
            @Override
            public Void apply(Void unused) {
                workWithBackgroundAndText();
                return null;
            }
        });

        workWithBackgroundAndText();

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        isLiked = checkLike(database, todayDate);
        likeButton.setLiked(isLiked);
        return view;
    }

    private int getCurrentAffirmationID(String todayDate, SQLiteDatabase database) {
        Cursor affirmationCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        affirmationCursor.moveToFirst();
        @SuppressLint("Range") int idTodayAffirmation
                = affirmationCursor.getInt(affirmationCursor.getColumnIndex(AffirmationsTable.COLUMN_ID));
        return idTodayAffirmation;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isOpenByWidget) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.hideBottomBar(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isOpenByWidget = true;
        Bundle bundle = getArguments();
        if (bundle == null) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.hideBottomBar(true);
                isOpenByWidget = false;
            }
        }
    }

    private void workWithBackgroundAndText() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        String todayDate = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH)) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        Handler handler = new Handler();
        loadingDialog.show();
        loadingDialog.startGifAnimation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkDatabaseForTodayAffirmation(database, todayDate)) {
                    try {
                        getDataFromServerToDatabase(database, todayDate);
                        loadingDialog.dismiss();
                    } catch (Exception e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.stopGifAnimation();
                            }
                        });
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Pair<String, String> pair = getTodayBackgroundAndText(database, todayDate);
                        if (pair != null) {
                            Drawable background = getBackgroundByName(pair.second);
                            frameLayout.setBackground(background);

                            affirmationText.setText(pair.first);
                            loadingDialog.dismiss();
                        }
                    }
                });
            }
        }).start();
    }

    private boolean checkDatabaseForTodayAffirmation(SQLiteDatabase database, String todayDate) {
        @SuppressLint("Recycle") Cursor affirmationsCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        return affirmationsCursor.getCount() > 0;
    }

    @SuppressLint("Range")
    private Pair<String, String> getTodayBackgroundAndText(SQLiteDatabase database, String todayDate) {
        Cursor affirmationsCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        if (affirmationsCursor.getCount() < 1) {
            return null;
        }
        affirmationsCursor.moveToLast();
        String text = affirmationsCursor.getString(affirmationsCursor.getColumnIndex(AffirmationsTable.COLUMN_TEXT));
        String backgroundName = affirmationsCursor.getString(affirmationsCursor.getColumnIndex(AffirmationsTable.COLUMN_PICTURE));
        return new Pair<>(text, backgroundName);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getBackgroundByName(String backgroundName) {
        Resources resources = getContext().getResources();
        return resources.getDrawable(getContext().getResources().getIdentifier(backgroundName, "drawable", getContext().getPackageName()));
    }

    @SuppressLint("Range")
    private boolean checkLike(SQLiteDatabase database, String todayDate) {
        Cursor affirmationCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        if (affirmationCursor.getCount() > 0) {
            affirmationCursor.moveToFirst();
            @SuppressLint("Range") int idTodayAffirmation
                    = affirmationCursor.getInt(affirmationCursor.getColumnIndex(AffirmationsTable.COLUMN_ID));
            Cursor favoriteAffirmationCursor = database.query(FavoriteAffirmationsTable.TABLE_NAME, null,
                    FavoriteAffirmationsTable.COLUMN_AFFIRMATION_ID + " = " + idTodayAffirmation,
                    null, null, null, null);
            return favoriteAffirmationCursor.getCount() >= 1;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    private void getDataFromServerToDatabase(SQLiteDatabase database, String todayDate) {
        ServerConnection serverConnection = new ServerConnection();
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        String params;
        userCursor.moveToLast();
        int userServerId = userCursor.getInt(userCursor.getColumnIndex(UserTable.COLUMN_SERVER_ID));
        if (userServerId == -1) {
            params = "affirmationNotReg/";
        } else {
            params = "affirmation/?id=" + userServerId;
        }
        String response = serverConnection.getStringResponseByParameters(params);
        affirmation = new Gson().fromJson(response, Affirmation.class);
        if (affirmation == null) {
            throw new NullPointerException();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(AffirmationsTable.COLUMN_DATE, todayDate);
        contentValues.put(AffirmationsTable.COLUMN_TEXT, affirmation.text);
        contentValues.put(AffirmationsTable.COLUMN_ID, affirmation.id);
        contentValues.put(AffirmationsTable.COLUMN_PICTURE, affirmation.picture);

        database.insert(AffirmationsTable.TABLE_NAME, null, contentValues);
    }

    private void updateFavoriteAffirmationTable(SQLiteDatabase database, String todayDate, boolean addAffirmation) {
        Cursor affirmationCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        affirmationCursor.moveToFirst();
        @SuppressLint("Range") Integer idTodayAffirmation
                = affirmationCursor.getInt(affirmationCursor.getColumnIndex(AffirmationsTable.COLUMN_ID));

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteAffirmationsTable.COLUMN_AFFIRMATION_ID, idTodayAffirmation);
        if (addAffirmation) {
            database.insert(FavoriteAffirmationsTable.TABLE_NAME, null, contentValues);
        } else {
            String[] args = {String.valueOf(idTodayAffirmation)};
            database.delete(FavoriteAffirmationsTable.TABLE_NAME, "AFFIRMATION_ID=?", args);
        }
    }
}