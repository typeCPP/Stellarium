package com.app.stellarium;

import android.annotation.SuppressLint;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.app.stellarium.dialog.LoadingDialog;
import com.app.stellarium.filters.NameFilter;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.CompatibilityNames;
import com.google.gson.Gson;

import java.util.function.UnaryOperator;


public class FragmentCompatibilityNameSelection extends Fragment {
    private EditText editTextMan;
    private EditText editTextWoman;
    private Button nextButton;
    private Bundle bundle;
    private Animation scaleUp;
    private NameFilter nameFilter = new NameFilter();
    private LoadingDialog loadingDialog;
    private String man, woman;

    public static FragmentCompatibilityNameSelection newInstance(String param1, String param2) {
        FragmentCompatibilityNameSelection fragment = new FragmentCompatibilityNameSelection();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compatibility_name_selection, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        loadingDialog = new LoadingDialog(view.getContext());
        loadingDialog.setOnClick(new UnaryOperator<Void>() {
            @Override
            public Void apply(Void unused) {
                workWithServerAndOpenFragment(man, woman);
                return null;
            }
        });
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setNumberOfPrevFragment();

        class ButtonOnTouchListenerNext implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.startAnimation(scaleUp);
                bundle = new Bundle();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    man = editTextMan.getText().toString();
                    woman = editTextWoman.getText().toString();
                    if (man.isEmpty() || woman.isEmpty()) {
                        Toast.makeText(getContext(), "Пожалуйста, введите имена", Toast.LENGTH_SHORT).show();
                    } else {
                        workWithServerAndOpenFragment(man, woman);
                    }
                }
                return true;
            }
        }

        editTextMan = view.findViewById(R.id.editTextMan);
        editTextWoman = view.findViewById(R.id.editTextWoman);
        editTextMan.setFilters(new InputFilter[]{nameFilter});
        editTextWoman.setFilters(new InputFilter[]{nameFilter});
        nextButton = view.findViewById(R.id.compButtonNext);

        nextButton.setOnTouchListener(new ButtonOnTouchListenerNext());
        return view;
    }

    private void workWithServerAndOpenFragment(String man, String woman) {
        loadingDialog.show();
        loadingDialog.startGifAnimation();
        Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                bundle = getServerDataAndHashIDToBundle(woman, man);
                if (bundle == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.stopGifAnimation();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Fragment fragmentCompatibilityName = new FragmentCompatibilityName();
                            fragmentCompatibilityName.setArguments(bundle);
                            loadingDialog.dismiss();
                            getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                    .addToBackStack(null).replace(R.id.frameLayout, fragmentCompatibilityName).commit();
                        }
                    });
                }
            }
        }).start();
    }

    private Bundle getServerDataAndHashIDToBundle(String firstName, String secondName) {
        Bundle bundle = new Bundle();
        firstName.trim().replaceAll("\\s+"," ");
        bundle.putString("nameWoman", firstName.trim().replaceAll("\\s+", " "));
       bundle.putString("nameMan", secondName.trim().replaceAll("\\s+"," "));
        ServerConnection serverConnection = new ServerConnection();
        String response = serverConnection.getStringResponseByParameters("compName/?first=" + firstName + "&second=" + secondName);
        CompatibilityNames compatibilityNames = new Gson().fromJson(response, CompatibilityNames.class);
        if (compatibilityNames != null && !compatibilityNames.love_text.isEmpty() && !compatibilityNames.friend_text.isEmpty() &&
                !compatibilityNames.job_text.isEmpty() && compatibilityNames.love_val != 0 &&
                compatibilityNames.friend_val != 0 && compatibilityNames.job_val != 0) {
            bundle.putString("loveText", compatibilityNames.love_text);
            bundle.putString("friendText", compatibilityNames.friend_text);
            bundle.putString("jobText", compatibilityNames.job_text);
            bundle.putInt("loveValue", compatibilityNames.love_val);
            bundle.putInt("friendValue", compatibilityNames.friend_val);
            bundle.putInt("jobValue", compatibilityNames.job_val);
            return bundle;
        } else {
            return null;
        }
    }
}