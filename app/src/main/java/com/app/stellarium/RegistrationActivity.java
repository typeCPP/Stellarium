package com.app.stellarium;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.dialog.EmailConfirmationDialog;
import com.app.stellarium.filters.UsernameFilter;
import com.app.stellarium.transitionGenerator.StellariumTransitionGenerator;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.ZodiacSignUtils;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.Calendar;
import java.util.function.UnaryOperator;


public class RegistrationActivity extends AppCompatActivity {
    private Animation scaleUp;
    private ImageSwitcher imageSwitcherMan;
    private ImageSwitcher imageSwitcherWoman;
    private ImageView imageCross;
    private boolean isTouchMan;
    private boolean isTouchWoman;
    private TextView editTextDate;
    private EditText editTextName;
    private int birthdayDay;
    private int birthdayMonth;
    private int birthdayYear;
    private static boolean isSelected;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private KenBurnsView kbv;
    private Button buttonEndRegistration;
    private UsernameFilter usernameFilter = new UsernameFilter();
    private EmailConfirmationDialog emailConfirmationDialog;
    private int serverID;
    private Intent mainActivityIntent;
    private boolean isReadyForResume = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        isSelected = false;
        imageSwitcherWoman = findViewById(R.id.imageSwitcherWoman);
        imageSwitcherMan = findViewById(R.id.imageSwitcherMan);

        editTextDate = findViewById(R.id.registration_date);
        buttonEndRegistration = findViewById(R.id.button_end_registration);

        imageCross = findViewById(R.id.cross);
        editTextName = findViewById(R.id.registration_name);
        editTextName.setFilters(new InputFilter[]{usernameFilter});
        editTextName.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(20)});
        setAnimation(imageSwitcherWoman);
        setAnimation(imageSwitcherMan);
        kbv = findViewById(R.id.image11);
        AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
        StellariumTransitionGenerator stellariumTransitionGenerator =
                new StellariumTransitionGenerator(10000, adi);
        kbv.setTransitionGenerator(stellariumTransitionGenerator);
        emailConfirmationDialog = new EmailConfirmationDialog(getLayoutInflater().getContext());


        Intent intent = getIntent();
        String name = intent.getStringExtra("userName");
        if (name != null) {
            editTextName.setText(name);
        }
        boolean isFacebookUID = intent.getBooleanExtra("isFacebook", false);
        String userUID = intent.getStringExtra("userUID");
        String email = intent.getStringExtra("userEmail");
        String password = intent.getStringExtra("userPassword");
        serverID = intent.getIntExtra("userServerID", 0);

        buttonEndRegistration.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                buttonEndRegistration.startAnimation(scaleUp);
                if (editTextName.getText().toString().isEmpty() || editTextDate.getText().toString().isEmpty() || (!isTouchMan && !isTouchWoman)) {
                    Toast.makeText(getApplicationContext(), "Заполните, пожалуйста, все поля.", Toast.LENGTH_LONG).show();
                } else {
                    ServerConnection serverConnection = new ServerConnection();
                    int sex = 0;
                    if (isTouchMan)
                        sex = 1;
                    String params = "/update_user/?name=" +
                            editTextName.getText().toString() +
                            "&birth=" + getBirthdayString(birthdayDay, birthdayMonth, birthdayYear) +
                            "&sex=" + sex;
                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    SQLiteDatabase database = databaseHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.put(UserTable.COLUMN_NAME, editTextName.getText().toString());
                    values.put(UserTable.COLUMN_DATE_OF_BIRTH, editTextDate.getText().toString());
                    values.put(UserTable.COLUMN_SEX, sex);
                    int signId = ZodiacSignUtils.getUserSignID(editTextDate.getText().toString());
                    values.put(UserTable.COLUMN_HOROSCOPE_SIGN_ID, signId);
                    params += "&sign=" + signId;

                    if (isFacebookUID && userUID != null) {
                        values.put(UserTable.COLUMN_FACEBOOK_ID, userUID);
                        params += "&facebook=" + userUID;
                    } else if (userUID != null) {
                        values.put(UserTable.COLUMN_GOOGLE_ID, userUID);
                        params += "&google=" + userUID;
                    }
                    if (email != null) {
                        values.put(UserTable.COLUMN_EMAIL, email);
                        params += "&mail=" + email;
                    }
                    if (password != null) {
                        values.put(UserTable.COLUMN_PASSWORD, password);
                        params += "&password=" + password;
                    }
                    mainActivityIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                    if ((email != null && password != null) || userUID != null) {
                        params += "&id=" + serverID;
                        String response = serverConnection.getStringResponseByParameters(params);
                        if (response != null) {
                            values.put(UserTable.COLUMN_SERVER_ID, serverID);
                            values.put(UserTable.COLUMN_MAIL_CONFIRMED, 1);
                            database.insert(UserTable.TABLE_NAME, null, values);
                            database.close();
                            databaseHelper.close();
                            isReadyForResume = true;

                            RegistrationActivity.this.startActivity(mainActivityIntent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Ошибка регистрации: проверьте введенные поля.", Toast.LENGTH_LONG).show();
                            database.close();
                            databaseHelper.close();
                        }
                    } else {
                        database.insert(UserTable.TABLE_NAME, null, values);
                        database.close();
                        databaseHelper.close();
                        RegistrationActivity.this.startActivity(mainActivityIntent);
                    }
                }
            }
        });
    }

