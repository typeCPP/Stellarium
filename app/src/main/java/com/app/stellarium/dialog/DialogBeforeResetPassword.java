package com.app.stellarium.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.stellarium.R;

public class DialogBeforeResetPassword extends Dialog implements
        android.view.View.OnClickListener {

    public Button okButton;
    private String text;

    public DialogBeforeResetPassword (@NonNull Context context, String text) {
        super(context);
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_before_reset_password);
        okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);
        TextView textView = findViewById(R.id.text_dialog);
        textView.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok_button) {
            dismiss();
        }
        dismiss();
    }

}
