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

public class FragmentSevenStars extends Fragment {
    private Button buttonStart;
    private TarotShuffleView taroShuffleView;
    private TarotSelectionView taroSelectionView;
    private String path = "android.resource://com.app.stellarium/drawable/";
    private String firstCard = "ОПИСАНИЕ ВАШЕГО ХАРАКТЕРА", secondCard = "КАК ВЕДЕТ СЕБЯ В ЛЮБВИ ИЗБРАННИК",
            thirdCard = "ОПИСАНИЕ ХАРАКТЕРА ВАШЕГО ИЗБРАННИКА",
            fourthCard = "ЧТО БУДЕТ ПОМОГАТЬ ВАМ В ВАШИХ ОТНОШЕНИЯХ",
            fifthCard = "ТАЙНЫЕ СКЛОННОСТИ И МЫСЛИ ВАШЕГО ИЗБРАННИКА",
            sixthCard = "КАКИЕ ОПАСНОСТИ ДЛЯ ВАШЕЙ ЛЮБВИ МОГУТ БЫТЬ",
            seventhCard = "ПЕРСПЕКТИВЫ ВАШИХ ОТНОШЕНИЙ";

    private String nameFirstPicture, nameFirstCard, descriptionFirstCard,
            nameSecondPicture, nameSecondCard, descriptionSecondCard,
            nameThirdPicture, nameThirdCard, descriptionThirdCard,
            nameFourthPicture, nameFourthCard, descriptionFourthCard,
            nameFifthPicture, nameFifthCard, descriptionFifthCard,
            nameSixthPicture, nameSixthCard, descriptionSixthCard,
            nameSeventhPicture, nameSeventhCard, descriptionSeventhCard;
    private ImageView first, second, third, fourth, fifth, sixth, seventh;
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
        View view = inflater.inflate(R.layout.fragment_seven_stars, container, false);

        ImageView infoButton = view.findViewById(R.id.infoAboutLayoutButton);
        taroShuffleView = view.findViewById(R.id.tarot_shuffle_view);
        buttonStart = view.findViewById(R.id.buttonStart);
        taroSelectionView = new TarotSelectionView(this.getContext(), 7);
        taroSelectionView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        taroSelectionView.setVisibility(View.GONE);
        RelativeLayout layout = view.findViewById(R.id.layout);
        layout.addView(taroSelectionView);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.descriprion_card_view, null);
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
                getSevenCardsFromServer();
                return null;
            }
        });

        pictures = new ArrayList<>();
        first = view.findViewById(R.id.first_open_image);
        second = view.findViewById(R.id.second_open_image);
        third = view.findViewById(R.id.third_open_image);
        fourth = view.findViewById(R.id.fourth_open_image);
        fifth = view.findViewById(R.id.fifth_open_image);
        sixth = view.findViewById(R.id.sixth_open_image);
        seventh = view.findViewById(R.id.seventh_open_image);
        getSevenCardsFromServer();
        taroShuffleView.setOnShuffleListener(() -> {
            taroShuffleView.setVisibility(View.GONE);
            taroSelectionView.setPictures(pictures);
            taroSelectionView.showTarotSelectionView();
            buttonStart.setVisibility(View.GONE);
        });

        class ViewOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View touchableView, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (touchableView.getId() == R.id.first_open_image && taroSelectionView.countOpenCards == 7) {
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
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.second_open_image && taroSelectionView.countOpenCards == 7) {
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
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.third_open_image && taroSelectionView.countOpenCards == 7) {
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
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.fourth_open_image && taroSelectionView.countOpenCards == 7) {
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
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.fifth_open_image && taroSelectionView.countOpenCards == 7) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameFifthCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameFifthPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionFifthCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(fifthCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.sixth_open_image && taroSelectionView.countOpenCards == 7) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameSixthCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameSixthPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionSixthCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(sixthCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.seventh_open_image && taroSelectionView.countOpenCards == 7) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameSeventhCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameSeventhPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionSeventhCard);
                        TextView characteristicCard = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCard.setText(seventhCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.infoAboutLayoutButton) {
                        Dialog fragment = new DialogInfoAboutLayout(view.getContext(), getString(R.string.description_seven_stars));
                        fragment.show();
                        fragment.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    } else if (touchableView.getId() == R.id.close) {
                        linearLayout.setVisibility(View.GONE);
                        first.setVisibility(View.VISIBLE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        fourth.setVisibility(View.VISIBLE);
                        fifth.setVisibility(View.VISIBLE);
                        sixth.setVisibility(View.VISIBLE);
                        seventh.setVisibility(View.VISIBLE);
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
        fourth.setOnTouchListener(new ViewOnTouchListener());
        fifth.setOnTouchListener(new ViewOnTouchListener());
        sixth.setOnTouchListener(new ViewOnTouchListener());
        seventh.setOnTouchListener(new ViewOnTouchListener());
        infoButton.setOnTouchListener(new ViewOnTouchListener());
        return view;
    }

    public void getSevenCardsFromServer() {
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
                            nameFirstPicture = sevenCardsTaro.seven.first.pic_name;
                            nameFirstCard = sevenCardsTaro.seven.first.name;
                            descriptionFirstCard = sevenCardsTaro.seven.first.description;

                            nameSecondPicture = sevenCardsTaro.seven.second.pic_name;
                            nameSecondCard = sevenCardsTaro.seven.second.name;
                            descriptionSecondCard = sevenCardsTaro.seven.second.description;

                            nameThirdPicture = sevenCardsTaro.seven.third.pic_name;
                            nameThirdCard = sevenCardsTaro.seven.third.name;
                            descriptionThirdCard = sevenCardsTaro.seven.third.description;

                            nameFourthPicture = sevenCardsTaro.seven.seventh.pic_name;
                            nameFourthCard = sevenCardsTaro.seven.seventh.name;
                            descriptionFourthCard = sevenCardsTaro.seven.seventh.description;

                            nameFifthPicture = sevenCardsTaro.seven.fifth.pic_name;
                            nameFifthCard = sevenCardsTaro.seven.fifth.name;
                            descriptionFifthCard = sevenCardsTaro.seven.fifth.description;

                            nameSixthPicture = sevenCardsTaro.seven.sixth.pic_name;
                            nameSixthCard = sevenCardsTaro.seven.sixth.name;
                            descriptionSixthCard = sevenCardsTaro.seven.sixth.description;

                            nameSeventhPicture = sevenCardsTaro.seven.fourth.pic_name;
                            nameSeventhCard = sevenCardsTaro.seven.fourth.name;
                            descriptionSeventhCard = sevenCardsTaro.seven.fourth.description;

                            pictures = new ArrayList<>();

                            first.setImageURI(Uri.parse(path + nameFirstPicture));
                            pictures.add(first);
                            second.setImageURI(Uri.parse(path + nameSecondPicture));
                            pictures.add(second);
                            third.setImageURI(Uri.parse(path + nameThirdPicture));
                            pictures.add(third);
                            fourth.setImageURI(Uri.parse(path + nameFourthPicture));
                            pictures.add(fourth);
                            fifth.setImageURI(Uri.parse(path + nameFifthPicture));
                            pictures.add(fifth);
                            sixth.setImageURI(Uri.parse(path + nameSixthPicture));
                            pictures.add(sixth);
                            seventh.setImageURI(Uri.parse(path + nameSeventhPicture));
                            pictures.add(seventh);

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
            String response = serverConnection.getStringResponseByParameters("taro/?count=7");
            return new Gson().fromJson(response, Taro.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}