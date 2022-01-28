package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class FragmentHoroscopeList extends Fragment {
    private Button ariesButton, taurusButton, geminiButton, cancerButton, leoButton, virgoButton,
            libraButton, scorpioButton, sagittariusButton, capricornButton, aquariusButton, piscesButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horoscope_list, container, false);
        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int idOfInformationTableElement = 1;
                    switch (view.getId()) {
                        case R.id.ariesButton:
                            idOfInformationTableElement = 1;
                            Fragment fragment = new FragmentHoroscopePage();
                            getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
                            break;
                        case R.id.taurusButton:
                            idOfInformationTableElement = 2;
                            break;
                        case R.id.geminiButton:
                            idOfInformationTableElement = 3;
                            break;
                        case R.id.cancerButton:
                            idOfInformationTableElement = 4;
                            break;
                        case R.id.leoButton:
                            idOfInformationTableElement = 5;
                            break;
                        case R.id.virgoButton:
                            idOfInformationTableElement = 6;
                            break;
                        case R.id.libraButton:
                            idOfInformationTableElement = 7;
                            break;
                        case R.id.scorpioButton:
                            idOfInformationTableElement = 8;
                            break;
                        case R.id.sagittariusButton:
                            idOfInformationTableElement = 9;
                            break;
                        case R.id.capricornButton:
                            idOfInformationTableElement = 10;
                            break;
                        case R.id.aquariusButton:
                            idOfInformationTableElement = 11;
                            break;
                        case R.id.piscesButton:
                            idOfInformationTableElement = 12;
                            break;
                    }

                }
                return true;
            }
        }

        ariesButton = view.findViewById(R.id.ariesButton);
        ariesButton.setOnTouchListener(new ButtonOnTouchListener());

        taurusButton = view.findViewById(R.id.taurusButton);
        taurusButton.setOnTouchListener(new ButtonOnTouchListener());

        geminiButton = view.findViewById(R.id.geminiButton);
        geminiButton.setOnTouchListener(new ButtonOnTouchListener());

        cancerButton = view.findViewById(R.id.cancerButton);
        cancerButton.setOnTouchListener(new ButtonOnTouchListener());

        leoButton = view.findViewById(R.id.leoButton);
        leoButton.setOnTouchListener(new ButtonOnTouchListener());

        virgoButton = view.findViewById(R.id.virgoButton);
        virgoButton.setOnTouchListener(new ButtonOnTouchListener());

        libraButton = view.findViewById(R.id.libraButton);
        libraButton.setOnTouchListener(new ButtonOnTouchListener());

        scorpioButton = view.findViewById(R.id.scorpioButton);
        scorpioButton.setOnTouchListener(new ButtonOnTouchListener());

        sagittariusButton = view.findViewById(R.id.sagittariusButton);
        sagittariusButton.setOnTouchListener(new ButtonOnTouchListener());

        capricornButton = view.findViewById(R.id.capricornButton);
        capricornButton.setOnTouchListener(new ButtonOnTouchListener());

        aquariusButton = view.findViewById(R.id.aquariusButton);
        aquariusButton.setOnTouchListener(new ButtonOnTouchListener());

        piscesButton = view.findViewById(R.id.piscesButton);
        piscesButton.setOnTouchListener(new ButtonOnTouchListener());
        return view;
    }
}