package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.app.stellarium.tarocards.FragmentDayCard;
import com.app.stellarium.tarocards.FragmentOneCard;
import com.app.stellarium.tarocards.FragmentPyramidLovers;
import com.app.stellarium.tarocards.FragmentSevenStars;
import com.app.stellarium.tarocards.FragmentThreeCards;


public class FragmentTaroCards extends Fragment {
    private Button oneCardButton, dayCardButton, threeCardsButton, sevenStarsButton, pyramidLoversButton;
    int countOfCards = 0;

    public static FragmentTaroCards newInstance(String param1, String param2) {
        FragmentTaroCards fragment = new FragmentTaroCards();
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
        View view = inflater.inflate(R.layout.fragment_taro_cards, container, false);
        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Fragment fragment = null;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    switch (view.getId()) {
                        case R.id.buttonNavigateToDayCard:
                            fragment = new FragmentDayCard();
                            countOfCards = 1;
                            break;
                        case R.id.buttonNavigateToOneCard:
                            fragment = new FragmentOneCard();
                            countOfCards = 1;
                            break;
                        case R.id.buttonNavigateToThreeCards:
                            fragment = new FragmentThreeCards();
                            countOfCards = 3;
                            break;
                        case R.id.buttonNavigateToSevenStars:
                            fragment = new FragmentSevenStars();
                            countOfCards = 7;
                            break;
                        case R.id.buttonNavigateToPyramidLovers:
                            fragment = new FragmentPyramidLovers();
                            countOfCards = 5;
                            break;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("countOfCards", countOfCards);
                    fragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                }
                return true;
            }
        }

        oneCardButton = view.findViewById(R.id.buttonNavigateToOneCard);
        oneCardButton.setOnTouchListener(new ButtonOnTouchListener());

        dayCardButton = view.findViewById(R.id.buttonNavigateToDayCard);
        dayCardButton.setOnTouchListener(new ButtonOnTouchListener());

        threeCardsButton = view.findViewById(R.id.buttonNavigateToThreeCards);
        threeCardsButton.setOnTouchListener(new ButtonOnTouchListener());

        sevenStarsButton = view.findViewById(R.id.buttonNavigateToSevenStars);
        sevenStarsButton.setOnTouchListener(new ButtonOnTouchListener());

        pyramidLoversButton = view.findViewById(R.id.buttonNavigateToPyramidLovers);
        pyramidLoversButton.setOnTouchListener(new ButtonOnTouchListener());
        return view;
    }
}