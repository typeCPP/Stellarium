package com.app.stellarium;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.stellarium.dialog.LoadingDialog;
import com.app.stellarium.utils.ServerConnection;
import com.google.gson.Gson;
import com.app.stellarium.utils.jsonmodels.MoonCalendar;

import java.util.Calendar;
import java.util.function.UnaryOperator;

public class FragmentMoonCalendarDateSelection extends Fragment {

    private LinearLayout calendar_layout;
    private CalendarView calendarView;
    private Calendar calendar = Calendar.getInstance();
    private TextView textViewDate;
    private LoadingDialog loadingDialog;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moon_calendar_date_selection, container, false);
        loadingDialog = new LoadingDialog(view.getContext());
        calendarView = view.findViewById(R.id.calendarView);
        Time minTime = new Time();
        Time maxTime = new Time();
        int serverYear = getCurrentYearFromServer();
        if (serverYear != 0) {
            minTime.set(1, 0, serverYear);
            maxTime.set(31, 11, serverYear);
        } else {
            minTime.set(1, 0, calendar.get(Calendar.YEAR));
            maxTime.set(31, 11, calendar.get(Calendar.YEAR));
        }
        calendarView.setMinDate(minTime.toMillis(true));
        calendarView.setMaxDate(maxTime.toMillis(true));
        calendarView.setFirstDayOfWeek(2);
        textViewDate = view.findViewById(R.id.date_date_selection);
        Bundle bundleFromMoonCalendar = getArguments();
        if (bundleFromMoonCalendar != null) {
            int dayOfMonth = bundleFromMoonCalendar.getInt("dayOfMonth");
            int month = bundleFromMoonCalendar.getInt("month");
            Time time = new Time();
            serverYear = getCurrentYearFromServer();
            if (serverYear != 0) {
                time.set(dayOfMonth, month, serverYear);
            } else {
                time.set(dayOfMonth, month, calendar.get(Calendar.YEAR));
            }
            calendarView.setDate(time.toMillis(true));
            textViewDate.setText(dayOfMonth + monthToString(month));
        }

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Time time = new Time();
            time.set(dayOfMonth, month, year);
            Fragment fragment = new FragmentMoonCalendar();
            calendarView.setDate(time.toMillis(true));
            textViewDate.setText(dayOfMonth + monthToString(month));
            loadingDialog.setOnClick(new UnaryOperator<Void>() {
                @Override
                public Void apply(Void unused) {
                    getDescriptionFromServerByDate(dayOfMonth, month);
                    return null;
                }
            });
            getDescriptionFromServerByDate(dayOfMonth, month);
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

    private int getCurrentYearFromServer() {
        ServerConnection serverConnection = new ServerConnection();
        String response = serverConnection.getStringResponseByParameters("get_current_year");
        if (response == null || response.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(response);
    }

    private void getDescriptionFromServerByDate(int dayOfMonth, int month) {
        Handler handler = new Handler();
        try {
            loadingDialog.show();
            loadingDialog.startGifAnimation();
            Calendar calendar = Calendar.getInstance();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerConnection serverConnection = new ServerConnection();
                    int date = (month + 1) * 100 + dayOfMonth;
                    System.out.println(date);
                    String response = serverConnection.getStringResponseByParameters("moonCalendar/?date=" + date);
                    MoonCalendar moonCalendar = new Gson().fromJson(response, MoonCalendar.class);
                    if (moonCalendar == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.stopGifAnimation();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Bundle bundle = new Bundle();
                                bundle.putString("phase", moonCalendar.phase);
                                bundle.putString("characteristics", moonCalendar.characteristics);
                                bundle.putString("health", moonCalendar.health);
                                bundle.putString("relations", moonCalendar.relations);
                                bundle.putString("business", moonCalendar.business);
                                bundle.putInt("dayOfMonth", dayOfMonth);
                                bundle.putInt("month", month);
                                Fragment fragment = new FragmentMoonCalendar();
                                getParentFragmentManager().popBackStack();
                                fragment.setArguments(bundle);
                                getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                        .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                                loadingDialog.dismiss();
                            }
                        });
                    }
                }
            }).start();
        } catch (Exception e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.stopGifAnimation();
                }
            });
        }
    }
}