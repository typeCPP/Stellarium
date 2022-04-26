package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.InformationTable;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.database.tables.ZodiacSignsTable;
import com.app.stellarium.transitionGenerator.StellariumTransitionGenerator;
import com.app.stellarium.utils.PasswordEncoder;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.User;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BadgeDrawable badgeDrawable;
    TextView titleText;
    private KenBurnsView kbv;
    private int numberOfPrevFragment = 2; //1 - personalAccount, 2 - Home, 3 - Information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle widgetBundle = getIntent().getExtras();
        if (widgetBundle != null && widgetBundle.get("key") == "widget") {
            System.out.println(widgetBundle.get("key"));
            Fragment fragmentAffirmation = new FragmentAffirmation();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragmentAffirmation).commit();
        }
        bottomNavigationView = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentHome()).commit();
        bottomNavigationView.findViewById(R.id.ic_home).performClick();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                badgeDrawable = bottomNavigationView.getBadge(item.getItemId());
                if (badgeDrawable != null) {
                    badgeDrawable.clearNumber();
                }
                Fragment fragment = null;
                FragmentManager fm = getSupportFragmentManager();
                int count = 0;
                switch (item.getItemId()) {

                    case R.id.ic_personalAccount:
                        count = fm.getBackStackEntryCount();
                        fragment = new FragmentPersonalAccount();
                        if (numberOfPrevFragment == 0) {
                            for (int i = 0; i < count; ++i) {
                                fm.popBackStack();
                            }
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .replace(R.id.frameLayout, fragment).commit();
                        } else if (numberOfPrevFragment > 1) {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right)
                                    .replace(R.id.frameLayout, fragment).commit();
                        }
                        numberOfPrevFragment = 1;
                        break;
                    case R.id.ic_home:
                        count = fm.getBackStackEntryCount();
                        fragment = new FragmentHome();
                        if (numberOfPrevFragment == 0) {
                            for (int i = 0; i < count; ++i) {
                                fm.popBackStack();
                            }
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .replace(R.id.frameLayout, fragment).commit();
                        } else if (numberOfPrevFragment == 1) {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .replace(R.id.frameLayout, fragment).commit();
                        } else if (numberOfPrevFragment == 3) {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right)
                                    .replace(R.id.frameLayout, fragment).commit();
                        }
                        numberOfPrevFragment = 2;
                        break;
                    case R.id.ic_information:
                        count = fm.getBackStackEntryCount();
                        for (int i = 0; i < count; ++i) {
                            fm.popBackStack();
                        }
                        fragment = new FragmentListOfInformation();
                        if (numberOfPrevFragment == 0) {
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .replace(R.id.frameLayout, fragment).commit();
                        } else if (numberOfPrevFragment < 3) {
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left)
                                    .replace(R.id.frameLayout, fragment).commit();
                        }
                        numberOfPrevFragment = 3;
                        break;
                }
                return true;
            }
        });
        kbv = findViewById(R.id.image11);
        AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
        StellariumTransitionGenerator stellariumTransitionGenerator =
                new StellariumTransitionGenerator(10000, adi);
        kbv.setTransitionGenerator(stellariumTransitionGenerator);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        databaseHelper.onUpgrade(database, 2, 2);
        createFillInformationTable(database);
        createFillZodiacSignsTable(database);
        hideBottomBar(false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (!checkSignedUser(database)) {
            database.close();
            databaseHelper.close();
            Intent myIntent = new Intent(MainActivity.this, MainRegistrationActivity.class);
            MainActivity.this.startActivity(myIntent);
        } else if (!checkIfUserIsGuest(database)) {
            try {
                Pair<User, String[]> pair = getSignedUserDataFromServer(database, databaseHelper);
                User user = pair.first;
                if (user.sign == null || user.sex == null || user.name == null || user.date == null) {
                    Intent myIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                    myIntent.putExtra("userServerID", user.id);
                    myIntent.putExtra("userEmail", pair.second[0]);
                    myIntent.putExtra("userPassword", pair.second[1]);
                    if (pair.second[2] == null) {
                        myIntent.putExtra("userUID", pair.second[3]);
                        myIntent.putExtra("isFacebook", false);
                    } else {
                        myIntent.putExtra("userUID", pair.second[2]);
                        myIntent.putExtra("isFacebook", true);
                    }
                    MainActivity.this.startActivity(myIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
                database.close();
                databaseHelper.close();
                Intent myIntent = new Intent(MainActivity.this, MainRegistrationActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        }

        database.close();
        databaseHelper.close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle widgetBundle = getIntent().getExtras();
        if (widgetBundle != null && widgetBundle.get("key") == "widget") {
            System.out.println(widgetBundle.get("key"));
            Fragment fragmentAffirmation = new FragmentAffirmation();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragmentAffirmation).commit();
        }
    }

    @Override
    protected void onResume() {
        Bundle widgetBundle = getIntent().getExtras();
        if (widgetBundle != null && widgetBundle.get("key") == "widget") {
            System.out.println(widgetBundle.get("key"));
            Fragment fragmentAffirmation = new FragmentAffirmation();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragmentAffirmation).commit();
        } else {
            super.onResume();
        }
    }

    private boolean checkIfUserIsGuest(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        userCursor.moveToLast();
        @SuppressLint("Range") String email = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_EMAIL));
        @SuppressLint("Range") String password = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_PASSWORD));
        @SuppressLint("Range") String facebookID = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_FACEBOOK_ID));
        @SuppressLint("Range") String googleID = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_GOOGLE_ID));

        return email == null && password == null && facebookID == null && googleID == null;
    }

    private Pair<User, String[]> getSignedUserDataFromServer(SQLiteDatabase database, DatabaseHelper databaseHelper) {
        ServerConnection serverConnection = new ServerConnection();
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        userCursor.moveToLast();
        @SuppressLint("Range") String email = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_EMAIL));
        @SuppressLint("Range") String password = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_PASSWORD));
        @SuppressLint("Range") String facebookID = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_FACEBOOK_ID));
        @SuppressLint("Range") String googleID = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_GOOGLE_ID));
        String params = "auth/?";
        if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
            params += "mail=" + email;
            params += "&password=" + PasswordEncoder.encodePasswordMD5(password);
        } else if (googleID != null && !googleID.isEmpty()) {
            params += "google=" + googleID;
        } else if (facebookID != null && !facebookID.isEmpty()) {
            params += "facebook=" + facebookID;
        }
        Log.d("QUERY", params);
        String response = serverConnection.getStringResponseByParameters(params);
        Log.d("RESPONSE", response);
        User user = new Gson().fromJson(response, User.class);
        if (user.sign == null || user.sex == null || user.name == null || user.date == null) {
            return new Pair<>(user, new String[]{email, password, facebookID, googleID});
        }
        databaseHelper.dropUserTable(database);
        insertSignedUserToDatabase(user, database, email, password, googleID, facebookID);
        return new Pair<>(user, new String[]{email, password, facebookID, googleID});
    }

    private void insertSignedUserToDatabase(User user, SQLiteDatabase database, String email, String password, String googleID, String facebookID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.COLUMN_SERVER_ID, user.id);
        contentValues.put(UserTable.COLUMN_NAME, user.name);
        contentValues.put(UserTable.COLUMN_DATE_OF_BIRTH, user.date);
        contentValues.put(UserTable.COLUMN_SEX, user.sex);
        contentValues.put(UserTable.COLUMN_HOROSCOPE_SIGN_ID, user.sign);
        contentValues.put(UserTable.COLUMN_MAIL_CONFIRMED, user.mail_confirm);
        if (email != null && password != null) {
            contentValues.put(UserTable.COLUMN_EMAIL, email);
            contentValues.put(UserTable.COLUMN_PASSWORD, password);
        } else if (googleID != null) {
            contentValues.put(UserTable.COLUMN_GOOGLE_ID, googleID);
        } else if (facebookID != null) {
            contentValues.put(UserTable.COLUMN_FACEBOOK_ID, facebookID);
        }
        database.insert(UserTable.TABLE_NAME, null, contentValues);
    }

    private boolean checkSignedUser(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        return userCursor.getCount() > 0;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            //super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    public void createFillInformationTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(InformationTable.COLUMN_NAME, "Аффирмация");
        values.put(InformationTable.COLUMN_DESCRIPTION, getResources().getString(R.string.info_affirmation));
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "Гороскоп");
        values.put(InformationTable.COLUMN_DESCRIPTION, getResources().getString(R.string.info_horoscope));
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "Карты Таро");
        values.put(InformationTable.COLUMN_DESCRIPTION, getResources().getString(R.string.info_tarot));
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "Совместимость");
        values.put(InformationTable.COLUMN_DESCRIPTION, getResources().getString(R.string.info_compatibility));
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "Лунный календарь");
        values.put(InformationTable.COLUMN_DESCRIPTION, getResources().getString(R.string.info_moon_calendar));
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "Нумерология");
        values.put(InformationTable.COLUMN_DESCRIPTION, getResources().getString(R.string.info_numerology));
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "Квадрат Пифагора");
        values.put(InformationTable.COLUMN_DESCRIPTION, getResources().getString(R.string.info_pythagorean_square));
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "Шар судьбы");
        values.put(InformationTable.COLUMN_DESCRIPTION, getResources().getString(R.string.info_yes_no));
        database.insert(InformationTable.TABLE_NAME, null, values);
    }

    public static void createFillZodiacSignsTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        String[] signs = {"Овен", "Телец", "Близнецы", "Рак", "Лев", "Дева", "Весы", "Скорпион", "Стрелец", "Козерог", "Водолей", "Рыбы"};
        for (int i = 0; i < signs.length; i++) {
            values.put(ZodiacSignsTable.COLUMN_NAME, signs[i]);
            values.put(ZodiacSignsTable.COLUMN_SIGN_ID, i + 1);
            database.insert(ZodiacSignsTable.TABLE_NAME, null, values);
        }
    }

    public void hideBottomBar(boolean isHidden) {
        bottomNavigationView.setVisibility(isHidden ? View.GONE : View.VISIBLE);
    }

    public void setNumberOfPrevFragment() {
        numberOfPrevFragment = 0;
    }
}