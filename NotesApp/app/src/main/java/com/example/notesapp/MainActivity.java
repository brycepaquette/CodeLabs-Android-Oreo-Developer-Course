package com.example.notesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // Instantiate components
    ListView notesListView;

    // Instantiate variables/objects
    static ArrayList<String> notesArray;
    static ArrayList<String> substringArray;
    static ArrayAdapter<String> arrayAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);

        // Define components
        notesListView = findViewById(R.id.notesListView);

        getSpArrays();
        initializeNotesListView();
        setNotesListViewListeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.addNote:
                try {
                    addNote();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    public void getSpArrays() {
        try {
            notesArray = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString(getString(R.string.notesSpName), ObjectSerializer.serialize(new ArrayList<String>())));
            substringArray = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString(getString(R.string.substringSpName), ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeNotesListView() {
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, substringArray);
        notesListView.setAdapter(arrayAdapter);
    }

    public void setNotesListViewListeners() {
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openNote(position);
            }
        });
        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alertRemoveNote(position);
                return true;
            }
        });
    }

    public void alertRemoveNote(int position) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.alertRemoveNoteTitle)
                .setMessage(R.string.alertRemoveNoteMessage)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            removeNote(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
    }

    public void addNote() throws IOException {
        notesArray.add("");
        substringArray.add(getString(R.string.newNote));
        updateSharedPreferences();
        arrayAdapter.notifyDataSetChanged();
    }

    public void removeNote(int position) throws IOException {
        notesArray.remove(position);
        substringArray.remove(position);
        updateSharedPreferences();
        arrayAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.noteRemoved, Toast.LENGTH_SHORT).show();
    }

    public void openNote(int position) {
        Intent noteIntent = new Intent(getApplicationContext(), NoteActivity.class);
        noteIntent.putExtra(getString(R.string.notesSpIndexName), position);
        startActivity(noteIntent);
    }

    public void updateSharedPreferences() {
        try {
            sharedPreferences.edit().putString(getString(R.string.notesSpName), ObjectSerializer.serialize(notesArray)).apply();
            sharedPreferences.edit().putString(getString(R.string.substringSpName), ObjectSerializer.serialize(substringArray)).apply();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}