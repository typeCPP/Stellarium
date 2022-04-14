package com.app.stellarium;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ActivityAfterClickWidget extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_click_widget);
        Bundle widgetBundle = new Bundle();
        widgetBundle.putInt("widget", 1);
        FragmentAffirmation fragment = new FragmentAffirmation();
        fragment.setArguments(widgetBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityAfterClickWidget.this, MainActivity.class);
        ActivityAfterClickWidget.this.startActivity(intent);
    }
}