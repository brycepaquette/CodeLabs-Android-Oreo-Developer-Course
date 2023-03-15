package com.example.InstagramClone;


import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myappID")
                .clientKey("nLox2tBi5hlH")
                .server("http://52.207.110.124/parse/")
                .build()
        );

        // Bypass user login
//        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}