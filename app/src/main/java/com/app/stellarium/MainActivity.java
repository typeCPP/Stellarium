package com.app.stellarium;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.HoroscopePredictionsByPeriodTable;
import com.app.stellarium.database.tables.HoroscopePredictionsTable;
import com.app.stellarium.database.tables.InformationTable;
import com.app.stellarium.database.tables.PythagoreanSquareTable;
import com.app.stellarium.transitionGenerator.StellariumTransitionGenerator;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BadgeDrawable badgeDrawable;

    private KenBurnsView kbv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                switch (item.getItemId()) {
                    case R.id.ic_personalAccount:
                        fragment = new FragmentPersonalAccount();
                        break;
                    case R.id.ic_home:
                        fragment = new FragmentHome();
                        break;
                    case R.id.ic_information:
                        fragment = new FragmentListOfInformation();
                        break;
                }

                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameLayout, fragment).commit();
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
        createFillHoroscopePredictionsByPeriodTable(database);
        createFillHoroscopePredictionsTable(database);
        createFillPythagoreanSquareTable(database);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

    public static void createFillInformationTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(InformationTable.COLUMN_NAME, "АФФИРМАЦИЯ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Аффирмации это круто, это класс, кто не согласен тот...");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "ГОРОСКОП");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Все знают что такое гороскоп.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "ТАРО");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Карты, деньги, два ствола.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "СОВМЕСТИМОСТЬ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Милый ты любишь меня?");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "ЛУННЫЙ КАЛЕНДАРЬ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Стригу волосы только, когда Луна в стадии жопы.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "НУМЕРОЛОГИЯ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Цифры решают всё.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "КВАДРАТ ПИФАГОРА");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Это не похоже на школьную программу.");
        database.insert(InformationTable.TABLE_NAME, null, values);

        values.put(InformationTable.COLUMN_NAME, "ДА-НЕТ");
        values.put(InformationTable.COLUMN_DESCRIPTION, "Ну тут уж совсем всё просто.");
        database.insert(InformationTable.TABLE_NAME, null, values);
    }

    public static void createFillHoroscopePredictionsByPeriodTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        String[] names = {"Овен", "Телец", "Близнецы", "Рак", "Лев", "Дева", "Весы", "Скорпион", "Стрелец", "Козерог", "Водолей", "Рыбы"};
        for (int i = 0; i < 12; i++) {
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_SIGN_NAME, names[i]);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_TODAY_PREDICTION_ID, 5 * i + 1);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_TOMORROW_PREDICTION_ID, 5 * i + 2);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_WEEK_PREDICTION_ID, 5 * i + 3);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_MONTH_PREDICTION_ID, 5 * i + 4);
            values.put(HoroscopePredictionsByPeriodTable.COLUMN_YEAR_PREDICTION_ID, 5 * i + 5);
            database.insert(HoroscopePredictionsByPeriodTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillHoroscopePredictionsTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        String[] love = {"Sex будет.", "Sexa не будет."};
        String[] common = {"Всё будет хорошо.", "Всё будет плохо."};
        String[] health = {"Будете здоровы.", "Будете болеть."};
        String[] business = {"Вас ждет куча бабла.", "Вас ждут убытки."};
        for (int i = 0; i < 60; i++) {
            Random random = new Random();
            if (random.nextInt(100) < 50) {
                values.put(HoroscopePredictionsTable.COLUMN_LOVE, love[0]);
            } else {
                values.put(HoroscopePredictionsTable.COLUMN_LOVE, love[1]);
            }
            if (random.nextInt(100) < 50) {
                values.put(HoroscopePredictionsTable.COLUMN_COMMON, common[0]);
            } else {
                values.put(HoroscopePredictionsTable.COLUMN_COMMON, common[1]);
            }
            if (random.nextInt(100) < 50) {
                values.put(HoroscopePredictionsTable.COLUMN_HEALTH, health[0]);
            } else {
                values.put(HoroscopePredictionsTable.COLUMN_HEALTH, health[1]);
            }
            if (random.nextInt(100) < 50) {
                values.put(HoroscopePredictionsTable.COLUMN_BUSINESS, business[0]);
            } else {
                values.put(HoroscopePredictionsTable.COLUMN_BUSINESS, business[1]);
            }
            database.insert(HoroscopePredictionsTable.TABLE_NAME, null, values);
        }
    }

    public static void createFillPythagoreanSquareTable(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        String[] descriptions = {"Ну ты лох конечно в этом плане.", "Всё не очень хорошо.", "Ну на троечку.",
                "Красавчик, гордый среднячок.", "У тебя всё отлично в этом плане.", "Богом одаренный.", "Ну это вообще неописуемый дар, лучше некуда."};
        for (int i = 1; i <= 9; i++) {
            values.put(PythagoreanSquareTable.COLUMN_NUMBER, i);
            values.put(PythagoreanSquareTable.COLUMN_NO_NUMBER, descriptions[0]);
            values.put(PythagoreanSquareTable.COLUMN_ONE_NUMBER, descriptions[1]);
            values.put(PythagoreanSquareTable.COLUMN_TWO_NUMBERS, descriptions[2]);
            values.put(PythagoreanSquareTable.COLUMN_THREE_NUMBERS, descriptions[3]);
            values.put(PythagoreanSquareTable.COLUMN_FOUR_NUMBERS, descriptions[4]);
            values.put(PythagoreanSquareTable.COLUMN_FIVE_NUMBERS, descriptions[5]);
            values.put(PythagoreanSquareTable.COLUMN_SIX_NUMBERS, descriptions[6]);
            database.insert(PythagoreanSquareTable.TABLE_NAME, null, values);
        }
    }
}