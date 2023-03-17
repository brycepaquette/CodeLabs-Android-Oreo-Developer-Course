package com.example.photoimportdemo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    VideoView videoView;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

/*        // Launcher for single file
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if(result.getData() != null) {
                        Uri uri = result.getData().getData();
                        Log.i("RESULT", uri.toString());
                    }

                }
        );*/

        // Launcher for multiple files
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getData() != null) {
                        List<Uri> uris = new ArrayList<>();
                        ClipData clipData = result.getData().getClipData();
                        if (clipData != null) {
                            for (int i=0; i< clipData.getItemCount(); i++) {
                                uris.add(clipData.getItemAt(i).getUri());
                            }
                        }
                        else {
                            uris.add(result.getData().getData());
                        }
                        Log.i("ITEM COUNT", Integer.toString(uris.size()));
                    }

                }
        );

        // Image launcher
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

        //Video launcher
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent.setType("video/*");
                activityResultLauncher.launch(intent);
            }
        });

        // Any content launcher
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setType("*/*");
                activityResultLauncher.launch(intent);
            }
        });


        // Picker for one file
/*        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageView.setImageURI(result);
                        }
                    }
                });*/

         // Picker for multiple files
/*        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetMultipleContents(),
                new ActivityResultCallback<List<Uri>>() {
                    @Override
                    public void onActivityResult(List<Uri> result) {
                        for (int i=0; i<result.size(); i++) {
                            Log.i("IMAGE", result.get(i).toString());
                        }
                    }
                }
        );*/
    }


}