package nl.mprog.projects.nPuzzle10348190;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;


public class ShowImage extends ActionBarActivity {

    private static int IMAGE_ID;
    private static int DIFFICULTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ImageView imageView = (ImageView)findViewById(R.id.showImage);
        Intent intent = getIntent();
        int image_ID = intent.getIntExtra("IMAGE_ID", 0);
        int difficulty = intent.getIntExtra("DIFFICULTY", 0);
        set_values(image_ID,difficulty);
        imageView.setImageResource(image_ID);
        Context context = getApplicationContext();

        CharSequence text = "Tab picture to start the game!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        imageView.setOnClickListener(respondToClick);

    }

    public View.OnClickListener respondToClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intentOut = new Intent(view.getContext(), Game.class);
            intentOut.putExtra("IMAGE_ID", get_image_id());
            intentOut.putExtra("Difficulty", get_difficulty());
            startActivity(intentOut);
        }
    };


    public static void set_values(int id, int diff){
        IMAGE_ID = id;
        DIFFICULTY = diff;
    }

    public static int get_image_id(){
        return IMAGE_ID;
    }

    public static int get_difficulty(){
        return DIFFICULTY;
    }
}
