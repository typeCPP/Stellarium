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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCompatibilityNameSelection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCompatibilityNameSelection.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCompatibilityNameSelection newInstance(String param1, String param2) {
        FragmentCompatibilityNameSelection fragment = new FragmentCompatibilityNameSelection();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                        Fragment fragmentCompatibilityName = new FragmentCompatibilityName();
                        fragmentCompatibilityName.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentCompatibilityName).commit();
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
}