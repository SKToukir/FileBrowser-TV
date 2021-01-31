package com.walton.filebrowser.ui.visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;


import java.util.Random;

/**
 * Custom view that creates a Circle and Bar visualizer effect for
 * the android {@link android.media.MediaPlayer}
 *
 * Created by gautam chibde on 20/11/17.
 */

public class CircleBarVisualizer extends BaseVisualizer {
    private float[] points;
    private Paint circlePaint;
    private int radius;

    public CircleBarVisualizer(Context context) {
        super(context);
    }

    public CircleBarVisualizer(Context context,
                               @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleBarVisualizer(Context context,
                               @Nullable AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        paint.setStyle(Paint.Style.STROKE);
        circlePaint = new Paint();
        radius = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (radius == -1) {
            radius = getHeight() < getWidth() ? getHeight() : getWidth();
            radius = (int) (radius * 0.65 / 2);
            double circumference = 4 * Math.PI * radius;
            paint.setStrokeWidth((float) (circumference / 120));
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setStrokeWidth(4);
        }

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, circlePaint);

        if (bytes != null) {
            if (points == null || points.length < bytes.length * 4) {
                points = new float[bytes.length * 4];
            }
            double angle = 0;

            for (int i = 0; i < 120; i++, angle += 10) {
                int x = (int) Math.ceil(i * 8.5);
                int t = ((byte) (-Math.abs(bytes[x]) + 128)) * (getHeight() / 4) / 128;
                circlePaint.setColor(Color.parseColor("#00000000"));
                points[i * 4] = (float) (getWidth() / 2
                        + radius
                        * Math.cos(Math.toRadians(angle)));

                points[i * 4 + 1] = (float) (getHeight() / 2
                        + radius
                        * Math.sin(Math.toRadians(angle)));

                points[i * 4 + 2] = (float) (getWidth() / 2
                        + (radius + t)
                        * Math.cos(Math.toRadians(angle)));

                points[i * 4 + 3] = (float) (getHeight() / 2
                        + (radius + t)
                        * Math.sin(Math.toRadians(angle)));

                Random rnd = new Random();


                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                paint.setColor(color);
            }



            canvas.drawLines(points, paint);
        }
        super.onDraw(canvas);
    }
}