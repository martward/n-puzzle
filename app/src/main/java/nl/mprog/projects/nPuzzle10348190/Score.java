package nl.mprog.projects.nPuzzle10348190;

/*
Martijn Wardenaar
10348190
martijnwardenaar@gmail.com

This activity shows the user how much moves and time he/she needed to complete the puzzle.
There is also a picture present of the puzzle which was completed, as well as two buttons to choose
another image or to restart.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class Score extends ActionBarActivity {

    private static int DIFFICULTY;
    private static int IMAGE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent intentIn = getIntent();
        int moves = intentIn.getIntExtra("MOVES", 100);
        long time = intentIn.getLongExtra("TIME", 22);
        IMAGE_ID = intentIn.getIntExtra("IMAGE", 0);
        DIFFICULTY = intentIn.getIntExtra("DIFFICULTY", 0);

        /*
        The program transforms the number of seconds in to a time stamp with hours, minutes and seconds.
         */
        int timeHours = (int) (time/3600);
        int timeMinutes = (int)(time - (timeHours * 3600))/60;
        int timeSeconds = (int) (time - (timeHours*3600) - (timeMinutes*60));

        String printHours;
        String printMinutes;
        String printSeconds;
        if(timeHours < 10) {
            printHours = "0" + timeHours;
        }else {
            printHours = "" + timeHours;
        }

        if(timeMinutes < 10) {
            printMinutes = "0" + timeMinutes;
        } else {
            printMinutes = "" + timeMinutes;
        }

        if(timeSeconds < 10) {
            printSeconds = "0" + timeSeconds;
        } else {
            printSeconds = "" + timeSeconds;
        }

        String printTime = "" + printHours +  ":" + printMinutes + ":" + printSeconds;

        TextView showMoves = (TextView)findViewById(R.id.showMoves);
        showMoves.setText("" + moves);
        TextView showTime = (TextView)findViewById(R.id.showTime);
        showTime.setText(printTime);
        ImageView gameImage = (ImageView)findViewById(R.id.scoreImage);
        gameImage.setImageResource(IMAGE_ID);

        Button restart = (Button)findViewById(R.id.finishRestart);
        Button changeImage = (Button)findViewById(R.id.finishChangeImage);

        restart.setOnClickListener(respondToClick);
        changeImage.setOnClickListener(respondToClick);

    }

    private View.OnClickListener respondToClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Context context = getApplicationContext();
            switch (view.getId()){
                case R.id.finishRestart:
                    Intent restartIntent = new Intent(context, Game.class);
                    restartIntent.putExtra("DIFFICULTY", DIFFICULTY);
                    restartIntent.putExtra("IMAGE_ID", IMAGE_ID);
                    startActivity(restartIntent);
                    finish();
                    break;
                case R.id.finishChangeImage:
                    Intent homeIntent = new Intent(context, Home.class);
                    startActivity(homeIntent);
                    finish();
                    break;
            }
        }
    };

}
