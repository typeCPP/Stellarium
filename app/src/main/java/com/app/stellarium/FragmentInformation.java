package com.app.stellarium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentInformation extends Fragment {

    private TextView titleText;
    private TextView textInformation;

    public static FragmentInformation newInstance(String param1, String param2) {
        FragmentInformation fragment = new FragmentInformation();
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
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setNumberOfPrevFragment();

        titleText = view.findViewById(R.id.title_text);
        textInformation = view.findViewById(R.id.text_information);

        Bundle bundle = getArguments();
        String title = null;
        String description = null;
        if (bundle != null) {
            title = bundle.getString("Name");
            description = bundle.getString("Description");
        }

        if (title != null && description != null) {
            titleText.setText(title);
            textInformation.setText(description);
        }
        return view;
    }
}