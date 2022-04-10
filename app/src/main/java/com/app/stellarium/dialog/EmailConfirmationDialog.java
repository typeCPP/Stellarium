package com.app.stellarium.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.app.stellarium.R;

import java.util.function.UnaryOperator;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class EmailConfirmationDialog extends Dialog implements android.view.View.OnClickListener {
    private GifImageView gifImageView;
    private GifDrawable gifDrawable;
    private TextView text;
    private TextView text2;
    private LinearLayout linearLayoutError;
    private Context context;
    private Button button;
    private UnaryOperator<Void> onClick;
    private UnaryOperator<Void> onCancel;

    public EmailConfirmationDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(true);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.email_confirmation_dialog);
        gifImageView = findViewById(R.id.gif_email_dialog);
        button = findViewById(R.id.button_error_email_dialog);

        gifDrawable = (GifDrawable) gifImageView.getDrawable();
        text = findViewById(R.id.text_dialog);
        text2 = findViewById(R.id.email_dialog_text_confirm);
        linearLayoutError = findViewById(R.id.layout_email_dialog_error);
        linearLayoutError.setAlpha(0f);

        class LoadingDialogOnClickListener implements View.OnClickListener {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                onClick.apply(null);
            }
        }
        button.setOnClickListener(new LoadingDialogOnClickListener());

        this.setOnCancelListener(new OnCancelListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onCancel.apply(null);
            }
        });
    }

    public void setOnClick(UnaryOperator<Void> onClick) {
        this.onClick = onClick;
    }

    public void setOnCancel(UnaryOperator<Void> onCancel) {
        this.onCancel = onCancel;
    }

    public void startGifAnimation() {
        text.setVisibility(View.VISIBLE);
        text2.setVisibility(View.VISIBLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        text.animate().alpha(1f).setDuration(250).setListener(null);
        text2.animate().alpha(1f).setDuration(250).setListener(null);
        linearLayoutError.animate().alpha(0f).setDuration(250).setListener(null);
        linearLayoutError.setVisibility(View.INVISIBLE);
        gifDrawable.start();
    }

    public void stopGifAnimation() {
        if (gifDrawable.isRunning()) {
            try {
                text.animate().alpha(0f).setDuration(250).setListener(null);
                text.setVisibility(View.INVISIBLE);
                text2.animate().alpha(0f).setDuration(250).setListener(null);
                text2.setVisibility(View.INVISIBLE);
                linearLayoutError.setVisibility(View.VISIBLE);
                linearLayoutError.animate().alpha(1f).setDuration(250).setListener(null);
                gifDrawable.stop();
            } catch (Exception e) {
                e.printStackTrace();
                gifDrawable.stop();
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
