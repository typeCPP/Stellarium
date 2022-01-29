package com.app.stellarium;

import android.annotation.SuppressLint;
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


public class FragmentPythagoreanSquareHomePage extends Fragment {

    private LinearLayout layoutDate;
    private TextView editTextDate;
    private FrameLayout buttonPersonality, buttonHealth, buttonLuck,
            buttonEnergy, buttonLogic, buttonDuty, buttonInterest,
            buttonLabor, buttonMindMemory;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentPythagoreanSquareHomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPythagoreanSquareHomePage newInstance(String param1, String param2) {
        FragmentPythagoreanSquareHomePage fragment = new FragmentPythagoreanSquareHomePage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View view = inflater.inflate(R.layout.fragment_pythagorean_square_home_page, container, false);
        layoutDate = view.findViewById(R.id.date_layout_second);
        editTextDate = view.findViewById(R.id.pythagorean_date_home_page);
        layoutDate.setAlpha(0f);
        layoutDate.animate().alpha(1f).setDuration(100).setListener(null);
        Bundle bundle = getArguments();
        String date = null;
        if (bundle != null) {
            date = bundle.getString("Date");
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
                            description = "офигенный характер 10/10";
                            break;
                        case R.id.ps_health:
                            idOfPythagoreanSquareElement = 2;
                            title = "ЗДОРОВЬЕ";
                            description = "сопьешься через 10 лет и сдохнешь";
                            break;
                        case R.id.ps_luck:
                            idOfPythagoreanSquareElement = 3;
                            title = "ВЕЗЕНИЕ";
                            description = "везение как у алены когда ее спалили с наушником на экзамене";
                            break;
                        case R.id.ps_energy:
                            idOfPythagoreanSquareElement = 4;
                            title = "ЭНЕРГИЯ";
                            description = "заводной апельсин тын тын тыр ры ры ты пы";
                            break;
                        case R.id.ps_logic:
                            idOfPythagoreanSquareElement = 5;
                            title = "ЛОГИКА";
                            description = "гении зачастую становятся отвергнутыми обществом";
                            break;
                        case R.id.ps_duty:
                            idOfPythagoreanSquareElement = 6;
                            title = "ДОЛГ";
                            description = "ты никому не должен(нужен) помни это с дества";
                            break;
                        case R.id.ps_interest:
                            idOfPythagoreanSquareElement = 7;
                            title = "ИНТЕРЕС";
                            description = "как у нас к вышмату";
                            break;
                        case R.id.ps_labor:
                            idOfPythagoreanSquareElement = 8;
                            title = "ТРУД";
                            description = "трудиться будешь на заводе";
                            break;
                        case R.id.ps_mind_memory:
                            idOfPythagoreanSquareElement = 9;
                            title = "УМ, ПАМЯТЬ";
                            description = "какой сегодня день";
                            break;

                    }
                    bundle.putString("Title", title);
                    bundle.putString("Description", description);
                    layoutDate.animate().alpha(0f).setDuration(200).setListener(null);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Fragment fragmentPythagoreanSquare = new FragmentPythagoreanSquare();
                            fragmentPythagoreanSquare.setArguments(bundle);
                            getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentPythagoreanSquare).commit();
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
}
