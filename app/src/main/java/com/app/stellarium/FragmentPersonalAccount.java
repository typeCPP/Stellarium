package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.UserTable;
import com.app.stellarium.dialog.CustomDialog;
import com.app.stellarium.dialog.DialogCheckingPassword;

public class FragmentPersonalAccount extends Fragment {

    private LinearLayout layoutEditProfile, layoutAffirmations, layoutFullRegistration;
    private ImageView exitImage, signImage;
    private TextView name, date, signText, email, registrationButton;
    private SwitchCompat switchCompat;
    private int signId;

    public static FragmentPersonalAccount newInstance(String param1, String param2) {
        FragmentPersonalAccount fragment = new FragmentPersonalAccount();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_account, container, false);
        layoutEditProfile = view.findViewById(R.id.layout_profile_edit);
        layoutAffirmations = view.findViewById(R.id.layout_profile_affirmations);
        layoutFullRegistration = view.findViewById(R.id.layout_account_full_registration);

        name = view.findViewById(R.id.personal_account_name);
        date = view.findViewById(R.id.personal_account_date);
        email = view.findViewById(R.id.personal_account_email);
        exitImage = view.findViewById(R.id.personal_account_exit);
        switchCompat = view.findViewById(R.id.switch_personal_account);
        registrationButton = view.findViewById(R.id.registration_button);

        signImage = view.findViewById(R.id.sign_image_personal_account);
        signText = view.findViewById(R.id.sign_text_personal_account);

        setSwitchMode();
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        setUserData(databaseHelper.getReadableDatabase());
        setSignImageAndText();

        View.OnClickListener layoutClickListener = new View.OnClickListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                switch (view.getId()) {
                    case R.id.layout_profile_edit:
                        if (databaseHelper.getCurrentUserServerID(databaseHelper.getReadableDatabase()) == 0 || databaseHelper.checkForUserUID(databaseHelper.getReadableDatabase())) {
                            FragmentEditPersonalAccount fragmentEditPersonalAccount = new FragmentEditPersonalAccount();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragmentEditPersonalAccount).commit();
                            break;
                        }
                        DialogCheckingPassword dialogCheckingPassword = new DialogCheckingPassword(getContext());
                        dialogCheckingPassword.setEmail(email.getText().toString());
                        dialogCheckingPassword.show();
                        dialogCheckingPassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogCheckingPassword.setOnDismissListener(dialogInterface -> {
                            if (dialogCheckingPassword.isRightPassword) {
                                FragmentEditPersonalAccount fragmentEditPersonalAccount = new FragmentEditPersonalAccount();
                                getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right)
                                        .addToBackStack(null).replace(R.id.frameLayout, fragmentEditPersonalAccount).commit();
                            }
                        });
                        break;
                    case R.id.layout_profile_affirmations:
                        fragment = new FragmentFavoriteAffirmations();
                        break;
                }
                if (fragment != null) {
                    getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right)
                            .addToBackStack(null).replace(R.id.frameLayout, fragment).commit();

                }
            }
        };

        exitImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(getContext());
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("yes_or_no", Context.MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isChecked", isChecked);
                editor.commit();

            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), MainRegistrationActivity.class);
                myIntent.putExtra("showSkipButton", false);
                getActivity().startActivity(myIntent);
            }
        });
        layoutEditProfile.setOnClickListener(layoutClickListener);
        layoutAffirmations.setOnClickListener(layoutClickListener);
        return view;
    }

    @SuppressLint({"Range", "SetTextI18n"})
    private void setUserData(SQLiteDatabase database) {
        Cursor userCursor = database.query(UserTable.TABLE_NAME, null,
                null,
                null, null, null, null);
        userCursor.moveToLast();
        String birthdayString = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_DATE_OF_BIRTH));
        String nameString = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_NAME));
        String emailString = userCursor.getString(userCursor.getColumnIndex(UserTable.COLUMN_EMAIL));
        if (emailString != null) {
            layoutFullRegistration.setVisibility(View.GONE);
            email.setText(emailString);
        } else {
            email.setVisibility(View.GONE);
        }
        signId = userCursor.getInt(userCursor.getColumnIndex(UserTable.COLUMN_HOROSCOPE_SIGN_ID));
        if (!birthdayString.isEmpty()) {
            birthdayString = birthdayString.replaceAll("\\.", "/");
            String[] temp = birthdayString.split("/", 3);
            date.setText(temp[0] + "." + temp[1] + "." + temp[2]);
        }
        if (!nameString.isEmpty()) {
            name.setText(nameString);
        }
    }

    private void setSwitchMode() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("yes_or_no", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isChecked", true)) {
            switchCompat.setChecked(true);
        } else {
            switchCompat.setChecked(false);
        }
    }

    private void setSignImageAndText() {
        if (signId != 0) {
            switch (signId) {
                case 1:
                    signText.setText("Овен");
                    signImage.setImageResource(R.drawable.big_aries);
                    break;
                case 2:
                    signText.setText("Телец");
                    signImage.setImageResource(R.drawable.big_taurus);
                    break;
                case 3:
                    signText.setText("Близнецы");
                    signImage.setImageResource(R.drawable.big_gemini);
                    break;
                case 4:
                    signText.setText("Рак");
                    signImage.setImageResource(R.drawable.big_cancer);
                    break;
                case 5:
                    signText.setText("Лев");
                    signImage.setImageResource(R.drawable.big_leo);
                    break;
                case 6:
                    signText.setText("Дева");
                    signImage.setImageResource(R.drawable.big_virgo);
                    break;
                case 7:
                    signText.setText("Весы");
                    signImage.setImageResource(R.drawable.big_libra);
                    break;
                case 8:
                    signText.setText("Скорпион");
                    signImage.setImageResource(R.drawable.big_scorpio);
                    break;
                case 9:
                    signText.setText("Стрелец");
                    signImage.setImageResource(R.drawable.big_sagittarius);
                    break;
                case 10:
                    signText.setText("Козерог");
                    signImage.setImageResource(R.drawable.big_capricorn);
                    break;
                case 11:
                    signText.setText("Водолей");
                    signImage.setImageResource(R.drawable.big_aquarius);
                    break;
                case 12:
                    signText.setText("Рыбы");
                    signImage.setImageResource(R.drawable.big_pisces);
                    break;
            }
        }
    }
}