package com.example.multipleactivitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends AppCompatActivity {

    Button goBack;

    public void goBack(View view) {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        goBack = findViewById(R.id.button2);

        Intent intent = getIntent();
        String variable = intent.getStringExtra("Variable");

        Log.i("INTENT VARIABLE", variable);

    }
}