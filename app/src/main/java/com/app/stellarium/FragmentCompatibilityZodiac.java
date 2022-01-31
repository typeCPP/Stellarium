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
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.CompatibilityZodiacTable;
import com.app.stellarium.database.tables.ZodiacSignsTable;
import com.app.stellarium.utils.OnSwipeTouchListener;


public class FragmentCompatibilityZodiac extends Fragment {
    private FrameLayout mainLayout;
    private ProgressBar progressBarLove, progressBarMarriage, progressBarSex, progressBarFriendship;
    private TextView loveButton, marriageButton, sexButton, friendshipButton,
            informationText, signTextWoman, signTextMan, textLoveProgressBar, textSexProgressBar, textMarriageProgressBar, textFriendshipProgressBar;
    private ImageSwitcher circleWoman, circleMan;
    private int numberOfButton; //1 - love, 2 - sex, 3 - marriage, 4 -friendship

    private Animation rightAnim, leftAnim;
    private boolean isStartPage = true;
    private LinearLayout contentLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCompatibilityZodiac() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCompatibilityZodiac.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCompatibilityZodiac newInstance(String param1, String param2) {
        FragmentCompatibilityZodiac fragment = new FragmentCompatibilityZodiac();
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

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compatibility_zodiac, container, false);

        signTextMan = view.findViewById(R.id.sign_text_man);
        signTextWoman = view.findViewById(R.id.sign_text_woman);
        informationText = view.findViewById(R.id.informationText);
        progressBarLove = view.findViewById(R.id.progressBarLove);
        progressBarSex = view.findViewById(R.id.progressBarSex);
        progressBarMarriage = view.findViewById(R.id.progressBarMarriage);
        progressBarFriendship = view.findViewById(R.id.progressBarFriendship);
        textLoveProgressBar = view.findViewById(R.id.textLoveProgressBar);
        textSexProgressBar = view.findViewById(R.id.textSexProgressBar);
        textMarriageProgressBar = view.findViewById(R.id.textMarriageProgressBar);
        textFriendshipProgressBar = view.findViewById(R.id.textFriendshipProgressBar);
        Bundle bundle = getArguments();

        int numberWomanSign = 1, numberManSign = 1;
        if (bundle != null) {
            numberWomanSign = bundle.getInt("womanSign");
            numberManSign = bundle.getInt("manSign");
            signTextMan.setText(bundle.getString("signTextMan"));
            signTextWoman.setText(bundle.getString("signTextWoman"));
        }
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(CompatibilityZodiacTable.TABLE_NAME, null,
                CompatibilityZodiacTable.COLUMN_FIRST_SIGN_ID + " = " + numberWomanSign + " and " +
                        CompatibilityZodiacTable.COLUMN_SECOND_SIGN_ID + " = " + numberManSign,
                null, null, null, null);
        cursor.moveToFirst();
        final String informationLove = cursor.getString(cursor.getColumnIndex(CompatibilityZodiacTable.COLUMN_COMP_LOVE_TEXT));
        final String informationSex = cursor.getString(cursor.getColumnIndex(CompatibilityZodiacTable.COLUMN_COMP_SEX_TEXT));
        final String informationMarriage = cursor.getString(cursor.getColumnIndex(CompatibilityZodiacTable.COLUMN_COMP_MARRIAGE_TEXT));
        final String informationFriendship = cursor.getString(cursor.getColumnIndex(CompatibilityZodiacTable.COLUMN_COMP_FRIENDSHIP_TEXT));

        final int loveValue = cursor.getInt(cursor.getColumnIndex(CompatibilityZodiacTable.COLUMN_COMP_LOVE_VALUE));
        final int sexValue = cursor.getInt(cursor.getColumnIndex(CompatibilityZodiacTable.COLUMN_COMP_SEX_VALUE));
        final int marriageValue = cursor.getInt(cursor.getColumnIndex(CompatibilityZodiacTable.COLUMN_COMP_MARRIAGE_VALUE));
        final int friendshipValue = cursor.getInt(cursor.getColumnIndex(CompatibilityZodiacTable.COLUMN_COMP_FRIENDSHIP_VALUE));

        progressBarLove.setProgress(loveValue);
        progressBarSex.setProgress(sexValue);
        progressBarMarriage.setProgress(marriageValue);
        progressBarFriendship.setProgress(friendshipValue);

        textLoveProgressBar.setText(loveValue + "%");
        textSexProgressBar.setText(sexValue + "%");
        textMarriageProgressBar.setText(marriageValue + "%");
        textFriendshipProgressBar.setText(friendshipValue + "%");

        informationText.setText(informationLove);

