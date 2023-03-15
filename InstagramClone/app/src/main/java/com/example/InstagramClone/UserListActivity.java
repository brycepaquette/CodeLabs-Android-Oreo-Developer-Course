package com.example.InstagramClone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    
    
    ListView userListView;
    
    ArrayList<String> userListArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userListView = findViewById(R.id.userListView);
        userListArray = new ArrayList<>();

        initializeUserListView();


        
    }
    
    public void getUsers() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");

        try {
            List<ParseUser> results = query.find();

            for (ParseUser user : results) {
                userListArray.add(user.getUsername());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            userListArray.add("No Users Found");
        }
    }

    public void initializeUserListView() {
        getUsers();

        userListView.setAdapter(
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        userListArray));
    }
}