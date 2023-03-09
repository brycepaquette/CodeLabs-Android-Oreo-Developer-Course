package com.example.whatstheweather;

// API token from signing into https://openweathermap.org/api

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    JsonDownloader jsonDownloader;
    String result;
    String city;
    ArrayList<String> weatherInfo;

    // Components
    EditText cityInput;
    TextView descriptionText;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Component
        cityInput = findViewById(R.id.cityInput);
        descriptionText = findViewById(R.id.descriptionText);
        btn = findViewById(R.id.button);

        jsonDownloader = new JsonDownloader();
        result = "";
        city = "";
        weatherInfo = new ArrayList<>();




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CLICKED", "BUTTON CLICKED");
                city = cityInput.getText().toString().trim();
                cityInput.setText("");
                if (!jsonDownloader.getStatus().toString().equals("RUNNING")) {
                    getWeather();
                }
                else {
                    jsonDownloader.cancel(true);
                    getWeather();
                }


            }
        });

    }

    public void getWeather() {
        try {
            jsonDownloader.execute(String.format("https://api.openweathermap.org/data/2.5/weather?appid=1c1b57f7957e460a9518dde39c6b6d0b&units=imperial&q=%s",city));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class JsonDownloader extends AsyncTask<String, Void, String> {

        private String readStream(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                Log.e("TAG", "IOException", e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e("TAG", "IOException", e);
                }
            }
            return sb.toString();
        }

        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection connection = null;

            try {
                Log.i("UPDATE:","BACKGROUND JSON TASK STARTED");
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                String result = readStream(in);
                connection.disconnect();
                return result;
            } catch (Exception e) {
                connection.disconnect();
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray weatherArr = new JSONArray(jsonObject.getString("weather"));
                JSONObject mainArr = jsonObject.getJSONObject("main");
                city = jsonObject.getString("name");

                weatherInfo.add(city);
                weatherInfo.add(weatherArr.getJSONObject(0).getString("description"));
                weatherInfo.add(mainArr.getString("temp"));
                weatherInfo.add(mainArr.getString("temp_min"));
                weatherInfo.add(mainArr.getString("temp_max"));




            } catch (Exception e) {
                e.printStackTrace();
                descriptionText.setText(String.format("%s is not a valid city. Please Try Again.", city));
            }
            descriptionText.setText(
                    String.format(
                            getResources().getString(R.string.description),
                            weatherInfo.get(0),
                            weatherInfo.get(1),
                            weatherInfo.get(2),
                            weatherInfo.get(3),
                            weatherInfo.get(4)
                            )
            );
        }
    }
}