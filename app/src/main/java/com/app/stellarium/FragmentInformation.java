package com.app.stellarium;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInformation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView titleText;
    private TextView textInformation;

    public FragmentInformation() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentInformation newInstance(String param1, String param2) {
        FragmentInformation fragment = new FragmentInformation();
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
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        titleText = view.findViewById(R.id.title_text);
        textInformation = view.findViewById(R.id.text_information);

        Bundle bundle = getArguments();
        String title = null;
        String description = null;
        if (bundle != null) {
            title = bundle.getString("Name");
            description = bundle.getString("Description");
        }

        if(title != null && description != null) {
            titleText.setText(title);
            textInformation.setText(description);
        }
        return view;
    }
}