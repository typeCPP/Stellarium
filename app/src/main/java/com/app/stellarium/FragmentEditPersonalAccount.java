package com.app.stellarium;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

public class FragmentEditPersonalAccount extends Fragment {

    public FragmentEditPersonalAccount() {
        // Required empty public constructor
    }

    public static FragmentEditPersonalAccount newInstance(String param1, String param2) {
        FragmentEditPersonalAccount fragment = new FragmentEditPersonalAccount();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_personal_account, container, false);
        return view;
    }
}