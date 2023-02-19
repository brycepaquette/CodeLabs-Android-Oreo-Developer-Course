package com.example.connect3game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    // 0: yellow, 1: red, 2: empty
    int activePlayer = 0;

    boolean gameActive = true;

    int[] gameState = {2,2,2,2,2,2,2,2,2};

    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};

    public void dropIn(View view) {

        ImageView counter = (ImageView) view;
        TextView winningText = findViewById(R.id.winningText);
        Button playAgainBtn = findViewById(R.id.playAgainButton);

        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameActive) {

            gameState[tappedCounter] = activePlayer;
            counter.setTranslationY(-1500);

            if (activePlayer == 0){
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            }
            else {
                counter.setImageResource(R.drawable.red);
                activePlayer = 0;
            }

            counter.animate().translationYBy(1500).setDuration(300);

            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {

                    gameActive = false;

                    if (gameState[winningPosition[0]] == 0) {
                        winningText.setText("Yellow Wins!");

                    } else {
                        winningText.setText("Red Wins!");
                    }

                    winningText.setVisibility(View.VISIBLE);
                    playAgainBtn.setVisibility(View.VISIBLE);
                }
            }
        }
    }

        public void playAgain(View view) {
            TextView winningText = findViewById(R.id.winningText);
            Button playAgainBtn = findViewById(R.id.playAgainButton);
            activePlayer = 0;

            gameActive = true;

            for (int i = 0; i<gameState.length; i++) {
                gameState[i] = 2;
            }
            System.out.println(gameState[0]);
            winningText.setVisibility(View.INVISIBLE);
            playAgainBtn.setVisibility(View.INVISIBLE);

            GridLayout gridLayout = findViewById(R.id.gridLayout);
            for (int i=0; i<gridLayout.getChildCount(); i++) {
                ImageView child = (ImageView) gridLayout.getChildAt(i);
                child.setImageDrawable(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}