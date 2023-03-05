package com.example.timestables;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Integer> arrayList;
    SeekBar seekBar;
    TextView seekBarText;

    Integer MAX_VALUE = 99;
    String SEEKBAR_STATIC_TEXT = "Current Value: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Objects
        listView = findViewById(R.id.listView);
        seekBar = findViewById(R.id.seekBar);
        seekBarText = findViewById(R.id.seekBarText);

        // Set max seek bar
        seekBar.setMax(MAX_VALUE);

        // Set initial seek bar text value
        seekBarText.setText(SEEKBAR_STATIC_TEXT + Integer.toString(seekBar.getProgress() + 1));

        // Initialize ArrayList
        arrayList = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));

        // Create and Set array adapter
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        // Set seekbar onchange listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // get the progress value and add one (prevent 0 value, min value is 1, max is MAX_VALUE + 1)
                Integer seekBarValue = progress + 1;

                // Update the seek bar text
                seekBarText.setText(SEEKBAR_STATIC_TEXT + Integer.toString(seekBarValue));

                // loop through all 20 array values and update them
                for (int i = 0; i < arrayList.size(); i++) {
                    Integer multiplier = i + 1;
                    arrayList.set(i, seekBarValue * multiplier);
                }
                // update the listView
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}