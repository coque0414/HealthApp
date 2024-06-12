package com.example.mobile_program;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StepsFragment extends Fragment implements SensorEventListener {

    private TextView stepCounterTextView, treasureCounterTextView;
    private ImageView imageView;
    private Button increaseStepsButton, resetButton;
    private ProgressBar progressBarSteps, recordProgressBar;
    private USER_DB db;
    private ExecutorService executorService;
    private String loggedInUserID;
    private String currentDate;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private boolean isSensorPresent;
    private int totalSteps = 0;
    private int previousTotalSteps = 0;
    private final int MAX_STEPS = 10000; // 하루 목표 걸음 수 설정

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main4, container, false);

        stepCounterTextView = view.findViewById(R.id.step_counter);
        treasureCounterTextView = view.findViewById(R.id.treasure_count);
        imageView = view.findViewById(R.id.imageView);
        increaseStepsButton = view.findViewById(R.id.increase_steps_button);
        resetButton = view.findViewById(R.id.reset_button);
        recordProgressBar = view.findViewById(R.id.record_progress_bar);



        recordProgressBar.setMax(MAX_STEPS); // Circular ProgressBar의 최대값 설정

        db = Room.databaseBuilder(requireContext(), USER_DB.class, "USER_DB")
                .fallbackToDestructiveMigration()
                .build();
        executorService = Executors.newSingleThreadExecutor();

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        loggedInUserID = sharedPref.getString("logged_in_user_id", null);

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
            Toast.makeText(requireContext(), "만보기가 지원되지 않습니다.", Toast.LENGTH_SHORT).show();
        }

        if (loggedInUserID != null) {
            updateStepCount();
            updateTreasureCount();

            imageView.setOnClickListener(v -> useTreasureBox());
            resetButton.setOnClickListener(v -> resetSteps());
            increaseStepsButton.setOnClickListener(v -> increaseSteps(100));
        } else {
            Toast.makeText(requireContext(), "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSensorPresent) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int steps = (int) event.values[0];
            int currentSteps = steps - previousTotalSteps;
            if (currentSteps > 0) {
                previousTotalSteps = steps;
                updateWalkingRecord(1);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void updateStepCount() {
        final String userId = loggedInUserID;
        final String date = currentDate;
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, currentDate);
            if (record != null) {
                getActivity().runOnUiThread(() -> {
                    stepCounterTextView.setText(String.valueOf(record.walking));
                    updateProgressBar(record.walking);
                });
            }
        });
    }

    private void updateTreasureCount() {
        final String userId = loggedInUserID;
        final String date = currentDate;
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, currentDate);
            if (record != null) {
                getActivity().runOnUiThread(() -> treasureCounterTextView.setText(String.valueOf(record.boxCount)));
            }
        });
    }

    private void useTreasureBox() {
        final String userId = loggedInUserID;
        final String date = currentDate;
        final TextView pointTextView = getView().findViewById(R.id.point); // pointTextView를 final로 선언하고 찾습니다.
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, currentDate);
            if (record != null && record.boxCount > 0) {
                record.boxCount--;
                db.HealthRecordDao().updateHealthRecord(userId, currentDate, record.walking, record.boxCount);

                USER_ENTITY user = db.userDao().getUser(userId);
                user.point += 1; // 포인트 증가
                db.userDao().updateUser(user);

                getActivity().runOnUiThread(() -> {
                    treasureCounterTextView.setText(String.valueOf(record.boxCount));

                    // 포인트 텍스트뷰에 포인트 값을 설정
                    pointTextView.setText("포인트: " + user.point);

                    Toast.makeText(requireContext(), "포인트가 추가되었습니다!", Toast.LENGTH_SHORT).show();
                });
            } else {
                getActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "소모할 상자가 없습니다", Toast.LENGTH_SHORT).show());
            }
        });
    }



    private void updateWalkingRecord(int steps) {
        final String userId = loggedInUserID;
        final String date = currentDate;
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, date);
            if (record == null) {
                record = new HealthRecord(userId, date, steps, 0, 0);
                db.HealthRecordDao().insertWalkingRecord(record);
            } else {
                record.addSteps(steps);
                db.HealthRecordDao().updateHealthRecord(record);
            }

            final HealthRecord updatedRecord = record;
            getActivity().runOnUiThread(() -> {
                stepCounterTextView.setText(String.valueOf(updatedRecord.walking));
                treasureCounterTextView.setText(String.valueOf(updatedRecord.boxCount));
                updateProgressBar(updatedRecord.walking);
            });
        });
    }

    private void updateProgressBar(int currentSteps) {
        // 프로그래스바 업데이트
        recordProgressBar.setProgress(currentSteps);
    }

    private void increaseSteps(int steps) {
        final String userId = loggedInUserID;
        final String date = currentDate;
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, currentDate);
            if (record != null) {
                record.addSteps(steps);
                db.HealthRecordDao().updateHealthRecord(record);

                getActivity().runOnUiThread(() -> {
                    stepCounterTextView.setText(String.valueOf(record.walking));
                    treasureCounterTextView.setText(String.valueOf(record.boxCount));
                    updateProgressBar(record.walking);
                    Toast.makeText(requireContext(), steps + " 걸음이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void resetSteps() {
        final String userId = loggedInUserID;
        final String date = currentDate;
        previousTotalSteps = totalSteps;
        stepCounterTextView.setText("0");
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, date);
            if (record != null) {
                record.walking = 0;
                record.boxCount = 0;
                db.HealthRecordDao().updateHealthRecord(record);
                getActivity().runOnUiThread(() -> {
                    stepCounterTextView.setText("0");
                    treasureCounterTextView.setText("0");
                    updateProgressBar(0);
                    Toast.makeText(requireContext(), "걸음 수와 상자 수가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
