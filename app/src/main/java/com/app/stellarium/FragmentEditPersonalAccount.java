package com.app.stellarium;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.utils.jsonmodels.User;

import java.util.Calendar;

public class FragmentEditPersonalAccount extends Fragment {

    private ImageView crossEditDate, crossEditName;
    private TextView editTextName, editTextDate;
    private DatePickerDialog datePickerDialog;
    private int birthdayDay;
    private int birthdayMonth;
    private int birthdayYear;
    private int userId;
    private boolean isSelected = false;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private RadioButton radioButtonMan, radioButtonWoman;
    private Button saveButton;
    private RadioGroup radioGroup;

    public FragmentEditPersonalAccount() {
        // Required empty public constructor
    }

    public static FragmentEditPersonalAccount newInstance(String param1, String param2) {
        FragmentEditPersonalAccount fragment = new FragmentEditPersonalAccount();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_personal_account, container, false);
        saveButton = view.findViewById(R.id.save_button);
        crossEditDate = view.findViewById(R.id.cross_edit_date);
        crossEditName = view.findViewById(R.id.cross_edit_name);
        editTextName = view.findViewById(R.id.edit_account_name);
        editTextDate = view.findViewById(R.id.edit_account_date);
        radioButtonMan = view.findViewById(R.id.radio_button_man);
        radioButtonWoman = view.findViewById(R.id.radio_button_woman);
        radioGroup = view.findViewById(R.id.radioGroup);
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        getUserData(databaseHelper.getReadableDatabase());

        radioButtonMan.setOnClickListener(radioButtonClickListener);
        radioButtonWoman.setOnClickListener(radioButtonClickListener);
        crossEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextDate.setText("");
            }
        });
        crossEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setText("");
            }
        });
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
                        StringBuffer text = new StringBuffer(birthdayDay + "/" + birthdayMonth + "/" + birthdayYear);
                        if (birthdayDay < 10) {
                            text.insert(0, 0);
                        }
                        if (birthdayMonth < 10) {
                            text.insert(3, 0);
                        }
                        editTextDate.setTextColor(getResources().getColor(R.color.white));
                        editTextDate.setText(text.toString());
                        isSelected = true;
                    }
                };

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                if (!isSelected) {
                    createDatePickerDialog(day, month, year, calendar);
                } else {
                    createDatePickerDialog(birthdayDay, birthdayMonth, birthdayYear, calendar);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                if (!editTextName.getText().toString().isEmpty() && !editTextDate.getText().toString().isEmpty()) {
                    contentValues.put(UserTable.COLUMN_SEX, radioButtonMan.isChecked());
                    contentValues.put(UserTable.COLUMN_NAME, editTextName.getText().toString());
                    contentValues.put(UserTable.COLUMN_DATE_OF_BIRTH, editTextDate.getText().toString());
                    database.update(UserTable.TABLE_NAME, contentValues, "_ID=" + userId, null);
                    database.close();
                } else {
                    Toast.makeText(getContext(), "Заполните все поля.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RadioButton radioButton = (RadioButton) view;
            switch (radioButton.getId()) {
                case R.id.radio_button_man:
                    break;
                case R.id.radio_button_woman:
                    break;
            }
        }
    };

    @SuppressLint("ResourceAsColor")
    private void createDatePickerDialog(int day, int month, int year, Calendar calendar) {
        datePickerDialog = new DatePickerDialog(getContext(), R.style.CustomDatePickerDialog, dateSetListener, year, month, day);
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.button_registration_bottom_text);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.button_registration_bottom_text);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        calendar.set(Calendar.YEAR, 1900);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
    }

    @SuppressLint("Range")
    private void getUserData(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToLast();
            String birthdayString = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_DATE_OF_BIRTH));
            String name = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_NAME));
            int sex = userCursor.getInt(userCursor.getColumnIndex(UserTable.COLUMN_SEX));
            userId = userCursor.getInt(userCursor.getColumnIndex(UserTable.COLUMN_ID));
            switch (sex) {
                case 0:
                    radioButtonWoman.setChecked(true);
                    break;
                case 1:
                    radioButtonMan.setChecked(true);
                    break;
            }
            editTextName.setText(name);
            editTextDate.setText(birthdayString);
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