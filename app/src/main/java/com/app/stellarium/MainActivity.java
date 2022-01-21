package com.app.stellarium;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BadgeDrawable badgeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new FragmentHome()).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
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
    }
}