package com.app.stellarium.tarocards;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.app.stellarium.R;
import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.CompatibilityNamesTable;
import com.app.stellarium.database.tables.TaroCardsTable;
import com.app.stellarium.dialog.DialogInfoAboutLayout;

import java.util.ArrayList;

public class FragmentOneCard extends Fragment {
    private Button buttonStart;
    private TarotShuffleView taroShuffleView;
    private TarotSelectionView taroSelectionView;
    private String path = "android.resource://com.app.stellarium/drawable/";
    private boolean isFirstClickOnCard = true;

    public static FragmentOneCard newInstance(String param1, String param2) {
        FragmentOneCard fragment = new FragmentOneCard();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        ImageButton infoButton = view.findViewById(R.id.infoAboutLayoutButton);
        ImageView first = view.findViewById(R.id.first_open_image);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.descriprion_card_view, null);
        ImageView closeView = linearLayout.findViewById(R.id.close);
        buttonStart.setOnClickListener(view1 -> taroShuffleView.anim());

        int firstCardId = (int) (Math.random() * (78));

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(TaroCardsTable.TABLE_NAME, null,
                CompatibilityNamesTable.COLUMN_ID + " = " + firstCardId,
                null, null, null, null);
        cursor.moveToFirst();
        @SuppressLint("Range") final String nameFirstPicture = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_PICTURE_NAME));
        @SuppressLint("Range") final String nameFirstCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_NAME));
        @SuppressLint("Range") final String descriptionFirstCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_DESCRIPTION_ONE_CARD));
        ArrayList<ImageView> pictures = new ArrayList<>();
        first.setImageURI(Uri.parse(path + nameFirstPicture));
        pictures.add(first);
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
                    if (touchableView.getId() == R.id.first_open_image) {
                        if (isFirstClickOnCard) {
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
                return true;
            }
        }

        closeView.setOnTouchListener(new ViewOnTouchListener());
        first.setOnTouchListener(new ViewOnTouchListener());
        infoButton.setOnTouchListener(new ViewOnTouchListener());

        return view;
    }
}