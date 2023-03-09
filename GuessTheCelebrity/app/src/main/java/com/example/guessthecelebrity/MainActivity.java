package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Variables
    Integer answerIndex;
    ArrayList<String> options;

    // Background Thread Variables
    DownloadHTML downloadHTML;
    DownloadImage downloadImage;
    ArrayList<String> celebNames;
    ArrayList<String> celebImgURLs;
    ArrayList<Bitmap> celebImgList;

    // View components
    ImageView imageView;
    LinearLayout linearLayout;
    ArrayList<Button> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize background thread variables;
        downloadHTML = new DownloadHTML();
        downloadImage = new DownloadImage();
        String result = "";
        answerIndex = -1;
        celebNames = new ArrayList<>();
        celebImgURLs = new ArrayList<>();
        celebImgList = new ArrayList<>();
        options = new ArrayList<>(Arrays.asList("default", "default", "default", "default"));

        // Initialize view components
        imageView = findViewById(R.id.imageView);
        linearLayout = findViewById(R.id.linearLayout);
        buttons = new ArrayList<>();

        // Background thread to download html response
        try {
            result = downloadHTML.execute("https://www.imdb.com/list/ls052283250/").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Parses result and adds names and img urls to celeb arrays
        parseHTML(result);

        // Background thread to download image
        try {downloadImage.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setImage();
        generateOptions();

        // Initialize all button components to array list and set listeners
        for(int i=0; i<linearLayout.getChildCount(); i++) {
            Button btn = (Button) linearLayout.getChildAt(i);
            buttons.add(btn);
            // Set an OnClick listener to all buttons
            btn.setOnClickListener(this);
            // Set initial values
            btn.setText(options.get(i));
        }

    }

    @Override
    public void onClick(View v) {
        if(((Button) v).getText() == celebNames.get(answerIndex)) {
            Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "INCORRECT :(", Toast.LENGTH_SHORT).show();
        }

        setImage();
        generateOptions();
        for(int i=0; i<linearLayout.getChildCount(); i++) {
            Button btn = (Button) linearLayout.getChildAt(i);
            btn.setText(options.get(i));
        }
    }

    public class DownloadHTML extends AsyncTask<String, Void, String> {

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
            Log.i("STATUSUPDATE", "HTML BACKGROUND JOB STARTED");
            HttpURLConnection connection = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(connection.getInputStream());
                String result = readStream(in);
                connection.disconnect();
                return result;
            } catch (Exception e) {
                connection.disconnect();
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DownloadImage extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.i("STATUSUPDATE", "IMAGE BACKGROUND JOB STARTED");
            HttpURLConnection connection = null;
            for(String urlString : celebImgURLs) {
                try {
                    URL url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream in = connection.getInputStream();
                    Bitmap celebBitmap = BitmapFactory.decodeStream(in);
                    celebImgList.add(celebBitmap);
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }

    public void parseHTML(String html) {
        Pattern p = Pattern.compile("<img alt=\"(.*)\".*\\n.*\\nsrc=\"(.*)\"");
        Matcher m = p.matcher(html);

        while(m.find()) {
            celebNames.add(m.group(1));
            celebImgURLs.add(m.group(2));
        }
    }

    public void generateOptions() {
        // Set index 0 as the answer
        options.set(0, celebNames.get(answerIndex));

        // Generate 3 random celeb names
        Random random = new Random();
        int celebIndex;
        for(int i=1; i<options.size(); i++) {
            celebIndex = answerIndex;
            while(options.contains(celebNames.get(celebIndex))) {
                celebIndex = random.nextInt(celebNames.size());
            }
            options.set(i, celebNames.get(celebIndex));
        }

        // Shuffle the options
        Collections.shuffle(options);
    }

    public void setImage() {
        answerIndex += 1;
        imageView.setImageBitmap(celebImgList.get(answerIndex));
    }
}