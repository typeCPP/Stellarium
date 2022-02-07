package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class FragmentCompatibilityMenu extends Fragment {

    private Button buttonNavigateToName;
    private Button buttonNavigateToDate;
    private Animation scaleUp;
    private Animation rightAnim, leftAnim;
    private boolean isStartPage = true;

    public static FragmentCompatibilityMenu newInstance(String param1, String param2) {
        FragmentCompatibilityMenu fragment = new FragmentCompatibilityMenu();
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
        View view = inflater.inflate(R.layout.fragment_compatibility_menu, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setNumberOfPrevFragment();

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
                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                            .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
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