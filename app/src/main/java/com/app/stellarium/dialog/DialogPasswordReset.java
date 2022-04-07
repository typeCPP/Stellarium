package com.app.stellarium.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.app.stellarium.R;

public class DialogPasswordReset extends Dialog implements
        android.view.View.OnClickListener {

    Button furtherButton, backButton;
    boolean checkExistenceUser = false;


    public DialogPasswordReset(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_password_reset);
        furtherButton = findViewById(R.id.further_button);
        backButton = findViewById(R.id.back_button);
        furtherButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.further_button:
                dismiss();
                Dialog dialog;
                if (checkExistenceUser) {
                    dialog = new DialogBeforeResetPassword(getContext(), "Мы отправили новый пароль на Ваш электронный адрес.");
                } else {
                    dialog = new DialogBeforeResetPassword(getContext(), "Пользователя с таким электронным адресом не существует.");
                }
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                break;
            case R.id.back_button:
                dismiss();
                break;
        }
        dismiss();
    }
}
