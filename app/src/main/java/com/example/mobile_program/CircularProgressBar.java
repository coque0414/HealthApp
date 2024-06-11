package com.example.mobile_program;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircularProgressBar extends View {

    private Paint paint = new Paint();
    private int progress = 0;
    private int max = 100;

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        paint.setColor(0xFFE0E0E0);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawCircle(width / 2, height / 2, radius - 20, paint);

        paint.setColor(0xFF3F51B5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        float sweepAngle = 360 * progress / max;
        canvas.drawArc(20, 20, width - 20, height - 20, -90, sweepAngle, false, paint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setMax(int max) {
        this.max = max;
    }
}
