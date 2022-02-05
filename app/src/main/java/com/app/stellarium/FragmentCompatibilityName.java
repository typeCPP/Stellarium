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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.CompatibilityNamesTable;
import com.app.stellarium.utils.OnSwipeTouchListener;

public class FragmentCompatibilityName extends Fragment {

    private Bundle bundle;
    private int numberOfButton; //1 - love, 2 - friendship, 3 - work
    private TextView loveButton, friendshipButton, workButton,
            nameWoman, nameMan;
    private Animation rightAnim, leftAnim;
    private boolean isStartPage = true;
    private LinearLayout contentLayout;

    private TextView informationTextView, loveProgressBarText, friendshipProgressBarText,
    workProgressBarText;
    private ProgressBar loveProgressBar, friendshipProgressBar, workProgressBar;

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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"Range", "ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compatibility_name, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setNumberOfPrevFragment();

        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    switch (view.getId()) {
                        case R.id.loveButton:
                            updateStateButtons(loveButton);
                            break;
                        case R.id.friendshipButton:
                            updateStateButtons(friendshipButton);
                            break;
                        case R.id.workButton:
                            Button:
                            updateStateButtons(workButton);
                            break;
                    }
                }
                return true;
            }
        }

        loveButton = view.findViewById(R.id.loveButton);
        loveButton.setScaleY(1.2f);
        loveButton.setScaleX(1.2f);
        activeSwipe(loveButton);
        friendshipButton = view.findViewById(R.id.friendshipButton);
        activeSwipe(friendshipButton);
        workButton = view.findViewById(R.id.workButton);
        activeSwipe(workButton);

        rightAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        leftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);

        updateStateButtons(loveButton);

        contentLayout = view.findViewById(R.id.contentLayout);
        activeSwipe(contentLayout);

        nameMan = view.findViewById(R.id.nameMan);
        nameWoman = view.findViewById(R.id.nameWoman);

        informationTextView = view.findViewById(R.id.informationText);
        loveProgressBarText = view.findViewById(R.id.textLoveProgressBar);
        friendshipProgressBarText = view.findViewById(R.id.textFriendshipProgressBar);
        workProgressBarText = view.findViewById(R.id.textWorkProgressBar);

        loveProgressBar = view.findViewById(R.id.progressBarLove);
        friendshipProgressBar = view.findViewById(R.id.progressBarFriendship);
        workProgressBar = view.findViewById(R.id.progressBarWork);

        String nameManString = null, nameWomanString = null;
        int hashedId = 1;
        Bundle bundle = getArguments();
        if (bundle != null) {
            nameManString = bundle.getString("NameMan");
            nameWomanString = bundle.getString("NameWoman");
            hashedId = bundle.getInt("hashedId");
        }
        if (nameManString != null && nameWomanString != null) {
            nameMan.setText(nameManString);
            nameWoman.setText(nameWomanString);
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(CompatibilityNamesTable.TABLE_NAME, null,
                CompatibilityNamesTable.COLUMN_HASHED_ID + " = " + hashedId,
                null, null, null, null);
        cursor.moveToFirst();

        final String loveText = cursor.getString(cursor.getColumnIndex(CompatibilityNamesTable.COLUMN_COMP_LOVE_TEXT));
        final String friendshipText = cursor.getString(cursor.getColumnIndex(CompatibilityNamesTable.COLUMN_COMP_FRIENDSHIP_TEXT));
        final String workText = cursor.getString(cursor.getColumnIndex(CompatibilityNamesTable.COLUMN_COMP_JOB_TEXT));

        int loveValue = cursor.getInt(cursor.getColumnIndex(CompatibilityNamesTable.COLUMN_COMP_LOVE_VALUE));
        int friendshipValue = cursor.getInt(cursor.getColumnIndex(CompatibilityNamesTable.COLUMN_COMP_FRIENDSHIP_VALUE));
        int workValue = cursor.getInt(cursor.getColumnIndex(CompatibilityNamesTable.COLUMN_COMP_JOB_VALUE));

        loveProgressBarText.setText(loveValue + "%");
        friendshipProgressBarText.setText(friendshipValue + "%");
        workProgressBarText.setText(workValue + "%");

        loveProgressBar.setProgress(loveValue);
        friendshipProgressBar.setProgress(friendshipValue);
        workProgressBar.setProgress(workValue);

        informationTextView.setText(loveText);

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
                        informationTextView.setText(loveText);
                        break;
                    case 2:
                        informationTextView.setText(friendshipText);
                        break;
                    case 3:
                        informationTextView.setText(workText);
                        break;
                }
            }
        }

        loveButton.setOnTouchListener(new ButtonOnTouchListener());
        friendshipButton.setOnTouchListener(new ButtonOnTouchListener());
        workButton.setOnTouchListener(new ButtonOnTouchListener());
        leftAnim.setAnimationListener(new SlideAnimationListener());
        rightAnim.setAnimationListener(new SlideAnimationListener());

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
        if (button != friendshipButton) {
            friendshipButton.setScaleX(1);
            friendshipButton.setScaleY(1);
        } else {
            numberOfButton = 2;
        }
        if (button != workButton) {
            workButton.setScaleX(1);
            workButton.setScaleY(1);
        } else {
            numberOfButton = 3;
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

    private void activeSwipe(View view) {
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                switch (numberOfButton) {
                    case 2:
                        updateStateButtons(loveButton);
                        break;
                    case 3:
                        updateStateButtons(friendshipButton);
                        break;
                }
            }

            public void onSwipeLeft() {
                switch (numberOfButton) {
                    case 1:
                        updateStateButtons(friendshipButton);
                        break;
                    case 2:
                        updateStateButtons(workButton);
                        break;
                }
            }
        });
    }
}
