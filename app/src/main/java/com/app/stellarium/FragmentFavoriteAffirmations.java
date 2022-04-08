package com.app.stellarium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FragmentFavoriteAffirmations extends Fragment {

    public static FragmentFavoriteAffirmations newInstance() {
        FragmentFavoriteAffirmations fragment = new FragmentFavoriteAffirmations();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_favorite_affirmations, container, false);
        return view;
    }
}