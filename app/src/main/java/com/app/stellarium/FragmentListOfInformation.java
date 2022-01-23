package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListOfInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListOfInformation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Button affirmationButton, horoscopeButton, taroButton, compatibilityButton,
            moonCalendarButton, numerologicButton, squareOfPythagorasButton, yesOrNoButton;
    private Animation scaleUp;
    private String mParam1;
    private String mParam2;

    public FragmentListOfInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Information.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListOfInformation newInstance(String param1, String param2) {
        FragmentListOfInformation fragment = new FragmentListOfInformation();
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

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list_of_information, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        affirmationButton = view.findViewById(R.id.affirmationButton);
        affirmationButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    affirmationButton.startAnimation(scaleUp);
                }
                return true;
            }
        });
        horoscopeButton= view.findViewById(R.id.horoscopeButton);
        horoscopeButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    horoscopeButton.startAnimation(scaleUp);
                }
                return true;
            }
        });
        taroButton = view.findViewById(R.id.taroButton);
        taroButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    taroButton.startAnimation(scaleUp);
                }
                return true;
            }
        });
        compatibilityButton = view.findViewById(R.id.compatibilityButton);
        compatibilityButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    compatibilityButton.startAnimation(scaleUp);
                }
                return true;
            }
        });
        moonCalendarButton = view.findViewById(R.id.moonCalendarButton);
        moonCalendarButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    moonCalendarButton.startAnimation(scaleUp);
                }
                return true;
            }
        });
        numerologicButton = view.findViewById(R.id.numerologicButton);
        numerologicButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    numerologicButton.startAnimation(scaleUp);
                }
                return true;
            }
        });
        squareOfPythagorasButton = view.findViewById(R.id.squareOfPythagorasButton);
        squareOfPythagorasButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    squareOfPythagorasButton.startAnimation(scaleUp);
                }
                return true;
            }
        });
        yesOrNoButton = view.findViewById(R.id.yesOrNoButton);
        yesOrNoButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                {
                    yesOrNoButton.startAnimation(scaleUp);
                }
                return true;
            }
        });
        return view;

    }
}