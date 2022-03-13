package com.app.stellarium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FragmentPersonalAccount extends Fragment {

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

        return view;
    }

}