package com.app.stellarium;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.InformationTable;

public class FragmentListOfInformation extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button affirmationButton, horoscopeButton, taroButton, compatibilityButton,
            moonCalendarButton, numerologicButton, squareOfPythagorasButton, yesOrNoButton;
    private Animation scaleUp;

    public static FragmentListOfInformation newInstance(String param1, String param2) {
        FragmentListOfInformation fragment = new FragmentListOfInformation();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_information, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                    Bundle bundle = new Bundle();

                    int idOfInformationTableElement = 1;
                    switch (view.getId()) {
                        case R.id.affirmationButton:
                            idOfInformationTableElement = 1;
                            break;
                        case R.id.horoscopeButton:
                            idOfInformationTableElement = 2;
                            break;
                        case R.id.taroButton:
                            idOfInformationTableElement = 3;
                            break;
                        case R.id.compatibilityButton:
                            idOfInformationTableElement = 4;
                            break;
                        case R.id.moonCalendarButton:
                            idOfInformationTableElement = 5;
                            break;
                        case R.id.numerologicButton:
                            idOfInformationTableElement = 6;
                            break;
                        case R.id.squareOfPythagorasButton:
                            idOfInformationTableElement = 7;
                            break;
                        case R.id.yesOrNoButton:
                            idOfInformationTableElement = 8;
                            break;
                    }

                    DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                    SQLiteDatabase database = databaseHelper.getReadableDatabase();
                    Cursor cursor = database.query(InformationTable.TABLE_NAME, null,
                            "_id = " + idOfInformationTableElement,
                            null, null, null, null);
                    cursor.moveToFirst();

                    bundle.putString("Name", cursor.getString(cursor.getColumnIndex(InformationTable.COLUMN_NAME)));
                    bundle.putString("Description", cursor.getString(cursor.getColumnIndex(InformationTable.COLUMN_DESCRIPTION)));

                    Fragment fragment = new FragmentInformation();
                    fragment.setArguments(bundle);

                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                            .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
                }
                return true;
            }
        }

        affirmationButton = view.findViewById(R.id.affirmationButton);
        affirmationButton.setOnTouchListener(new ButtonOnTouchListener());

        horoscopeButton = view.findViewById(R.id.horoscopeButton);
        horoscopeButton.setOnTouchListener(new ButtonOnTouchListener());

        taroButton = view.findViewById(R.id.taroButton);
        taroButton.setOnTouchListener(new ButtonOnTouchListener());

        compatibilityButton = view.findViewById(R.id.compatibilityButton);
        compatibilityButton.setOnTouchListener(new ButtonOnTouchListener());

        moonCalendarButton = view.findViewById(R.id.moonCalendarButton);
        moonCalendarButton.setOnTouchListener(new ButtonOnTouchListener());

        numerologicButton = view.findViewById(R.id.numerologicButton);
        numerologicButton.setOnTouchListener(new ButtonOnTouchListener());

        squareOfPythagorasButton = view.findViewById(R.id.squareOfPythagorasButton);
        squareOfPythagorasButton.setOnTouchListener(new ButtonOnTouchListener());

        yesOrNoButton = view.findViewById(R.id.yesOrNoButton);
        yesOrNoButton.setOnTouchListener(new ButtonOnTouchListener());

        return view;
    }
}
