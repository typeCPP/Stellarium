package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.utils.OnSwipeTouchListener;


public class FragmentNumerology extends Fragment {

    private Button generalCharacteristicButton, advantagesButton, disadvantagesButton, purposeButton;
    private Animation rightAnim, leftAnim;
    private Animation scaleUp;
    private int numberOfActiveButton = 1;
    private TextView textView, numerologyTitle;
    private boolean isStartPage = true;
    private HorizontalScrollView scrollView;
    private ScrollView scrollViewVertical;
    private int numerologyNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_numerology, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setNumberOfPrevFragment(0);
        }

        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        numerologyTitle = view.findViewById(R.id.numerology_title);
        Bundle bundle = getArguments();
        if (bundle != null) {
            numerologyNumber = bundle.getInt("numerologyNumber");
            numerologyTitle.setText("Число судьбы - " + numerologyNumber);
        }
        class ButtonOnClickListener implements View.OnClickListener {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "ResourceType"})
            @Override
            public void onClick(View view) {
                view.startAnimation(scaleUp);
                switch (view.getId()) {
                    case R.id.generalCharacteristicButton:
                        updateStateButtons(generalCharacteristicButton);
                        break;
                    case R.id.advantagesButton:
                        updateStateButtons(advantagesButton);
                        break;
                    case R.id.disadvantagesButton:
                        updateStateButtons(disadvantagesButton);
                        break;
                    case R.id.purposeButton:
                        updateStateButtons(purposeButton);
                        break;
                }
            }
        }

        class SlideAnimationListener implements Animation.AnimationListener {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scrollViewVertical.scrollTo(0, 0);
                textView.setText(getDescriptionFromBundle(numberOfActiveButton, bundle));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        }

        generalCharacteristicButton = view.findViewById(R.id.generalCharacteristicButton);
        generalCharacteristicButton.setOnClickListener(new ButtonOnClickListener());


        advantagesButton = view.findViewById(R.id.advantagesButton);
        advantagesButton.setOnClickListener(new ButtonOnClickListener());

        disadvantagesButton = view.findViewById(R.id.disadvantagesButton);
        disadvantagesButton.setOnClickListener(new ButtonOnClickListener());

        purposeButton = view.findViewById(R.id.purposeButton);
        purposeButton.setOnClickListener(new ButtonOnClickListener());

        scrollViewVertical = view.findViewById(R.id.scrollViewVertical);
        activeSwipe(scrollViewVertical);

        textView = view.findViewById(R.id.typesOfPredictionsNumerology);
        textView.setText(getDescriptionFromBundle(numberOfActiveButton, bundle));
        activeSwipe(textView);

        rightAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        leftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        leftAnim.setAnimationListener(new SlideAnimationListener());
        rightAnim.setAnimationListener(new SlideAnimationListener());

        scrollView = view.findViewById(R.id.scrollView);

        updateStateButtons(generalCharacteristicButton);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateStateButtons(@NonNull Button button) {
        int oldNumberOfActiveButton = numberOfActiveButton;
        button.setTextSize(17);
        if (button != generalCharacteristicButton) {
            generalCharacteristicButton.setTextSize(14);
        } else {
            numberOfActiveButton = 1;
            scrollView.scrollTo(0, 0);
        }
        if (button != advantagesButton) {
            advantagesButton.setTextSize(14);
        } else {
            numberOfActiveButton = 2;
            scrollView.scrollTo(R.id.advantagesButton, 0);
        }
        if (button != disadvantagesButton) {
            disadvantagesButton.setTextSize(14);
        } else {
            numberOfActiveButton = 3;
            scrollView.scrollTo(R.id.disadvantagesButton, 0);
        }
        if (button != purposeButton) {
            purposeButton.setTextSize(14);
        } else {
            numberOfActiveButton = 4;
            scrollView.scrollTo(R.id.purposeButton, 0);
        }
        if (!isStartPage) {
            if (oldNumberOfActiveButton < numberOfActiveButton) {
                textView.startAnimation(rightAnim);
            } else if (oldNumberOfActiveButton > numberOfActiveButton) {
                textView.startAnimation(leftAnim);
            }
        }
        isStartPage = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "WrongConstant"})
    private void setAttributesForTextView(String text, TextView textView) {
        textView.setMaxLines(1);
        textView.setText(text);
        textView.setPadding(20, 30, 20, 30);
    }

    private void activeSwipe(View view) {
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                switch (numberOfActiveButton) {
                    case 2:
                        updateStateButtons(generalCharacteristicButton);
                        break;
                    case 3:
                        updateStateButtons(advantagesButton);
                        break;
                    case 4:
                        updateStateButtons(disadvantagesButton);
                        break;
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onSwipeLeft() {
                switch (numberOfActiveButton) {
                    case 1:
                        updateStateButtons(advantagesButton);
                        break;
                    case 2:
                        updateStateButtons(disadvantagesButton);
                        break;
                    case 3:
                        updateStateButtons(purposeButton);
                        break;
                }
            }
        });
    }

    private String getDescriptionFromBundle(int numberOfButton, Bundle bundle) {
        String description = "";
        if (numberOfButton == 1) {
            description = bundle.getString("general");
        }
        if (numberOfButton == 2) {
            description = bundle.getString("dignities");
        }
        if (numberOfButton == 3) {
            description = bundle.getString("disadvantages");
        }
        if (numberOfButton == 4) {
            description = bundle.getString("fate");
        }
        return description;
    }
}
