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


public class FragmentAffirmation extends Fragment {



    public FragmentAffirmation() {
    }

    public static FragmentAffirmation newInstance(String param1, String param2) {
        FragmentAffirmation fragment = new FragmentAffirmation();
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
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setNumberOfPrevFragment();
            activity.hideBottomBar(true);
        }
        View view = inflater.inflate(R.layout.fragment_affirmation, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        MainActivity activity = (MainActivity) getActivity();

        if (activity != null) {
            activity.hideBottomBar(false);
        }
    }
}