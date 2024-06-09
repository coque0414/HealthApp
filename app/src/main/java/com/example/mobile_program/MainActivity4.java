package com.example.mobile_program;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity4 extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private TextView stepCounterTextView;
    private TextView treasureCounterTextView;
    private int stepCount = 0;
    private int treasureCount = 0;
    private int dailyTreasureCount = 0; // 하루에 획득한 보물 상자 수
    private long lastStepTime = 0;
    private static final long TIME_THRESHOLD = 1000; // 시간 간격 임계값 (1초)
    private static final int STEP_THRESHOLD = 100; // 걸음 수 임계값
    private static final int MAX_TREASURE_COUNT = 10; // 최대 보물상자 수
    private static final int MAX_DAILY_TREASURE_COUNT = 10; // 하루 최대 보물상자 수
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String STEP_COUNT_KEY = "stepCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        stepCounterTextView = findViewById(R.id.step_counter);
        treasureCounterTextView = findViewById(R.id.treasure_count);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastStepTime = System.currentTimeMillis(); // 이전 걸음 감지 시간 초기화

        Button increaseStepsButton = findViewById(R.id.increase_steps_button);
        increaseStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseSteps(100);
            }
        });

        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCounts();
            }
        });

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseTreasureCount();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        double magnitude = Math.sqrt(x * x + y * y + z * z);

        long currentTime = System.currentTimeMillis();
        long stepInterval = currentTime - lastStepTime;

        // 걸음 감지
        if (magnitude > 25 && stepInterval > TIME_THRESHOLD) {
            stepCount++;
            updateStepCount();
            lastStepTime = currentTime; // 현재 걸음 감지 시간을 이전 걸음 감지 시간으로 업데이트
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void increaseSteps(int steps) {
        stepCount += steps;
        updateStepCount();
    }

    private void decreaseTreasureCount() {
        if (treasureCount > 0) {
            treasureCount--;
            treasureCounterTextView.setText(String.valueOf(treasureCount));
        }
    }

    private void resetCounts() {
        stepCount = 0;
        treasureCount = 0;
        dailyTreasureCount = 0;
        stepCounterTextView.setText("걸음 수: " + stepCount);
        treasureCounterTextView.setText(String.valueOf(treasureCount));
    }

    private void updateStepCount() {
        stepCounterTextView.setText("걸음 수: " + stepCount);

        // 보물상자 수 업데이트
        int newTreasureCount = stepCount / STEP_THRESHOLD;
        if (newTreasureCount > MAX_TREASURE_COUNT) {
            newTreasureCount = MAX_TREASURE_COUNT;
        }
        if (newTreasureCount > dailyTreasureCount) {
            int additionalTreasures = newTreasureCount - dailyTreasureCount;
            if (dailyTreasureCount + additionalTreasures > MAX_DAILY_TREASURE_COUNT) {
                additionalTreasures = MAX_DAILY_TREASURE_COUNT - dailyTreasureCount;
            }
            dailyTreasureCount += additionalTreasures;
            treasureCount += additionalTreasures;
            if (treasureCount > MAX_TREASURE_COUNT) {
                treasureCount = MAX_TREASURE_COUNT;
            }
            treasureCounterTextView.setText(String.valueOf(treasureCount));
        }
    }
}

