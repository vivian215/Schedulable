package com.example.schedule;
import android.graphics.Point;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SchedActivity extends AppCompatActivity {
    private SchedView schedView;
    public TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("activitycheck", "oncreate");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        schedView = new SchedView(this, getResources());
        setContentView(schedView);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        schedView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        schedView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        schedView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
