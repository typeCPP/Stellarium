package com.app.stellarium.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;

import com.app.stellarium.FragmentAffirmation;
import com.app.stellarium.R;

public class LoadingDialog {
    private Activity activity;
    private Fragment fragment;
    private AlertDialog dialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public LoadingDialog(Fragment fragment) {
        this.fragment = fragment;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = null;
        if (activity != null) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.loading_dialog, null));
            builder.setCancelable(false);

            dialog = builder.create();
            dialog.show();
        } else if(fragment != null) {
            builder = new AlertDialog.Builder(fragment.getContext());
            LayoutInflater inflater = fragment.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.loading_dialog, null));
            builder.setCancelable(false);

            dialog = builder.create();
            dialog.show();
        }
    }

    public void dismissLoadingDialog() {
        dialog.dismiss();
    }
}
