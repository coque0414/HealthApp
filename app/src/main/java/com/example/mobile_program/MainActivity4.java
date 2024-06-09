package com.example.mobile_program;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity4 extends AppCompatActivity implements SensorEventListener {

    private TextView stepCount, treasureCounterTextView;
    private ImageView imageView;
    private Button increaseStepsButton;
    private Button resetButton;
    private USER_DB db;
    private ExecutorService executorService;
    private int loggedInUserID; // 예시 사용자 ID (실제로는 로그인된 사용자 ID를 가져와야 함)
    private String currentDate;

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private boolean isSensorPresent;
    private int totalSteps = 0;
    private int previousTotalSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        stepCount = findViewById(R.id.step_counter);
        treasureCounterTextView = findViewById(R.id.treasure_count);
        imageView = findViewById(R.id.imageView);
        increaseStepsButton = findViewById(R.id.increase_steps_button);
        resetButton = findViewById(R.id.reset_button);

        db = Room.databaseBuilder(this, USER_DB.class, "USER_DB")
                .fallbackToDestructiveMigration()
                .build();
        executorService = Executors.newSingleThreadExecutor();

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        loggedInUserID = sharedPref.getInt("logged_in_user_id", -1);

        // 현재 날짜 가져오기
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
            Toast.makeText(this, "만보기가 지원되지 않습니다.", Toast.LENGTH_SHORT).show();
        }

        if (loggedInUserID != -1) {
            updateStepCount();
            updateTreasureCount();

            imageView.setOnClickListener(v -> useTreasureBox());
            resetButton.setOnClickListener(v -> resetSteps());
        } else {
            Toast.makeText(this, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }


        // 실시간 걸음 수 업데이트
        updateStepCount();

        // 상자 갯수 업데이트
        updateTreasureCount();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useTreasureBox();
            }
        });

        increaseStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseSteps(100);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI); // 센서 동작 딜레이 설정
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == stepSensor) {
            totalSteps = (int) event.values[0];
            int currentSteps = totalSteps - previousTotalSteps;
            if (currentSteps > 0) {
                previousTotalSteps = totalSteps;
                updateWalkingRecord(1); // 걸음 수가 증가할 때마다 1을 증가시킴
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    private void updateStepCount() {
        executorService.execute(() -> {
            WalkingRecord record = db.walkingRecordDao().getWalkingRecordByDate(loggedInUserID, currentDate);
            if (record != null) {
                runOnUiThread(() -> stepCount.setText(String.valueOf(record.walking)));
            }
        });
    }

    private void updateTreasureCount() {
        executorService.execute(() -> {
            WalkingRecord record = db.walkingRecordDao().getWalkingRecordByDate(loggedInUserID, currentDate);
            if (record != null) {
                runOnUiThread(() -> treasureCounterTextView.setText(String.valueOf(record.boxCount)));
            }
        });
    }

    private void useTreasureBox() {
        executorService.execute(() -> {
            WalkingRecord record = db.walkingRecordDao().getWalkingRecordByDate(loggedInUserID, currentDate);
            if (record != null && record.boxCount > 0) {
                record.boxCount--;
                db.walkingRecordDao().updateWalkingRecord(record.id, currentDate, record.walking, record.boxCount);

                USER_ENTITY user = db.userDao().getUser(loggedInUserID);
                user.point += 1;
                db.userDao().updateUser(user);

                runOnUiThread(() -> {
                    treasureCounterTextView.setText(String.valueOf(record.boxCount));
                    Toast.makeText(MainActivity4.this, "포인트가 추가되었습니다!", Toast.LENGTH_SHORT).show();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(MainActivity4.this, "소모할 상자가 없습니다", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateWalkingRecord(int steps) {
        final int userId = loggedInUserID;
        final String date = currentDate;
        executorService.execute(() -> {
            WalkingRecord record = db.walkingRecordDao().getWalkingRecordByDate(userId, date);
            if (record == null) {
                record = new WalkingRecord();
                record.id = userId;
                record.datetime = date;
                record.walking = steps;
                db.walkingRecordDao().insertWalkingRecord(record);
            } else {
                record.addSteps(steps);
                db.walkingRecordDao().updateWalkingRecord(record);
            }

            final WalkingRecord updatedRecord = record;
            runOnUiThread(() -> {
                stepCount.setText(String.valueOf(updatedRecord.walking));
                treasureCounterTextView.setText(String.valueOf(updatedRecord.boxCount));
            });
        });
    }

    private void increaseSteps(int steps) {
        executorService.execute(() -> {
            WalkingRecord record = db.walkingRecordDao().getWalkingRecordByDate(loggedInUserID, currentDate);
            if (record != null) {
                record.addSteps(steps);
                db.walkingRecordDao().updateWalkingRecord(record);

                runOnUiThread(() -> {
                    stepCount.setText(String.valueOf(record.walking));
                    treasureCounterTextView.setText(String.valueOf(record.boxCount));
                    Toast.makeText(MainActivity4.this, steps + " 걸음이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                });
            }
        });
        }
        private void resetSteps() {
            final int userId = loggedInUserID;
            final String date = currentDate;
            previousTotalSteps = totalSteps;
            stepCount.setText("0");
            executorService.execute(() -> {
                WalkingRecord record = db.walkingRecordDao().getWalkingRecordByDate(userId, date);
                if (record != null) {
                    record.walking = 0;
                    db.walkingRecordDao().updateWalkingRecord(record);
                    runOnUiThread(() -> {
                        stepCount.setText("0");
                        treasureCounterTextView.setText(String.valueOf(record.boxCount));
                        Toast.makeText(MainActivity4.this, "걸음 수가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }