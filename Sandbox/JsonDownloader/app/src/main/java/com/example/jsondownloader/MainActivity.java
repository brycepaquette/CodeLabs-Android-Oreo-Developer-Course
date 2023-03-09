package com.example.jsondownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

public class MainActivity extends AppCompatActivity {

    JsonDownloader jsonDownloader;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonDownloader = new JsonDownloader();
        result = "";

        try {
            result = jsonDownloader.execute("https://samples.openweathermap.org/data/2.5/weather?q=London&appid=b1b15e88fa797225412429c1c50c122a1").get();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DONE");
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

                String weatherInfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i=0; i<arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);



                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}