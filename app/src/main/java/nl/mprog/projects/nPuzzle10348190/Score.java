package nl.mprog.projects.nPuzzle10348190;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class Score extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent intentIn = getIntent();
        int difficulty = intentIn.getIntExtra("DIFFICULTY", 0);
        int moves = intentIn.getIntExtra("MOVES", 100);
        long time = intentIn.getLongExtra("TIME", 22);
        int imageId = intentIn.getIntExtra("IMAGE", 0);

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
        gameImage.setImageResource(imageId);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.settings_restart || super.onOptionsItemSelected(item);
    }


}
