package nl.mprog.projects.nPuzzle10348190;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class Game extends ActionBarActivity {

    private static int DIFFICULTY;
    private static int IMAGE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ImageView selected_image = (ImageView)findViewById(R.id.selec_image);
        Intent intent = getIntent();
        int image_ID = intent.getIntExtra("IMAGE_ID", 0);
        int difficulty = intent.getIntExtra("DIFFICULTY", 0);
        selected_image.setImageResource(image_ID);
        System.out.println(image_ID);
        System.out.println(difficulty);
        /*
        
        set_difficulty(difficulty);
        set_image_id(image_ID);
        GridView gameView = (GridView) findViewById(R.id.gameView);
        switch (get_difficulty()) {
            case 0:
                gameView.setNumColumns(3);
            case 1:
                gameView.setNumColumns(4);
            case 2:
                gameView.setNumColumns(5);
        }
        
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.settings_restart) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void set_image_id(int id){
        IMAGE_ID = id;
    }

    public static int get_image_id() {
        return IMAGE_ID;
    }

    public static void set_difficulty(int d){
        DIFFICULTY = d;
    }

    public static int get_difficulty(){
        return DIFFICULTY;
    }

}
