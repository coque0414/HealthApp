package com.example.mobile_program;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public EditText editTextID;
    public EditText editTextPassword;
    public Button button_register;
    public Button button_Login;
    public USER_DB db;
    private ExecutorService executorService;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(getString(R.string.combined_text), Html.FROM_HTML_MODE_COMPACT));
        }

        editTextID = findViewById(R.id.editTextID);
        editTextPassword = findViewById(R.id.editTextPassword);
        button_register = findViewById(R.id.button_register);
        button_Login = findViewById(R.id.button_Login);
        db = Room.databaseBuilder(this, USER_DB.class, "USER_DB")
                .fallbackToDestructiveMigration()
                .build();
        executorService = Executors.newSingleThreadExecutor();

        // 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    Manifest.permission.BODY_SENSORS
            }, PERMISSION_REQUEST_CODE);
        }

        button_register.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });

        button_Login.setOnClickListener(view -> {
            String id = editTextID.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (id.isEmpty()) {
                Toast.makeText(MainActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(MainActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            executorService.execute(() -> {
                int userCount = db.userDao().checkUserExists(id);
                if (userCount == 0) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "아이디 또는 비밀번호가 잘못입력 되었다", Toast.LENGTH_SHORT).show());
                    return;
                }

                String storedPassword = db.userDao().getPassword(id);
                if (!storedPassword.equals(password)) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "아이디 또는 비밀번호가 잘못입력 되었다", Toast.LENGTH_SHORT).show());
                    return;
                }
                // 로그인 상태 저장 (SharedPreferences 사용)
                USER_ENTITY user = db.userDao().getUserByID(id);
                SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("logged_in_user_id", user.id);
                editor.apply();

                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "로그인이 완료 되었습니다", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                    startActivity(intent);
                });
            });
        });

    }
}

//        Button button = (Button) findViewById(R.id.check);
//        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);