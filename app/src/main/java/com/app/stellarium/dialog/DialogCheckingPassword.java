package com.app.stellarium.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.stellarium.R;
import com.app.stellarium.utils.PasswordEncoder;
import com.app.stellarium.utils.ServerConnection;
import com.google.android.material.textfield.TextInputEditText;

public class DialogCheckingPassword extends Dialog implements
        android.view.View.OnClickListener {

    private Button sendButton;
    private Button okButton;
    private Button tryAgainButton;
    private ImageView eyeImage;
    private TextInputEditText passwordEditText;
    private float letterSpacing = 0.27f;
    private Boolean isShowText = false;
    public Boolean isRightPassword = false;
    private String email;

    public DialogCheckingPassword(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cheking_password);
        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);
        eyeImage = findViewById(R.id.eye_password);
        eyeImage.setVisibility(View.INVISIBLE);

        passwordEditText = findViewById(R.id.passwordEditText);
        passwordEditText.setLetterSpacing(letterSpacing);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEyeVisibility(eyeImage, passwordEditText);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setEyeVisibility(eyeImage, passwordEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setEyeVisibility(eyeImage, passwordEditText);
            }
        });

        View.OnClickListener eyeOnClickListener = view -> {
            if (view.getId() == R.id.eye_password) {
                eyeChange(isShowText, passwordEditText, eyeImage);
                isShowText = !isShowText;
            }
        };
        eyeImage.setOnClickListener(eyeOnClickListener);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_button) {
            String password = passwordEditText.getText().toString();
            if (!password.isEmpty()) {
                try {
                    ServerConnection serverConnection = new ServerConnection();
                    String response = serverConnection.getStringResponseByParameters("auth/?mail=" + email + "&password=" + PasswordEncoder.encodePasswordMD5(password));
                    isRightPassword = !response.equals("False");
                } catch (Exception exception) {
                    Toast.makeText(getContext(), "Ошибка соединения с сервером.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!isRightPassword) {
                    setContentView(R.layout.dialog_for_wrong_password);
                    okButton = findViewById(R.id.ok_button);
                    okButton.setOnClickListener(this);
                    tryAgainButton = findViewById(R.id.try_again_button);
                    tryAgainButton.setOnClickListener(this);
                } else {
                    dismiss();
                }
            } else {
                Toast.makeText(getContext(), "Введите, пожалуйста, пароль.", Toast.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.ok_button) {
            dismiss();
        }
        if (v.getId() == R.id.try_again_button) {
            setContentView(R.layout.dialog_cheking_password);
            sendButton = findViewById(R.id.send_button);
            sendButton.setOnClickListener(this);
            eyeImage = findViewById(R.id.eye_password);
            eyeImage.setVisibility(View.INVISIBLE);

            passwordEditText = findViewById(R.id.passwordEditText);
            passwordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    setEyeVisibility(eyeImage, passwordEditText);
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    setEyeVisibility(eyeImage, passwordEditText);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    setEyeVisibility(eyeImage, passwordEditText);
                }
            });

            View.OnClickListener eyeOnClickListener = view -> {
                if (view.getId() == R.id.eye_password) {
                    eyeChange(isShowText, passwordEditText, eyeImage);
                    isShowText = !isShowText;
                }
            };
            eyeImage.setOnClickListener(eyeOnClickListener);
        }
    }

    private void eyeChange(boolean isShow, TextInputEditText textInputEditText, ImageView eye) {
        if (isShow) {
            eye.setImageResource(R.drawable.ic_eye_hide_white);
            textInputEditText.setLetterSpacing(letterSpacing);
            textInputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            eye.setImageResource(R.drawable.ic_eye_show_white);
            textInputEditText.setLetterSpacing(0f);
            textInputEditText.setTransformationMethod(null);
        }
        textInputEditText.setSelection(textInputEditText.length());
    }

    private void setEyeVisibility(ImageView eye, TextInputEditText text) {
        if (text.getText().toString().isEmpty()) {
            eye.setVisibility(View.INVISIBLE);
        } else {
            eye.setVisibility(View.VISIBLE);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
