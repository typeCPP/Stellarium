package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button affirmationButton, horoscopeButton, taroButton, compatibilityButton,
            moonCalendarButton, numerologicButton, squareOfPythagorasButton, yesOrNoButton;
    private Animation scaleUp;

    public FragmentHome() {
        // Required empty public constructor
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                    Bundle bundle = new Bundle();
                    switch (view.getId()) {
                        case R.id.affirmationButton:
                            break;
                        case R.id.horoscopeButton:
                            Intent intent = new Intent(getActivity(), HoroscopeListActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.taroButton:
                            break;
                        case R.id.compatibilityButton:
                            break;
                        case R.id.moonCalendarButton:
                            break;
                        case R.id.numerologicButton:
                            break;
                        case R.id.squareOfPythagorasButton:
                            break;
                        case R.id.yesOrNoButton:
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

        numerologicButton = view.findViewById(R.id.numerologicButton);
        numerologicButton.setOnTouchListener(new ButtonOnTouchListener());

        squareOfPythagorasButton = view.findViewById(R.id.squareOfPythagorasButton);
        squareOfPythagorasButton.setOnTouchListener(new ButtonOnTouchListener());

        yesOrNoButton = view.findViewById(R.id.yesOrNoButton);
        yesOrNoButton.setOnTouchListener(new ButtonOnTouchListener());

        return view;
    }

}