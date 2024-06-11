package com.example.mobile_program;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class WaterFragment extends Fragment {

    private ImageView[] imageViews = new ImageView[8];
    private TextView[] textViews = new TextView[8];
    private boolean[] clicked = new boolean[8];
    private int currentCupIndex = 0;

    private USER_DB db;
    private ExecutorService executorService;
    private String loggedInUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main5, container, false);

        for (int i = 0; i < 8; i++) {
            int imageResId = getResources().getIdentifier("imageView" + (i + 1), "id", requireActivity().getPackageName());
            int textResId = getResources().getIdentifier("textView" + (i + 1), "id", requireActivity().getPackageName());
            imageViews[i] = view.findViewById(imageResId);
            textViews[i] = view.findViewById(textResId);

            final int index = i;
            imageViews[i].setOnClickListener(v -> handleCupClick(index));
        }
        imageViews[0].setImageResource(R.drawable.beforeplus);

        db = Room.databaseBuilder(requireContext(), USER_DB.class, "USER_DB")
                .fallbackToDestructiveMigration()
                .build();
        executorService = Executors.newSingleThreadExecutor();

        SharedPreferences sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        loggedInUserId = sharedPref.getString("logged_in_user_id", null);

        if (loggedInUserId != null) {
            loadHealthRecord();
        } else {
            Toast.makeText(requireContext(), "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }

        return view;
    }

    private void loadHealthRecord() {
        final String userId = loggedInUserId;
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, getCurrentDate());
            if (record != null) {
                getActivity().runOnUiThread(() -> {
                    for (int i = 0; i < record.waterCount; i++) {
                        imageViews[i].setImageResource(R.drawable.beforefull);
                        clicked[i] = true;
                    }
                });
            } else {
                HealthRecord newRecord = new HealthRecord(userId, getCurrentDate(), 0, 0, 0);
                db.HealthRecordDao().insertHealthRecord(newRecord);
            }
        });
    }

    private void handleCupClick(int index) {
        if (!clicked[index] && index == currentCupIndex) {
            clicked[index] = true;
            imageViews[index].setImageResource(R.drawable.beforefull);

            if (currentCupIndex < 7) {
                Toast.makeText(requireContext(), "잘하셨습니다!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "오늘 하루 수고하셨습니다.", Toast.LENGTH_LONG).show();
            }

            updateHealthRecord(currentCupIndex + 1);
            currentCupIndex++;
            if (currentCupIndex < 8) {
                imageViews[currentCupIndex].setImageResource(R.drawable.beforeplus);
            }
        }
    }

    private void updateHealthRecord(int newWaterCount) {
        final String userId = loggedInUserId;
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, getCurrentDate());
            if (record != null) {
                record.waterCount = newWaterCount;
                db.HealthRecordDao().updateHealthRecord(record);

                USER_ENTITY user = db.userDao().getUserByID(userId);
                user.point += (newWaterCount == 8) ? 5 : 1;
                db.userDao().updateUser(user);

                getActivity().runOnUiThread(() -> {
                    String message = (newWaterCount == 8) ? "수고하셨습니다" : "잘하고있어요!";
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}
