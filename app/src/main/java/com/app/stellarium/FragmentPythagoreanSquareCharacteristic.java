package com.app.stellarium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentPythagoreanSquareCharacteristic extends Fragment {
    private TextView titleTextView;
    private TextView descriptionTextView;

    public static FragmentPythagoreanSquareCharacteristic newInstance(String param1, String param2) {
        FragmentPythagoreanSquareCharacteristic fragment = new FragmentPythagoreanSquareCharacteristic();
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
        return inflater.inflate(R.layout.fragment_pythagorean_square_characteristic, container, false);
    }
}