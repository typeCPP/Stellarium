package com.app.stellarium;

import android.annotation.SuppressLint;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.stellarium.utils.OnSwipeTouchListener;

public class FragmentCompatibilityName extends Fragment {

    private Bundle bundle;
    private int numberOfButton; //1 - love, 2 - friendship, 3 - work
    private TextView loveButton, friendshipButton, jobButton,
            nameWoman, nameMan;
    private Animation rightAnim, leftAnim;
    private boolean isStartPage = true;
    private LinearLayout contentLayout;
    private String loveText, friendshipText, jobText;
    private ScrollView scrollViewVertical;
    private int loveValue, friendshipValue, jobValue;

    private TextView informationTextView, loveProgressBarText, friendshipProgressBarText,
            jobProgressBarText;

    private ProgressBar loveProgressBar, friendshipProgressBar, jobProgressBar;


    public FragmentCompatibilityName() {
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
                            updateStateButtons(jobButton);
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
        jobButton = view.findViewById(R.id.workButton);
        activeSwipe(jobButton);

        rightAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        leftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);

        contentLayout = view.findViewById(R.id.contentLayout);
        activeSwipe(contentLayout);

        nameMan = view.findViewById(R.id.nameMan);
        nameWoman = view.findViewById(R.id.nameWoman);

        informationTextView = view.findViewById(R.id.informationText);
        loveProgressBarText = view.findViewById(R.id.textLoveProgressBar);
        friendshipProgressBarText = view.findViewById(R.id.textFriendshipProgressBar);
        jobProgressBarText = view.findViewById(R.id.textWorkProgressBar);

        loveProgressBar = view.findViewById(R.id.progressBarLove);
        friendshipProgressBar = view.findViewById(R.id.progressBarFriendship);
        jobProgressBar = view.findViewById(R.id.progressBarWork);

        scrollViewVertical = view.findViewById(R.id.scrollViewVertical);
        updateStateButtons(loveButton);

        String nameManString = null, nameWomanString = null;
        Bundle bundle = getArguments();
        if (bundle != null) {
            nameManString = bundle.getString("nameMan");
            nameWomanString = bundle.getString("nameWoman");
            loveText = bundle.getString("loveText");
            friendshipText = bundle.getString("friendText");
            jobText = bundle.getString("jobText");

            loveValue = bundle.getInt("loveValue");
            friendshipValue = bundle.getInt("friendValue");
            jobValue = bundle.getInt("jobValue");
        }
        if (nameManString != null && nameWomanString != null) {
            nameMan.setText(nameManString);
            nameWoman.setText(nameWomanString);
        }

        loveProgressBarText.setText(loveValue + "%");
        friendshipProgressBarText.setText(friendshipValue + "%");
        jobProgressBarText.setText(jobValue + "%");

        loveProgressBar.setProgress(loveValue);
        friendshipProgressBar.setProgress(friendshipValue);
        jobProgressBar.setProgress(jobValue);

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
                        informationTextView.setText(jobText);
                        break;
                }
            }
        }

        loveButton.setOnTouchListener(new ButtonOnTouchListener());
        friendshipButton.setOnTouchListener(new ButtonOnTouchListener());
        jobButton.setOnTouchListener(new ButtonOnTouchListener());
        leftAnim.setAnimationListener(new SlideAnimationListener());
        rightAnim.setAnimationListener(new SlideAnimationListener());

        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateStateButtons(TextView button) {
        scrollViewVertical.scrollTo(0,0);
        button.setScaleX(1.2f);
        button.setScaleY(1.2f);
        int oldNumberOfActiveButton = numberOfButton;
        if (button != loveButton) {
            loveButton.setScaleX(1);
            loveButton.setScaleY(1);
            setProgressAndBackground(loveProgressBar,
                    getResources().getDrawable(R.drawable.progress_bar_background_dark),
                    getResources().getDrawable(R.drawable.progress_bar_progress_dark),
                    loveValue);

        } else {
            numberOfButton = 1;
            setProgressAndBackground(loveProgressBar,
                    getResources().getDrawable(R.drawable.progress_bar_background),
                    getResources().getDrawable(R.drawable.progress_bar_progress),
                    loveValue);

        }
        if (button != friendshipButton) {
            friendshipButton.setScaleX(1);
            friendshipButton.setScaleY(1);
            setProgressAndBackground(friendshipProgressBar,
                    getResources().getDrawable(R.drawable.progress_bar_background_dark),
                    getResources().getDrawable(R.drawable.progress_bar_progress_dark),
                    friendshipValue);
        } else {
            numberOfButton = 2;
            setProgressAndBackground(friendshipProgressBar,
                    getResources().getDrawable(R.drawable.progress_bar_background),
                    getResources().getDrawable(R.drawable.progress_bar_progress),
                    friendshipValue);
        }
        if (button != jobButton) {
            jobButton.setScaleX(1);
            jobButton.setScaleY(1);
            setProgressAndBackground(jobProgressBar,
                    getResources().getDrawable(R.drawable.progress_bar_background_dark),
                    getResources().getDrawable(R.drawable.progress_bar_progress_dark),
                    jobValue);
        } else {
            numberOfButton = 3;
            setProgressAndBackground(jobProgressBar,
                    getResources().getDrawable(R.drawable.progress_bar_background),
                    getResources().getDrawable(R.drawable.progress_bar_progress),
                    jobValue);
        }
        if (!isStartPage) {
            if (oldNumberOfActiveButton < numberOfButton) {
                contentLayout.startAnimation(rightAnim);
            } else if (oldNumberOfActiveButton > numberOfButton) {
                contentLayout.startAnimation(leftAnim);
            }
        }
        isStartPage = false;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setProgressAndBackground(ProgressBar progressBar, Drawable background, Drawable progress, int value) {
        progressBar.setProgress(0);
        progressBar.setBackground(background);
        progressBar.setProgressDrawable(progress);
        progressBar.setProgress(value);
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
                        updateStateButtons(jobButton);
                        break;
                }
            }
        });
    }
}
