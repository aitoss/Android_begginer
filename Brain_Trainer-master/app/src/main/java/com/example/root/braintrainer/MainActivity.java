package com.example.root.braintrainer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    Button startbutton;
    TextView timertext;
    Button option1;
    Button option2;
    Button option3;
    Button option4;
    Button playagain;
    TextView correct;
    RelativeLayout gamelayout;
    TextView questiontext;
    TextView scoretext;
    TextView scoretextview;
    ArrayList<Integer> answers = new ArrayList<Integer>();
    int correctanswerlocation;
    int score;
    int questiontotal;

    public void again(View view){
        score = 0;
        questiontotal = 0;
        timertext.setText("30s");
        scoretext.setText("0/0");
        scoretextview.setText("");
        playagain.setVisibility(View.INVISIBLE);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timertext.setText(String.valueOf(millisUntilFinished/1000)+ "s");
            }

            @Override
            public void onFinish() {
                playagain.setVisibility(View.VISIBLE);
                timertext.setText("0s");
                scoretextview.setText("Your Score: "+ Integer.toString(score)+ "/"+ Integer.toString(questiontotal));
            }
        }.start();

        questiongenerator();

    }

    public void questiongenerator(){

        Random random = new Random();

        int a = random.nextInt(50);
        int b = random.nextInt(50);

        questiontext.setText(Integer.toString(a)+ "+" + Integer.toString(b));

        int incorrectanswer;
        correctanswerlocation = random.nextInt(4);
        answers.clear();

        for (int i=0 ;i<4; i++){
            if (i == correctanswerlocation){
                answers.add(a + b);
            }
            else {
                incorrectanswer = random.nextInt(100);
                while (incorrectanswer == a + b)
                {incorrectanswer = random.nextInt(100);}
                answers.add(incorrectanswer);
            }

        }
        option1.setText(Integer.toString(answers.get(0)));
        option2.setText(Integer.toString(answers.get(1)));
        option3.setText(Integer.toString(answers.get(2)));
        option4.setText(Integer.toString(answers.get(3)));


    }

    public void Chosenanswer(View view){

        if (view.getTag().toString().equals(Integer.toString(correctanswerlocation))){

            correct.setText("Correct!");
            score++;

        }
        else {
            correct.setText("Wrong!");
        }

        questiontotal++;
        scoretext.setText(Integer.toString(score)+ "/" + Integer.toString(questiontotal));
        questiongenerator();
    }

    public void start(View view){

        startbutton.setVisibility(View.INVISIBLE);
        gamelayout.setVisibility(View.VISIBLE);
        again(findViewById(R.id.startbutton));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        correct = (TextView) findViewById(R.id.correct);
        scoretext = (TextView) findViewById(R.id.scoretext);


         option1 = (Button)findViewById(R.id.option1);
         option2 = (Button)findViewById(R.id.option2);
         option3 = (Button)findViewById(R.id.option3);
         option4 = (Button)findViewById(R.id.option4);
         scoretextview = (TextView)findViewById(R.id.scoretextview);
         playagain = (Button)findViewById(R.id.playagain);
        startbutton = (Button) findViewById(R.id.startbutton);
        questiontext = (TextView)findViewById(R.id.questiontext);
        timertext = (TextView)findViewById(R.id.timertext);
        gamelayout = (RelativeLayout)findViewById(R.id.gamelayout);




    }
}