        class SlideAnimationListener implements Animation.AnimationListener {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateInformationText();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            private void updateInformationText() {
                switch (numberOfButton) {
                    case 1:
                        informationText.setText(informationLove);
                        break;
                    case 2:
                        informationText.setText(informationSex);
                        break;
                    case 3:
                        informationText.setText(informationMarriage);
                        break;
                    case 4:
                        informationText.setText(informationFriendship);
                        break;
                }
            }
        }

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    switch (view.getId()) {
                        case R.id.loveButton:
                            updateStateButtons(loveButton);
                            break;
                        case R.id.sexButton:
                            updateStateButtons(sexButton);
                            break;
                        case R.id.marriageButton:
                            updateStateButtons(marriageButton);
                            break;
                        case R.id.friendshipButton:
                            updateStateButtons(friendshipButton);
                            break;
                    }
                }
                return true;
            }
        }

        numberOfButton = 1;
        progressBarLove = view.findViewById(R.id.progressBarLove);
        progressBarSex = view.findViewById(R.id.progressBarSex);
        progressBarMarriage = view.findViewById(R.id.progressBarMarriage);
        progressBarFriendship = view.findViewById(R.id.progressBarFriendship);

        loveButton = view.findViewById(R.id.loveButton);
        activeSwipe(loveButton);
        sexButton = view.findViewById(R.id.sexButton);
        activeSwipe(sexButton);
        marriageButton = view.findViewById(R.id.marriageButton);
        activeSwipe(marriageButton);
        friendshipButton = view.findViewById(R.id.friendshipButton);
        activeSwipe(friendshipButton);
        loveButton.setScaleX(1.2f);
        loveButton.setScaleY(1.2f);

        rightAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        rightAnim.setAnimationListener(new SlideAnimationListener());
        leftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        leftAnim.setAnimationListener(new SlideAnimationListener());

        contentLayout = view.findViewById(R.id.contentLayout);
        activeSwipe(contentLayout);

        informationText = view.findViewById(R.id.informationText);
        signTextWoman = view.findViewById(R.id.sign_text_woman);
        signTextMan = view.findViewById(R.id.sign_text_man);

        mainLayout = view.findViewById(R.id.main_layout_zodiac);
        mainLayout.setAlpha(0f);
        mainLayout.animate().alpha(1f).setDuration(250).setListener(null);

        updateStateButtons(loveButton);

        circleWoman = view.findViewById(R.id.circle_woman);
        circleMan = view.findViewById(R.id.circle_man);

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

        addSign(circleWoman, numberWomanSign);
        addSign(circleMan, numberManSign);
        loveButton.setOnTouchListener(new ButtonOnTouchListener());
        sexButton.setOnTouchListener(new ButtonOnTouchListener());
        marriageButton.setOnTouchListener(new ButtonOnTouchListener());
        friendshipButton.setOnTouchListener(new ButtonOnTouchListener());
        return view;
    }

    private void updateStateButtons(TextView button) {
        button.setScaleX(1.2f);
        button.setScaleY(1.2f);
        int oldNumberOfActiveButton = numberOfButton;
        if (button != loveButton) {
            loveButton.setScaleX(1);
            loveButton.setScaleY(1);
        } else {
            numberOfButton = 1;
        }
        if (button != sexButton) {
            sexButton.setScaleX(1);
            sexButton.setScaleY(1);
        } else {
            numberOfButton = 2;
        }
        if (button != marriageButton) {
            marriageButton.setScaleX(1);
            marriageButton.setScaleY(1);
        } else {
            numberOfButton = 3;
        }
        if (button != friendshipButton) {
            friendshipButton.setScaleX(1);
            friendshipButton.setScaleY(1);
        } else {
            numberOfButton = 4;
        }
        if (!isStartPage) {
            if (oldNumberOfActiveButton < numberOfButton) {
                contentLayout.startAnimation(rightAnim);
            } else {
                contentLayout.startAnimation(leftAnim);

            }
        }
        isStartPage = false;
    }

    private void addSign(ImageSwitcher circle, int numberSign) {
        switch (numberSign) {
            case 1:
                circle.setImageResource(R.drawable.comp_aries);
                break;
            case 2:
                circle.setImageResource(R.drawable.comp_taurus);
                break;
            case 3:
                circle.setImageResource(R.drawable.comp_gemini);
                break;
            case 4:
                circle.setImageResource(R.drawable.comp_cancer);
                break;
            case 5:
                circle.setImageResource(R.drawable.comp_leo);
                break;
            case 6:
                circle.setImageResource(R.drawable.comp_virgo);
                break;
            case 7:
                circle.setImageResource(R.drawable.comp_libra);
                break;
            case 8:
                circle.setImageResource(R.drawable.comp_scorpio);
                break;
            case 9:
                circle.setImageResource(R.drawable.comp_sagittarius);
                break;
            case 10:
                circle.setImageResource(R.drawable.comp_capricorn);
                break;
            case 11:
                circle.setImageResource(R.drawable.comp_aquarius);
                break;
            case 12:
                circle.setImageResource(R.drawable.comp_pisces);
                break;
        }
    }

    private void activeSwipe(View view) {
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                switch (numberOfButton) {
                    case 2:
                        updateStateButtons(loveButton);
                        break;
                    case 3:
                        updateStateButtons(sexButton);
                        break;
                    case 4:
                        updateStateButtons(marriageButton);
                        break;
                }

            }

            public void onSwipeLeft() {
                switch (numberOfButton) {
                    case 1:
                        updateStateButtons(sexButton);
                        break;
                    case 2:
                        updateStateButtons(marriageButton);
                        break;
                    case 3:
                        updateStateButtons(friendshipButton);
                        break;
                }
            }
        });
    }
}