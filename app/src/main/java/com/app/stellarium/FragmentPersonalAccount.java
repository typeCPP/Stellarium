package com.app.stellarium;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class FragmentPersonalAccount extends Fragment {

    private ImageView arrowEditProfile;

    public static FragmentPersonalAccount newInstance(String param1, String param2) {
        FragmentPersonalAccount fragment = new FragmentPersonalAccount();
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
        View view = inflater.inflate(R.layout.fragment_personal_account, container, false);
        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "RestrictedApi"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Bundle bundle = new Bundle();

                    switch (view.getId()) {
                        case R.id.arrow_edit_profile:
                            Fragment fragment = new FragmentEditPersonalAccount();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();

                            break;

                    }
                }
                return true;
            }
        }
        arrowEditProfile = view.findViewById(R.id.arrow_edit_profile);
        arrowEditProfile.setOnTouchListener(new ButtonOnTouchListener());
        return view;
    }
}