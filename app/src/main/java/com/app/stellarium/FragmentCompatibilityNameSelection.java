package com.app.stellarium;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class FragmentCompatibilityNameSelection extends Fragment {
    private EditText editTextMan;
    private EditText editTextWoman;
    private Button nextButton;
    private Bundle bundle;
    private Animation scaleUp;

    public static FragmentCompatibilityNameSelection newInstance(String param1, String param2) {
        FragmentCompatibilityNameSelection fragment = new FragmentCompatibilityNameSelection();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compatibility_name_selection, container, false);
        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);

        class ButtonOnTouchListenerNext implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.startAnimation(scaleUp);
                bundle = new Bundle();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    String man = editTextMan.getText().toString();
                    String woman = editTextWoman.getText().toString();
                    if (man.isEmpty() || woman.isEmpty()) {
                        Toast.makeText(getContext(), "Пожалуйста, введите имена", Toast.LENGTH_SHORT).show();
                    } else if ((man.trim().contains(" ")) || (woman.trim().contains(" "))) {
                        Toast.makeText(getContext(), "Имена не должны содержать пробелы", Toast.LENGTH_SHORT).show();
                    } else {
                        bundle.putString("NameWoman", woman.replaceAll("\\s+", ""));
                        bundle.putString("NameMan", man.replaceAll("\\s+", ""));
                        bundle.putInt("hashedId", hashNames(woman, man));
                        Fragment fragmentCompatibilityName = new FragmentCompatibilityName();
                        fragmentCompatibilityName.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameLayout, fragmentCompatibilityName).commit();
                    }
                }
                return true;
            }
        }

        editTextMan = view.findViewById(R.id.editTextMan);
        editTextWoman = view.findViewById(R.id.editTextWoman);
        nextButton = view.findViewById(R.id.compButtonNext);

        nextButton.setOnTouchListener(new ButtonOnTouchListenerNext());
        return view;
    }

    private int hashNames(String firstName, String secondName) {
        int hashed = firstName.length() + secondName.length();
        if (hashed > 32) {
            hashed = 32;
        }
        else if(hashed == 0) {
            hashed = 1;
        }
        return hashed;
    }
}