package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.utils.OnSwipeTouchListener;


public class FragmentHoroscopePage extends Fragment {
    private Button todayButton, tomorrowButton, weekButton, monthButton, yearButton;
    private int numberOfActiveButton = 1;
    private Button commonHoroscopeButton, healthHoroscopeButton, loveHoroscopeButton,
            businessHoroscopeButton;
    private ImageButton infoAboutSignButton;
    private LinearLayout contentLayout, firstFreeSpace, secondFreeSpace, thirdFreeSpace, fourthFreeSpace;
    private TextView commonHoroscopeTextView;
    private TextView signTitle;
    private ImageView signImage;
    private TextView textCommonHoroscope, textLoveHoroscope, textHealthHoroscope, textBusinessHoroscope;
    private boolean isClickCommonHoroscopeButton = false, isClickHealthHoroscopeButton = false,
            isClickLoveHoroscopeButton = false, isClickBusinessHoroscopeButton = false;
    private Animation scaleUp;

    private Bundle bundle;
    private String[][] predictions;

    public static FragmentHoroscopePage newInstance(String param1, String param2) {
        FragmentHoroscopePage fragment = new FragmentHoroscopePage();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_page, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        class ButtonOnClickListener implements View.OnClickListener {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "ResourceType"})
            @Override
            public void onClick(View view) {
                view.startAnimation(scaleUp);
                switch (view.getId()) {
                    case R.id.todayButton:
                        updateStateButtons(todayButton);
                        break;
                    case R.id.tomorrowButton:
                        updateStateButtons(tomorrowButton);
                        break;
                    case R.id.weekButton:
                        updateStateButtons(weekButton);
                        break;
                    case R.id.monthButton:
                        updateStateButtons(monthButton);
                        break;
                    case R.id.yearButton:
                        updateStateButtons(yearButton);
                        break;
                    case R.id.infoAboutSignButton:
                        Fragment fragment = new FragmentInformationAboutSign();
                        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                        break;
                }
            }
        }

        bundle = getArguments();

        if (bundle != null) {
            predictions = new String[5][4];
            predictions[0] = bundle.getStringArray("todayPredictions");
            predictions[1] = bundle.getStringArray("tomorrowPredictions");
            predictions[2] = bundle.getStringArray("weekPredictions");
            predictions[3] = bundle.getStringArray("monthPredictions");
            predictions[4] = bundle.getStringArray("yearPredictions");
            signTitle = view.findViewById(R.id.signTitle);
            signTitle.setText(bundle.getString("signName"));

            signImage = view.findViewById(R.id.signImage);
            signImage.setImageResource(bundle.getInt("signPictureDrawableId"));
        }

        todayButton = view.findViewById(R.id.todayButton);
        todayButton.setOnClickListener(new ButtonOnClickListener());

        tomorrowButton = view.findViewById(R.id.tomorrowButton);
        tomorrowButton.setOnClickListener(new ButtonOnClickListener());

        weekButton = view.findViewById(R.id.weekButton);
        weekButton.setOnClickListener(new ButtonOnClickListener());

        monthButton = view.findViewById(R.id.monthButton);
        monthButton.setOnClickListener(new ButtonOnClickListener());

        yearButton = view.findViewById(R.id.yearButton);
        yearButton.setOnClickListener(new ButtonOnClickListener());

        commonHoroscopeButton = view.findViewById(R.id.commonHoroscopeButton);
        activeSwipe(commonHoroscopeButton);

        loveHoroscopeButton = view.findViewById(R.id.loveHoroscopeButton);
        activeSwipe(loveHoroscopeButton);

        healthHoroscopeButton = view.findViewById(R.id.healthHoroscopeButton);
        activeSwipe(healthHoroscopeButton);

        businessHoroscopeButton = view.findViewById(R.id.businessHoroscopeButton);
        activeSwipe(businessHoroscopeButton);

        infoAboutSignButton = view.findViewById(R.id.infoAboutSignButton);
        infoAboutSignButton.setOnClickListener(new ButtonOnClickListener());

        contentLayout = view.findViewById(R.id.contentLayout);
        activeSwipe(contentLayout);

        firstFreeSpace = view.findViewById(R.id.firstFreeSpace);
        secondFreeSpace = view.findViewById(R.id.secondFreeSpace);
        thirdFreeSpace = view.findViewById(R.id.thirdFreeSpace);
        fourthFreeSpace = view.findViewById(R.id.fourthFreeSpace);
        updateStateButtons(todayButton);
        return view;
    }

    private void updateStateButtons(@NonNull Button button) {
        button.setScaleX(1.4f);
        button.setScaleY(1.4f);
        if (button != todayButton) {
            todayButton.setScaleX(1);
            todayButton.setScaleY(1);
        } else {
            numberOfActiveButton = 1;
        }
        if (button != tomorrowButton) {
            tomorrowButton.setScaleX(1);
            tomorrowButton.setScaleY(1);
        } else {
            numberOfActiveButton = 2;
        }
        if (button != weekButton) {
            weekButton.setScaleX(1);
            weekButton.setScaleY(1);
        } else {
            numberOfActiveButton = 3;
        }
        if (button != monthButton) {
            monthButton.setScaleX(1);
            monthButton.setScaleY(1);
        } else {
            numberOfActiveButton = 4;
        }
        if (button != yearButton) {
            yearButton.setScaleX(1);
            yearButton.setScaleY(1);
        } else {
            numberOfActiveButton = 5;
        }
        updateTextViews();
    }

    private void activeSwipe(View view) {
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                switch (numberOfActiveButton) {
                    case 2:
                        updateStateButtons(todayButton);
                        break;
                    case 3:
                        updateStateButtons(tomorrowButton);
                        break;
                    case 4:
                        updateStateButtons(weekButton);
                        break;
                    case 5:
                        updateStateButtons(monthButton);
                        break;
                }

            }

            public void onSwipeLeft() {
                switch (numberOfActiveButton) {
                    case 1:
                        updateStateButtons(tomorrowButton);
                        break;
                    case 2:
                        updateStateButtons(weekButton);
                        break;
                    case 3:
                        updateStateButtons(monthButton);
                        break;
                    case 4:
                        updateStateButtons(yearButton);
                        break;
                }
            }

            @SuppressLint("NonConstantResourceId")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick() {
                view.startAnimation(scaleUp);
                if (bundle != null) {
                    switch (view.getId()) {
                        case R.id.commonHoroscopeButton:
                            if (isClickCommonHoroscopeButton) {
                                isClickCommonHoroscopeButton = false;
                                firstFreeSpace.removeView(textCommonHoroscope);
                                firstFreeSpace.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                isClickCommonHoroscopeButton = true;
                                textCommonHoroscope = new TextView(getActivity());
                                setAttributesForTextView(predictions[numberOfActiveButton - 1][0], textCommonHoroscope, firstFreeSpace);
                            }
                            break;
                        case R.id.loveHoroscopeButton:
                            if (isClickLoveHoroscopeButton) {
                                isClickLoveHoroscopeButton = false;
                                secondFreeSpace.removeView(textLoveHoroscope);
                                secondFreeSpace.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                isClickLoveHoroscopeButton = true;
                                textLoveHoroscope = new TextView(getActivity());
                                setAttributesForTextView(predictions[numberOfActiveButton - 1][1], textLoveHoroscope, secondFreeSpace);
                            }
                            break;
                        case R.id.healthHoroscopeButton:
                            if (isClickHealthHoroscopeButton) {
                                isClickHealthHoroscopeButton = false;
                                thirdFreeSpace.removeView(textHealthHoroscope);
                                thirdFreeSpace.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                isClickHealthHoroscopeButton = true;
                                textHealthHoroscope = new TextView(getActivity());
                                setAttributesForTextView(predictions[numberOfActiveButton - 1][2], textHealthHoroscope, thirdFreeSpace);
                            }
                            break;
                        case R.id.businessHoroscopeButton:
                            if (isClickBusinessHoroscopeButton) {
                                isClickBusinessHoroscopeButton = false;
                                fourthFreeSpace.removeView(textBusinessHoroscope);
                                fourthFreeSpace.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                isClickBusinessHoroscopeButton = true;
                                textBusinessHoroscope = new TextView(getActivity());
                                setAttributesForTextView(predictions[numberOfActiveButton - 1][3], textBusinessHoroscope, fourthFreeSpace);
                            }
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    private void setAttributesForTextView(String text, TextView textView, LinearLayout layout) {
        textView.setText(text);
        textView.setPadding(50, 30, 50, 30);
        textView.setTextSize(17);
        textView.setTextAppearance(R.style.style_horoscope_title);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.9f));
        textView.setBackgroundResource(R.drawable.button_for_horoscope_item);
        layout.getChildAt(0).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.05f));
        layout.addView(textView, 1);
        layout.getChildAt(2).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.05f));
    }

    private void updateTextViews() {
        if (textCommonHoroscope != null) {
            textCommonHoroscope.setText(predictions[numberOfActiveButton - 1][0]);
        }
        if (textLoveHoroscope != null) {
            textLoveHoroscope.setText(predictions[numberOfActiveButton - 1][1]);
        }
        if (textHealthHoroscope != null) {
            textHealthHoroscope.setText(predictions[numberOfActiveButton - 1][2]);
        }
        if (textBusinessHoroscope != null) {
            textBusinessHoroscope.setText(predictions[numberOfActiveButton - 1][3]);
        }
    }

}