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

        System.out.println(difficulty);
        System.out.println(moves);
        System.out.println(time);

        TextView show_moves = (TextView)findViewById(R.id.showMoves);
        show_moves.setText("" + moves);
        TextView show_time = (TextView)findViewById(R.id.showTime);
        show_time.setText(""+ time);
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
