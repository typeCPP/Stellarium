package com.app.stellarium;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
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

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.dialog.LoadingDialog;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.PythagoreanSquare;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.function.UnaryOperator;

public class FragmentPythagoreanSquareDateSelection extends Fragment {

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
    private Animation scaleUp;
    private LoadingDialog loadingDialog;

    public static FragmentPythagoreanSquareDateSelection newInstance(String param1, String param2) {
        FragmentPythagoreanSquareDateSelection fragment = new FragmentPythagoreanSquareDateSelection();
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
        View view = inflater.inflate(R.layout.fragment_pythagorean_square_date_selection, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setNumberOfPrevFragment();

        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        isSetDate = false;
        editTextDate = view.findViewById(R.id.pythagorean_date_date_selection);
        layoutDate = view.findViewById(R.id.pythagorean_square_date_layout_1);
        nextButton = view.findViewById(R.id.nextPythSquareButton);
        nextButton.setAlpha(0f);
        nextButton.setVisibility(View.INVISIBLE);
        bundle = new Bundle();

        loadingDialog = new LoadingDialog(view.getContext());
        loadingDialog.setOnClick(new UnaryOperator<Void>() {
            @Override
            public Void apply(Void unused) {
                workWithServer();
                return null;
            }
        });

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                    workWithServer();
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
                        //  Bundle bundle = new Bundle();
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

    private void workWithServer() {
        Fragment fragmentHomePage = new FragmentPythagoreanSquareHomePage();
        fragmentHomePage.setArguments(bundle);
        loadingDialog.show();
        loadingDialog.startGifAnimation();
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Pair<int[], String[]> data = getDataFromServer(birthdayDay, birthdayMonth, birthdayYear);
                int[] matrixValues = data.first;
                String[] texts = data.second;
                if (matrixValues == null || texts == null || matrixValues.length == 0 || texts.length == 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.stopGifAnimation();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            bundle.putIntArray("matrixValues", matrixValues);
                            bundle.putStringArray("texts", texts);
                            loadingDialog.dismiss();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragmentHomePage).commit();
                        }
                    });
                }
            }
        }).start();
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

    private Pair<int[], String[]> getDataFromServer(int birthdayDay, int birthdayMonth, int birthdayYear) {
        ServerConnection serverConnection = new ServerConnection();
        String response = serverConnection.getStringResponseByParameters("pifagorSquare/?day=" + birthdayDay +
                "&month=" + birthdayMonth + "&year=" + birthdayYear);
        PythagoreanSquare pythagoreanSquare = new Gson().fromJson(response, PythagoreanSquare.class);
        if (pythagoreanSquare == null) {
            return new Pair<>(null, null);
        }
        int[] counts = {pythagoreanSquare.one.count, pythagoreanSquare.two.count, pythagoreanSquare.three.count,
                pythagoreanSquare.four.count, pythagoreanSquare.five.count, pythagoreanSquare.six.count,
                pythagoreanSquare.seven.count, pythagoreanSquare.eight.count, pythagoreanSquare.nine.count};

        String[] texts = {pythagoreanSquare.one.text, pythagoreanSquare.two.text, pythagoreanSquare.three.text,
                pythagoreanSquare.four.text, pythagoreanSquare.five.text, pythagoreanSquare.six.text,
                pythagoreanSquare.seven.text, pythagoreanSquare.eight.text, pythagoreanSquare.nine.text};

        return new Pair<>(counts, texts);
    }

    private void getUserBirthday(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        userCursor.moveToLast();
        @SuppressLint("Range") String birthdayString = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_DATE_OF_BIRTH));
        birthdayString = birthdayString.replaceAll("\\.", "/");
        String[] temp = birthdayString.split("/", 3);
        birthdayDay = Integer.parseInt(temp[0]);
        birthdayMonth = Integer.parseInt(temp[1]);
        birthdayYear = Integer.parseInt(temp[2]);
    }
}
