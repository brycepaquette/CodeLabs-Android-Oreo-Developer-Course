package com.example.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Users", Context.MODE_PRIVATE, null);

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS newUsers (name VARCHAR, age INT(3), id INTEGER PRIMARY KEY)");
//        sqLiteDatabase.execSQL("INSERT INTO newUsers (name,age) VALUES ('Nick', 23)");
//        sqLiteDatabase.execSQL("INSERT INTO newUsers (name,age) VALUES ('Nick', 43)");
//        sqLiteDatabase.execSQL("INSERT INTO newUsers (name,age) VALUES ('Dave', 14)");

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM newUsers", null);

        int nameIndex = c.getColumnIndex("name");
        int ageIndex = c.getColumnIndex("age");
        int idIndex = c.getColumnIndex("id");

        c.moveToFirst();

        while (!c.isAfterLast()) {
            Log.i("name", c.getString(nameIndex));
            Log.i("age", c.getString(ageIndex));
            Log.i("id", c.getString(idIndex));
            c.moveToNext();
        }
    }
}