package com.example.root.timer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    SeekBar seekBar;
    TextView timertext;
    Boolean counteractive = false;
    Button startbutton;
    CountDownTimer countDownTimer;


    public void resetTimer() {

        timertext.setText("00:30");
        seekBar.setProgress(30);
        countDownTimer.cancel();
        startbutton.setText("Go!");
        seekBar.setEnabled(true);
        counteractive = false;

    }
    public void update(int secondsdown){
        int minutes = (int) secondsdown / 60;
        int sec = (int) secondsdown-minutes*60;

        String secondstring = Integer.toString(sec);
        String minstring = Integer.toString(minutes);
        if (sec <= 9){
            secondstring = "0"+ secondstring;
        }
        if (minutes <= 9){
            minstring = "0"+ minstring;
        }

        timertext.setText(minstring + ":"+secondstring);
    }

    public void Starttimer(View view){

        if(counteractive ==false) {

            counteractive = true;
            seekBar.setEnabled(false);
            startbutton.setText("STOP");
            countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    update((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    resetTimer();
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.horn);
                    mediaPlayer.start();
                }
            }.start();
        }
        else {
            resetTimer();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        timertext = (TextView)findViewById(R.id.timertext);
        startbutton = (Button)findViewById(R.id.startbutton);
        seekBar.setMax(600);
        seekBar.setProgress(30);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                update(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
