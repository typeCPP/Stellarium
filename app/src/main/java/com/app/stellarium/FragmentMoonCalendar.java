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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.MoonCalendarTable;
import com.app.stellarium.utils.OnSwipeTouchListener;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.MoonCalendar;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.Calendar;

public class FragmentMoonCalendar extends Fragment {
    private Calendar calendar = Calendar.getInstance();
    private ImageView imageViewMoon, imageViewCalendar;
    private TextView textViewDate, textViewPhase, textViewCharacteristic, textViewHealth, textViewBusiness,
            textViewRelations, textViewPhaseTitle, textViewCharacteristicTitle, textViewHealthTitle,
            textViewBusinessTitle, textViewRelationsTitle;
    private Animation scaleUp;
    private Button nextDayButton, prevDayButton;
    private String leftTitle, rightTitle;
    private LinearLayout textLayout;
    private FrameLayout frameLayout;
    private Animation rightAnim, leftAnim;

    private int dateOfMonthSelection;
    private int monthSelection;

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

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setNumberOfPrevFragment();
        }
        imageViewMoon = view.findViewById(R.id.moon);
        imageViewCalendar = view.findViewById(R.id.calendarImage);
        textViewDate = view.findViewById(R.id.date);
        textViewPhase = view.findViewById(R.id.phase);
        textViewCharacteristic = view.findViewById(R.id.characteristic);
        textViewHealth = view.findViewById(R.id.health);
        textViewRelations = view.findViewById(R.id.relations);
        textViewBusiness = view.findViewById(R.id.business);
        textViewPhaseTitle = view.findViewById(R.id.title_phase);
        textViewCharacteristicTitle = view.findViewById(R.id.title_characteristic);
        textViewHealthTitle = view.findViewById(R.id.title_health);
        textViewRelationsTitle = view.findViewById(R.id.title_relations);
        textViewBusinessTitle = view.findViewById(R.id.title_business);

        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        class ButtonOnClickListener implements View.OnClickListener {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "ResourceType", "SetTextI18n", "NewApi"})
            @Override
            public void onClick(View clickView) {
                switch (clickView.getId()) {
                    case R.id.calendarImage:
                        Fragment fragment = new FragmentMoonCalendarDateSelection();
                        Bundle bundle = new Bundle();
                        bundle.putInt("dayOfMonth", dateOfMonthSelection);
                        bundle.putInt("month", monthSelection);
                        fragment.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                        break;
                    case R.id.datePrevDay:
                        clickView.startAnimation(scaleUp);
                        textLayout.startAnimation(leftAnim);
                        break;
                    case R.id.dateNextDay:
                        clickView.startAnimation(scaleUp);
                        textLayout.startAnimation(rightAnim);
                        break;
                }
            }

        }
        imageViewCalendar = view.findViewById(R.id.calendarImage);
        imageViewCalendar.setOnClickListener(new ButtonOnClickListener());

        nextDayButton = view.findViewById(R.id.dateNextDay);
        nextDayButton.setOnClickListener(new ButtonOnClickListener());

        prevDayButton = view.findViewById(R.id.datePrevDay);
        prevDayButton.setOnClickListener(new ButtonOnClickListener());
        Bundle bundle = getArguments();
        if (bundle != null) {
            dateOfMonthSelection = bundle.getInt("dayOfMonth");
            monthSelection = bundle.getInt("month");
            textViewDate.setText(dateOfMonthSelection + monthToString(monthSelection));
            changeDateViews(dateOfMonthSelection, monthSelection);
            getDescriptionFromServerByDate((monthSelection + 1) * 100 + dateOfMonthSelection);
        } else {
            dateOfMonthSelection = calendar.get(Calendar.DAY_OF_MONTH);
            monthSelection = calendar.get(Calendar.MONTH);
            textViewDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + monthToString(calendar.get(Calendar.MONTH)));
            changeDateViews(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH));
            getDescriptionFromServerByDate((calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH));
        }

        class SlideAnimationListener implements Animation.AnimationListener {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateInformationText(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            private void updateInformationText(Animation animation) {
                Time time;
                if (leftAnim == animation) {
                    dateOfMonthSelection = Integer.parseInt(leftTitle.substring(0, 2));
                    monthSelection = Integer.parseInt(leftTitle.substring(3, 5)) - 1;
                    System.out.println("date: " + dateOfMonthSelection + " month :" + monthSelection);
                    textViewDate.setText(leftTitle.substring(0, 2) + monthToString(Integer.parseInt(leftTitle.substring(3, 5)) - 1));
                    getDescriptionFromServerByDate(Integer.parseInt(leftTitle.substring(3, 5)) * 100 + Integer.parseInt(leftTitle.substring(0, 2)));
                    changeDateViews(Integer.parseInt(leftTitle.substring(0, 2)), Integer.parseInt(leftTitle.substring(3, 5)) - 1);
                    time = new Time();
                    time.set(Integer.parseInt(leftTitle.substring(0, 2)) + 1, Integer.parseInt(leftTitle.substring(3, 5)) - 1, calendar.get(Calendar.YEAR));
                } else if (rightAnim == animation) {
                    dateOfMonthSelection = Integer.parseInt(rightTitle.substring(0, 2));
                    monthSelection = Integer.parseInt(rightTitle.substring(3, 5)) - 1;
                    textViewDate.setText(rightTitle.substring(0, 2) + monthToString(Integer.parseInt(rightTitle.substring(3, 5)) - 1));
                    getDescriptionFromServerByDate(Integer.parseInt(rightTitle.substring(3, 5)) * 100 + Integer.parseInt(rightTitle.substring(0, 2)));
                    changeDateViews(Integer.parseInt(rightTitle.substring(0, 2)), Integer.parseInt(rightTitle.substring(3, 5)) - 1);
                    time = new Time();
                    time.set(Integer.parseInt(rightTitle.substring(0, 2)) - 1, Integer.parseInt(rightTitle.substring(3, 5)) - 1, calendar.get(Calendar.YEAR));
                }
            }
        }

        rightAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        leftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        rightAnim.setAnimationListener(new SlideAnimationListener());
        leftAnim.setAnimationListener(new SlideAnimationListener());

        frameLayout = view.findViewById(R.id.mainFrame);
        textLayout = view.findViewById(R.id.textLayout);
        activeSwipe(frameLayout);
        activeSwipe(textViewHealth);
        activeSwipe(textViewPhase);
        activeSwipe(textViewCharacteristic);
        activeSwipe(textViewBusiness);
        activeSwipe(textViewRelations);
        activeSwipe(textViewHealthTitle);
        activeSwipe(textViewPhaseTitle);
        activeSwipe(textViewCharacteristicTitle);
        activeSwipe(textViewBusinessTitle);
        activeSwipe(textViewRelationsTitle);
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
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSwipeLeft() {
                textLayout.startAnimation(rightAnim);
            }
        });
    }


    private void getDescriptionFromServerByDate(int date) {
        try {
            ServerConnection serverConnection = new ServerConnection();
            String response = serverConnection.getStringResponseByParameters("moonCalendar/?date=" + date);
            MoonCalendar moonCalendar = new Gson().fromJson(response, MoonCalendar.class);

            textViewPhase.setText(moonCalendar.phase);
            textViewCharacteristic.setText(moonCalendar.characteristics);
            textViewHealth.setText(moonCalendar.health);
            textViewRelations.setText(moonCalendar.relations);
            textViewBusiness.setText(moonCalendar.business);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка загрузки.", Toast.LENGTH_LONG).show();
        }
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