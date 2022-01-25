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
import com.app.stellarium.database.tables.InformationTable;
import com.app.stellarium.transitionGenerator.StellariumTransitionGenerator;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
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
        databaseHelper.onUpgrade(database, 1, 1);
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
}