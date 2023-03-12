package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class NoteActivity extends AppCompatActivity {

    EditText noteTextInput;

    int noteIndex;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteTextInput = findViewById(R.id.noteTextInput);

        noteIndex = getIntent().getIntExtra(getString(R.string.notesSpIndexName), 9999);
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        noteTextInput.setText(MainActivity.notesArray.get(noteIndex));


    }

    @Override
    public void onBackPressed() {
        Log.i("BACK PRESSED", "true");
        try {
            saveNote();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "An Error Occurred while saving", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void saveNote() throws IOException {
        String text = noteTextInput.getText().toString();
        String substring = createSubstring(text);

        MainActivity.notesArray.set(noteIndex, text);
        MainActivity.substringArray.set(noteIndex, substring);

        updateSharedPreferences();
        MainActivity.arrayAdapter.notifyDataSetChanged();
    }

    public void updateSharedPreferences() {
        try {
            sharedPreferences.edit().putString(getString(R.string.notesSpName), ObjectSerializer.serialize(MainActivity.notesArray)).apply();
            sharedPreferences.edit().putString(getString(R.string.substringSpName), ObjectSerializer.serialize(MainActivity.substringArray)).apply();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createSubstring(String text) {
        int substringCharLimit = 25;
        int newLineIndex = text.indexOf("\n");
        String substring = "";

        if (newLineIndex != -1 && newLineIndex < substringCharLimit) {
            substring = text.substring(0,newLineIndex);
        }
        else {
            text.substring(0,substringCharLimit);
        }

        return substring;
    }
}