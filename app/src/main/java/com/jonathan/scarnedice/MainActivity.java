package com.jonathan.scarnedice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Random rand;
    private ImageButton roll;
    private ImageButton hold;
    private ImageButton reset;
    private ImageView dice;
    private TextView currentTurn;
    private TextView rollTotal;
    private TextView score1;
    private TextView score2;

    Player player1;
    Player player2;

    private int turnNumber = 0;
    private int currentRoll = 0;
    private int tempScore = 0;
    private boolean playerFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1 = new Player();
        player2 = new Player();

        dice = (ImageView)findViewById(R.id.dice);
        score1 = (TextView)findViewById(R.id.score1);
        score2 = (TextView) findViewById(R.id.score2);
        currentTurn = (TextView) findViewById(R.id.currentTurn);
        rollTotal = (TextView)findViewById(R.id.rollTotal);

        // roll dice
        roll = (ImageButton)findViewById(R.id.rollButton);
        roll.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                rollDice(player1);
            }
        });

        // add turn score with overall score
        hold = (ImageButton)findViewById(R.id.holdButton);
        hold.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                updateScore(player1);
            }
        });

        // reset game along with current scores
        reset = (ImageButton)findViewById(R.id.resetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEverything(player1);
                resetEverything(player2);
                score1.setText("" + player1.getOverallScore());
                score2.setText("" + player1.getOverallScore());
            }
        });
    }

    private void computerTurn() {
        roll.setEnabled(false);
        hold.setEnabled(false);
        rollDice(player2);
        roll.setEnabled(true);
        hold.setEnabled(true);
        if(playerFinished) updateScore(player2);
    }

    private void updateScore(Player player) {
        int overallScore = 0;
        overallScore = tempScore + player.getOverallScore();
        player.setOverallScore(overallScore); //update ending player's overall score
        resetTempScore(player);
        Log.i("Player finished","" + playerFinished);

        if(playerFinished){
            score2.setText("" + player.getOverallScore());
        }
        else score1.setText("" + player.getOverallScore());

        playerFinished =  playerFinished ? false: true; // current player is finished with round
        Log.i("Player finished After","" + playerFinished);

        if(player1.getOverallScore() >= 100){ // Player wins
            Toast.makeText(MainActivity.this,"Player 1 wins!",Toast.LENGTH_SHORT).show();
            roll.setEnabled(false);
            hold.setEnabled(false);
            return;
        }
        else if(player2.getOverallScore() >= 100){ // Computer wins
            Toast.makeText(MainActivity.this,"Computer wins!",Toast.LENGTH_SHORT).show();
            roll.setEnabled(false);
            hold.setEnabled(false);
            return;
        }

        if(playerFinished){ // Computer's turn
            Log.i("Playing now","Computer's turn");
            computerTurn();
        }
    }

    private void rollDice(Player player) {
        rand = new Random();
        currentRoll = rand.nextInt(6) + 1;
        // rotate animation for the dice
        RotateAnimation rotateAnimation = new RotateAnimation(30, 90,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        dice.setAnimation(rotateAnimation);

        // update the dice image according to what was rolled
        updateDiceImage(currentRoll);
        // update amount of turns taken so far
        turnNumber++;
        currentTurn.setText("Turn: " + turnNumber);
        Log.i("Current roll","" + currentRoll);

        if(currentRoll != 1){ // update turn total if a 1 hasn't been rolled
            tempScore += currentRoll;
            rollTotal.setText("Roll total: " + tempScore);
            Log.i("Roll total","" + tempScore);
            player.setTurnScore(tempScore);
        }
        else {
            resetTempScore(player);
            playerFinished =  playerFinished ? false: true; // end turn for current player
            if(playerFinished){
                Log.i("Entered","Computer's turn");
                computerTurn();
            }
        }
    }

    // reset overall and turn score for all players
    private void resetEverything(Player player){
        player.setTurnScore(0);
        player.setOverallScore(0);
        roll.setEnabled(true);
        hold.setEnabled(true);
    }

    // reset turn total when next player is up
    private void resetTempScore(Player player) {
        tempScore = 0;
        turnNumber = 0;
        currentTurn.setText("Turn: " + turnNumber);
        player.setTurnScore(0);
    }

    private void updateDiceImage(int roll) {
        switch(roll){
            case 1:
                dice.setImageResource(R.drawable.dice1);
                break;
            case 2:
                dice.setImageResource(R.drawable.dice2);
                break;
            case 3:
                dice.setImageResource(R.drawable.dice3);
                break;
            case 4:
                dice.setImageResource(R.drawable.dice4);
                break;
            case 5:
                dice.setImageResource(R.drawable.dice5);
                break;
            case 6:
                dice.setImageResource(R.drawable.dice6);
                break;
            default:
                break;
        }
    }
}

class Player{
    int turnScore;
    int overallScore;

    public Player(){
        turnScore = 0;
        overallScore = 0;
    }
    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public int getTurnScore() {

        return turnScore;
    }

    public void setTurnScore(int turnScore) {
        this.turnScore = turnScore;
    }
}