package com.example.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.sharedpreferences", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("username", "bpaq0296").commit();

        String username = sharedPreferences.getString("username","not_found");

        Log.i("LOG", username);

        ArrayList<String> friends = new ArrayList<>(Arrays.asList("Victor", "Abe", "Ben", "Jon"));

        try {
            sharedPreferences.edit().putString("friends", ObjectSerializer.serialize(friends)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("SERIAL", sharedPreferences.getString("friends", "NULL"));

        ArrayList<String> newFriends = new ArrayList<>();

        try {
            newFriends = (ArrayList) ObjectSerializer.deserialize(sharedPreferences.getString("friends", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("FRIENDSNEW", newFriends.toString());

    }
}