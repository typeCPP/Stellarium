package com.app.stellarium;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class FragmentMoonCalendarDateSelection extends Fragment {

    private LinearLayout calendar_layout;
    private CalendarView calendarView;
    private Calendar calendar = Calendar.getInstance();
    private TextView textViewDate;

    public FragmentMoonCalendarDateSelection() {
        // Required empty public constructor
    }

    public static FragmentMoonCalendarDateSelection newInstance(String param1, String param2) {
        FragmentMoonCalendarDateSelection fragment = new FragmentMoonCalendarDateSelection();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moon_calendar_date_selection, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        Time minTime = new Time();
        minTime.set(1, 0, calendar.get(Calendar.YEAR));
        calendarView.setMinDate(minTime.toMillis(true));
        Time maxTime = new Time();
        maxTime.set(31, 11, calendar.get(Calendar.YEAR));
        calendarView.setMaxDate(maxTime.toMillis(true));
        calendarView.setFirstDayOfWeek(2);
        textViewDate = view.findViewById(R.id.date_date_selection);
        Bundle bundleFromMoonCalendar = getArguments();
        if (bundleFromMoonCalendar != null) {
            int dayOfMonth = bundleFromMoonCalendar.getInt("dayOfMonth");
            int month = bundleFromMoonCalendar.getInt("month");
            Time time = new Time();
            time.set(dayOfMonth, month, calendar.get(Calendar.YEAR));
            calendarView.setDate(time.toMillis(true));
            textViewDate.setText(dayOfMonth + monthToString(month));
        }

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Time time = new Time();
            time.set(dayOfMonth, month, year);
            Fragment fragment = new FragmentMoonCalendar();
            calendarView.setDate(time.toMillis(true));
            textViewDate.setText(dayOfMonth + monthToString(month));
            Bundle bundle = new Bundle();
            bundle.putInt("dayOfMonth", dayOfMonth);
            bundle.putInt("month", month);
            bundle.putInt("year", year);
            fragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
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