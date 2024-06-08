package com.example.mobile_program;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity {

    private TextView welcomeText;
    private Button buttonWater, buttonSteps, buttonGraph, buttonStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        welcomeText = findViewById(R.id.welcome_text);
        buttonWater = findViewById(R.id.button_water);
        buttonSteps = findViewById(R.id.button_steps);
        buttonGraph = findViewById(R.id.button_graph);
        buttonStore = findViewById(R.id.button_store);

        // 닉네임 가져오기 (예시)
        String nickname = "User"; // 실제로는 Intent로 전달받거나 SharedPreferences에서 가져올 수 있습니다.
        welcomeText.setText(nickname + "님, 환영합니다.");

        buttonWater.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity3.this, MainActivity5.class);
            startActivity(intent);
        });

        buttonSteps.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
            startActivity(intent);
        });

        buttonGraph.setOnClickListener(v -> {
            Toast.makeText(MainActivity3.this, "만드는중...", Toast.LENGTH_SHORT).show();
            return;
//            Intent intent = new Intent(MainActivity3.this, MainActivity6.class);
//            startActivity(intent);
        });

        buttonStore.setOnClickListener(v -> {
            Toast.makeText(MainActivity3.this, "만드는중...", Toast.LENGTH_SHORT).show();
            return;
//            Intent intent = new Intent(MainActivity3.this, MainActivity7.class);
//            startActivity(intent);
        });
    }
}