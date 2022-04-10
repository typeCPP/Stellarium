package com.app.stellarium.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.app.stellarium.MainRegistrationActivity;
import com.app.stellarium.R;
import com.app.stellarium.database.DatabaseHelper;

public class CustomDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Button positiveButton, negativeButton;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        positiveButton = findViewById(R.id.positive_button);
        negativeButton = findViewById(R.id.negative_button);
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                databaseHelper.dropUserTable(databaseHelper.getWritableDatabase());
                databaseHelper.dropAffirmationTable(databaseHelper.getWritableDatabase());
                databaseHelper.dropFavoriteAffirmationsTable(databaseHelper.getWritableDatabase());
                databaseHelper.close();
                Intent myIntent = new Intent(getContext(), MainRegistrationActivity.class);
                getContext().startActivity(myIntent);
                break;
            case R.id.negative_button:
                dismiss();
                break;
        }
        dismiss();
    }

}

