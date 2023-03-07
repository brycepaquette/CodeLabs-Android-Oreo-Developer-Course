package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Component variables
    Button goBtn;
    Button playAgainBtn;
    TextView timerText;
    TextView scoreText;
    TextView problemText;
    TextView resultText;
    ConstraintLayout constraintLayout;
    GridLayout gridLayout;
    ArrayList<Button> buttons;
    CountDownTimer gameTimer;
    Resources res;
    Random random;

    Integer counter;
    Integer score;
    Integer answer;
    ArrayList<Integer> problemValues;
    ArrayList<Integer> answerValues;

    public void updateGame() {
        // Update the problem text
        problemText.setText(String.format(res.getString(R.string.problemText), problemValues.get(0), problemValues.get(1)));
        // Update the score text
        scoreText.setText(String.format(res.getString(R.string.scoreText), score, counter));
        // Loop through and update the buttons in gridlayout
        for(int i=0; i<buttons.size(); i++) {
            buttons.get(i).setText(String.valueOf(answerValues.get(i)));
        }
    }

    public void generateProblem() {
        // Increase the problem counter
        counter += 1;

        // 2 Random Integers between 0 and 50
        problemValues.set(0, random.nextInt(50));
        problemValues.set(1, random.nextInt(50));

        // Compute and Store in the answer variable and answerValues
        answer = problemValues.get(0) + problemValues.get(1);
        answerValues.set(0, answer);

        // Generate 3 random integers between (answer / 2) and (answer * 2)
        int lowerBound = answer / 2;
        int upperBound = (answer * 2) - lowerBound + 1 ;
        answerValues.set(1, random.nextInt(upperBound) + lowerBound);
        answerValues.set(2, random.nextInt(upperBound) + lowerBound);
        answerValues.set(3, random.nextInt(upperBound) + lowerBound);

        // Shuffle the list to randomize the order
        Collections.shuffle(answerValues);
    }


    public void go(View view) {
        goBtn.setVisibility(View.INVISIBLE);

        // Reset timer text
        timerText.setText(String.format(res.getString(R.string.timerText), 30));

        // Set all components to visible, excluding the ids in the exclusions list
        ArrayList<Integer> exclusions = new ArrayList<>(Arrays.asList(R.id.goBtn, R.id.playAgainBtn));
        for(int i=0; i<constraintLayout.getChildCount(); i++) {
            View comp = (View) constraintLayout.getChildAt(i);
            if(!exclusions.contains(comp.getId())) {
                comp.setVisibility(View.VISIBLE);
            }
        }

        // Call function to generate the next problem
        generateProblem();
        // Update the game
        updateGame();

        gameTimer.start();
    }

    public void playAgain(View view) {
        // Reset variables
        counter = -1;
        score = 0;
        answer = 0;

        // Reset texts
        timerText.setText(String.format(res.getString(R.string.timerText), 30));
        resultText.setText("");

        // Hide play again button
        playAgainBtn.setVisibility(View.INVISIBLE);

        // Enable answer buttons
        for(Button button : buttons) {
            button.setClickable(true);
        }

        generateProblem();
        updateGame();

        gameTimer.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();

        // Initialize variables
        random = new Random();
        counter = -1;
        score = 0;
        answer = 0;
        problemValues = new ArrayList<>(Arrays.asList(0,0));
        answerValues = new ArrayList<>(Arrays.asList(0,0,0,0));
        buttons = new ArrayList<>();

        // Initialize component objects
        goBtn = findViewById(R.id.goBtn);
        playAgainBtn = findViewById(R.id.playAgainBtn);
        timerText = findViewById(R.id.timerText);
        scoreText = findViewById(R.id.scoreText);
        problemText = findViewById(R.id.problemText);
        resultText = findViewById(R.id.resultText);
        constraintLayout = findViewById(R.id.constraintLayout);
        gridLayout = findViewById(R.id.answerLayout);

        // Initialize button component list
        for(int i=0; i<gridLayout.getChildCount(); i++) {
            buttons.add((Button) gridLayout.getChildAt(i));
        }

        // Set button listeners
        for(Button button : buttons) {
            button.setOnClickListener(this);
        }

        gameTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.format(res.getString(R.string.timerText), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                resultText.setText(res.getString(R.string.game_over));
                playAgainBtn.setVisibility(View.VISIBLE);
                for(Button button : buttons) {
                    button.setClickable(false);
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        Log.i("LOG:", "CLICKED " + view.getTag() + " " + ((TextView) view).getText() + answer);
        Button clickedBtn = (Button) view;

        // Check if the button clicked is the correct answer
        if(Integer.parseInt(clickedBtn.getText().toString()) == answer) {
            //Increase the score
            score += 1;
            resultText.setText(res.getString(R.string.correctText));
        }
        else {
            // Update result text to indicate their answer is incorrect
            resultText.setText(res.getString(R.string.incorrectText));
        }
        generateProblem();
        updateGame();

    }
}


























