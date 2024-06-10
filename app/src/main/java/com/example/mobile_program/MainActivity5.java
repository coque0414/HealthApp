package com.example.mobile_program;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity5 extends AppCompatActivity {

    private ImageView[] imageViews = new ImageView[8];
    private boolean[] clicked = new boolean[8];
    private USER_DB db;
    private ExecutorService executorService;
    private String loggedInUserId;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        imageViews[0] = findViewById(R.id.imageView1);
        imageViews[1] = findViewById(R.id.imageView2);
        imageViews[2] = findViewById(R.id.imageView3);
        imageViews[3] = findViewById(R.id.imageView4);
        imageViews[4] = findViewById(R.id.imageView5);
        imageViews[5] = findViewById(R.id.imageView6);
        imageViews[6] = findViewById(R.id.imageView7);
        imageViews[7] = findViewById(R.id.imageView8);

        db = Room.databaseBuilder(this, USER_DB.class, "USER_DB")
                .fallbackToDestructiveMigration()
                .build();
        executorService = Executors.newSingleThreadExecutor();

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        loggedInUserId = sharedPref.getString("logged_in_user_id", null);

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (loggedInUserId != null) {
            loadHealthRecord();

            for (int i = 0; i < imageViews.length; i++) {
                final int index = i;
                imageViews[i].setOnClickListener(v -> handleClick(index));
            }
        } else {
            Toast.makeText(this, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadHealthRecord() {
        final String userId = loggedInUserId;
        final String date = currentDate;
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, date);
            if (record != null) {
                runOnUiThread(() -> {
                    for (int i = 0; i < record.waterCount; i++) {
                        imageViews[i].setImageResource(R.drawable.full);
                        clicked[i] = true;
                    }
                });
            } else {
                HealthRecord newRecord = new HealthRecord(userId, date, 0,0,0);
                db.HealthRecordDao().insertHealthRecord(newRecord);
            }
        });
    }

    private void handleClick(int index) {
        if (!clicked[index]) {
            clicked[index] = true;
            imageViews[index].setImageResource(R.drawable.full);

            updateHealthRecord(index + 1);
        }
    }

    private void updateHealthRecord(int newWaterCount) {
        final String userId = loggedInUserId;
        final String date = currentDate;
        executorService.execute(() -> {
            HealthRecord record = db.HealthRecordDao().getHealthRecordByDate(userId, date);
            if (record != null) {
                record.waterCount = newWaterCount;
                db.HealthRecordDao().updateHealthRecord(record);

                USER_ENTITY user = db.userDao().getUserByID(userId);
                user.point += (newWaterCount == 8) ? 5 : 1;
                db.userDao().updateUser(user);

                runOnUiThread(() -> {
                    String message = (newWaterCount == 8) ? "수고하셨습니다" : "잘하고있어요!";
                    Toast.makeText(MainActivity5.this, message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
