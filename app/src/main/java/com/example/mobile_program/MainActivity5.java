package com.example.mobile_program;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity5 extends AppCompatActivity {

    private ImageView[] imageViews;
    private TextView[] textViews;
    private int currentCupIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        imageViews = new ImageView[8];
        textViews = new TextView[8];

        for (int i = 0; i < 8; i++) {
            int imageResId = getResources().getIdentifier("imageView" + (i + 1), "id", getPackageName());
            int textResId = getResources().getIdentifier("textView" + (i + 1), "id", getPackageName());
            imageViews[i] = findViewById(imageResId);
            textViews[i] = findViewById(textResId);

            // Set click listener for each image view
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleCupClick(view);
                }
            });
        }
        imageViews[0].setImageResource(R.drawable.plus);
    }
    private void handleCupClick(View view) {
        if (currentCupIndex >= 8) return;

        ImageView clickedImageView = (ImageView) view;

        // Ensure the clicked cup is the current cup to be clicked
        if (clickedImageView == imageViews[currentCupIndex]) {
            // Change the image to full.png and update the timestamp
            clickedImageView.setImageResource(R.drawable.full);
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            textViews[currentCupIndex].setText(currentTime);

            if (currentCupIndex < 7) {
                Toast.makeText(this, "잘하셨습니다!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "오늘 하루 수고하셨습니다.", Toast.LENGTH_LONG).show();
            }

            // Move to the next cup if it exists
            currentCupIndex++;
            if (currentCupIndex < 8) {
                imageViews[currentCupIndex].setImageResource(R.drawable.plus);
            }
        }
    }
}