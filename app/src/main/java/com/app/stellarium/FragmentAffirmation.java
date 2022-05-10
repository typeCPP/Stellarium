package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.app.stellarium.utils.ServerConnection;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.Calendar;


public class FragmentAffirmation extends Fragment {

    private FrameLayout frameLayout;
    private TextView affirmationText;
    private LikeButton likeButton;
    private boolean isLiked = false;
    private boolean isOpenByWidget = false;
    public static String text;
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
        if (bundle != null && bundle.getInt("widget") != 1) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null) {
                activity.hideBottomBar(true);
                activity.setNumberOfPrevFragment(0);
            }
            isOpenByWidget = false;
        }

        View view = inflater.inflate(R.layout.fragment_affirmation, container, false);

        frameLayout = view.findViewById(R.id.frameLayout);
        affirmationText = view.findViewById(R.id.affirmation_text);
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

        text = bundle.getString("text");
        affirmationText.setText(text);
        Drawable background = getBackgroundByName(bundle.getString("backgroundName"));
        frameLayout.setBackground(background);
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