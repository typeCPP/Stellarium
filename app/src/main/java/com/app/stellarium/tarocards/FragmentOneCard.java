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

public class FragmentOneCard extends Fragment {
    private Button buttonStart;
    private TarotShuffleView taroShuffleView;
    private TarotSelectionView taroSelectionView;
    private String path = "android.resource://com.app.stellarium/drawable/";
    private boolean isFirstClickOnCard = true;
    private LoadingDialog loadingDialog;
    private String nameFirstPicture;
    private String nameFirstCard;
    private ArrayList<ImageView> pictures;
    private ImageView first;
    private String descriptionFirstCard;
    private ScrollView scrollView;
    private boolean isReadyToStartAnimation = false;

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
        View view = inflater.inflate(R.layout.fragment_one_card, container, false);
        taroShuffleView = view.findViewById(R.id.tarot_shuffle_view);
        buttonStart = view.findViewById(R.id.buttonStart);
        taroSelectionView = new TarotSelectionView(this.getContext(), 1);
        taroSelectionView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        taroSelectionView.setVisibility(View.GONE);
        RelativeLayout layout = view.findViewById(R.id.layout);
        layout.addView(taroSelectionView);
        ImageView infoButton = view.findViewById(R.id.infoAboutLayoutButton);
        first = view.findViewById(R.id.first_open_image);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.descriprion_card_view, null);
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
                getOneCardFromServer();
                return null;
            }
        });
        getOneCardFromServer();
        taroShuffleView.setOnShuffleListener(() -> {
            taroShuffleView.setVisibility(View.GONE);
            taroSelectionView.setPictures(pictures);
            taroSelectionView.showTarotSelectionView();
            buttonStart.setVisibility(View.GONE);
        });

        scrollView = linearLayout.findViewById(R.id.scroll);

        class ViewOnClickListener implements View.OnClickListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public void onClick(View touchableView) {
                if (touchableView.getId() == R.id.first_open_image) {
                    if (isFirstClickOnCard) {
                        TextView characteristicCardText = linearLayout.findViewById(R.id.characteristic_card);
                        characteristicCardText.setVisibility(View.GONE);
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameFirstCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameFirstPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionFirstCard);
                        layout.addView(linearLayout);
                        first.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                        isFirstClickOnCard = false;
                    } else {
                        scrollView.scrollTo(0, 0);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    }

                } else if (touchableView.getId() == R.id.infoAboutLayoutButton) {
                    Dialog fragment = new DialogInfoAboutLayout(view.getContext(), getString(R.string.description_one_card));
                    fragment.show();
                    fragment.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                } else if (touchableView.getId() == R.id.close) {
                    linearLayout.setVisibility(View.GONE);
                    first.setVisibility(View.VISIBLE);
                    infoButton.setVisibility(View.VISIBLE);
                }
            }
        }

        closeView.setOnClickListener(new ViewOnClickListener());
        first.setOnClickListener(new ViewOnClickListener());
        infoButton.setOnClickListener(new ViewOnClickListener());

        return view;
    }

    public void getOneCardFromServer() {
        loadingDialog.show();
        loadingDialog.startGifAnimation();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Taro oneTaro = getServerResponse();
                if (oneTaro == null) {
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
                            nameFirstPicture = oneTaro.one.pic_name;
                            nameFirstCard = oneTaro.one.name;
                            descriptionFirstCard = oneTaro.one.description;
                            pictures = new ArrayList<>();
                            first.setImageURI(Uri.parse(path + nameFirstPicture));
                            pictures.add(first);
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
            String response = serverConnection.getStringResponseByParameters("taro/?count=1");
            return new Gson().fromJson(response, Taro.class);
        } catch (Exception ignored) {
            return null;
        }
    }
}