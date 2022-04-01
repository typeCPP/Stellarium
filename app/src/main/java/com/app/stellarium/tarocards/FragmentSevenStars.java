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

public class FragmentSevenStars extends Fragment {
    private Button buttonStart;
    private TarotShuffleView taroShuffleView;
    private TarotSelectionView taroSelectionView;
    private String path = "android.resource://com.app.stellarium/drawable/";

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
        View view = inflater.inflate(R.layout.fragment_seven_stars, container, false);

        ImageButton infoButton = view.findViewById(R.id.infoAboutLayoutButton);
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
        buttonStart.setOnClickListener(view1 -> taroShuffleView.anim());

        int firstCardId = (int) (Math.random() * (78));
        int secondCardId = firstCardId;
        while (secondCardId == firstCardId) {
            secondCardId = (int) (Math.random() * (78));
        }
        int thirdCardId = secondCardId;
        while (thirdCardId == firstCardId || thirdCardId == secondCardId) {
            thirdCardId = (int) (Math.random() * (78));
        }
        int fourthCardId = thirdCardId;
        while (fourthCardId == firstCardId || fourthCardId == secondCardId || fourthCardId == thirdCardId) {
            fourthCardId = (int) (Math.random() * (78));
        }
        int fifthCardId = fourthCardId;
        while (fifthCardId == firstCardId || fifthCardId == secondCardId || fifthCardId == thirdCardId || fifthCardId == fourthCardId) {
            fifthCardId = (int) (Math.random() * (78));
        }
        int sixthCardId = fifthCardId;
        while (sixthCardId == firstCardId || sixthCardId == secondCardId || sixthCardId == thirdCardId || sixthCardId == fourthCardId || sixthCardId == fifthCardId) {
            sixthCardId = (int) (Math.random() * (78));
        }
        int seventhCardId = sixthCardId;
        while (seventhCardId == firstCardId || seventhCardId == secondCardId || seventhCardId == thirdCardId || seventhCardId == fourthCardId || seventhCardId == fifthCardId || seventhCardId == sixthCardId) {
            seventhCardId = (int) (Math.random() * (78));
        }
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.query(TaroCardsTable.TABLE_NAME, null,
                CompatibilityNamesTable.COLUMN_ID + " = " + firstCardId,
                null, null, null, null);
        cursor.moveToFirst();
        @SuppressLint("Range") final String nameFirstPicture = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_PICTURE_NAME));
        @SuppressLint("Range") final String nameFirstCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_NAME));
        @SuppressLint("Range") final String descriptionFirstCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_DESCRIPTION_FIRST_OF_SEVEN_CARDS));

        cursor = database.query(TaroCardsTable.TABLE_NAME, null,
                CompatibilityNamesTable.COLUMN_ID + " = " + secondCardId,
                null, null, null, null);
        cursor.moveToFirst();
        @SuppressLint("Range") final String nameSecondPicture = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_PICTURE_NAME));
        @SuppressLint("Range") final String nameSecondCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_NAME));
        @SuppressLint("Range") final String descriptionSecondCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_DESCRIPTION_SECOND_OF_SEVEN_CARDS));

        cursor = database.query(TaroCardsTable.TABLE_NAME, null,
                CompatibilityNamesTable.COLUMN_ID + " = " + thirdCardId,
                null, null, null, null);
        cursor.moveToFirst();
        @SuppressLint("Range") final String nameThirdPicture = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_PICTURE_NAME));
        @SuppressLint("Range") final String nameThirdCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_NAME));
        @SuppressLint("Range") final String descriptionThirdCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_DESCRIPTION_THIRD_OF_SEVEN_CARDS));

        cursor = database.query(TaroCardsTable.TABLE_NAME, null,
                CompatibilityNamesTable.COLUMN_ID + " = " + fourthCardId,
                null, null, null, null);
        cursor.moveToFirst();
        @SuppressLint("Range") final String nameFourthPicture = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_PICTURE_NAME));
        @SuppressLint("Range") final String nameFourthCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_NAME));
        @SuppressLint("Range") final String descriptionFourthCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_DESCRIPTION_FOURTH_OF_SEVEN_CARDS));

        cursor = database.query(TaroCardsTable.TABLE_NAME, null,
                CompatibilityNamesTable.COLUMN_ID + " = " + fifthCardId,
                null, null, null, null);
        cursor.moveToFirst();
        @SuppressLint("Range") final String nameFifthPicture = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_PICTURE_NAME));
        @SuppressLint("Range") final String nameFifthCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_NAME));
        @SuppressLint("Range") final String descriptionFifthCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_DESCRIPTION_FIFTH_OF_SEVEN_CARDS));

        cursor = database.query(TaroCardsTable.TABLE_NAME, null,
                CompatibilityNamesTable.COLUMN_ID + " = " + sixthCardId,
                null, null, null, null);
        cursor.moveToFirst();
        @SuppressLint("Range") final String nameSixthPicture = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_PICTURE_NAME));
        @SuppressLint("Range") final String nameSixthCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_NAME));
        @SuppressLint("Range") final String descriptionSixthCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_DESCRIPTION_SIXTH_OF_SEVEN_CARDS));

        cursor = database.query(TaroCardsTable.TABLE_NAME, null,
                CompatibilityNamesTable.COLUMN_ID + " = " + seventhCardId,
                null, null, null, null);
        cursor.moveToFirst();
        @SuppressLint("Range") final String nameSeventhPicture = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_PICTURE_NAME));
        @SuppressLint("Range") final String nameSeventhCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_NAME));
        @SuppressLint("Range") final String descriptionSeventhCard = cursor.getString(cursor.getColumnIndex(TaroCardsTable.COLUMN_DESCRIPTION_SEVENTH_OF_SEVEN_CARDS));

        ArrayList<ImageView> pictures = new ArrayList<>();
        ImageView first = view.findViewById(R.id.first_open_image);
        first.setImageURI(Uri.parse(path + nameFirstPicture));
        pictures.add(first);
        ImageView second = view.findViewById(R.id.second_open_image);
        second.setImageURI(Uri.parse(path + nameSecondPicture));
        pictures.add(second);
        ImageView third = view.findViewById(R.id.third_open_image);
        third.setImageURI(Uri.parse(path + nameThirdPicture));
        pictures.add(third);
        ImageView fourth = view.findViewById(R.id.fourth_open_image);
        fourth.setImageURI(Uri.parse(path + nameFourthPicture));
        pictures.add(fourth);
        ImageView fifth = view.findViewById(R.id.fifth_open_image);
        fifth.setImageURI(Uri.parse(path + nameFifthPicture));
        pictures.add(fifth);
        ImageView sixth = view.findViewById(R.id.sixth_open_image);
        sixth.setImageURI(Uri.parse(path + nameSixthPicture));
        pictures.add(sixth);
        ImageView seventh = view.findViewById(R.id.seventh_open_image);
        seventh.setImageURI(Uri.parse(path + nameSeventhPicture));
        pictures.add(seventh);


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
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameFirstCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameFirstPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionFirstCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.second_open_image) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameSecondCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameSecondPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionSecondCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.third_open_image) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameThirdCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameThirdPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionThirdCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);
                    } else if (touchableView.getId() == R.id.fourth_open_image) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameFourthCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameFourthPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionFourthCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.fifth_open_image) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameFifthCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameFifthPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionFifthCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.sixth_open_image) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameSixthCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameSixthPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionSixthCard);
                        linearLayout.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        fourth.setVisibility(View.GONE);
                        fifth.setVisibility(View.GONE);
                        sixth.setVisibility(View.GONE);
                        seventh.setVisibility(View.GONE);
                        infoButton.setVisibility(View.GONE);

                    } else if (touchableView.getId() == R.id.seventh_open_image) {
                        TextView titleView = linearLayout.findViewById(R.id.title_view);
                        titleView.setText(nameSeventhCard);
                        ImageView imageView = linearLayout.findViewById(R.id.description_image_view);
                        imageView.setImageURI(Uri.parse(path + nameSeventhPicture));
                        TextView descriptionView = linearLayout.findViewById(R.id.description_view);
                        descriptionView.setText(descriptionSeventhCard);
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
}