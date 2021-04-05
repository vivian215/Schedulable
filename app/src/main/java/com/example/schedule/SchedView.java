package com.example.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class SchedView extends SurfaceView implements Runnable {
    private Thread thread;
    private Paint halfPaint;
    private Paint textPaint;
    private Paint paint;
    private Bitmap clock;
    private ArrayList<Integer[]> times;
    private Integer[][] exampleTimes;
    private ArrayList<String> events;
    private PieChart pie;
    private ArrayList<Paint> paints;
    private int size;
    private int distFromCenter;
    private ColorKey key;
    private Intent intent;
    private ArrayList<Integer> starts;
    private ArrayList<Integer> ends;
    private boolean hasEdited;
    private boolean run;
    private Bitmap backarrow;
    private Bitmap minutehand;
    private float angle;
    private RectF border;
    private Bitmap bigminutehand;
    private Bitmap bigrotatedminutehand;
    private int origW;
    private int origH;
    private TextToSpeech tts;

    public SchedView(Context context, Resources res) {
        super(context);
        intent = getActivity().getIntent();
        hasEdited = intent.getBooleanExtra("hasEdited", false);

        size = 5;
        paint = new Paint();
        halfPaint = new Paint();
        halfPaint.setAlpha(150);

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(80);
        clock = BitmapFactory.decodeResource(getResources(), R.drawable.clockbg);
        clock = Bitmap.createScaledBitmap(clock, (int) (clock.getWidth()*0.45), (int) (clock.getHeight()*0.45), false);
        backarrow = BitmapFactory.decodeResource(getResources(), R.drawable.backarrow);
        backarrow = Bitmap.createScaledBitmap(backarrow, (int) (clock.getWidth()*0.13), (int) (clock.getHeight()*0.13), false);

        minutehand = BitmapFactory.decodeResource(getResources(), R.drawable.minutehand);
        minutehand = Bitmap.createScaledBitmap(minutehand, (int) (clock.getWidth()*0.05), (int) (clock.getHeight()*0.5), false);
        bigminutehand = BitmapFactory.decodeResource(getResources(), R.drawable.bigminutehand);
        bigminutehand = Bitmap.createScaledBitmap(bigminutehand, (int) (clock.getWidth()), (int) (clock.getWidth()), false);
        bigrotatedminutehand = bigminutehand;
        origW = bigminutehand.getWidth();
        origH = bigminutehand.getHeight();

        exampleTimes = new Integer[][]{{12, 2}, {2, 5}, {5, 7}, {7, 9}, {9, 12}};
        times = new ArrayList<Integer[]>();
        for (int i = 0; i < size; i++) {
            times.add(new Integer[2]);
            times.set(i, exampleTimes[i]);
        }
        if (hasEdited) {
            starts = intent.getIntegerArrayListExtra("starts");
            ends = intent.getIntegerArrayListExtra("ends");
            for (int i = 0; i < size; i++) {
                times.set(i, new Integer[]{starts.get(i), ends.get(i)});
                if (starts.get(i) == 12) {
                    times.set(i, new Integer[]{0, ends.get(i)});
                }
            }
        }

        events = new ArrayList<String>();
        events.add("Enter Activity Name!");
        events.add("Enter Activity Name!");
        events.add("Enter Activity Name!");
        events.add("Enter Activity Name!");
        events.add("Enter Activity Name!");
        if (hasEdited) {
            events = intent.getStringArrayListExtra("events");
        }

        this.paints = new ArrayList<Paint>();
        Random rnd = new Random();
        for (int i = 0; i < size; i++) {
            paints.add(new Paint());
            paints.get(i).setARGB(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            paints.get(i).setStyle(Paint.Style.FILL);
        }

        pie = new PieChart(events, size, times, paints);

        distFromCenter = 125;

        key = new ColorKey(events, paints, size, times);

        run = true;

        angle = 0;
        border = new RectF();

        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                }
                else
                    Log.e("error", "Initialization Failed!");
            }
        });
        tts.setLanguage(Locale.US);
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle,source.getWidth()/2,source.getHeight()/2);

        Bitmap result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return result;
    }

    public void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = this.getHolder().lockCanvas();

            canvas.drawARGB(255, 255, 255, 255);

            // draw top bar
            Paint topBarPaint = new Paint();
            topBarPaint.setARGB(255, 126, 86, 175);
            RectF topBar = new RectF(0, 0, 1100, 260);
            canvas.drawRect(topBar, topBarPaint);
            canvas.drawBitmap(backarrow, 40, 60, paint);
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(130);
            canvas.drawText("Schedulable", 240, 175, textPaint);

            Point center = new Point(canvas.getWidth()/2, canvas.getHeight()/2-distFromCenter);
            canvas.drawBitmap(clock, center.x-clock.getWidth()/2, center.y-clock.getHeight()/2-distFromCenter/4+30, paint);

            pie.drawPie(canvas, distFromCenter);

            bigrotatedminutehand = rotateBitmap(bigminutehand, angle);

            int newX = (bigrotatedminutehand.getWidth() - origW) / 2;
            if (!(angle % 90 == 0))
                bigrotatedminutehand = Bitmap.createBitmap(bigrotatedminutehand, newX, newX, origW, origH);

            canvas.drawBitmap(bigrotatedminutehand,center.x-clock.getWidth()/2, center.y-clock.getHeight()/2-distFromCenter/4+30, paint);

            key.drawKey(canvas, getCurrentEvent());

            getHolder().unlockCanvasAndPost(canvas);

            if (isStart()) {
                speak("Next up is " + events.get(getCurrentEvent()));
            }
            angle += 1;
        }
    }

    public void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    public int getCurrentEvent() {
        int currentTime = (int) (angle / 30);
        for (int i = 0; i < times.size(); i++) {
            if (currentTime >= times.get(i)[0] && currentTime < times.get(i)[1]) {
                return i;
            }
        }
        return 0;
    }

    public boolean isStart() {
        for (int i = 0; i < times.size(); i++) {
            int start = times.get(i)[0];
            if (start * 30 == angle) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        while (run) {
            draw();
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {
        thread = new Thread(this);
        thread.start();
    }

    public void pause () {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public boolean onPressEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (x < 200 && y < 200) {
                    return true;
                }
                break;
        }
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean clicked = onPressEvent(event);
        if (clicked) {
            Activity schedActivity = getActivity();
            Intent intent = new Intent(schedActivity, MainActivity.class);
            run = false;
            schedActivity.startActivity(intent);
        }

        return true;
    }
}
