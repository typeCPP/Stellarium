package com.app.stellarium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentPythagoreanSquare extends Fragment {
    private TextView titleTextView;
    private TextView descriptionTextView;

    public static FragmentPythagoreanSquare newInstance(String param1, String param2) {
        FragmentPythagoreanSquare fragment = new FragmentPythagoreanSquare();
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
        View view = inflater.inflate(R.layout.fragment_pythagorean_square, container, false);
        titleTextView = view.findViewById(R.id.title_pyth_square);
        descriptionTextView = view.findViewById(R.id.description_pyth_square);
        Bundle bundle = getArguments();
        String title = null;
        String description = null;
        if (bundle != null) {
            title = bundle.getString("Title");
            description = bundle.getString("Description");
        }
        if (title != null && description != null) {
            titleTextView.setText(title);
            descriptionTextView.setText(description);
        }
        return view;
    }
}