package com.app.stellarium.tarocards;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.R;
import com.app.stellarium.dialog.DialogInfoAboutLayout;
import com.app.stellarium.dialog.LoadingDialog;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Taro;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class FragmentPyramidLovers extends Fragment {
    private Button buttonStart;
    private TarotShuffleView taroShuffleView;
    private TarotSelectionView taroSelectionView;
    private String path = "android.resource://com.app.stellarium/drawable/";
    private Integer[] cardsUsedInLayout = new Integer[]{3, 4, 5, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 35, 36};
    private String firstCard = "ВАШ ПАРТНЕР", secondCard = "ОТНОШЕНИЯ", thirdCard = "ВЫ", fourthCard = "БУДУЩЕЕ";
    private String nameFirstPicture, nameFirstCard, descriptionFirstCard,
            nameSecondPicture, nameSecondCard, descriptionSecondCard,
            nameThirdPicture, nameThirdCard, descriptionThirdCard,
            nameFourthPicture, nameFourthCard, descriptionFourthCard;

    private ImageView first, second, third, fourth;
    private ArrayList<ImageView> pictures;
    private LoadingDialog loadingDialog;

    public static FragmentOneCard newInstance(String param1, String param2) {
        FragmentOneCard fragment = new FragmentOneCard();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pyramid_lovers, container, false);

        taroShuffleView = view.findViewById(R.id.tarot_shuffle_view);
        buttonStart = view.findViewById(R.id.buttonStart);
        taroSelectionView = new TarotSelectionView(this.getContext(), 4);
        taroSelectionView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        taroSelectionView.setVisibility(View.GONE);
        RelativeLayout layout = view.findViewById(R.id.layout);
        layout.addView(taroSelectionView);
        @SuppressLint("InflateParams") LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.descriprion_card_view, null);
        linearLayout.setVisibility(View.GONE);
        layout.addView(linearLayout);
        ImageView closeView = linearLayout.findViewById(R.id.close);
        buttonStart.setOnClickListener(view1 -> {
            taroShuffleView.anim();
            view1.setVisibility(View.GONE);
        });
        loadingDialog = new LoadingDialog(view.getContext());
        loadingDialog.setOnClick(new UnaryOperator<Void>() {
            @Override
            public Void apply(Void unused) {
                getFourCardsFromServer();
                return null;
            }
        });

        pictures = new ArrayList<>();
        first = view.findViewById(R.id.first_open_image);
        second = view.findViewById(R.id.second_open_image);
        third = view.findViewById(R.id.third_open_image);
        fourth = view.findViewById(R.id.fourth_open_image);

        getFourCardsFromServer();

        taroShuffleView.setOnShuffleListener(() -> {
            taroShuffleView.setVisibility(View.GONE);
            buttonStart.setVisibility(View.GONE);
            taroSelectionView.setPictures(pictures);
            taroSelectionView.showTarotSelectionView();
        });
        ImageButton infoButton = view.findViewById(R.id.infoAboutLayoutButton);
        System.out.println(infoButton);
        class ViewOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View touchableView, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (touchableView.getId() == R.id.first_open_image && taroSelectionView.countOpenCards == 4) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameFirstCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameFirstPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionFirstCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(firstCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.second_open_image && taroSelectionView.countOpenCards == 4) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameSecondCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameSecondPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionSecondCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(secondCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.third_open_image && taroSelectionView.countOpenCards == 4) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameThirdCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameThirdPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionThirdCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(thirdCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.fourth_open_image && taroSelectionView.countOpenCards == 4) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameFourthCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameFourthPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionFourthCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(fourthCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.infoAboutLayoutButton) {
                        Dialog fragment = new DialogInfoAboutLayout(view.getContext(), getString(R.string.description_pyramid_of_lovers));
                        fragment.show();
                        fragment.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    } else if (touchableView.getId() == R.id.close) {
                        linearLayout.setVisibility(View.GONE);
                        first.setVisibility(View.VISIBLE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        fourth.setVisibility(View.VISIBLE);
                        infoButton.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        }

        infoButton.setOnTouchListener(new ViewOnTouchListener());
        closeView.setOnTouchListener(new ViewOnTouchListener());
        first.setOnTouchListener(new ViewOnTouchListener());
        second.setOnTouchListener(new ViewOnTouchListener());
        third.setOnTouchListener(new ViewOnTouchListener());
        fourth.setOnTouchListener(new ViewOnTouchListener());

        return view;
    }

    public void getFourCardsFromServer() {
        loadingDialog.show();
        loadingDialog.startGifAnimation();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Taro sevenCardsTaro = getServerResponse();
                if (sevenCardsTaro == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.stopGifAnimation();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            nameFirstPicture = sevenCardsTaro.four.first.pic_name;
                            nameFirstCard = sevenCardsTaro.four.first.name;
                            descriptionFirstCard = sevenCardsTaro.four.first.description;

                            nameSecondPicture = sevenCardsTaro.four.second.pic_name;
                            nameSecondCard = sevenCardsTaro.four.second.name;
                            descriptionSecondCard = sevenCardsTaro.four.second.description;

                            nameThirdPicture = sevenCardsTaro.four.third.pic_name;
                            nameThirdCard = sevenCardsTaro.four.third.name;
                            descriptionThirdCard = sevenCardsTaro.four.third.description;

                            nameFourthPicture = sevenCardsTaro.four.fourth.pic_name;
                            nameFourthCard = sevenCardsTaro.four.fourth.name;
                            descriptionFourthCard = sevenCardsTaro.four.fourth.description;

                            pictures = new ArrayList<>();

                            first.setImageURI(Uri.parse(path + nameFirstPicture));
                            pictures.add(first);
                            second.setImageURI(Uri.parse(path + nameSecondPicture));
                            pictures.add(second);
                            third.setImageURI(Uri.parse(path + nameThirdPicture));
                            pictures.add(third);
                            fourth.setImageURI(Uri.parse(path + nameFourthPicture));
                            pictures.add(fourth);

                            loadingDialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

    private Taro getServerResponse() {
        try {
            ServerConnection serverConnection = new ServerConnection();
            String response = serverConnection.getStringResponseByParameters("taro/?count=4");
            return new Gson().fromJson(response, Taro.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}