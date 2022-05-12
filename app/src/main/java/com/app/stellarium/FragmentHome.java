package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.utils.jsonmodels.MoonCalendar;
import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.AffirmationsTable;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.dialog.LoadingDialog;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Affirmation;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.function.UnaryOperator;

public class FragmentHome extends Fragment {

    private TextView titleText;
    private Button affirmationButton, horoscopeButton, taroButton, compatibilityButton,
            moonCalendarButton, numerologyButton, squareOfPythagorasButton, yesOrNoButton;
    private Animation scaleUp;
    private LoadingDialog loadingDialog;
    private boolean readyToGoNextAffirmation = true, readyToGiveTextOfMoonCalendar = true;

    public FragmentHome() {
    }

    public static FragmentHome newInstance() {
        FragmentHome fragment = new FragmentHome();
        return fragment;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"ResourceType", "ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        loadingDialog = new LoadingDialog(view.getContext());
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        String name = getUserName(databaseHelper.getReadableDatabase());
        titleText = view.findViewById(R.id.title_text);
        titleText.setText(getHelloStringByCurrentTime() + ", " + name);
        class ButtonOnTouchListener implements View.OnTouchListener {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                    Fragment fragment;
                    switch (view.getId()) {
                        case R.id.affirmationButton:
                            loadingDialog.setOnClick(new UnaryOperator<Void>() {
                                @Override
                                public Void apply(Void unused) {
                                    workWithBackgroundAndTextForAffirmation();
                                    return null;
                                }
                            });
                            workWithBackgroundAndTextForAffirmation();
                            break;
                        case R.id.horoscopeButton:
                            fragment = new FragmentHoroscopeList();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                            break;
                        case R.id.taroButton:
                            fragment = new FragmentTaroCards();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                            break;
                        case R.id.compatibilityButton:
                            fragment = new FragmentCompatibilityMenu();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                            break;
                        case R.id.moonCalendarButton:
                            loadingDialog.setOnClick(new UnaryOperator<Void>() {
                                @Override
                                public Void apply(Void unused) {
                                    workWithTextForMoonCalendar();
                                    return null;
                                }
                            });
                            workWithTextForMoonCalendar();
                            break;
                        case R.id.numerologicButton:
                            fragment = new FragmentNumerologyDateSelection();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                            break;
                        case R.id.squareOfPythagorasButton:
                            fragment = new FragmentPythagoreanSquareDateSelection();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                            break;
                        case R.id.yesOrNoButton:
                            fragment = new FragmentYesOrNo();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                            break;
                    }

                }
                return true;
            }
        }

        affirmationButton = view.findViewById(R.id.affirmationButton);
        affirmationButton.setOnTouchListener(new ButtonOnTouchListener());

        horoscopeButton = view.findViewById(R.id.horoscopeButton);
        horoscopeButton.setOnTouchListener(new ButtonOnTouchListener());

        taroButton = view.findViewById(R.id.taroButton);
        taroButton.setOnTouchListener(new ButtonOnTouchListener());

        compatibilityButton = view.findViewById(R.id.compatibilityButton);
        compatibilityButton.setOnTouchListener(new ButtonOnTouchListener());

        moonCalendarButton = view.findViewById(R.id.moonCalendarButton);
        moonCalendarButton.setOnTouchListener(new ButtonOnTouchListener());

        numerologyButton = view.findViewById(R.id.numerologicButton);
        numerologyButton.setOnTouchListener(new ButtonOnTouchListener());

        squareOfPythagorasButton = view.findViewById(R.id.squareOfPythagorasButton);
        squareOfPythagorasButton.setOnTouchListener(new ButtonOnTouchListener());

        yesOrNoButton = view.findViewById(R.id.yesOrNoButton);
        yesOrNoButton.setOnTouchListener(new ButtonOnTouchListener());

        return view;
    }

    @SuppressLint("Range")
    private String getUserName(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToLast();
            return userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_NAME));
        } else {
            return "друг";
        }
    }

    private String getHelloStringByCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 12 && hour <= 17) {
            return "Добрый день";
        } else if (hour >= 18 && hour <= 23) {
            return "Добрый вечер";
        } else if (hour == 0 || hour <= 5) {
            return "Доброй ночи";
        } else if (hour >= 6 && hour <= 11) {
            return "Доброе утро";
        } else {
            return "Добрый день";
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setNumberOfPrevFragment(2);
        }
    }

    private void workWithBackgroundAndTextForAffirmation() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        String todayDate = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH)) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        Handler handler = new Handler();
        readyToGoNextAffirmation = true;
        loadingDialog.show();
        loadingDialog.startGifAnimation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkDatabaseForTodayAffirmation(database, todayDate)) {
                    try {
                        getAffirmationFromServerToDatabase(database, todayDate);
                    } catch (Exception e) {
                        database.close();
                        databaseHelper.close();
                        readyToGoNextAffirmation = false;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.stopGifAnimation();
                            }
                        });
                    }
                }
                if (readyToGoNextAffirmation) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Pair<String, String> pair = getTodayAffirmationTextAndBackground(database, todayDate);
                            if (pair != null) {
                                Bundle bundle = new Bundle();
                                bundle.putString("text", pair.first);
                                bundle.putString("backgroundName", pair.second);
                                Fragment fragment = new FragmentAffirmation();
                                fragment.setArguments(bundle);
                                getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                        .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                                database.close();
                                databaseHelper.close();
                                loadingDialog.dismiss();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private void workWithTextForMoonCalendar() {
        Handler handler = new Handler();
        try {
            loadingDialog.show();
            loadingDialog.startGifAnimation();
            Calendar calendar = Calendar.getInstance();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerConnection serverConnection = new ServerConnection();
                    int date = (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH);
                    String response = serverConnection.getStringResponseByParameters("moonCalendar/?date=" + date);
                    MoonCalendar moonCalendar = new Gson().fromJson(response, MoonCalendar.class);
                    if (moonCalendar == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                readyToGiveTextOfMoonCalendar = false;
                                loadingDialog.stopGifAnimation();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                readyToGiveTextOfMoonCalendar = true;
                                Bundle bundle = new Bundle();
                                bundle.putString("phase", moonCalendar.phase);
                                bundle.putString("characteristics", moonCalendar.characteristics);
                                bundle.putString("health", moonCalendar.health);
                                bundle.putString("relations", moonCalendar.relations);
                                bundle.putString("business", moonCalendar.business);
                                bundle.putInt("dayOfMonth", calendar.get(Calendar.DAY_OF_MONTH));
                                bundle.putInt("month", calendar.get(Calendar.MONTH));
                                Fragment fragment = new FragmentMoonCalendar();
                                fragment.setArguments(bundle);
                                getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                        .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                                loadingDialog.dismiss();
                            }
                        });
                    }
                }
            }).start();
        } catch (Exception e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.stopGifAnimation();
                }
            });
        }
    }

    private boolean checkDatabaseForTodayAffirmation(SQLiteDatabase database, String todayDate) {
        @SuppressLint("Recycle") Cursor affirmationsCursor = database.query(AffirmationsTable.TABLE_NAME, null,
                AffirmationsTable.COLUMN_DATE + " = " + todayDate,
                null, null, null, null);
        return affirmationsCursor.getCount() > 0;
    }

    @SuppressLint("Range")
    private Pair<String, String> getTodayAffirmationTextAndBackground(SQLiteDatabase database, String todayDate) {
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

    @SuppressLint("Range")
    private void getAffirmationFromServerToDatabase(SQLiteDatabase database, String todayDate) {
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
        contentValues.put(AffirmationsTable.COLUMN_PICTURE, affirmation.picture);

        database.insert(AffirmationsTable.TABLE_NAME, null, contentValues);
    }
}