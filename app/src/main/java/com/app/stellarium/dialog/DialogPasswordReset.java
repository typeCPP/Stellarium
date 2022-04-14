package com.app.stellarium.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.stellarium.R;
import com.app.stellarium.utils.ServerConnection;
import com.google.android.material.textfield.TextInputEditText;

public class DialogPasswordReset extends Dialog implements
        android.view.View.OnClickListener {

    Button furtherButton, backButton;
    boolean checkExistenceUser = false;
    TextInputEditText signInEmailEditText;


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
        signInEmailEditText = findViewById(R.id.signInEmailEditText);
        furtherButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.further_button:
                String email = signInEmailEditText.getText().toString();
                if (!email.isEmpty()) {
                    dismiss();
                    Dialog dialog;
                    try {
                        ServerConnection serverConnection = new ServerConnection();
                        String response = serverConnection.getStringResponseByParameters("passRecovery/?mail=" + email);
                        checkExistenceUser = response.contains("True");
                    } catch (Exception exception) {
                        Toast.makeText(getContext(), "Ошибка соединения с сервером.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if (checkExistenceUser) {
                        dialog = new DialogBeforeResetPassword(getContext(), "Мы отправили новый пароль на Ваш электронный адрес.");

                    } else {
                        dialog = new DialogBeforeResetPassword(getContext(), "Пользователя с таким электронным адресом не существует.");
                    }
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                } else {
                    Toast.makeText(getContext(), "Введите, пожалуйста, почту.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.back_button:
                dismiss();
                break;
        }
        dismiss();
    }
}
