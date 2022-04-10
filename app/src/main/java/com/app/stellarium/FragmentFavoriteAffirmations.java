package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.AffirmationsTable;
import com.app.stellarium.database.tables.FavoriteAffirmationsTable;
import com.app.stellarium.utils.AffirmationPagerAdapter;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Affirmation;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentFavoriteAffirmations extends Fragment {

    public static FragmentFavoriteAffirmations newInstance() {
        FragmentFavoriteAffirmations fragment = new FragmentFavoriteAffirmations();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setNumberOfPrevFragment();
            activity.hideBottomBar(true);
        }

        inflater = LayoutInflater.from(getContext());
        List<View> pages = new ArrayList<View>();

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ServerConnection serverConnection = new ServerConnection();
        int userID = databaseHelper.getCurrentUserServerID(database);
        Affirmation[] affirmations;
        if (userID != 0) {
            String response = serverConnection.getStringResponseByParameters("get_liked_affirm/?user_id=" + userID);
            affirmations = new Gson().fromJson(response, Affirmation[].class);
        } else {
            Cursor favoriteAffirmationsCursor = database.query(FavoriteAffirmationsTable.TABLE_NAME, null,
                    null, null, null, null, null);
            int countAffirmations = favoriteAffirmationsCursor.getCount();
            favoriteAffirmationsCursor.moveToFirst();
            affirmations = new Affirmation[countAffirmations];
            for (int i = 0; i < countAffirmations; i++) {
                int idOfAffirmation = favoriteAffirmationsCursor.getInt(favoriteAffirmationsCursor.getColumnIndex(FavoriteAffirmationsTable.COLUMN_AFFIRMATION_ID));
                Cursor affirmationCursor = database.query(AffirmationsTable.TABLE_NAME, null, AffirmationsTable.COLUMN_ID + "=" + idOfAffirmation, null, null, null, null);
                affirmationCursor.moveToFirst();
                String affirmationText = affirmationCursor.getString(affirmationCursor.getColumnIndex(AffirmationsTable.COLUMN_TEXT));
                String affirmationPictureName = affirmationCursor.getString(affirmationCursor.getColumnIndex(AffirmationsTable.COLUMN_PICTURE));
                affirmations[i] = new Affirmation(idOfAffirmation, affirmationText, affirmationPictureName);
                favoriteAffirmationsCursor.moveToNext();
            }
        }
        View page;
        for (Affirmation affirmation : affirmations) {
            page = inflater.inflate(R.layout.fragment_favorite_affirmations, null);
            if (affirmation == null) {
                throw new NullPointerException();
            }
            TextView textView = page.findViewById(R.id.affirmation_text);
            textView.setText(affirmation.text);
            FrameLayout frameLayout = page.findViewById(R.id.frameLayout);
            frameLayout.setBackground(getBackgroundByName(affirmation.picture));
            pages.add(page);
        }
        AffirmationPagerAdapter pagerAdapter = new AffirmationPagerAdapter(pages);
        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
        return viewPager;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();

        if (activity != null) {
            activity.hideBottomBar(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity activity = (MainActivity) getActivity();

        if (activity != null) {
            activity.hideBottomBar(false);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getBackgroundByName(String backgroundName) {
        Resources resources = getContext().getResources();
        return resources.getDrawable(getContext().getResources().getIdentifier(backgroundName, "drawable", getContext().getPackageName()));
    }
}