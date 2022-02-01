package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.HoroscopePredictionsByPeriodTable;
import com.app.stellarium.database.tables.ZodiacSignsTable;
import com.szugyi.circlemenu.view.CircleLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class FragmentCompatibilitySignSelection extends Fragment {
    private ImageSwitcher circleWoman, circleMan;
    private LinearLayout layout_with_spinner;
    private CircleLayout spinner;
    private Button ariesButton, taurusButton, geminiButton, cancerButton, leoButton, virgoButton,
            libraButton, scorpioButton, sagittariusButton, capricornButton, aquariusButton, piscesButton, nextButton;
    private boolean isWoman;
    private boolean isSelectedWoman;
    private boolean isSelectedMan;
    private float deltaX;
    private float deltaY;
    private PointF actionDownPoint = new PointF(0f, 0f);
    private short touchMoveFactor = 10;
    private FrameLayout mainLayout;
    private Bundle bundle;
    private Animation scaleUp;
    private TextView signTextWoman, signTextMan;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCompatibilitySignSelection() {
        // Required empty public constructor
    }

    public static FragmentCompatibilitySignSelection newInstance(String param1, String param2) {
        FragmentCompatibilitySignSelection fragment = new FragmentCompatibilitySignSelection();
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
        View view = inflater.inflate(R.layout.fragment_compatibility_sign_selection, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        signTextMan = view.findViewById(R.id.sign_text_man);
        signTextWoman = view.findViewById(R.id.sign_text_woman);

        signTextWoman.setText("Не выбрано");
        signTextMan.setText("Не выбрано");
        class ButtonOnTouchListenerSexSelection implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    switch (view.getId()) {
                        case R.id.circle_woman:
                            isWoman = true;

                            break;
                        case R.id.circle_man:
                            isWoman = false;
                            break;
                    }
                    if (isSelectedWoman && isSelectedMan) {
                        nextButton.animate().alpha(0).setDuration(500).setListener(null);
                        layout_with_spinner.setVisibility(View.VISIBLE);
                    }

                    spinner.animate().alpha(1f).setDuration(500).setListener(null);
                }
                return true;
            }
        }

        class ButtonOnTouchListenerChooseSign implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "Recycle", "Range"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.startAnimation(scaleUp);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    actionDownPoint.x = motionEvent.getX();
                    actionDownPoint.y = motionEvent.getY();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    try {
                        Method method = CircleLayout.class.getDeclaredMethod("rotateButtons", float.class);
                        method.setAccessible(true);
                        float p = 2.5f;
                        method.invoke(spinner, p);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    boolean isTouchLength = (Math.abs(motionEvent.getX() - actionDownPoint.x) +
                            Math.abs(motionEvent.getY() - actionDownPoint.y)) < touchMoveFactor;
                    if (isTouchLength) {
                        int idOfWomanSign = 1;
                        int idOfManSign = 1;
                        switch (view.getId()) {
                            case R.id.compAriesButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_aries);
                                    isSelectedWoman = true;
                                    idOfWomanSign=1;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_aries);
                                    isSelectedMan = true;
                                    idOfManSign=1;
                                }
                                break;
                            case R.id.compTaurusButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_taurus);
                                    isSelectedWoman = true;
                                    idOfWomanSign=2;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_taurus);
                                    isSelectedMan = true;
                                    idOfManSign=2;
                                }
                                break;
                            case R.id.compGeminiButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_gemini);
                                    isSelectedWoman = true;
                                    idOfWomanSign=3;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_gemini);
                                    isSelectedMan = true;
                                    idOfManSign=3;
                                }
                                break;
                            case R.id.compCancerButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_cancer);
                                    isSelectedWoman = true;
                                    idOfWomanSign=4;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_cancer);
                                    isSelectedMan = true;
                                    idOfManSign=4;
                                }
                                break;
                            case R.id.compLeoButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_leo);
                                    isSelectedWoman = true;
                                    idOfWomanSign=5;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_leo);
                                    isSelectedMan = true;
                                    idOfManSign=5;
                                }
                                break;
                            case R.id.compVirgoButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_virgo);
                                    isSelectedWoman = true;
                                    idOfWomanSign=6;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_virgo);
                                    isSelectedMan = true;
                                    idOfManSign=6;
                                }
                                break;
                            case R.id.compLibraButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_libra);
                                    isSelectedWoman = true;
                                    idOfWomanSign=7;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_libra);
                                    isSelectedMan = true;
                                    idOfManSign=7;
                                }
                                break;
                            case R.id.compScorpioButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_scorpio);
                                    isSelectedWoman = true;
                                    idOfWomanSign=8;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_scorpio);
                                    isSelectedMan = true;
                                    idOfManSign=8;
                                }
                                break;
                            case R.id.compSagittariusButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_sagittarius);
                                    isSelectedWoman = true;
                                    idOfWomanSign=9;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_sagittarius);
                                    isSelectedMan = true;
                                    idOfManSign=9;
                                }
                                break;
                            case R.id.compCapricornButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_capricorn);
                                    isSelectedWoman = true;
                                    idOfWomanSign=10;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_capricorn);
                                    isSelectedMan = true;
                                    idOfManSign=10;
                                }
                                break;
                            case R.id.compAquariusButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_aquarius);
                                    isSelectedWoman = true;
                                    idOfWomanSign=11;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_aquarius);
                                    isSelectedMan = true;
                                    idOfManSign=11;
                                }
                                break;
                            case R.id.compPiscesButton:
                                if (isWoman) {
                                    circleWoman.setImageResource(R.drawable.comp_pisces);
                                    isSelectedWoman = true;
                                    idOfWomanSign=12;
                                } else {
                                    circleMan.setImageResource(R.drawable.comp_pisces);
                                    isSelectedMan = true;
                                    idOfManSign=12;
                                }
                                break;
                        }
                        spinner.animate().alpha(0f).setDuration(500).setListener(null);
                        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                        SQLiteDatabase database = databaseHelper.getReadableDatabase();

                        Cursor cursor;
                        if (isWoman) {
                            cursor = database.query(ZodiacSignsTable.TABLE_NAME, null,
                                    "SIGN_ID = " + idOfWomanSign,
                                    null, null, null, null);
                            cursor.moveToFirst();
                            String signText = cursor.getString(cursor.getColumnIndex(ZodiacSignsTable.COLUMN_NAME));
                            signTextWoman.setText(signText);
                            bundle.putString("signTextWoman", signText);
                            bundle.putInt("womanSign", idOfWomanSign);
                        } else {
                            cursor = database.query(ZodiacSignsTable.TABLE_NAME, null,
                                    "SIGN_ID = " + idOfManSign,
                                    null, null, null, null);
                            cursor.moveToFirst();
                            String signText = cursor.getString(cursor.getColumnIndex(ZodiacSignsTable.COLUMN_NAME));
                            signTextMan.setText(signText);
                            bundle.putInt("manSign", idOfManSign);
                            bundle.putString("signTextMan", signText);
                        }
                        if (isSelectedWoman && isSelectedMan) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    layout_with_spinner.setVisibility(View.INVISIBLE);
                                }
                            }, 500);

                            nextButton.animate().alpha(1).setDuration(500).setListener(null);
                        }
                    }
                }
                return true;
            }
        }

        class ButtonOnTouchListenerNext implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mainLayout.animate().alpha(0f).setDuration(250).setListener(null);
                    Fragment fragmentCompatibilityZodiac = new FragmentCompatibilityZodiac();
                    fragmentCompatibilityZodiac.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameLayout, fragmentCompatibilityZodiac).commit();
                }
                return true;
            }
        }
        bundle = new Bundle();
        isSelectedMan = false;
        isSelectedWoman = false;
        mainLayout = view.findViewById(R.id.main_layout_sign_selection);
        circleWoman = view.findViewById(R.id.circle_woman);
        circleMan = view.findViewById(R.id.circle_man);
        spinner = view.findViewById(R.id.spinner_zodiac_sign);
        nextButton = view.findViewById(R.id.compButtonNext);
        layout_with_spinner = view.findViewById(R.id.linear_with_spinner);
        circleWoman.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                return imageView;
            }
        });

        circleMan.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                return imageView;
            }
        });
        circleWoman.setImageResource(R.drawable.comp_main_circle);
        circleMan.setImageResource(R.drawable.comp_main_circle);
        Animation inAnimation = new AlphaAnimation(0, 1);
        inAnimation.setDuration(500);
        Animation outAnimation = new AlphaAnimation(1, 0);
        outAnimation.setDuration(500);
        circleWoman.setInAnimation(inAnimation);
        circleMan.setInAnimation(inAnimation);
        circleWoman.setOutAnimation(outAnimation);
        circleMan.setOutAnimation(outAnimation);


        nextButton.setAlpha(0f);
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deltaY = size.y;
        deltaX = size.x;
        spinner.setRadius(deltaX / 2 * 1.25f);
        spinner.setAlpha(0f);

        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) layout_with_spinner.getLayoutParams();
        params2.setMargins(0, (int) (deltaY / 3), 0, 0);
        layout_with_spinner.setLayoutParams(params2);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) spinner.getLayoutParams();
        int deltaForSpinner = (int) (-deltaY / 1.5);
        params.setMargins(0, 0, 0, deltaForSpinner);
        spinner.setLayoutParams(params);


        nextButton.setOnTouchListener(new ButtonOnTouchListenerNext());
        circleWoman.setOnTouchListener(new ButtonOnTouchListenerSexSelection());
        circleMan.setOnTouchListener(new ButtonOnTouchListenerSexSelection());

        ariesButton = view.findViewById(R.id.compAriesButton);
        ariesButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        taurusButton = view.findViewById(R.id.compTaurusButton);
        taurusButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        geminiButton = view.findViewById(R.id.compGeminiButton);
        geminiButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        cancerButton = view.findViewById(R.id.compCancerButton);
        cancerButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        leoButton = view.findViewById(R.id.compLeoButton);
        leoButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        virgoButton = view.findViewById(R.id.compVirgoButton);
        virgoButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        libraButton = view.findViewById(R.id.compLibraButton);
        libraButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        scorpioButton = view.findViewById(R.id.compScorpioButton);
        scorpioButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        sagittariusButton = view.findViewById(R.id.compSagittariusButton);
        sagittariusButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        capricornButton = view.findViewById(R.id.compCapricornButton);
        capricornButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        aquariusButton = view.findViewById(R.id.compAquariusButton);
        aquariusButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());

        piscesButton = view.findViewById(R.id.compPiscesButton);
        piscesButton.setOnTouchListener(new ButtonOnTouchListenerChooseSign());
        return view;
    }
}
