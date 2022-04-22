package com.app.stellarium.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.app.stellarium.R;

public class DialogForWrongPassword extends Dialog implements
        android.view.View.OnClickListener {

    public Button okButton;
    public Button tryAgainButton;

    public DialogForWrongPassword(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_for_wrong_password);
        okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);
        tryAgainButton = findViewById(R.id.try_again_button);
        tryAgainButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok_button) {
            dismiss();
        }
        if (v.getId() == R.id.try_again_button) {
            dismiss();
            Dialog dialog = new DialogCheckingPassword(getContext());
            dialog.show();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dismiss();
    }

}