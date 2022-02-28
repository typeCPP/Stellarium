package com.app.stellarium;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.MoonCalendarTable;
import com.app.stellarium.utils.OnSwipeTouchListener;

import java.time.LocalDate;
import java.util.Calendar;

public class FragmentMoonCalendar extends Fragment {
    private Calendar calendar = Calendar.getInstance();
    private ImageView imageViewMoon, imageViewCalendar;
    private TextView textViewDate, textViewDescription;
    private Animation scaleUp;
    private Button nextDayButton, prevDayButton;
    private String leftTitle, rightTitle;
    private LinearLayout textLayout;
    private FrameLayout frameLayout;
    private Animation rightAnim, leftAnim;
    CalendarView calendarView;

    public static FragmentMoonCalendar newInstance(String param1, String param2) {
        FragmentMoonCalendar fragment = new FragmentMoonCalendar();
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
        View view = inflater.inflate(R.layout.fragment_moon_calendar, container, false);
        imageViewMoon = view.findViewById(R.id.moon);
        imageViewCalendar = view.findViewById(R.id.calendarImage);
        textViewDate = view.findViewById(R.id.date);
        textViewDescription = view.findViewById(R.id.textView);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);

        calendarView = view.findViewById(R.id.calendarView);
        Time minTime = new Time();
        minTime.set(1, 0, calendar.get(Calendar.YEAR));
        calendarView.setMinDate(minTime.toMillis(true));
        Time maxTime = new Time();
        maxTime.set(31, 11, calendar.get(Calendar.YEAR));
        calendarView.setMaxDate(maxTime.toMillis(true));
        calendarView.setFirstDayOfWeek(2);

