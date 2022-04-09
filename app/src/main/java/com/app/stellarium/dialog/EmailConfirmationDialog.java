package com.app.stellarium.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.app.stellarium.R;


public class EmailConfirmationDialog extends Dialog implements android.view.View.OnClickListener {
    private Context context;

    public EmailConfirmationDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(false);
    }

    @Override
    public void onClick(View view) {

    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.email_confirmation_dialog);
    }
}
