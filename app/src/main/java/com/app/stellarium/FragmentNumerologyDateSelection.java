package com.app.stellarium;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.stellarium.database.tables.NumerologyTable;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.dialog.LoadingDialog;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Numerology;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

import pl.droidsonroids.gif.GifDrawable;

public class FragmentNumerologyDateSelection extends Fragment {

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private int birthdayDay;
    private int birthdayMonth;
    private int birthdayYear;
    private TextView editTextDate;
    private Button nextButton;
    private Bundle bundle;
    private LinearLayout layoutDate;
    private boolean isSetDate;
    private LoadingDialog loadingDialog;
    Animation scaleUp;

    public static FragmentPythagoreanSquareDateSelection newInstance(String param1, String param2) {
        FragmentPythagoreanSquareDateSelection fragment = new FragmentPythagoreanSquareDateSelection();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_numerology_date_selection, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setNumberOfPrevFragment();
        loadingDialog = new LoadingDialog(requireContext());

        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        isSetDate = false;
        editTextDate = view.findViewById(R.id.numerologic_date_selection);
        layoutDate = view.findViewById(R.id.numerologic_date_layout_1);
        nextButton = view.findViewById(R.id.nextNumerologicButton);
        LayoutInflater layoutInflater = getLayoutInflater();
        nextButton.setAlpha(0f);
        nextButton.setVisibility(View.INVISIBLE);
        bundle = new Bundle();

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                    int numerologyNumber = calculateNumber(birthdayDay, birthdayMonth, birthdayYear);
                    loadingDialog.show();
                    loadingDialog.startGifAnimation();
                    loadingDialog.stopGifAnimation();
                    loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                    Handler handler = new Handler();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getServerResponseToDatabase(numerologyNumber);
                            try {
                                Thread.sleep(50000);
                            } catch (InterruptedException ignored) {

                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Fragment fragment = new FragmentNumerology();
                                    bundle.putInt("numerologyNumber", numerologyNumber);
                                    fragment.setArguments(bundle);
//                                    loadingDialog.dismiss();
                                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                            .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                                }
                            });
                        }
                    }).start();
                }
                return true;
            }
        }
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int mothOfYear, int dayOfMonth) {
                        birthdayDay = dayOfMonth;
                        birthdayMonth = mothOfYear + 1;
                        birthdayYear = year;
                        addTextToTextView(birthdayDay, birthdayMonth, birthdayYear);
                        if (!isSetDate) {
                            nextButton.setVisibility(View.VISIBLE);
                            nextButton.animate().alpha(1f).setDuration(500).setListener(null);
                            isSetDate = true;
                        }
                    }
                };
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                createDatePickerDialog(day, month, year, calendar);

            }
        });
        nextButton.setOnTouchListener(new ButtonOnTouchListener());
        return view;
    }

    private void getServerResponseToDatabase(int number) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        databaseHelper.dropNumerology(database);
        try {
            ServerConnection serverConnection = new ServerConnection();
            Log.d("NUMEROLOGY", String.valueOf(number));
            String response = serverConnection.getStringResponseByParameters("numerology/?num=" + number);
            Numerology numerology = new Gson().fromJson(response, Numerology.class);
            insertNumerology(database, numerology);
        } catch (Exception e) {
            e.printStackTrace();
            database.close();
            databaseHelper.close();
        }
        database.close();
        databaseHelper.close();
    }

    private void insertNumerology(SQLiteDatabase database, Numerology numerology) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(NumerologyTable.COLUMN_NUMBER, numerology.number);
        contentValues.put(NumerologyTable.COLUMN_GENERAL, numerology.general);
        contentValues.put(NumerologyTable.COLUMN_DIGNITIES, numerology.dignities);
        contentValues.put(NumerologyTable.COLUMN_DISADVANTAGES, numerology.disadvantages);
        contentValues.put(NumerologyTable.COLUMN_FATE, numerology.fate);

        database.insert(NumerologyTable.TABLE_NAME, null, contentValues);
    }

    @SuppressLint("ResourceAsColor")
    private void createDatePickerDialog(int day, int month, int year, Calendar calendar) {
        datePickerDialog = new DatePickerDialog(getContext(), R.style.CustomDatePickerDialog, dateSetListener, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "моя дата рождения", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                getUserBirthday(databaseHelper.getReadableDatabase());
                addTextToTextView(birthdayDay, birthdayMonth, birthdayYear);
                if (!isSetDate) {
                    nextButton.setVisibility(View.VISIBLE);
                    nextButton.animate().alpha(1f).setDuration(500).setListener(null);
                    isSetDate = true;
                }
                datePickerDialog.dismiss();
            }
        });

        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(R.color.button_registration_bottom_text);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setText("ок");
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.button_registration_bottom_text);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setText("назад");
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.button_registration_bottom_text);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        calendar.set(Calendar.YEAR, 1900);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
    }

    @SuppressLint("SetTextI18n")
    private void addTextToTextView(int birthdayDay, int birthdayMonth, int birthdayYear) {
        StringBuffer text = new StringBuffer(birthdayDay + "/" + birthdayMonth + "/" + birthdayYear);
        if (birthdayDay < 10) {
            text.insert(0, 0);
        }
        if (birthdayMonth < 10) {
            text.insert(3, 0);
        }
        editTextDate.setText(text.toString());
        bundle.putString("Date", editTextDate.getText().toString());
    }

    private int calculateNumber(int birthdayDay, int birthdayMonth, int birthdayYear) {
        //example date: 12.07.2002
        int result = birthdayDay % 10 + birthdayDay / 10 + birthdayMonth % 10 + birthdayMonth / 10
                + birthdayYear % 10 + (birthdayYear / 10) % 10 + (birthdayYear / 100) % 10 + birthdayYear / 1000;
        while (result > 9) {
            result = result % 10 + result / 10;
        }
        return result;
    }

    private void getUserBirthday(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToLast();
            @SuppressLint("Range") String birthdayString = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_DATE_OF_BIRTH));
            String[] temp = birthdayString.split("/", 3);
            birthdayDay = Integer.parseInt(temp[0]);
            birthdayMonth = Integer.parseInt(temp[1]);
            birthdayYear = Integer.parseInt(temp[2]);
        } else {
            birthdayDay = 18;
            birthdayMonth = 7;
            birthdayYear = 2002;
        }
    }
}
