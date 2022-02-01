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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.utils.OnSwipeTouchListener;

public class FragmentInformationAboutSign extends Fragment {
    private Button descriptionButton, characterButton, loveButton, careerButton;
    private Animation scaleUp;
    private int numberOfActiveButton = 1;
    private TextView textView, signTitle;
    private ImageView signImage;
    private Animation rightAnim, leftAnim;
    private boolean isStartPage = true;
    private Bundle bundle;
    private String[] characteristics;
    private HorizontalScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_about_sign, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        class ButtonOnClickListener implements View.OnClickListener {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId", "ResourceType"})
            @Override
            public void onClick(View view) {
                view.startAnimation(scaleUp);
                switch (view.getId()) {
                    case R.id.descriptionButton:
                        updateStateButtons(descriptionButton);
                        break;
                    case R.id.characterButton:
                        updateStateButtons(characterButton);
                        break;
                    case R.id.loveButton:
                        updateStateButtons(loveButton);
                        break;
                    case R.id.careerButton:
                        updateStateButtons(careerButton);
                        break;
                }
            }
        }

        bundle = getArguments();

        if (bundle != null) {
            characteristics = new String[4];
            characteristics[0] = bundle.getString("description");
            characteristics[1] = bundle.getString("character");
            characteristics[2] = bundle.getString("love");
            characteristics[3] = bundle.getString("career");
            signTitle = view.findViewById(R.id.signTitle);
            signTitle.setText(bundle.getString("signName") + " | " + bundle.getString("signPeriod"));

            signImage = view.findViewById(R.id.signImage);
            signImage.setImageResource(bundle.getInt("signPictureDrawableId"));
        }

        descriptionButton = view.findViewById(R.id.descriptionButton);
        descriptionButton.setOnClickListener(new ButtonOnClickListener());


        characterButton = view.findViewById(R.id.characterButton);
        characterButton.setOnClickListener(new ButtonOnClickListener());

        loveButton = view.findViewById(R.id.loveButton);
        loveButton.setOnClickListener(new ButtonOnClickListener());

        careerButton = view.findViewById(R.id.careerButton);
        careerButton.setOnClickListener(new ButtonOnClickListener());

        LinearLayout contentLayout = view.findViewById(R.id.contentLayout);
        activeSwipe(contentLayout);

        textView = view.findViewById(R.id.textInfoAboutSign);
        activeSwipe(textView);

        ScrollView scrollViewVertical = view.findViewById(R.id.scrollViewVertical);
        activeSwipe(scrollViewVertical);

        View firstEmptyView = view.findViewById(R.id.firstEmptyView);
        View secondEmptyView = view.findViewById(R.id.secondEmptyView);
        activeSwipe(firstEmptyView);
        activeSwipe(secondEmptyView);

        rightAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        leftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);

        scrollView = view.findViewById(R.id.scrollView);

        updateStateButtons(descriptionButton);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateStateButtons(@NonNull Button button) {
        int oldNumberOfActiveButton = numberOfActiveButton;
        button.setTextSize(17);
        if (button != descriptionButton) {
            descriptionButton.setTextSize(14);
        } else {
            numberOfActiveButton = 1;
            scrollView.scrollTo(0, 0);
        }
        if (button != characterButton) {
            characterButton.setTextSize(14);
        } else {
            numberOfActiveButton = 2;
            scrollView.scrollTo(R.id.characterButton, 0);
        }
        if (button != loveButton) {
            loveButton.setTextSize(14);
        } else {
            numberOfActiveButton = 3;
            scrollView.scrollTo(R.id.loveButton, 0);
        }
        if (button != careerButton) {
            careerButton.setTextSize(14);
        } else {
            numberOfActiveButton = 4;
            scrollView.scrollTo(R.id.careerButton, 0);
        }
        if (!isStartPage) {
            if (oldNumberOfActiveButton < numberOfActiveButton) {
                textView.startAnimation(rightAnim);
            } else if(oldNumberOfActiveButton > numberOfActiveButton){
                textView.startAnimation(leftAnim);
            }
        }
        isStartPage = false;
        setAttributesForTextView(characteristics[numberOfActiveButton - 1], textView);
    }

    private void activeSwipe(View view) {
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                switch (numberOfActiveButton) {
                    case 2:
                        updateStateButtons(descriptionButton);
                        break;
                    case 3:
                        updateStateButtons(characterButton);
                        break;
                    case 4:
                        updateStateButtons(loveButton);
                        break;
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onSwipeLeft() {
                switch (numberOfActiveButton) {
                    case 1:
                        updateStateButtons(characterButton);
                        break;
                    case 2:
                        updateStateButtons(loveButton);
                        break;
                    case 3:
                        updateStateButtons(careerButton);
                        break;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "WrongConstant"})
    private void setAttributesForTextView(String text, TextView textView) {
        textView.setMaxLines(1);
        textView.setText(text);
        textView.setPadding(10, 30, 10, 30);
    }

}