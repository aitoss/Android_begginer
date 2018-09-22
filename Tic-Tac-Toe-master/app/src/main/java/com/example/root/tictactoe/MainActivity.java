package com.example.root.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // 0 =red , 1 = Yellow
    int activeP = 0;

    boolean activegame = true;

    int[] currentstate = {2,2,2,2,2,2,2,2,2};
    int[][] winningpos = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{0,4,8},{2,4,6},{2,5,8}};


    public void dropin(View view) {

        ImageView Image = (ImageView) view;
        int Tappedimage = Integer.parseInt(Image.getTag().toString());

        if (currentstate[Tappedimage] == 2 && activegame) {
            currentstate[Tappedimage] = activeP;


            Image.setTranslationY(-1000f);
            if (activeP == 0) {
                Image.setImageResource(R.drawable.red);
                activeP = 1;
            } else {
                Image.setImageResource(R.drawable.yellow);
                activeP = 0;
            }

            Image.animate().translationYBy(1000f).rotation(360f).setDuration(300);

            for(int[] winningpo : winningpos)
            {
                if (currentstate[winningpo[0]] == currentstate[winningpo[1]] && currentstate[winningpo[1]] == currentstate[winningpo[2]] && currentstate[winningpo[0]]!= 2 ){

                    activegame = false;
                    String Winner = "RED";
                    if (currentstate[winningpo[0]] == 1){
                        Winner = "YELLOW";
                    }
                    TextView Winnermsg = (TextView)findViewById(R.id.Winnermsg);

                    Winnermsg.setText(Winner+" Has Won!");

                    LinearLayout layout = (LinearLayout)findViewById(R.id.playagain);
                    layout.setVisibility(View.VISIBLE);
                }
                else {
                    boolean gameover = true ;
                    for (int countstate : currentstate)
                        if (countstate == 2) gameover =false;

                    if (gameover){
                        TextView Winnermsg = (TextView)findViewById(R.id.Winnermsg);

                        Winnermsg.setText("  DRAW");

                        LinearLayout layout = (LinearLayout)findViewById(R.id.playagain);
                        layout.setVisibility(View.VISIBLE);

                    }
                }
            }

        }
    }
    public void playagain(View view){

        LinearLayout layout = (LinearLayout)findViewById(R.id.playagain);
        layout.setVisibility(View.INVISIBLE);

        activeP = 0;

        for (int i=0; i < currentstate.length ; i++)
        {
            currentstate[i] = 2;
        }

        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        for(int i=0 ; i < gridLayout.getChildCount(); i++)
        {
            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);
        }
        activegame = true;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