/*    @Override
    protected void onResume() {
        super.onResume();
        if (isReadyForResume) {
            waitForEmailConfirmation(serverID, mainActivityIntent);
        }
    }*/

    /*private void waitForEmailConfirmation(int userServerID, Intent myIntent) {
        emailConfirmationDialog.show();
        emailConfirmationDialog.startGifAnimation();
        ServerConnection serverConnection = new ServerConnection();
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isConfirmed = false;
                while (!isConfirmed) {
                    try {
                        String response = serverConnection.getStringResponseByParameters("check_confirm/?user_id=" + userServerID);
                        if (Integer.parseInt(response) == 1) {
                            isConfirmed = true;
                        }
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                emailConfirmationDialog.stopGifAnimation();
                            }
                        });
                    }
                }
                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(UserTable.COLUMN_MAIL_CONFIRMED, 1);
                database.update(UserTable.TABLE_NAME, contentValues, UserTable.COLUMN_SERVER_ID + "=" + userServerID, null);
                database.close();
                databaseHelper.close();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        emailConfirmationDialog.dismiss();
                        RegistrationActivity.this.startActivity(myIntent);
                    }
                });
            }
        }).start();

    }*/

    private String getBirthdayString(int birthdayDay, int birthdayMonth, int birthdayYear) {
        StringBuffer text = new StringBuffer(birthdayDay + "." + birthdayMonth + "." + birthdayYear);
        if (birthdayDay < 10) {
            text.insert(0, 0);
        }
        if (birthdayMonth < 10) {
            text.insert(3, 0);
        }
        return text.toString();
    }

    @SuppressLint("ResourceAsColor")
    public void onClickDate(View view) {
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
                editTextDate.setTextColor(getResources().getColor(R.color.text_registration));
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

    @SuppressLint("ResourceAsColor")
    private void createDatePickerDialog(int day, int month, int year, Calendar calendar) {
        datePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerDialog, dateSetListener, year, month, day);
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.button_registration_bottom_text);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.button_registration_bottom_text);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        calendar.set(Calendar.YEAR, 1900);
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
    }

    public void onClickCross(View view) {
        editTextName.setText("");
    }

    public void onSwitcherClickWoman(View view) {
        if (!isTouchWoman) {
            imageSwitcherWoman.showNext();
            isTouchWoman = true;
        }
        if (isTouchMan) {
            imageSwitcherMan.showNext();
            isTouchMan = false;
        }
    }

    public void onSwitcherClickMan(View view) {
        if (!isTouchMan) {
            imageSwitcherMan.showNext();
            isTouchMan = true;
        }
        if (isTouchWoman) {
            imageSwitcherWoman.showNext();
            isTouchWoman = false;
        }
    }

    private void setAnimation(ImageSwitcher imageSwitcher) {
        Animation inAnimation = new AlphaAnimation(0, 1);
        inAnimation.setDuration(400);
        Animation outAnimation = new AlphaAnimation(1, 0);
        outAnimation.setDuration(400);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        imageSwitcher.setInAnimation(inAnimation);
        imageSwitcher.setOutAnimation(outAnimation);
    }
}