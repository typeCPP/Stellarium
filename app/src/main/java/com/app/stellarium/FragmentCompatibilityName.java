package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentCompatibilityName extends Fragment {

    private Bundle bundle;
    private int numberOfButton; //1 - love, 2 - friendship, 3 - work
    private TextView loveButton, friendshipButton, workButton,
            nameWoman, nameMan;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCompatibilityName() {
        // Required empty public constructor
    }


    public static FragmentCompatibilityName newInstance(String param1, String param2) {
        FragmentCompatibilityName fragment = new FragmentCompatibilityName();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compatibility_name, container, false);

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    switch (view.getId()) {
                        case R.id.loveButton:
                            setScaleForButton(loveButton);
                            break;
                        case R.id.friendshipButton:
                            setScaleForButton(friendshipButton);
                            break;
                        case R.id.workButton:Button:
                            setScaleForButton(workButton);
                            break;
                    }
                }
                return true;
            }
        }

        loveButton = view.findViewById(R.id.loveButton);
        loveButton.setScaleY(1.2f);
        loveButton.setScaleX(1.2f);
        friendshipButton = view.findViewById(R.id.friendshipButton);
        workButton = view.findViewById(R.id.workButton);
        nameMan = view.findViewById(R.id.nameMan);
        nameWoman = view.findViewById(R.id.nameWoman);
        String nameManString = null, nameWomanString = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            nameManString = bundle.getString("NameMan");
            nameWomanString = bundle.getString("NameWoman");
        }
        if (nameManString != null && nameWomanString != null) {
            nameMan.setText(nameManString);
            nameWoman.setText(nameWomanString);
        }

        loveButton.setOnTouchListener(new ButtonOnTouchListener());
        friendshipButton.setOnTouchListener(new ButtonOnTouchListener());
        workButton.setOnTouchListener(new ButtonOnTouchListener());
        return view;
    }

    private void setScaleForButton(TextView button) {
        button.setScaleX(1.2f);
        button.setScaleY(1.2f);
        if (button != loveButton) {
            loveButton.setScaleX(1);
            loveButton.setScaleY(1);
            numberOfButton = 1;
        }
        if (button != friendshipButton) {
            friendshipButton.setScaleX(1);
            friendshipButton.setScaleY(1);
            numberOfButton = 2;
        }
        if (button != workButton) {
            workButton.setScaleX(1);
            workButton.setScaleY(1);
            numberOfButton = 3;
        }
    }
}
