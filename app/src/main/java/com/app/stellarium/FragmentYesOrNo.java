package com.app.stellarium;

import static android.content.Context.SENSOR_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.app.stellarium.utils.Ball;

import java.util.List;


public class FragmentYesOrNo extends Fragment {
    private Sensor accelerometer;
    private Ball ball;
    private Button ballAskButton;
    private Button ballClearButton;
    private TextView ballResponseTextView;
    private ImageView ballTriangleImageView;
    private Animation fadeInAnimation;
    private SensorEventListener accelerometerListener;
    private List<Sensor> sensorList;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private Animation scaleUp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yes_or_no, container, false);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setNumberOfPrevFragment();

        scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        class ButtonOnTouchListener implements View.OnTouchListener {
            @SuppressLint({"ClickableViewAccessibility", "NonConstantResourceId"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.startAnimation(scaleUp);
                }
                return true;
            }
        }
        ball = new Ball();
        ballAskButton = (Button) view.findViewById(R.id.ball_ask_button);
        ballClearButton = (Button) view.findViewById(R.id.ball_clear_button);
        ballResponseTextView = (TextView) view.findViewById(R.id.ball_response_text_view);
        ballTriangleImageView = (ImageView) view.findViewById(R.id.ball_triangle_image_view);
        ballResponseTextView.setAlpha(0.0f);
        ballTriangleImageView.setAlpha(1.0f);
        fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        accelerometerListener = new SensorEventListener() {
            private boolean flipped = false;

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                final float x = sensorEvent.values[1];
                final float y = sensorEvent.values[0];
                final float z = sensorEvent.values[2];
                if (z < -10) {
                    if (!flipped) {
                        ask();
                        flipped = true;
                    }
                } else if (z >= 0) {
                    flipped = false;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensorList.size() > 0) {
            accelerometer = sensorList.get(0);
        } else {
            accelerometer = null;
        }

        ballAskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scaleUp);
                ask();
            }
        });
        ballClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scaleUp);
                clear();
            }
        });

        return view;
    }

    public void ask() {
        if (ballTriangleImageView != null
                && ballResponseTextView != null && vibrator != null) {
            ballTriangleImageView.setAlpha(1.0f);
            ballResponseTextView.setAlpha(1.0f);
            ballResponseTextView.startAnimation(fadeInAnimation);
            ballResponseTextView.setText(ball.shake());
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("yes_or_no", Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean("isChecked", true)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, 100));
                } else {
                    vibrator.vibrate(300);
                }
            }
        }
    }

    public void clear() {
        if (ballTriangleImageView != null
                && ballResponseTextView != null && vibrator != null) {
            ballResponseTextView.setAlpha(0.0f);
            ballTriangleImageView.setAlpha(1.0f);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("yes_or_no", Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean("isChecked", true)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100);
                }
            }
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (accelerometer != null) {
            sensorManager.unregisterListener(accelerometerListener);
        }
    }
}