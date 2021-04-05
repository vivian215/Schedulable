package com.example.schedule;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.ArrayList;

public class ColorKey {
    private ArrayList<String> events;
    private ArrayList<Paint> paints;
    private int len;
    private int squareSize;
    private int x;
    private ArrayList<Integer[]> times;

    public ColorKey(ArrayList<String> events, ArrayList<Paint> paints, int size, ArrayList<Integer[]> times) {
        this.events = events;
        this.paints = paints;
        len = size;
        x = 150;
        squareSize = 80;
        this.times = times;
    }

    public void drawSquare(Canvas canvas, int y, Paint paint) {
        canvas.drawRect(x, y, x + squareSize, y + squareSize, paint);
    }

    public void drawKey(Canvas canvas, int currEvent) {
        int y = 1450;
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(50);
        Paint highlight = new Paint();
        highlight.setARGB(255, 126, 86, 175);
        highlight.setStyle(Paint.Style.FILL);
        highlight.setTextSize(50);
        highlight.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        for (int i = 0; i < len; i++) {
            drawSquare(canvas, y + i * 100, paints.get(i));
            if (i == currEvent) {
                canvas.drawText(" = " + events.get(i) + " (" + times.get(i)[0] + ":00 - " + times.get(i)[1] + ":00)", x + squareSize, y + i * 100 + squareSize/2 + 15, highlight);
            } else
                canvas.drawText(" = " + events.get(i) + " (" + times.get(i)[0] + ":00 - " + times.get(i)[1] + ":00)", x + squareSize, y + i * 100 + squareSize/2 + 15, textPaint);
        }

    }


}
