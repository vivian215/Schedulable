package com.example.schedule;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class InputActivity extends AppCompatActivity {
    EditText mEvent1;
    EditText mEvent2;
    EditText mEvent3;
    EditText mEvent4;
    EditText mEvent5;
    EditText start1;
    EditText start2;
    EditText start3;
    EditText start4;
    EditText start5;
    EditText end1;
    EditText end2;
    EditText end3;
    EditText end4;
    EditText end5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.input);

        mEvent1 = findViewById(R.id.event1);
        mEvent2 = findViewById(R.id.event2);
        mEvent3 = findViewById(R.id.event3);
        mEvent4 = findViewById(R.id.event4);
        mEvent5 = findViewById(R.id.event5);

        start1 = findViewById(R.id.start1);
        start2 = findViewById(R.id.start2);
        start3 = findViewById(R.id.start3);
        start4 = findViewById(R.id.start4);
        start5 = findViewById(R.id.start5);

        end1 = findViewById(R.id.end1);
        end2 = findViewById(R.id.end2);
        end3 = findViewById(R.id.end3);
        end4 = findViewById(R.id.end4);
        end5 = findViewById(R.id.end5);

        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputActivity.this, SchedActivity.class);
                String[] events = new String[] {mEvent1.getText().toString(), mEvent2.getText().toString(), mEvent3.getText().toString(), mEvent4.getText().toString(), mEvent5.getText().toString()};
                ArrayList<String> eventsArrayList = new ArrayList<String>(Arrays.asList(events));
                int[] starts = new int[] {Integer.parseInt(start1.getText().toString()),
                        Integer.parseInt(start2.getText().toString()),
                        Integer.parseInt(start3.getText().toString()),
                        Integer.parseInt(start4.getText().toString()),
                        Integer.parseInt(start5.getText().toString())};
                int[] ends = new int[] {Integer.parseInt(end1.getText().toString()),
                        Integer.parseInt(end2.getText().toString()),
                        Integer.parseInt(end3.getText().toString()),
                        Integer.parseInt(end4.getText().toString()),
                        Integer.parseInt(end5.getText().toString())};
                ArrayList<Integer> startsArrayList = new ArrayList<Integer>(starts.length);
                ArrayList<Integer> endsArrayList = new ArrayList<Integer>(ends.length);
                for (int i = 0; i < starts.length; i++) {
                    startsArrayList.add(starts[i]);
                    endsArrayList.add(ends[i]);
                }
                intent.putExtra("events", eventsArrayList);
                intent.putExtra("starts", startsArrayList);
                intent.putExtra("ends", endsArrayList);
                intent.putExtra("hasEdited", true);
                startActivity(intent);
            }
        });


    }
}