package com.example.InstagramClone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.security.Key;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    //Bitnami Dashboard - user - nLox2tBi5hlH
    //http://52.207.110.124/apps
    //http://52.207.110.124/parse

    // Managers
    InputMethodManager inputMethodManager;

    // Components
    EditText usernameInput;
    EditText passwordInput;
    Button signupButton;
    Button loginButton;
    TextView buttonToggle;
    ConstraintLayout backgroundLayout;
    ImageView instaLogo;

    boolean toggleBool;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        // Managers
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        // If a user session exists, start the UserListActivity
        checkIsLoggedIn();

        // true = signup, false = login
        toggleBool = true;

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);
        buttonToggle = findViewById(R.id.buttonToggle);
        backgroundLayout = findViewById(R.id.backgroundLayout);
        instaLogo = findViewById(R.id.instaLogo);

        // Initialize buttonToggle text
        buttonToggle.setText(String.format(getString(R.string.buttonToggle), getString(R.string.loginName)));

        // OnKey Listener
        passwordInput.setOnKeyListener(this);






    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (toggleBool) {
                signup(signupButton);
            }
            else {
                login(loginButton);
            }
        }
        return false;
    }

    public void toggleButton(View view) {
        if (toggleBool) {
            signupButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            buttonToggle.setText(String.format(getString(R.string.buttonToggle), getString(R.string.signupName)));
        }
        else {
            signupButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            buttonToggle.setText(String.format(getString(R.string.buttonToggle), getString(R.string.loginName)));
        }
        toggleBool = !toggleBool;
    }

    public void signup(View view) {
        if (checkRequiredFields()) {
            if (!userExists()) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameInput.getText().toString());
                user.setPassword(passwordInput.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("NewUserSignUp", "A new user was created: " + usernameInput.getText().toString());
                            Toast.makeText(MainActivity.this, "Sign up Successful, Please login", Toast.LENGTH_SHORT).show();
                            toggleButton(buttonToggle);
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, R.string.toastUserExists, Toast.LENGTH_SHORT).show();
                toggleButton(buttonToggle);
            }
        }
        else {
            toastRequiredFields();
        }
    }

    public void login(View view) {
        if (checkRequiredFields()) {
            ParseUser.logInInBackground(usernameInput.getText().toString(), passwordInput.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Log.i("NewUserLogin", "The user was logged in: " + usernameInput.getText().toString());
                        Toast.makeText(MainActivity.this, "Welcome back " + usernameInput.getText().toString(), Toast.LENGTH_SHORT).show();
                        showUserList();
                    }
                    else {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            toastRequiredFields();
        }

    }

    public boolean checkRequiredFields() {
        // true = requirements met, false = missing requirements
        return usernameInput.getText().toString().length() > 0 && passwordInput.getText().toString().length() > 0;
    }

    public boolean userExists() {
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        Log.i("USER CHECK: ", usernameInput.getText().toString());
        userQuery.whereEqualTo("username", usernameInput.getText().toString());
        try {
            List<ParseUser> results = userQuery.find();
            return results.size() > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public void toastRequiredFields() {
        Toast.makeText(this, R.string.toastRequiredFields, Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard(View view) {
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void checkIsLoggedIn() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            Toast.makeText(MainActivity.this, "Welcome back " + user.getUsername(), Toast.LENGTH_SHORT).show();
            showUserList();
        }
    }

    public void showUserList() {
        Intent userListIntent;
        userListIntent = new Intent(this, UserListActivity.class);
        startActivity(userListIntent);
    }



}