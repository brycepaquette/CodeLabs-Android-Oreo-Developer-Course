package com.example.parseserverstarterkit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Bitnami Dashboard - user - nLox2tBi5hlH
    //http://52.207.110.124/apps
    //http://52.207.110.124/parse



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ParseUser user = new ParseUser();
//        user.setUsername("Bryce");
//        user.setPassword("abc123");

//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.i("SIGN UP OK", "We did it!");
//                }
//                else {
//                    e.printStackTrace();
//                }
//            }
//        });
//        ParseUser.logInInBackground("Bryce", "abc123", new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (e == null) {
//                    Log.i("SUCESSFUL LOGIN", "Login Succeeded");
//                }
//                else {
//                    e.printStackTrace();
//                }
//            }
//        });
        if (ParseUser.getCurrentUser() != null) {
            Log.i("Signed in", ParseUser.getCurrentUser().getUsername());
        }
        else {
            Log.i("Please sign in", "Not signed in");
        }



        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}