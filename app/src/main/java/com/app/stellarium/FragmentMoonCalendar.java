package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class FragmentMoonCalendar extends Fragment {

    private Button dateButton;
    private Calendar calendar = Calendar.getInstance();
    private ImageView imageView;
    String strDate;
    TextView textViewDate;

    public static FragmentMoonCalendar newInstance(String param1, String param2) {
        FragmentMoonCalendar fragment = new FragmentMoonCalendar();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moon_calendar, container, false);
        imageView = view.findViewById(R.id.moon);
        textViewDate = view.findViewById(R.id.date);

        LinearLayout layoutCalendar = view.findViewById(R.id.layout_calendar);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        class ButtonOnClickListener implements View.OnClickListener {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "ResourceType"})
            @Override
            public void onClick(View clickView) {
                switch (clickView.getId()) {
                    case R.id.date:
                        Time minTime = new Time();
                        minTime.set(1, 0, calendar.get(Calendar.YEAR));
                        calendarView.setMinDate(minTime.toMillis(true));
                        Time maxTime = new Time();
                        maxTime.set(31, 11, calendar.get(Calendar.YEAR));
                        calendarView.setMaxDate(maxTime.toMillis(true));
                        calendarView.setFirstDayOfWeek(2);
                        layoutCalendar.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);

                }
            }

        }

        textViewDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + monthToString(calendar.get(Calendar.MONTH)));
        dateButton = view.findViewById(R.id.date);
        dateButton.setOnClickListener(new ButtonOnClickListener());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, final int year, final int month, final int dayOfMonth) {
                strDate = dayOfMonth + "-" + month + "-" + year;
                textViewDate.setText(dayOfMonth + monthToString(month));
                Time time = new Time();
                time.set(dayOfMonth, month, year);
                calendarView.setDate(time.toMillis(true));
                layoutCalendar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    String monthToString(int month) {
        String str = " ";
        switch (month) {
            case 0:
                str = " января";
                break;
            case 1:
                str = " февраля";
                break;
            case 2:
                str = " марта";
                break;
            case 3:
                str = " апреля";
                break;
            case 4:
                str = " мая";
                break;
            case 5:
                str = " июня";
                break;
            case 6:
                str = " июля";
                break;
            case 7:
                str = " августа";
                break;
            case 8:
                str = " сентября";
                break;
            case 9:
                str = " октября";
                break;
            case 10:
                str = " ноября";
                break;
            case 11:
                str = " декабря";
                break;
        }
        return str;
    }
}