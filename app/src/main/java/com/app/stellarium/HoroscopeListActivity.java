package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.app.stellarium.transitionGenerator.StellariumTransitionGenerator;
import com.flaviofaria.kenburnsview.KenBurnsView;


public class HoroscopeListActivity extends AppCompatActivity {
    private KenBurnsView kbv;
    private Animation scaleUp;
    private Button ariesButton,taurusButton, geminiButton, cancerButton, leoButton, virgoButton,
            libraButton, scorpioButton, sagittariusButton,capricornButton, aquariusButton, piscesButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope_list);
        kbv = findViewById(R.id.image11);
        AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
        StellariumTransitionGenerator stellariumTransitionGenerator =
                new StellariumTransitionGenerator(10000, adi);
        kbv.setTransitionGenerator(stellariumTransitionGenerator);
        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                    Bundle bundle = new Bundle();
                    setAnimation();

                    int idOfInformationTableElement = 1;
                    switch (view.getId()) {
                        case R.id.ariesButton:
                            idOfInformationTableElement = 1;
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

        ariesButton = findViewById(R.id.ariesButton);
        ariesButton.setOnTouchListener(new ButtonOnTouchListener());

        taurusButton = findViewById(R.id.taurusButton);
        taurusButton.setOnTouchListener(new ButtonOnTouchListener());

        geminiButton = findViewById(R.id.geminiButton);
        geminiButton.setOnTouchListener(new ButtonOnTouchListener());

        cancerButton = findViewById(R.id.cancerButton);
        cancerButton.setOnTouchListener(new ButtonOnTouchListener());

        leoButton = findViewById(R.id.leoButton);
        leoButton.setOnTouchListener(new ButtonOnTouchListener());

        virgoButton = findViewById(R.id.virgoButton);
        virgoButton.setOnTouchListener(new ButtonOnTouchListener());

        libraButton = findViewById(R.id.libraButton);
        libraButton.setOnTouchListener(new ButtonOnTouchListener());

        scorpioButton = findViewById(R.id.scorpioButton);
        scorpioButton.setOnTouchListener(new ButtonOnTouchListener());

        sagittariusButton = findViewById(R.id.sagittariusButton);
        sagittariusButton.setOnTouchListener(new ButtonOnTouchListener());

        capricornButton = findViewById(R.id.capricornButton);
        capricornButton.setOnTouchListener(new ButtonOnTouchListener());

        aquariusButton = findViewById(R.id.aquariusButton);
        aquariusButton.setOnTouchListener(new ButtonOnTouchListener());

        piscesButton = findViewById(R.id.piscesButton);
        piscesButton.setOnTouchListener(new ButtonOnTouchListener());
    }

    private void setAnimation() {
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
    }
}