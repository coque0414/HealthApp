package com.example.mobile_program;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public EditText editText_person;
    public EditText editTextEmail;
    public EditText editTextPassword;
    public Button button_Login;
    public CheckBox check;
    public USER_DB db;
    private ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(getString(R.string.combined_text), Html.FROM_HTML_MODE_COMPACT));
        }

        Button button = (Button) findViewById(R.id.check);
        button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        editText_person = findViewById(R.id.editText_person);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        button_Login = findViewById(R.id.button_Login);
        check = findViewById(R.id.check);

        db = Room.databaseBuilder(this, USER_DB.class, "USER_DB").allowMainThreadQueries().build();
        executorService = Executors.newSingleThreadExecutor();

        button_Login.setOnClickListener(v -> registerUser());
    }
    private void registerUser() {
        String name = editText_person.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty()) {
            editText_person.setError("이름을 입력해주세요");
            editText_person.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("아이디를 입력해주세요");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("비밀번호를 입력해주세요");
            editTextPassword.requestFocus();
            return;
        }

        if (!check.isChecked()) {
            Toast.makeText(this, "회원가입을 위해 체크박스를 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            int count = db.userDao().checkUserExists(email);
            if (count > 0) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show());
            } else {
                USER_ENTITY newUser = new USER_ENTITY(email, password, name);
                db.userDao().insertUser(newUser);
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                    // MainActivity2로 이동
                    Intent intent = new Intent(this, MainActivity4.class);
                    startActivity(intent);
//                    finish(); // 현재 액티비티를 종료하여 백 스택에서 제거
                });
            }
        });
    }
}
