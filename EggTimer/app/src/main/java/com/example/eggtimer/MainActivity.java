package com.example.eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar;
    Button goBtn;
    TextView timerText;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;

    int milliseconds = 310000;
    Integer MAX_SEEK_VALUE = 1199999;

    public void updateTimerText(int ms) {
        int minutes = (ms / 1000) / 60;
        int seconds = (ms / 1000) % 60;
        timerText.setText(String.format("%d:%02d", minutes, seconds));
    }

    public void startTimer(View view) {
        countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText((int) millisUntilFinished);
                seekBar.setProgress((int) millisUntilFinished);
            }

            @Override
            public void onFinish() {
                mediaPlayer.start();
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        goBtn = findViewById(R.id.goBtn);
        timerText = findViewById(R.id.textView);

        seekBar.setMax(MAX_SEEK_VALUE);
        seekBar.setProgress(milliseconds);
        updateTimerText(milliseconds);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimerText(progress);
                milliseconds = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                countDownTimer.cancel();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}