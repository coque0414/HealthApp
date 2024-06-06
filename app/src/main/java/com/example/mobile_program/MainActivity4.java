package com.example.mobile_program;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
    private int stepCount = 0;
    private long lastStepTime = 0;
    private static final long TIME_THRESHOLD = 1000; // 시간 간격 임계값 (1초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepCounterTextView = findViewById(R.id.step_counter);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastStepTime = System.currentTimeMillis(); // 이전 걸음 감지 시간 초기화
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
            stepCounterTextView.setText("걸음 수: " + stepCount);
            lastStepTime = currentTime; // 현재 걸음 감지 시간을 이전 걸음 감지 시간으로 업데이트
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}