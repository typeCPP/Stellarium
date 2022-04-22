package com.app.stellarium;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.UserTable;

import java.util.Calendar;

public class FragmentHome extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView titleText;
    private Button affirmationButton, horoscopeButton, taroButton, compatibilityButton,
            moonCalendarButton, numerologyButton, squareOfPythagorasButton, yesOrNoButton;
    private Animation scaleUp;

    public FragmentHome() {
    }

    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        String name = getUserName(databaseHelper.getReadableDatabase());
        titleText = view.findViewById(R.id.title_text);
        titleText.setText(getHelloStringByCurrentTime() + ", " + name);
        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                    Fragment fragment;
                    switch (view.getId()) {
                        case R.id.affirmationButton:
                            fragment = new FragmentAffirmation();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
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
                            fragment = new FragmentMoonCalendar();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
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
        Log.d("TIME", String.valueOf(hour));
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


}