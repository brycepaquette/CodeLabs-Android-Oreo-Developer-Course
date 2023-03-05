package com.example.timersdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Timer Option #1 - Handler and Runnable
        Handler handler = new Handler();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                Log.i("Log:", "A second passed by...");

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(run);

        // Timer Option #2 - CountDownTimer
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisecondsUntilDone) {
                Log.i("log:", Long.toString(millisecondsUntilDone / 1000));
            }

            public void onFinish() {
                Log.i("Completed:", "No more countdown");
            }
        }.start();


    }
}