        LinearLayout layoutCalendar = view.findViewById(R.id.layout_calendar);
        class ButtonOnClickListener implements View.OnClickListener {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "ResourceType", "SetTextI18n", "NewApi"})
            @Override
            public void onClick(View clickView) {
                Time time;
                switch (clickView.getId()) {
                    case R.id.calendarImage:
                        layoutCalendar.setVisibility(View.VISIBLE);
                        imageViewMoon.setVisibility(View.GONE);
                        textViewDescription.setVisibility(View.GONE);
                        imageViewCalendar.setVisibility(View.INVISIBLE);
                        prevDayButton.setVisibility(View.INVISIBLE);
                        nextDayButton.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.datePrevDay:
                        clickView.startAnimation(scaleUp);
                        textLayout.startAnimation(leftAnim);
                        textViewDate.setText(leftTitle.substring(0, 2) + monthToString(Integer.parseInt(leftTitle.substring(3, 5)) - 1));
                        textViewDescription.setText(getDescriptionFromDatabaseByDate(Integer.parseInt(leftTitle.substring(3, 5)) * 100 + Integer.parseInt(leftTitle.substring(0, 2))));
                        changeDateViews(Integer.parseInt(leftTitle.substring(0, 2)), Integer.parseInt(leftTitle.substring(3, 5)) - 1);
                        time = new Time();
                        time.set(Integer.parseInt(leftTitle.substring(0, 2)) + 1, Integer.parseInt(leftTitle.substring(3, 5)) - 1, calendar.get(Calendar.YEAR));
                        calendarView.setDate(time.toMillis(true));
                        break;
                    case R.id.dateNextDay:
                        clickView.startAnimation(scaleUp);
                        textLayout.startAnimation(rightAnim);
                        textViewDate.setText(rightTitle.substring(0, 2) + monthToString(Integer.parseInt(rightTitle.substring(3, 5)) - 1));
                        textViewDescription.setText(getDescriptionFromDatabaseByDate(Integer.parseInt(rightTitle.substring(3, 5)) * 100 + Integer.parseInt(rightTitle.substring(0, 2))));
                        changeDateViews(Integer.parseInt(rightTitle.substring(0, 2)), Integer.parseInt(rightTitle.substring(3, 5)) - 1);
                        time = new Time();
                        time.set(Integer.parseInt(rightTitle.substring(0, 2)) - 1, Integer.parseInt(rightTitle.substring(3, 5)) - 1, calendar.get(Calendar.YEAR));
                        calendarView.setDate(time.toMillis(true));
                        break;
                }
            }

        }

        textViewDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + monthToString(calendar.get(Calendar.MONTH)));
        textViewDescription.setText(getDescriptionFromDatabaseByDate((calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH)));
        imageViewCalendar = view.findViewById(R.id.calendarImage);
        imageViewCalendar.setOnClickListener(new ButtonOnClickListener());

        nextDayButton = view.findViewById(R.id.dateNextDay);
        nextDayButton.setOnClickListener(new ButtonOnClickListener());

        prevDayButton = view.findViewById(R.id.datePrevDay);
        prevDayButton.setOnClickListener(new ButtonOnClickListener());

        changeDateViews(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH));

        rightAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        leftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);

        frameLayout = view.findViewById(R.id.mainFrame);
        textLayout = view.findViewById(R.id.textLayout);
        activeSwipe(frameLayout);
        activeSwipe(textViewDescription);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            textViewDate.setText(dayOfMonth + monthToString(month));
            Time time = new Time();
            time.set(dayOfMonth, month, year);
            changeDateViews(dayOfMonth, month);
            calendarView.setDate(time.toMillis(true));
            layoutCalendar.setVisibility(View.GONE);
            imageViewMoon.setVisibility(View.VISIBLE);
            textViewDescription.setVisibility(View.VISIBLE);
            imageViewCalendar.setVisibility(View.VISIBLE);
            prevDayButton.setVisibility(View.VISIBLE);
            nextDayButton.setVisibility(View.VISIBLE);
            textViewDescription.setText(getDescriptionFromDatabaseByDate((month + 1) * 100 + dayOfMonth));
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

    private void activeSwipe(View view) {
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSwipeRight() {
                textLayout.startAnimation(leftAnim);
                textViewDate.setText(leftTitle.substring(0, 2) + monthToString(Integer.parseInt(leftTitle.substring(3, 5)) - 1));
                textViewDescription.setText(getDescriptionFromDatabaseByDate(Integer.parseInt(leftTitle.substring(3, 5)) * 100 + Integer.parseInt(leftTitle.substring(0, 2))));
                changeDateViews(Integer.parseInt(leftTitle.substring(0, 2)), Integer.parseInt(leftTitle.substring(3, 5)) - 1);
                Time time = new Time();
                time.set(Integer.parseInt(leftTitle.substring(0, 2)) + 1, Integer.parseInt(leftTitle.substring(3, 5)) - 1, calendar.get(Calendar.YEAR));
                calendarView.setDate(time.toMillis(true));
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSwipeLeft() {
                textLayout.startAnimation(rightAnim);
                textViewDate.setText(rightTitle.substring(0, 2) + monthToString(Integer.parseInt(rightTitle.substring(3, 5)) - 1));
                textViewDescription.setText(getDescriptionFromDatabaseByDate(Integer.parseInt(rightTitle.substring(3, 5)) * 100 + Integer.parseInt(rightTitle.substring(0, 2))));
                changeDateViews(Integer.parseInt(rightTitle.substring(0, 2)), Integer.parseInt(rightTitle.substring(3, 5)) - 1);
                Time time = new Time();
                time.set(Integer.parseInt(rightTitle.substring(0, 2)) - 1, Integer.parseInt(rightTitle.substring(3, 5)) - 1, calendar.get(Calendar.YEAR));
                calendarView.setDate(time.toMillis(true));

            }
        });
    }

    @SuppressLint("Range")
    private String getDescriptionFromDatabaseByDate(int date) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(MoonCalendarTable.TABLE_NAME, null,
                "DATE = " + date,
                null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(MoonCalendarTable.COLUMN_DESCRIPTION));
    }

    private String fixDate(int month, int day) {
        String time;
        if (day < 10) {
            time = "0" + day + ".";
        } else {
            time = day + ".";
        }
        if (month < 10) {
            time += "0" + month;
        } else {
            time += month;
        }
        return time;
    }

    private String fixMonthOrDay(int number) {
        String result;
        if (number < 10) {
            result = "0" + number;
        } else {
            result = String.valueOf(number);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void changeDateViews(int centerDay, int centerMonth) {
        String currentDate = calendar.get(Calendar.YEAR) + "-" + fixMonthOrDay(centerMonth + 1) + "-" + fixMonthOrDay(centerDay);

        String nextDate = LocalDate.parse(currentDate).plusDays(1).toString();
        rightTitle = fixDate(Integer.parseInt(nextDate.substring(5, 7)), Integer.parseInt(nextDate.substring(8, 10)));
        nextDayButton.setText(rightTitle);

        String prevDate = LocalDate.parse(currentDate).minusDays(1).toString();
        leftTitle = fixDate(Integer.parseInt(prevDate.substring(5, 7)), Integer.parseInt(prevDate.substring(8, 10)));
        prevDayButton.setText(leftTitle);
    }
}