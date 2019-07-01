package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView timer;
    TextView scoreBoard;
    TextView question;
    TextView options;
    androidx.gridlayout.widget.GridLayout gridLayout;
    Button playAgain;

    //Game sound
    MediaPlayer mediaPlayer;

    CountDownTimer countDown;

    //Random number generator
    Random rand;

    //Question variables
    int answer;
    int q1;
    int q2;
    String operators;
    int operatorSelect;

    //ScoreBoard variables
    int totalQuestions = 0;
    int rightAnswers = 0;

    public void random(){
        q1 = rand.nextInt(10) + 1;
        q2 = rand.nextInt(10) + 1;

        operatorSelect = rand.nextInt(4) + 1;
        switch (operatorSelect){
            case 1: operators = "+";
                answer = q1 + q2;
                break;

            case 2: operators = "-";
                answer = q1 - q2;
                break;

            case 3: operators = "*";
                answer = q1 * q2;
                break;

            case 4: operators = "/";
                answer = q1 / q2;
        }

        question.setText(q1 +" "+ operators +" "+ q2);

        scoreBoard.setText(rightAnswers+"/"+totalQuestions);
    }
    public void randomizeAnswer(){
        gridLayout = (androidx.gridlayout.widget.GridLayout) findViewById(R.id.gridLayout);

        int answerOptimizer = answer + 10;

        for(int i = 0; i < gridLayout.getChildCount(); i++){
            View b = gridLayout.getChildAt(i);
            if(b instanceof TextView){
                TextView o = (TextView) b;

                o.setText(Integer.toString(rand.nextInt(answerOptimizer) + 1));

            }
        }

        int randTag = rand.nextInt(4);
        View v = gridLayout.getChildAt(randTag);
        if(v instanceof TextView){
            TextView correctAnswer = (TextView) v;
            correctAnswer.setText(Integer.toString(answer));
        }

    }

    public void currentTime(int seconds){
        timer.setText(Integer.toString(seconds));
    }

    public void resetGame(View view){
        gridLayout.setVisibility(View.VISIBLE);
        totalQuestions = 0;
        rightAnswers = 0;

        random();
        randomizeAnswer();

        timer.setText("60");

        playAgain.setVisibility(View.INVISIBLE);

        counter();

    }

    public void checkAnswer(View view){
        options = (TextView) view;

        //getting user selected option value
        int userAnswer = Integer.parseInt(options.getText().toString());

        if(userAnswer == answer){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.rightanswer);
            mediaPlayer.start();
            totalQuestions++;
            rightAnswers++;
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wronganswer);
            mediaPlayer.start();
            totalQuestions++;
        }

        random();
        randomizeAnswer();
    }

    public void counter(){
        countDown = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                currentTime((int) l/1000);
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                playAgain.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.INVISIBLE);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.gameover);
                mediaPlayer.start();
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        question = (TextView) findViewById(R.id.question);
        scoreBoard = (TextView) findViewById(R.id.scoreBoard);
        timer = (TextView) findViewById(R.id.timer);
        playAgain = (Button) findViewById(R.id.playAgain);

        rand = new Random();

        random();

        randomizeAnswer();

        counter();
    }
}
