package com.app.stellarium;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.FavoriteAffirmationsTable;
import com.app.stellarium.utils.AffirmationPagerAdapter;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.Affirmation;
import com.google.gson.Gson;

import java.util.ArrayList;
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
        Cursor favoriteAffirmationsCursor = database.query(FavoriteAffirmationsTable.TABLE_NAME, null,
                null, null, null, null, null);
        int countAffirmations = favoriteAffirmationsCursor.getCount();
        if (countAffirmations < 1) {
            return null;
        } else {
            favoriteAffirmationsCursor.moveToFirst();
            View page;

            for (int i = 0; i < countAffirmations; i++) {

                if (i != 0) {
                    favoriteAffirmationsCursor.moveToNext();
                }
                page = inflater.inflate(R.layout.fragment_favorite_affirmations, null);
                int idAffirmation = favoriteAffirmationsCursor.getInt(favoriteAffirmationsCursor.getColumnIndex(FavoriteAffirmationsTable.COLUMN_AFFIRMATION_ID));
                String params = "affirmation/?id=" + idAffirmation;
                ServerConnection serverConnection = new ServerConnection();
                String response = serverConnection.getStringResponseByParameters(params);
                Affirmation affirmation = new Gson().fromJson(response, Affirmation.class);
                if (affirmation == null) {
                    throw new NullPointerException();
                }
                TextView textView = page.findViewById(R.id.affirmation_text);
                textView.setText(affirmation.text);
                FrameLayout frameLayout = page.findViewById(R.id.frameLayout);
                //frameLayout.setBackground();
                pages.add(page);
            }

            AffirmationPagerAdapter pagerAdapter = new AffirmationPagerAdapter(pages);
            ViewPager viewPager = new ViewPager(getContext());
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(1);
            return viewPager;
        }
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
}