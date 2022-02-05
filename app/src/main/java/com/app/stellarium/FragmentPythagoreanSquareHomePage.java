package com.app.stellarium;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.PythagoreanSquareTable;


public class FragmentPythagoreanSquareHomePage extends Fragment {

    private LinearLayout layoutDate;
    private TextView editTextDate;
    private FrameLayout buttonPersonality, buttonHealth, buttonLuck,
            buttonEnergy, buttonLogic, buttonDuty, buttonInterest,
            buttonLabor, buttonMindMemory;
    private TextView characterNumberTextView, energyNumberTextView, healthNumberTextView,
            logicNumberTextView, interestNumberTextView, laborNumberTextView, luckNumberTextView,
            dutyNumberTextView, mindNumberTextView;
    private int[] matrixValues;

    public static FragmentPythagoreanSquareHomePage newInstance(String param1, String param2) {
        FragmentPythagoreanSquareHomePage fragment = new FragmentPythagoreanSquareHomePage();
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
        View view = inflater.inflate(R.layout.fragment_pythagorean_square_home_page, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setNumberOfPrevFragment();

        layoutDate = view.findViewById(R.id.date_layout_second);
        editTextDate = view.findViewById(R.id.pythagorean_date_home_page);
        layoutDate.setAlpha(0f);
        layoutDate.animate().alpha(1f).setDuration(100).setListener(null);
        Bundle bundle = getArguments();
        String date = null;
        if (bundle != null) {
            date = bundle.getString("Date");
            matrixValues = bundle.getIntArray("matrixValues");

            characterNumberTextView = view.findViewById(R.id.characterNumberText);
            characterNumberTextView.setText(getCharacteristicNumber(1, matrixValues[1]));

            energyNumberTextView = view.findViewById(R.id.energyNumberText);
            energyNumberTextView.setText(getCharacteristicNumber(2, matrixValues[2]));

            interestNumberTextView = view.findViewById(R.id.interestNumberText);
            interestNumberTextView.setText(getCharacteristicNumber(3, matrixValues[3]));

            healthNumberTextView = view.findViewById(R.id.healthNumberText);
            healthNumberTextView.setText(getCharacteristicNumber(4, matrixValues[4]));

            logicNumberTextView = view.findViewById(R.id.logicNumberText);
            logicNumberTextView.setText(getCharacteristicNumber(5, matrixValues[5]));

            laborNumberTextView = view.findViewById(R.id.laborNumberText);
            laborNumberTextView.setText(getCharacteristicNumber(6, matrixValues[6]));

            luckNumberTextView = view.findViewById(R.id.luckNumberText);
            luckNumberTextView.setText(getCharacteristicNumber(7, matrixValues[7]));

            dutyNumberTextView = view.findViewById(R.id.dutyNumberText);
            dutyNumberTextView.setText(getCharacteristicNumber(8, matrixValues[8]));

            mindNumberTextView = view.findViewById(R.id.mindNumberText);
            mindNumberTextView.setText(getCharacteristicNumber(9, matrixValues[9]));
        }
        if (date != null) {
            editTextDate.setText(date);
        }

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Bundle bundle = new Bundle();
                    String title = null;
                    String description = null;

                    int idOfPythagoreanSquareElement = 1;
                    switch (view.getId()) {
                        case R.id.ps_personality:
                            idOfPythagoreanSquareElement = 1;
                            title = "ХАРАКТЕР";
                            break;
                        case R.id.ps_energy:
                            idOfPythagoreanSquareElement = 2;
                            title = "ЭНЕРГИЯ";
                            break;
                        case R.id.ps_interest:
                            idOfPythagoreanSquareElement = 3;
                            title = "ИНТЕРЕС";
                            break;
                        case R.id.ps_health:
                            idOfPythagoreanSquareElement = 4;
                            title = "ЗДОРОВЬЕ";
                            break;
                        case R.id.ps_logic:
                            idOfPythagoreanSquareElement = 5;
                            title = "ЛОГИКА";
                            break;
                        case R.id.ps_labor:
                            idOfPythagoreanSquareElement = 6;
                            title = "ТРУД";
                            break;
                        case R.id.ps_luck:
                            idOfPythagoreanSquareElement = 7;
                            title = "ВЕЗЕНИЕ";
                            break;
                        case R.id.ps_duty:
                            idOfPythagoreanSquareElement = 8;
                            title = "ДОЛГ";
                            break;
                        case R.id.ps_mind_memory:
                            idOfPythagoreanSquareElement = 9;
                            title = "УМ, ПАМЯТЬ";
                            break;

                    }
                    description = getDescriptionFromDatabaseByNumber(idOfPythagoreanSquareElement, matrixValues[idOfPythagoreanSquareElement]);
                    bundle.putString("Title", title);
                    bundle.putString("Description", description);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Fragment fragmentPythagoreanSquare = new FragmentPythagoreanSquare();
                            fragmentPythagoreanSquare.setArguments(bundle);
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in,R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragmentPythagoreanSquare).commit();
                        }
                    }, 200);
                }
                return true;
            }
        }
        buttonPersonality = view.findViewById(R.id.ps_personality);
        buttonPersonality.setOnTouchListener(new ButtonOnTouchListener());

        buttonHealth = view.findViewById(R.id.ps_health);
        buttonHealth.setOnTouchListener(new ButtonOnTouchListener());

        buttonLuck = view.findViewById(R.id.ps_luck);
        buttonLuck.setOnTouchListener(new ButtonOnTouchListener());

        buttonEnergy = view.findViewById(R.id.ps_energy);
        buttonEnergy.setOnTouchListener(new ButtonOnTouchListener());

        buttonLogic = view.findViewById(R.id.ps_logic);
        buttonLogic.setOnTouchListener(new ButtonOnTouchListener());

        buttonDuty = view.findViewById(R.id.ps_duty);
        buttonDuty.setOnTouchListener(new ButtonOnTouchListener());

        buttonInterest = view.findViewById(R.id.ps_interest);
        buttonInterest.setOnTouchListener(new ButtonOnTouchListener());

        buttonLabor = view.findViewById(R.id.ps_labor);
        buttonLabor.setOnTouchListener(new ButtonOnTouchListener());

        buttonMindMemory = view.findViewById(R.id.ps_mind_memory);
        buttonMindMemory.setOnTouchListener(new ButtonOnTouchListener());
        return view;
    }

    private String getCharacteristicNumber(int number, int count) {
        if (count == 0) {
            return "---";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            stringBuilder.append(number);
        }
        return stringBuilder.toString();
    }

    @SuppressLint("Range")
    private String getDescriptionFromDatabaseByNumber(int number, int count) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(PythagoreanSquareTable.TABLE_NAME, null,
                "NUMBER = " + number,
                null, null, null, null);
        cursor.moveToFirst();
        String description = "";
        if (count == 0) {
            description = cursor.getString(cursor.getColumnIndex(PythagoreanSquareTable.COLUMN_NO_NUMBER));
        }
        if (count == 1) {
            description = cursor.getString(cursor.getColumnIndex(PythagoreanSquareTable.COLUMN_ONE_NUMBER));
        }
        if (count == 2) {
            description = cursor.getString(cursor.getColumnIndex(PythagoreanSquareTable.COLUMN_TWO_NUMBERS));
        }
        if (count == 3) {
            description = cursor.getString(cursor.getColumnIndex(PythagoreanSquareTable.COLUMN_THREE_NUMBERS));
        }
        if (count == 4) {
            description = cursor.getString(cursor.getColumnIndex(PythagoreanSquareTable.COLUMN_FOUR_NUMBERS));
        }
        if (count == 5) {
            description = cursor.getString(cursor.getColumnIndex(PythagoreanSquareTable.COLUMN_FIVE_NUMBERS));
        }
        if (count >= 6) {
            description = cursor.getString(cursor.getColumnIndex(PythagoreanSquareTable.COLUMN_SIX_NUMBERS));
        }
        return description;
    }
}
