package com.app.stellarium.tarocards;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.stellarium.R;

public class DialogInfoAboutLayout extends Dialog implements
        android.view.View.OnClickListener {

    public Button positiveButton, negativeButton;
    private String text;

    public DialogInfoAboutLayout (@NonNull Context context, String text) {
        super(context);
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_info_about_layout);
        negativeButton = findViewById(R.id.ok_button);
        negativeButton.setOnClickListener(this);
        TextView textView = findViewById(R.id.text_dialog);
        textView.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.negative_button:
                dismiss();
                break;
        }
        dismiss();
    }

}

