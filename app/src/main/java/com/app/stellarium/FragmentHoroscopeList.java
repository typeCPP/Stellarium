package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.HoroscopePredictionsByPeriodTable;
import com.app.stellarium.database.tables.HoroscopePredictionsTable;
import com.app.stellarium.database.tables.HoroscopeSignCharacteristicTable;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Horoscope;
import com.google.gson.Gson;
import com.szugyi.circlemenu.view.CircleLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FragmentHoroscopeList extends Fragment {
    private LinearLayout ariesButton, taurusButton, geminiButton, cancerButton, leoButton, virgoButton,
            libraButton, scorpioButton, sagittariusButton, capricornButton, aquariusButton, piscesButton;
    private CircleLayout circleLayout;
    private short touchMoveFactor = 10;
    private PointF actionDownPoint = new PointF(0f, 0f);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        databaseHelper.dropHoroscopeTables(database);
        database.close();
        databaseHelper.close();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_list, container, false);
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        int MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 300.0f + 0.5);

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    actionDownPoint.x = motionEvent.getX();
                    actionDownPoint.y = motionEvent.getY();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    try {
                        Method method = CircleLayout.class.getDeclaredMethod("rotateButtons", float.class);
                        method.setAccessible(true);
                        float p = 0;
                        float deltaY = actionDownPoint.y - motionEvent.getY();
                        if (Math.abs(deltaY) > MIN_DISTANCE) {
                            if (deltaY < 0)
                                p = 3f;
                            else if (deltaY > 0)
                                p = -3f;
                        }
                        method.invoke(circleLayout, p);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    boolean isTouchLength = (Math.abs(motionEvent.getX() - actionDownPoint.x) +
                            Math.abs(motionEvent.getY() - actionDownPoint.y)) < touchMoveFactor;
                    if (isTouchLength) {
                        int drawableId = R.drawable.big_aries;
                        int idOfHoroscopePredictionsByPeriodTableElement = 1;
                        switch (view.getId()) {
                            case R.id.ariesButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 1;
                                drawableId = R.drawable.big_aries;
                                break;
                            case R.id.taurusButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 2;
                                drawableId = R.drawable.big_taurus;
                                break;
                            case R.id.geminiButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 3;
                                drawableId = R.drawable.big_gemini;
                                break;
                            case R.id.cancerButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 4;
                                drawableId = R.drawable.big_cancer;
                                break;
                            case R.id.leoButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 5;
                                drawableId = R.drawable.big_leo;
                                break;
                            case R.id.virgoButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 6;
                                drawableId = R.drawable.big_virgo;
                                break;
                            case R.id.libraButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 7;
                                drawableId = R.drawable.big_libra;
                                break;
                            case R.id.scorpioButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 8;
                                drawableId = R.drawable.big_scorpio;
                                break;
                            case R.id.sagittariusButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 9;
                                drawableId = R.drawable.big_sagittarius;
                                break;
                            case R.id.capricornButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 10;
                                drawableId = R.drawable.big_capricorn;
                                break;
                            case R.id.aquariusButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 11;
                                drawableId = R.drawable.big_aquarius;
                                break;
                            case R.id.piscesButton:
                                idOfHoroscopePredictionsByPeriodTableElement = 12;
                                drawableId = R.drawable.big_pisces;
                                break;
                        }
                        getHoroscopeDataFromServerToLocalTables(idOfHoroscopePredictionsByPeriodTableElement);
                        Bundle bundle = findAndGetHoroscopeSignDataFromDatabase(idOfHoroscopePredictionsByPeriodTableElement);
                        bundle.putInt("signPictureDrawableId", drawableId);
                        bundle.putInt("signId", idOfHoroscopePredictionsByPeriodTableElement);
                        Fragment fragment = new FragmentHoroscopePage();
                        fragment.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                    }
                }
                return true;
            }
        }
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setNumberOfPrevFragment();

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        circleLayout = view.findViewById(R.id.circle_layout);
        circleLayout.setRadius(dpHeight - 20);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ariesButton = view.findViewById(R.id.ariesButton);
        ariesButton.setOnTouchListener(new ButtonOnTouchListener());
        ariesButton.setPadding(20, 20, 20, 20);
        if (dpHeight < 620)
            layoutParams.setMargins((int) (circleLayout.getRadius() * -2 - 190), 0, 0, 0);
        else
            layoutParams.setMargins((int) (circleLayout.getRadius() * -2), 0, 0, 0);
        circleLayout.setLayoutParams(layoutParams);

        taurusButton = view.findViewById(R.id.taurusButton);
        taurusButton.setOnTouchListener(new ButtonOnTouchListener());
        taurusButton.setPadding(20, 20, 20, 20);

        geminiButton = view.findViewById(R.id.geminiButton);
        geminiButton.setOnTouchListener(new ButtonOnTouchListener());
        geminiButton.setPadding(20, 20, 20, 20);

        cancerButton = view.findViewById(R.id.cancerButton);
        cancerButton.setOnTouchListener(new ButtonOnTouchListener());
        cancerButton.setPadding(20, 20, 20, 20);

        leoButton = view.findViewById(R.id.leoButton);
        leoButton.setOnTouchListener(new ButtonOnTouchListener());
        leoButton.setPadding(20, 20, 20, 20);

        virgoButton = view.findViewById(R.id.virgoButton);
        virgoButton.setOnTouchListener(new ButtonOnTouchListener());
        virgoButton.setPadding(20, 20, 20, 20);

        libraButton = view.findViewById(R.id.libraButton);
        libraButton.setOnTouchListener(new ButtonOnTouchListener());
        libraButton.setPadding(20, 20, 20, 20);

        scorpioButton = view.findViewById(R.id.scorpioButton);
        scorpioButton.setOnTouchListener(new ButtonOnTouchListener());
        scorpioButton.setPadding(20, 20, 20, 20);

        sagittariusButton = view.findViewById(R.id.sagittariusButton);
        sagittariusButton.setOnTouchListener(new ButtonOnTouchListener());
        sagittariusButton.setPadding(20, 20, 20, 20);

        capricornButton = view.findViewById(R.id.capricornButton);
        capricornButton.setOnTouchListener(new ButtonOnTouchListener());
        capricornButton.setPadding(20, 20, 20, 20);

        aquariusButton = view.findViewById(R.id.aquariusButton);
        aquariusButton.setOnTouchListener(new ButtonOnTouchListener());
        aquariusButton.setPadding(20, 20, 20, 20);

        piscesButton = view.findViewById(R.id.piscesButton);
        piscesButton.setOnTouchListener(new ButtonOnTouchListener());
        piscesButton.setPadding(20, 20, 20, 20);

        circleLayout.setAngle(getDegreeForCircleLayout());

        return view;
    }

    private void getHoroscopeDataFromServerToLocalTables(int signId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String[] names = {"Овен", "Телец", "Близнецы", "Рак", "Лев", "Дева", "Весы", "Скорпион", "Стрелец", "Козерог", "Водолей", "Рыбы"};
        String[] periods = {"21 марта – 20 апреля", "21 апреля – 21 мая", "22 мая – 21 июня",
                "22 июня – 22 июля", "23 июля – 23 августа", "24 августа – 22 сентября",
                "23 сентября – 22 октября", "23 октября – 22 ноября", "22 ноября – 21 декабря",
                "22 декабря – 20 января", "21 января – 19 февраля", "20 февраля – 20 марта"};
        try {
            ServerConnection serverConnection = new ServerConnection();
            String response;
            response = serverConnection.getStringResponseByParameters("/horoscopes/?sign=" + signId);
            Log.d("HOROSCOPE", String.valueOf(signId));
            Horoscope horoscope = new Gson().fromJson(response, Horoscope.class);
            Log.d("HOROSCOPE", horoscope.toString());
            Long characteristicId = insertSignCharacteristic(database, horoscope);
            insertPredictions(database, horoscope, periods[signId - 1], names[signId - 1], characteristicId);
        } catch (Exception e) {
            database.close();
            databaseHelper.close();
        }
        database.close();
        databaseHelper.close();
    }

    private void insertPredictions(SQLiteDatabase sqLiteDatabase, Horoscope horoscope, String period, String name, Long characteristicId) {
        ContentValues predictionCV = new ContentValues();
        ContentValues periodCV = new ContentValues();
        long rowId = -1;
        predictionCV.put(HoroscopePredictionsTable.COLUMN_COMMON, horoscope.today.common);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_LOVE, horoscope.today.love);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_HEALTH, horoscope.today.health);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_BUSINESS, horoscope.today.business);

        rowId = sqLiteDatabase.insert(HoroscopePredictionsTable.TABLE_NAME, null, predictionCV);
        periodCV.put(HoroscopePredictionsByPeriodTable.COLUMN_TODAY_PREDICTION_ID, rowId);

        predictionCV.put(HoroscopePredictionsTable.COLUMN_COMMON, horoscope.tomorrow.common);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_LOVE, horoscope.tomorrow.love);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_HEALTH, horoscope.tomorrow.health);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_BUSINESS, horoscope.tomorrow.business);

        rowId = sqLiteDatabase.insert(HoroscopePredictionsTable.TABLE_NAME, null, predictionCV);
        periodCV.put(HoroscopePredictionsByPeriodTable.COLUMN_TOMORROW_PREDICTION_ID, rowId);


        predictionCV.put(HoroscopePredictionsTable.COLUMN_COMMON, horoscope.week.common);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_LOVE, horoscope.week.love);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_HEALTH, horoscope.week.health);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_BUSINESS, horoscope.week.business);

        rowId = sqLiteDatabase.insert(HoroscopePredictionsTable.TABLE_NAME, null, predictionCV);
        periodCV.put(HoroscopePredictionsByPeriodTable.COLUMN_WEEK_PREDICTION_ID, rowId);

        predictionCV.put(HoroscopePredictionsTable.COLUMN_COMMON, horoscope.month.common);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_LOVE, horoscope.month.love);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_HEALTH, horoscope.month.health);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_BUSINESS, horoscope.month.business);

        rowId = sqLiteDatabase.insert(HoroscopePredictionsTable.TABLE_NAME, null, predictionCV);
        periodCV.put(HoroscopePredictionsByPeriodTable.COLUMN_MONTH_PREDICTION_ID, rowId);

        predictionCV.put(HoroscopePredictionsTable.COLUMN_COMMON, horoscope.year.common);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_LOVE, horoscope.year.love);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_HEALTH, horoscope.year.health);
        predictionCV.put(HoroscopePredictionsTable.COLUMN_BUSINESS, horoscope.year.business);

        rowId = sqLiteDatabase.insert(HoroscopePredictionsTable.TABLE_NAME, null, predictionCV);
        periodCV.put(HoroscopePredictionsByPeriodTable.COLUMN_YEAR_PREDICTION_ID, rowId);

        periodCV.put(HoroscopePredictionsByPeriodTable.COLUMN_SIGN_NAME, name);
        periodCV.put(HoroscopePredictionsByPeriodTable.COLUMN_PERIOD_SIGN, period);
        periodCV.put(HoroscopePredictionsByPeriodTable.COLUMN_CHARACTERISTIC_ID, characteristicId);
        periodCV.put(HoroscopePredictionsByPeriodTable.COLUMN_ID, horoscope.info.id);

        sqLiteDatabase.insert(HoroscopePredictionsByPeriodTable.TABLE_NAME, null, periodCV);
    }

    private long insertSignCharacteristic(SQLiteDatabase sqLiteDatabase, Horoscope horoscope) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HoroscopeSignCharacteristicTable.COLUMN_ID, horoscope.info.id);
        contentValues.put(HoroscopeSignCharacteristicTable.COLUMN_DESCRIPTION, horoscope.character.description);
        contentValues.put(HoroscopeSignCharacteristicTable.COLUMN_CHARACTER, horoscope.character.charact);
        contentValues.put(HoroscopeSignCharacteristicTable.COLUMN_LOVE, horoscope.character.love);
        contentValues.put(HoroscopeSignCharacteristicTable.COLUMN_CAREER, horoscope.character.career);
        return sqLiteDatabase.insert(HoroscopeSignCharacteristicTable.TABLE_NAME, null, contentValues);
    }

    @SuppressLint("Range")
    private Bundle findAndGetHoroscopeSignDataFromDatabase(int signId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        @SuppressLint("Recycle") Cursor periodTableCursor = database.query(HoroscopePredictionsByPeriodTable.TABLE_NAME, null,
                "_id = " + signId,
                null, null, null, null);
        periodTableCursor.moveToFirst();

        String[][] predictions = new String[5][4];
        int[] predictionIds = new int[5];
        String signName = periodTableCursor.getString(periodTableCursor.getColumnIndex(HoroscopePredictionsByPeriodTable.COLUMN_SIGN_NAME));
        String signPeriod = periodTableCursor.getString(periodTableCursor.getColumnIndex(HoroscopePredictionsByPeriodTable.COLUMN_PERIOD_SIGN));
        predictionIds[0] = periodTableCursor.getInt(periodTableCursor.
                getColumnIndex(HoroscopePredictionsByPeriodTable.COLUMN_TODAY_PREDICTION_ID));
        predictionIds[1] = periodTableCursor.getInt(periodTableCursor.
                getColumnIndex(HoroscopePredictionsByPeriodTable.COLUMN_TOMORROW_PREDICTION_ID));
        predictionIds[2] = periodTableCursor.getInt(periodTableCursor.
                getColumnIndex(HoroscopePredictionsByPeriodTable.COLUMN_WEEK_PREDICTION_ID));
        predictionIds[3] = periodTableCursor.getInt(periodTableCursor.
                getColumnIndex(HoroscopePredictionsByPeriodTable.COLUMN_MONTH_PREDICTION_ID));
        predictionIds[4] = periodTableCursor.getInt(periodTableCursor.
                getColumnIndex(HoroscopePredictionsByPeriodTable.COLUMN_YEAR_PREDICTION_ID));

        for (int i = 0; i < 5; i++) {
            Cursor predictionsCursor = database.query(HoroscopePredictionsTable.TABLE_NAME, null,
                    "_id = " + predictionIds[i],
                    null, null, null, null);
            predictionsCursor.moveToFirst();
            predictions[i][0] = predictionsCursor.getString(predictionsCursor.getColumnIndex(HoroscopePredictionsTable.COLUMN_COMMON));
            predictions[i][1] = predictionsCursor.getString(predictionsCursor.getColumnIndex(HoroscopePredictionsTable.COLUMN_LOVE));
            predictions[i][2] = predictionsCursor.getString(predictionsCursor.getColumnIndex(HoroscopePredictionsTable.COLUMN_HEALTH));
            predictions[i][3] = predictionsCursor.getString(predictionsCursor.getColumnIndex(HoroscopePredictionsTable.COLUMN_BUSINESS));

        }

        Bundle bundle = new Bundle();
        bundle.putString("signName", signName);
        bundle.putString("signPeriod", signPeriod);
        bundle.putStringArray("todayPredictions", predictions[0]);
        bundle.putStringArray("tomorrowPredictions", predictions[1]);
        bundle.putStringArray("weekPredictions", predictions[2]);
        bundle.putStringArray("monthPredictions", predictions[3]);
        bundle.putStringArray("yearPredictions", predictions[4]);

        return bundle;
    }

    int getDegreeForCircleLayout() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor userTableCursor = database.query(UserTable.TABLE_NAME, null,
                null, null, null, null, null);
        userTableCursor.moveToFirst();
        @SuppressLint("Range")
        Integer sign = userTableCursor.getInt(userTableCursor.getColumnIndex(UserTable.COLUMN_HOROSCOPE_SIGN_ID));
        return sign * -30;
    }
}