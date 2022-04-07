package com.app.stellarium;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.app.stellarium.database.DatabaseHelper;
import com.app.stellarium.database.tables.CompatibilityNamesTable;
import com.app.stellarium.filters.NameFilter;
import com.app.stellarium.utils.ServerConnection;
import com.app.stellarium.utils.jsonmodels.CompatibilityNames;
import com.google.gson.Gson;


public class FragmentCompatibilityNameSelection extends Fragment {
    private EditText editTextMan;
    private EditText editTextWoman;
    private Button nextButton;
    private Bundle bundle;
    private Animation scaleUp;
    private NameFilter nameFilter = new NameFilter();

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
                    String man = editTextMan.getText().toString();
                    String woman = editTextWoman.getText().toString();
                    if (man.isEmpty() || woman.isEmpty()) {
                        Toast.makeText(getContext(), "Пожалуйста, введите имена", Toast.LENGTH_SHORT).show();
                    } else if ((man.trim().contains(" ")) || (woman.trim().contains(" "))) {
                        Toast.makeText(getContext(), "Имена не должны содержать пробелы", Toast.LENGTH_SHORT).show();
                    } /*else {
                        LoadingDialog loadingDialog = new LoadingDialog(FragmentCompatibilityNameSelection.this);
                        loadingDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                bundle.putString("NameWoman", woman.replaceAll("\\s+", ""));
                                bundle.putString("NameMan", man.replaceAll("\\s+", ""));
                                bundle.putInt("hashedId", getServerDataAndHashID(woman, man));
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Fragment fragmentCompatibilityName = new FragmentCompatibilityName();
                                        fragmentCompatibilityName.setArguments(bundle);
                                        loadingDialog.dismissLoadingDialog();
                                        getParentFragmentManager().beginTransaction().setCustomAnimations(R.animator.fragment_alpha_in, R.animator.fragment_alpha_out, R.animator.fragment_alpha_in, R.animator.fragment_alpha_out)
                                                .addToBackStack(null).replace(R.id.frameLayout, fragmentCompatibilityName).commit();
                                    }
                                });
                            }
                        }).start();*/
                    /*     }*/
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

    private int getServerDataAndHashID(String firstName, String secondName) {
        ServerConnection serverConnection = new ServerConnection();
        String response = serverConnection.getStringResponseByParameters("compName/?first=" + firstName + "&second=" + secondName);
        CompatibilityNames compatibilityNames = new Gson().fromJson(response, CompatibilityNames.class);
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        databaseHelper.dropCompatibilityNames(database);
        insertCompatibilityNamesToDatabase(database, compatibilityNames);
        return compatibilityNames.hashedId;
    }

    private void insertCompatibilityNamesToDatabase(SQLiteDatabase sqLiteDatabase, CompatibilityNames compatibilityNames) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CompatibilityNamesTable.COLUMN_HASHED_ID, compatibilityNames.hashedId);

        contentValues.put(CompatibilityNamesTable.COLUMN_COMP_LOVE_TEXT, compatibilityNames.love_text);
        contentValues.put(CompatibilityNamesTable.COLUMN_COMP_JOB_TEXT, compatibilityNames.job_text);
        contentValues.put(CompatibilityNamesTable.COLUMN_COMP_FRIENDSHIP_TEXT, compatibilityNames.friend_text);

        contentValues.put(CompatibilityNamesTable.COLUMN_COMP_LOVE_VALUE, compatibilityNames.love_val);
        contentValues.put(CompatibilityNamesTable.COLUMN_COMP_JOB_VALUE, compatibilityNames.job_val);
        contentValues.put(CompatibilityNamesTable.COLUMN_COMP_FRIENDSHIP_VALUE, compatibilityNames.friend_val);

        sqLiteDatabase.insert(CompatibilityNamesTable.TABLE_NAME, null, contentValues);
    }
}