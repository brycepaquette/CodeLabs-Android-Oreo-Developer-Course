package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    ListView locListView;
    static ArrayList<String> locArray;
    static ArrayList<Double> latitudes;
    static ArrayList<Double> longitudes;
    static ArrayAdapter arrayAdapter;
    Intent intentMaps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locListView = findViewById(R.id.locListView);


        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.sharedpreferences", Context.MODE_PRIVATE);

        try {
            locArray = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("locArray", ObjectSerializer.serialize(new ArrayList<String>(Arrays.asList("Add a new place...")))));
            latitudes = (ArrayList<Double>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes", ObjectSerializer.serialize(new ArrayList<Double>(Arrays.asList(0.0)))));
            longitudes = (ArrayList<Double>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes", ObjectSerializer.serialize(new ArrayList<Double>(Arrays.asList(0.0)))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, locArray);
        locListView.setAdapter(arrayAdapter);



        locListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intentMaps = new Intent(getApplicationContext(), MapsActivity.class);
                intentMaps.putExtra("placeNumber", position);
                startActivity(intentMaps);
            }
        });
    }
}