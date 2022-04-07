package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.dialog.LoadingDialog;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Affirmation;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Random;
import java.util.function.UnaryOperator;


public class FragmentAffirmation extends Fragment {

    private FrameLayout frameLayout;
    private TextView affirmationText;
    private LoadingDialog loadingDialog;

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
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setNumberOfPrevFragment();
            activity.hideBottomBar(true);
        }
        View view = inflater.inflate(R.layout.fragment_affirmation, container, false);

        frameLayout = view.findViewById(R.id.frameLayout);
        affirmationText = view.findViewById(R.id.affirmation_text);
        loadingDialog = new LoadingDialog(view.getContext());
        loadingDialog.setOnClick(new UnaryOperator<Void>() {
            @Override
            public Void apply(Void unused) {
                workWithBackgroundAndText();
                return null;
            }
        });

        workWithBackgroundAndText();

        return view;
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
                        Pair<String, Integer> pair = getTodayBackgroundAndText(database, todayDate);
                        if (pair != null) {
                            Drawable background = getBackgroundByID(pair.second);
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
    private Pair<String, Integer> getTodayBackgroundAndText(SQLiteDatabase database, String todayDate) {
        Cursor affirmationsCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        if (affirmationsCursor.getCount() < 1) {
            return null;
        }
        affirmationsCursor.moveToLast();
        String text = affirmationsCursor.getString(affirmationsCursor.getColumnIndex(AffirmationsTable.COLUMN_TEXT));
        int backgroundID = affirmationsCursor.getInt(affirmationsCursor.getColumnIndex(AffirmationsTable.COLUMN_PICTURE));
        return new Pair<>(text, backgroundID);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getBackgroundByID(int backgroundID) {
        Resources resources = getContext().getResources();
        return resources.getDrawable(backgroundID);
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
        Affirmation affirmation = new Gson().fromJson(response, Affirmation.class);
        if (affirmation == null) {
            throw new NullPointerException();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(AffirmationsTable.COLUMN_DATE, todayDate);
        contentValues.put(AffirmationsTable.COLUMN_TEXT, affirmation.text);
        contentValues.put(AffirmationsTable.COLUMN_ID, affirmation.id);
        contentValues.put(AffirmationsTable.COLUMN_PICTURE, getRandomAffirmationBackgroundName());

        database.insert(AffirmationsTable.TABLE_NAME, null, contentValues);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private int getRandomAffirmationBackgroundName() {
        @SuppressLint("Recycle") TypedArray imgs = getResources().obtainTypedArray(R.array.apptour);
        Random rand = new Random();
        int rndInt = rand.nextInt(imgs.length());
        int resID = imgs.getResourceId(rndInt, 0);
        Resources resources = getContext().getResources();
        Log.d("DRAWABLE", resources.getDrawable(resID).toString());
        return resID;
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity activity = (MainActivity) getActivity();

        if (activity != null) {
            activity.hideBottomBar(false);
        }
    }
}