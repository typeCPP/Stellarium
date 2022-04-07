package com.app.stellarium;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.filters.PasswordFilter;
import com.app.stellarium.filters.UsernameFilter;
import com.app.stellarium.utils.ZodiacSignUtils;

import java.util.Calendar;

public class FragmentEditPersonalAccount extends Fragment {

    private ImageView crossEditDate, crossEditName, signImage, eyeEditPassword;
    private TextView editTextDate, signText;
    private EditText editTextName, editTextPassword;
    private DatePickerDialog datePickerDialog;
    private int birthdayDay;
    private int birthdayMonth;
    private int birthdayYear;
    private int userId, signId;
    private boolean isSelected = false;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private RadioButton radioButtonMan, radioButtonWoman;
    private Button saveButton;
    private RadioGroup radioGroup;
    private boolean isShow = false;
    private UsernameFilter usernameFilter = new UsernameFilter();
    private PasswordFilter passwordFilter = new PasswordFilter();
    private View lastView;
    private LinearLayout layoutPassword;


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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_personal_account, container, false);
        saveButton = view.findViewById(R.id.save_button);
        crossEditDate = view.findViewById(R.id.cross_edit_date);
        crossEditName = view.findViewById(R.id.cross_edit_name);
        editTextName = view.findViewById(R.id.edit_account_name);
        editTextDate = view.findViewById(R.id.edit_account_date);
        radioButtonMan = view.findViewById(R.id.radio_button_man);
        radioButtonWoman = view.findViewById(R.id.radio_button_woman);
        radioGroup = view.findViewById(R.id.radioGroup);
        signImage = view.findViewById(R.id.sign_edit_personal_account);
        signText = view.findViewById(R.id.sign_text_edit_personal_account);
        eyeEditPassword = view.findViewById(R.id.eye_password);
        editTextPassword = view.findViewById(R.id.edit_account_password);
        editTextPassword.setLetterSpacing(0.2f);
        layoutPassword = view.findViewById(R.id.edit_account_layout_password);
        lastView = view.findViewById(R.id.lastView);
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        getUserData(databaseHelper.getReadableDatabase());
        setSignImageAndText(signId);
        crossEditDate.setOnClickListener(crossClickListener);
        crossEditName.setOnClickListener(crossClickListener);
        editTextName.setFilters(new InputFilter[]{usernameFilter});
        editTextPassword.setFilters(new InputFilter[]{passwordFilter});
        eyeEditPassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (isShow) {
                    eyeEditPassword.setImageResource(R.drawable.ic_eye_hide);
                    editTextPassword.setLetterSpacing(0.2f);
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editTextPassword.setSelection(editTextPassword.length());
                    isShow = false;
                } else {
                    eyeEditPassword.setImageResource(R.drawable.ic_eye_show);
                    editTextPassword.setLetterSpacing(0f);
                    editTextPassword.setTransformationMethod(null);
                    editTextPassword.setSelection(editTextPassword.length());
                    isShow = true;
                }
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
                        signId = ZodiacSignUtils.getUserSignID(text.toString());
                        setSignImageAndText(signId);
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
                if (!editTextName.getText().toString().isEmpty() && !editTextDate.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty()) {
                    contentValues.put(UserTable.COLUMN_SEX, radioButtonMan.isChecked());
                    contentValues.put(UserTable.COLUMN_NAME, editTextName.getText().toString());
                    contentValues.put(UserTable.COLUMN_DATE_OF_BIRTH, editTextDate.getText().toString());
                    contentValues.put(UserTable.COLUMN_HOROSCOPE_SIGN_ID, signId);
                    contentValues.put(UserTable.COLUMN_PASSWORD, editTextPassword.getText().toString());
                    database.update(UserTable.TABLE_NAME, contentValues, "_ID=" + userId, null);
                    database.close();
                    Fragment fragment = new FragmentPersonalAccount();
                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right,R.animator.slide_in_left, R.animator.slide_out_right)
                            .replace(R.id.frameLayout, fragment).commit();
                } else {
                    Toast.makeText(getContext(), "Заполните все поля.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    View.OnClickListener crossClickListener = new View.OnClickListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cross_edit_date:
                    editTextDate.setText("");
                    break;
                case R.id.cross_edit_name:
                    editTextName.setText("");
                    break;
            }
        }
    };

    @SuppressLint("ResourceAsColor")
    private void createDatePickerDialog(int day, int month, int year, Calendar calendar) {
        signImage.animate().alpha(0f).setDuration(500).setListener(null);
        signText.animate().alpha(0f).setDuration(500).setListener(null);
        datePickerDialog = new DatePickerDialog(getContext(), R.style.CustomDatePickerDialog, dateSetListener, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                signImage.animate().alpha(1f).setDuration(500).setListener(null);
                signText.animate().alpha(1f).setDuration(500).setListener(null);
            }
        });
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.button_registration_bottom_text);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.button_registration_bottom_text);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        calendar.set(Calendar.YEAR, 1900);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Range")
    private void getUserData(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        if (userCursor.getCount() > 0) {
            userCursor.moveToLast();
            String birthdayString = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_DATE_OF_BIRTH));
            String name = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_NAME));
            String email = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_EMAIL));
            String password = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_PASSWORD));
            if (email != null && password != null) {
                editTextPassword.setText(password);
            } else {
                hidePasswordField();
            }
            int sex = userCursor.getInt(userCursor.getColumnIndex(UserTable.COLUMN_SEX));
            userId = userCursor.getInt(userCursor.getColumnIndex(UserTable.COLUMN_ID));
            signId = userCursor.getInt(userCursor.getColumnIndex(UserTable.COLUMN_HOROSCOPE_SIGN_ID));
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

    private void hidePasswordField() {
        layoutPassword.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lastView.getLayoutParams();
        params.weight = 1.5f;
        lastView.setLayoutParams(params);
    }

    private void setSignImageAndText(int signId) {
        if (signId != 0) {
            switch (signId) {
                case 1:
                    signText.setText("Овен");
                    signImage.setImageResource(R.drawable.big_aries);
                    break;
                case 2:
                    signText.setText("Телец");
                    signImage.setImageResource(R.drawable.big_taurus);
                    break;
                case 3:
                    signText.setText("Близнецы");
                    signImage.setImageResource(R.drawable.big_gemini);
                    break;
                case 4:
                    signText.setText("Рак");
                    signImage.setImageResource(R.drawable.big_cancer);
                    break;
                case 5:
                    signText.setText("Лев");
                    signImage.setImageResource(R.drawable.big_leo);
                    break;
                case 6:
                    signText.setText("Дева");
                    signImage.setImageResource(R.drawable.big_virgo);
                    break;
                case 7:
                    signText.setText("Весы");
                    signImage.setImageResource(R.drawable.big_libra);
                    break;
                case 8:
                    signText.setText("Скорпион");
                    signImage.setImageResource(R.drawable.big_scorpio);
                    break;
                case 9:
                    signText.setText("Стрелец");
                    signImage.setImageResource(R.drawable.big_sagittarius);
                    break;
                case 10:
                    signText.setText("Козерог");
                    signImage.setImageResource(R.drawable.big_capricorn);
                    break;
                case 11:
                    signText.setText("Водолей");
                    signImage.setImageResource(R.drawable.big_aquarius);
                    break;
                case 12:
                    signText.setText("Рыбы");
                    signImage.setImageResource(R.drawable.big_pisces);
                    break;
            }
        }
        signImage.animate().alpha(1f).setDuration(500).setListener(null);
        signText.animate().alpha(1f).setDuration(500).setListener(null);

    }
}