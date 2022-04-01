package com.app.stellarium.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.stellarium.FragmentAffirmation;
import com.app.stellarium.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class LoadingDialog extends Dialog implements android.view.View.OnClickListener {
    private Activity activity;
    private Fragment fragment;
    private AlertDialog dialog;
    private GifImageView gifImageView;
    private GifDrawable gifDrawable;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog);
       gifImageView=findViewById(R.id.gif_loading_dialog);
        try {
            gifDrawable=new GifDrawable(getContext().getResources(), R.id.gif_loading_dialog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {

    }

    public void startGifAnimation(){
        gifDrawable.start();
    }

    public void stopAnimation(){
        gifDrawable.stop();
    }
}
