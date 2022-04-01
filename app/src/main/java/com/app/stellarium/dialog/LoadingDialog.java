package com.app.stellarium.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.stellarium.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class LoadingDialog extends Dialog implements android.view.View.OnClickListener {
    private GifImageView gifImageView;
    private GifDrawable gifDrawable;
    private TextView text;
    private LinearLayout linearLayoutError;
    private Context context;
    private Button button;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.setCancelable(false);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog);
        gifImageView = findViewById(R.id.gif_loading_dialog);
        button = findViewById(R.id.button_error_loading_dialog);

        gifDrawable = (GifDrawable) gifImageView.getDrawable();
        text = findViewById(R.id.text_dialog);
        linearLayoutError = findViewById(R.id.layout_dialog_error);
        linearLayoutError.setAlpha(0f);

        class LoadingDialogOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                startGifAnimation();
            }
        }
        button.setOnClickListener(new LoadingDialogOnClickListener());
    }


    @Override
    public void onClick(View view) {

    }

    public void startGifAnimation() {
        text.setVisibility(View.VISIBLE);
        text.animate().alpha(1f).setDuration(250).setListener(null);
        linearLayoutError.animate().alpha(0f).setDuration(250).setListener(null);
        linearLayoutError.setVisibility(View.INVISIBLE);
        gifDrawable.start();
    }

    public void stopGifAnimation() {
        if (gifDrawable.isRunning()) {
            text.animate().alpha(0f).setDuration(250).setListener(null);
            text.setVisibility(View.INVISIBLE);
            linearLayoutError.setVisibility(View.VISIBLE);
            linearLayoutError.animate().alpha(1f).setDuration(250).setListener(null);
            gifDrawable.stop();
        }
    }
}
