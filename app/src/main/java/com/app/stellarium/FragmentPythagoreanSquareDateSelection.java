package com.app.stellarium;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.Calendar;

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
    Animation scaleUp;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentPythagoreanSquareDateSelection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPythagoreanSquareDateSelection newInstance(String param1, String param2) {
        FragmentPythagoreanSquareDateSelection fragment = new FragmentPythagoreanSquareDateSelection();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pythagorean_square_date_selection, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        isSetDate = false;
        editTextDate = view.findViewById(R.id.pythagorean_date_date_selection);
        layoutDate = view.findViewById(R.id.pythagorean_square_date_layout_1);
        nextButton = view.findViewById(R.id.nextPythSquareButton);
        nextButton.setAlpha(0f);
        bundle = new Bundle();

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                    layoutDate.animate().alpha(0f).setDuration(250).setListener(null);
                    Fragment fragmentHomePage = new FragmentPythagoreanSquareHomePage();
                    fragmentHomePage.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentHomePage).commit();
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

    @SuppressLint("ResourceAsColor")
    private void createDatePickerDialog(int day, int month, int year, Calendar calendar) {
        datePickerDialog = new DatePickerDialog(getContext(), R.style.CustomDatePickerDialog, dateSetListener, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "моя дата рождения", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                birthdayDay = 18;
                birthdayMonth = 7;
                birthdayYear = 2002;
                addTextToTextView(birthdayDay, birthdayMonth, birthdayYear);
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
}
