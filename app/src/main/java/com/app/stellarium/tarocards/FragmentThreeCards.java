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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

public class FragmentThreeCards extends Fragment {
    private Button buttonStart;
    private TarotShuffleView taroShuffleView;
    private TarotSelectionView taroSelectionView;
    private String path = "android.resource://com.app.stellarium/drawable/";
    private String firstCard = "ПРОШЛОЕ", secondCard = "НАСТОЯЩЕЕ", thirdCard = "БУДУЩЕЕ";
    private LoadingDialog loadingDialog;
    private String nameFirstPicture, nameFirstCard, descriptionFirstCard, nameSecondPicture,
            nameSecondCard, descriptionSecondCard, nameThirdPicture, nameThirdCard, descriptionThirdCard;
    private ArrayList<ImageView> pictures;
    private ImageView first, second, third;
    private ScrollView scrollView;
    private boolean isReadyToStartAnimation = false;

    public static FragmentThreeCards newInstance(String param1, String param2) {
        FragmentThreeCards fragment = new FragmentThreeCards();
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
        View view = inflater.inflate(R.layout.fragment_three_cards, container, false);

        taroShuffleView = view.findViewById(R.id.tarot_shuffle_view);
        buttonStart = view.findViewById(R.id.buttonStart);
        taroSelectionView = new TarotSelectionView(this.getContext(), 3);
        taroSelectionView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        taroSelectionView.setVisibility(View.GONE);
        RelativeLayout layout = view.findViewById(R.id.layout);
        ImageView infoButton = view.findViewById(R.id.infoAboutLayoutButton);
        layout.addView(taroSelectionView);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.descriprion_card_view, null);
        linearLayout.setVisibility(View.GONE);
        layout.addView(linearLayout);
        ImageView closeView = linearLayout.findViewById(R.id.close);
        buttonStart.setOnClickListener(view1 -> {
            if(isReadyToStartAnimation) {
                taroShuffleView.anim();
                view1.setVisibility(View.GONE);
            } else {
                Toast.makeText(view.getContext(), "Ошибка соединения с сервером.", Toast.LENGTH_LONG).show();
            }
        });
        loadingDialog = new LoadingDialog(view.getContext());
        loadingDialog.setOnClick(new UnaryOperator<Void>() {
            @Override
            public Void apply(Void unused) {
                getThreeCardsFromServer();
                return null;
            }
        });

        pictures = new ArrayList<>();
        first = view.findViewById(R.id.first_open_image);
        second = view.findViewById(R.id.second_open_image);
        third = view.findViewById(R.id.third_open_image);
        getThreeCardsFromServer();

        taroShuffleView.setOnShuffleListener(() -> {
            taroShuffleView.setVisibility(View.GONE);
            taroSelectionView.setPictures(pictures);
            taroSelectionView.showTarotSelectionView();
            buttonStart.setVisibility(View.GONE);
        });

        scrollView = view.findViewById(R.id.scroll);

        class ViewOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View touchableView, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (touchableView.getId() == R.id.first_open_image && taroSelectionView.countOpenCards == 3) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameFirstCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameFirstPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionFirstCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(firstCard);
                        scrollView.scrollTo(0, 0);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.second_open_image && taroSelectionView.countOpenCards == 3) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameSecondCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameSecondPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionSecondCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(secondCard);
                        scrollView.scrollTo(0, 0);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.third_open_image && taroSelectionView.countOpenCards == 3) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameThirdCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameThirdPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionThirdCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(thirdCard);
                        scrollView.scrollTo(0, 0);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.infoAboutLayoutButton) {
                        Dialog fragment = new DialogInfoAboutLayout(view.getContext(), getString(R.string.description_three_cards));
                        fragment.show();
                        fragment.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    } else if (touchableView.getId() == R.id.close) {
                        linearLayout.setVisibility(View.GONE);
                        first.setVisibility(View.VISIBLE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        infoButton.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        }

        closeView.setOnTouchListener(new ViewOnTouchListener());
        first.setOnTouchListener(new ViewOnTouchListener());
        second.setOnTouchListener(new ViewOnTouchListener());
        third.setOnTouchListener(new ViewOnTouchListener());
        infoButton.setOnTouchListener(new ViewOnTouchListener());

        return view;
    }

    public void getThreeCardsFromServer() {
        loadingDialog.show();
        loadingDialog.startGifAnimation();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Taro threeCardsTaro = getServerResponse();
                if (threeCardsTaro == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.stopGifAnimation();
                            isReadyToStartAnimation = false;
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            nameFirstPicture = threeCardsTaro.three.first.pic_name;
                            nameFirstCard = threeCardsTaro.three.first.name;
                            descriptionFirstCard = threeCardsTaro.three.first.description;

                            nameSecondPicture = threeCardsTaro.three.second.pic_name;
                            nameSecondCard = threeCardsTaro.three.second.name;
                            descriptionSecondCard = threeCardsTaro.three.second.description;

                            nameThirdPicture = threeCardsTaro.three.third.pic_name;
                            nameThirdCard = threeCardsTaro.three.third.name;
                            descriptionThirdCard = threeCardsTaro.three.third.description;

                            pictures = new ArrayList<>();

                            first.setImageURI(Uri.parse(path + nameFirstPicture));
                            pictures.add(first);
                            second.setImageURI(Uri.parse(path + nameSecondPicture));
                            pictures.add(second);
                            third.setImageURI(Uri.parse(path + nameThirdPicture));
                            pictures.add(third);
                            isReadyToStartAnimation = true;
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
            String response = serverConnection.getStringResponseByParameters("taro/?count=3");
            return new Gson().fromJson(response, Taro.class);
        } catch (Exception ignored) {
            return null;
        }
    }

}