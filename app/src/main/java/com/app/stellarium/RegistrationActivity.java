package com.app.stellarium;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.stellarium.transitionGenerator.StellariumTransitionGenerator;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.Calendar;


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        isSelected = false;
        imageSwitcherWoman = findViewById(R.id.imageSwitcherWoman);
        imageSwitcherMan = findViewById(R.id.imageSwitcherMan);

        editTextDate = findViewById(R.id.registration_date);

        imageCross = findViewById(R.id.cross);
        editTextName = findViewById(R.id.registration_name);
        setAnimation(imageSwitcherWoman);
        setAnimation(imageSwitcherMan);
        kbv = findViewById(R.id.image11);
        AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
        StellariumTransitionGenerator stellariumTransitionGenerator =
                new StellariumTransitionGenerator(10000, adi);
        kbv.setTransitionGenerator(stellariumTransitionGenerator);
    }

    @SuppressLint("ResourceAsColor")
    public void onClickDate(View view) {
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mothOfYear, int dayOfMonth) {
                birthdayDay = dayOfMonth;
                birthdayMonth = mothOfYear;
                birthdayYear = year;
                editTextDate.setText(dayOfMonth + "/" + (mothOfYear + 1) + "/" + year);
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