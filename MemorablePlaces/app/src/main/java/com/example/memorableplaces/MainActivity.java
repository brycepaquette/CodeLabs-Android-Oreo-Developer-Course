package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView locListView;
    static ArrayList<String> locArray;
    static ArrayList<LatLng> locations;
    static ArrayAdapter arrayAdapter;
    Intent intentMaps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locListView = findViewById(R.id.locListView);
        locArray = new ArrayList<>();
        locArray.add("Add a new place...");
        locations = new ArrayList<>();
        locations.add(new LatLng(0,0));
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