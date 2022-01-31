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

public class FragmentCompatibilityMenu extends Fragment {

    private Button buttonNavigateToName;
    private Button buttonNavigateToDate;
    private Animation scaleUp;
    private Animation rightAnim, leftAnim;
    private boolean isStartPage = true;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCompatibilityMenu() {
        // Required empty public constructor
    }


    public static FragmentCompatibilityMenu newInstance(String param1, String param2) {
        FragmentCompatibilityMenu fragment = new FragmentCompatibilityMenu();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compatibility_menu, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.startAnimation(scaleUp);
                Fragment fragment = null;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    switch (view.getId()) {
                        case R.id.buttonNavigateToDate:
                            fragment = new FragmentCompatibilitySignSelection();
                            break;
                        case R.id.buttonNavigateToName:
                            fragment = new FragmentCompatibilityNameSelection();
                            break;

                    }
                    getParentFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                }
                return true;
            }
        }

        buttonNavigateToDate = view.findViewById(R.id.buttonNavigateToDate);
        buttonNavigateToName = view.findViewById(R.id.buttonNavigateToName);

        buttonNavigateToDate.setOnTouchListener(new ButtonOnTouchListener());
        buttonNavigateToName.setOnTouchListener(new ButtonOnTouchListener());
        return view;
    }

}