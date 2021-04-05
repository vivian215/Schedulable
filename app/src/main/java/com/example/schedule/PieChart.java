package com.example.schedule;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import java.util.ArrayList;

public class PieChart {
    private ArrayList<String> events;
    private int size;
    private ArrayList<Integer[]> times;
    private ArrayList<Paint> paints;


    public PieChart(ArrayList<String> events, int size, ArrayList<Integer[]> times, ArrayList<Paint> paints) {
        this.events = events;
        this.size = size;
        this.times = times;
        this.paints = paints;
    }

    public void drawArc(int sweep, int offset, Paint paint, Canvas canvas, int distFromCenter) {
        Point center = new Point(canvas.getWidth()/2, canvas.getHeight()/2-distFromCenter);
        int inner_radius = 0;
        int outer_radius = 460;

        RectF outer_rect = new RectF(center.x-outer_radius, center.y-outer_radius, center.x+outer_radius, center.y+outer_radius);
        RectF inner_rect = new RectF(center.x-inner_radius, center.y-inner_radius, center.x+inner_radius, center.y+inner_radius);

        Path path = new Path();
        path.arcTo(outer_rect, offset, sweep);
        path.arcTo(inner_rect, offset + sweep, sweep);
        path.close();

        canvas.drawPath(path, paint);

        Paint border = new Paint();
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(2);
        canvas.drawPath(path, border);
    }

    public void drawPie(Canvas canvas, int distFromCenter) {
        int sweep = 0;
        int offset = 210;
        int firstTime = times.get(0)[1] - times.get(0)[0];
        offset += firstTime * 30;
        for (int i = 0; i < size; i++) {
            int start = times.get(i)[0];
            int end = times.get(i)[1];
            if (start == 12) {
                start = 0;
            }
            sweep = (end - start) * 30;
            drawArc(sweep, offset, paints.get(i), canvas, distFromCenter);

            offset += sweep;
        }
    }



}
